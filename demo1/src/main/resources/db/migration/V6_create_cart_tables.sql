-- Create cart table
CREATE TABLE cart (
                      id BIGSERIAL PRIMARY KEY,
                      user_id BIGINT NOT NULL UNIQUE,
                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP,
                      CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create cart_item table
CREATE TABLE cart_item (
                           id BIGSERIAL PRIMARY KEY,
                           cart_id BIGINT NOT NULL,
                           product_id BIGINT NOT NULL,
                           quantity INTEGER NOT NULL CHECK (quantity > 0),
                           added_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           CONSTRAINT fk_cart_item_cart FOREIGN KEY (cart_id) REFERENCES cart(id) ON DELETE CASCADE,
                           CONSTRAINT fk_cart_item_product FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
                           CONSTRAINT unique_cart_product UNIQUE (cart_id, product_id)
);

-- Create indexes for better performance
CREATE INDEX idx_cart_user_id ON cart(user_id);
CREATE INDEX idx_cart_item_cart_id ON cart_item(cart_id);
CREATE INDEX idx_cart_item_product_id ON cart_item(product_id);