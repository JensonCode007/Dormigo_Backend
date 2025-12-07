-- Create USERS table
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100) NOT NULL,
                       phone_number VARCHAR(20),
                       role VARCHAR(20) NOT NULL CHECK (role IN ('STUDENT', 'ADMIN')),
                       is_active BOOLEAN NOT NULL DEFAULT true,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create index on email for faster login queries
CREATE INDEX idx_users_email ON users(email);

-- Create index on role for faster role-based queries
CREATE INDEX idx_users_role ON users(role);