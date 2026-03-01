-- Create orders table (Campus marketplace specific)
CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        order_number VARCHAR(50) UNIQUE NOT NULL,
                        buyer_id BIGINT NOT NULL,
                        total_amount DECIMAL(10, 2) NOT NULL,
                        stripe_payment_intent_id VARCHAR(255),
                        stripe_payment_status VARCHAR(50),
                        status VARCHAR(30) NOT NULL DEFAULT 'PENDING_PAYMENT',
                        meeting_location VARCHAR(500),
                        meeting_time TIMESTAMP,
                        meeting_notes VARCHAR(1000),
                        otp_code VARCHAR(6),
                        otp_generated_at TIMESTAMP,
                        otp_expires_at TIMESTAMP,
                        otp_verified_at TIMESTAMP,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP,
                        completed_at TIMESTAMP,
                        CONSTRAINT fk_order_buyer FOREIGN KEY (buyer_id) REFERENCES users(id) ON DELETE RESTRICT
);

CREATE TABLE order_item (
                            id BIGSERIAL PRIMARY KEY,
                            order_id BIGINT NOT NULL,
                            product_id BIGINT NOT NULL,
                            seller_id BIGINT NOT NULL,
                            quantity INTEGER NOT NULL CHECK (quantity > 0),
                            price_at_purchase DECIMAL(10, 2) NOT NULL,
                            subtotal DECIMAL(10, 2) NOT NULL,
                            CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
                            CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE RESTRICT,
                            CONSTRAINT fk_order_item_seller FOREIGN KEY (seller_id) REFERENCES users(id) ON DELETE RESTRICT
);

ALTER TABLE product ADD COLUMN IF NOT EXISTS version INTEGER DEFAULT 0;

CREATE INDEX IF NOT EXISTS idx_order_buyer_id ON orders(buyer_id);
CREATE INDEX IF NOT EXISTS idx_order_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_order_created_at ON orders(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_order_otp_code ON orders(otp_code);
CREATE INDEX IF NOT EXISTS idx_order_item_order_id ON order_item(order_id);
CREATE INDEX IF NOT EXISTS idx_order_item_seller_id ON order_item(seller_id);