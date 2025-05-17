CREATE TABLE IF NOT EXISTS restaurant_category (
    id SERIAL PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL,
    added_at TIMESTAMP DEFAULT NOW(),
    is_active BOOLEAN DEFAULT TRUE
);

INSERT INTO restaurant_category (category_name, added_at, is_active) VALUES
('Indonesian', NOW(), true),
('Western', NOW(), true),
('Japanese', NOW(), true),
('Chinese', NOW(), true),
('Korean', NOW(), true),
('Dessert', NOW(), true),
('Drinks', NOW(), true),
('Vegetarian', NOW(), true),
('Halal', NOW(), true),
('Seafood', NOW(), true);
