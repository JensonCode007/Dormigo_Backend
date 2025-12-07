-- Create PRODUCTS table
CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          title VARCHAR(255) NOT NULL,
                          description TEXT NOT NULL,
                          price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
                          quantity INTEGER NOT NULL DEFAULT 1 CHECK (quantity >= 0),
                          condition VARCHAR(20) NOT NULL CHECK (condition IN ('NEW', 'LIKE_NEW', 'GOOD', 'FAIR', 'POOR')),
                          category_id BIGINT NOT NULL,
                          seller_id BIGINT NOT NULL,
                          is_available BOOLEAN NOT NULL DEFAULT true,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Foreign Keys
                          CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT,
                          CONSTRAINT fk_products_seller FOREIGN KEY (seller_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for faster queries
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_seller ON products(seller_id);
CREATE INDEX idx_products_available ON products(is_available);
CREATE INDEX idx_products_price ON products(price);