-- Create CATEGORIES table
CREATE TABLE categories (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(100) NOT NULL UNIQUE,
                            description TEXT,
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Insert default categories
INSERT INTO categories (name, description) VALUES
                                               ('Books', 'Textbooks, novels, and educational materials'),
                                               ('Stationery', 'Pens, pencils, notebooks, and office supplies'),
                                               ('Electronics', 'Calculators, USB drives, headphones'),
                                               ('Fashion', 'Clothes, shoes, accessories'),
                                               ('Toys', 'Toy cars, Hot Wheels, accessories'),
                                               ('Sports', 'Sports equipment and gear'),
                                               ('Other', 'Miscellaneous items');