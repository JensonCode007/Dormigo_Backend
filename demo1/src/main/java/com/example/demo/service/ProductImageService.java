package com.example.demo.service;


import com.example.demo.Entity.Product;
import com.example.demo.Entity.ProductImage;
import com.example.demo.Repository.ProductImageRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.dto.response.ProductImageResponse;
import com.example.demo.exception.FileStorageException;
import com.example.demo.exception.ForbiddenException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ProductImageMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.security.UserPrincipal;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final CloudinaryService cloudinaryService;
    private final ProductRepository productRepository;

    private static final int MAX_IMAGES_PER_PRODUCT = 5;

    @Transactional
    public ProductImageResponse uploadImage(
            MultipartFile file,
            boolean isPrimary, Long productId,
            UserPrincipal userPrincipal){
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product " + "id" + productId ));

        verifyOwnerShip(product, userPrincipal);
        log.info("Uploading Image for product id {}", productId);
        long count = productImageRepository.countByProductId(productId);
        if(count > MAX_IMAGES_PER_PRODUCT){
            throw new FileStorageException(
                    String.format("Product Already has %d of images", MAX_IMAGES_PER_PRODUCT)
            );
        }
        if(isPrimary){
            removeExistingPrimaryImage(productId);
        }
        Map<String, Object> cloudinaryUpload = cloudinaryService.uploadImage(file);
        String imageUrl = (String) cloudinaryUpload.get("secure_url");
        String format = (String) cloudinaryUpload.get("format");
        String publicId = (String) cloudinaryUpload.get("public_id");
        Long fileSize = ( (Number) cloudinaryUpload.get("bytes")).longValue();






        ProductImage productImage = new ProductImage();
        productImage.setProduct(product);
        productImage.setImageUrl(imageUrl);
        productImage.setFileSize(fileSize);
        productImage.setProduct(product);
        productImage.setCloudinaryPublicId(publicId);
        productImage.setFileName(file.getOriginalFilename());
        productImage.setFileType("image/" +  format);
        productImage.setUploadedAt(productImage.getUploadedAt());
        productImage.setIsPrimary(isPrimary);

        ProductImage productImageSaved = productImageRepository.save(productImage);

        log.info("Product image saved with id {}", productImageSaved.getId());

        return ProductImageMapper.toResponse(productImageSaved);


    }

    public List<ProductImageResponse> getProductImages(Long productId){
        if(productRepository.findById(productId).isEmpty()){
            throw new ResourceNotFoundException("Product" + "id" + productId);
        }

        Collection<ProductImage> images = productImageRepository.findByProductId(productId);
        log.info("Images found with id {}", productId);


        return images.stream().map(ProductImageMapper::toResponse).collect(Collectors.toList());


    }

    public ProductImageResponse getPrimaryImage(Long productId){
        if(productRepository.findById(productId).isEmpty()){
            throw new ResourceNotFoundException("Product" + "id" + productId);
        }
        ProductImage primaryImage = productImageRepository.findByProductIdAndIsPrimaryTrue(productId).orElse(null);
        if(primaryImage == null){
            List<ProductImage> images = productImageRepository.findByProductId(productId);
            if(images.isEmpty()){
                return null;
            }
            primaryImage = images.getFirst();
        }
        return ProductImageMapper.toResponse(primaryImage);
    }

    @Transactional
    public void setPrimaryImage(Long imageId, UserPrincipal userPrincipal){
        ProductImage productImage = productImageRepository.findByProductIdAndIsPrimaryTrue(imageId).orElseThrow(
                () -> new ResourceNotFoundException("ProductImage " + "id" + imageId)
        );
        Product product = productImage.getProduct();
        verifyOwnerShip(product, userPrincipal);

        long productId = product.getId();
        removeExistingPrimaryImage(productId);

        productImage.setIsPrimary(true);
        productImageRepository.save(productImage);

        log.info("Primary image with id {} is set for product with id {}", productImage.getId(), productId);
    }

    @Transactional
    public void deleteProductImage(Long imageId, UserPrincipal userPrincipal){

        ProductImage image = productImageRepository.findById(imageId).orElseThrow(
                () -> new ResourceNotFoundException("Product Image " + "id" + imageId)
        );
        Product product = image.getProduct();
        verifyOwnerShip(product, userPrincipal);
        if(image.getCloudinaryPublicId()!=null){
            cloudinaryService.deleteImage(image.getCloudinaryPublicId());
        }
        log.info("Product image with id {} is deleted", imageId);
    }


    /*Verification of Ownership of Product*/
    private void verifyOwnerShip(Product product, UserPrincipal userPrincipal){
        long userId = userPrincipal.getId();

        if(!product.getSeller().getId().equals(userId)){
            log.info("User is not owner of product with id {}", product.getId());
            throw new ForbiddenException("User is not owner of product with id " + product.getId());
        }
        log.info("User is owner of product with id {}", product.getId());
    }

    public void removeExistingPrimaryImage(Long productId){
        productImageRepository.findById(productId).ifPresent(productImage -> {

            productImage.setIsPrimary(false);
            productImageRepository.save(productImage);

            log.info("Primary image of product with id {} has been removed", productImage.getId());
        });
    }
}
