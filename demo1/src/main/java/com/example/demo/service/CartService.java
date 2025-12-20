package com.example.demo.service;

import com.example.demo.Entity.Cart;
import com.example.demo.Entity.CartItems;
import com.example.demo.Entity.Product;
import com.example.demo.Entity.User;
import com.example.demo.Repository.CartItemRepository;
import com.example.demo.Repository.CartRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.dto.request.AddToCartRequest;
import com.example.demo.dto.request.UpdateCartItemRequest;
import com.example.demo.dto.response.CartItemResponse;
import com.example.demo.dto.response.CartResponse;
import com.example.demo.exception.ForbiddenException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.CartMapper;
import com.example.demo.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    @Transactional
    public CartResponse getCart(UserPrincipal userPrincipal) {


        Cart cart = createOrGetCart(userPrincipal);
        return CartMapper.toResponse(cart);

    }

    private Cart createOrGetCart(UserPrincipal userPrincipal) {
        return cartRepository.findByUserId(userPrincipal.getId()).orElseGet(
                () -> {
                    log.info("Creating a cart for the user with id : {} ", userPrincipal.getId());
                    User user = userRepository.findById(userPrincipal.getId()).orElseThrow(
                            () -> new ResourceNotFoundException("User", "id", userPrincipal.getId())
                    );

                    Cart cart = Cart.builder()
                            .user(user)
                            .build();

                    return cartRepository.save(cart);

                }
        );


    }

    @Transactional
    public CartResponse addToCart(UserPrincipal userPrincipal, AddToCartRequest request) {
        log.info("Adding to cart for the user with id : {} ", userPrincipal.getId());
        Cart cart = createOrGetCart(userPrincipal);

        Product product = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", request.getProductId())
        );
        checkProductAvailability(product, request.getQuantity());
        Optional<CartItems> existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), request.getProductId());


        if(existingItem.isPresent()){
            CartItems cartItems = existingItem.get();
            cartItems.setQuantity(cartItems.getQuantity() + request.getQuantity());
        }
        else{
            CartItems item = CartItems.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();

            cart.addItem(item);
            cartItemRepository.save(item);
        }
        return CartMapper.toResponse(cart);
    }

    @Transactional
    public CartResponse updateCart(UserPrincipal userPrincipal, UpdateCartItemRequest request, long cartItemId) {
        Cart cart = cartRepository.findByUserId(userPrincipal.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Cart not found")
        );
        Long id = userPrincipal.getId();


        CartItems item = cartItemRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Cart", "id", id)
        );
        Product product = productRepository.findById(item.getProduct().getId()).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", item.getProduct().getId())
        );

        if(!item.getCart().getId().equals(cart.getId())){
            throw new ForbiddenException("Cart does not belong to the same user");
        }

        Integer quantity = request.getQuantity();
        checkProductAvailability(product, quantity);

        item.setQuantity(quantity);
        cartItemRepository.save(item);

        cart = cartRepository.findByUserId(userPrincipal.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Cart not found")
        );

        return CartMapper.toResponse(cart);
    }

    @Transactional
    public CartResponse deleteCartItem(UserPrincipal userPrincipal, long cartItemId) {
        log.info("Deleting cart item for the user with id : {} ", userPrincipal.getId());
        Long id = userPrincipal.getId();
        CartItems item = cartItemRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Cart item", "id", cartItemId)
        );
        Cart cart = cartRepository.findByUserId(userPrincipal.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Cart not found")
        );
        if(!cart.getItems().contains(item)){
            throw new ResourceNotFoundException("Cart item does not exist in the cart");
        }
        cart.removeItem(item);
        cartItemRepository.delete(item);
        log.info("Cart item with id {} has been deleted", cartItemId);

        cart =  cartRepository.findByUserId(userPrincipal.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Cart not found")
        );

        return CartMapper.toResponse(cart);


    }

    @Transactional
    public void clearEntireCart(UserPrincipal userPrincipal) {
        log.info("Clearing entier cart for the user with id : {} ", userPrincipal.getId());
        Cart cart = cartRepository.findByUserId(userPrincipal.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Cart not found")
        );

        cart.clearItems();
        cartRepository.save(cart);

        log.info("Cart for the user with id {} has been cleared", userPrincipal.getId());

    }


    private void checkProductAvailability(Product product, Integer quantity) {
        if(!product.getIsAvailable()){
            throw new ForbiddenException("Product is not available");
        }
        if(product.getQuantity() < quantity){
            throw new IllegalStateException(
                    String.format("Insufficient stock for product: %s.  Available: %d, Requested:  %d",
                            product.getTitle(), product.getQuantity(), quantity)
            );
        }
    }

}
