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

CREATE TABLE IF NOT EXISTS restaurant (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    rating NUMERIC(2,1),
    description TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    user_id INTEGER,
    opening_hour TIME,
    closing_hour TIME,
    location VARCHAR(255),
    telephone_number VARCHAR(50),
    restaurant_image UUID,
    qris_image UUID,
    color VARCHAR(20),
    restaurant_category INTEGER,
    FOREIGN KEY (restaurant_category) REFERENCES restaurant_category(id)
);

INSERT INTO restaurant (id, name, rating, description, created_at, updated_at, is_active, user_id, opening_hour, closing_hour, location, telephone_number, restaurant_image, qris_image, color, restaurant_category)
VALUES
  ('550e8400-e29b-41d4-a716-446655440000', 'Sunset Grill', 4.5, 'Cozy rooftop grill with city views.', '2024-01-01T10:00:00', '2025-05-01T09:30:00', true, 101, '10:00:00', '22:00:00', '123 Skyline Ave', '555-1234', '6fa459ea-ee8a-3ca4-894e-db77e160355e', '7fa459ea-ee8a-3ca4-894e-db77e160355f', '#FF5733', 1),

  ('550e8400-e29b-41d4-a716-446655440001', 'Green Garden', 4.7, 'Organic vegan restaurant.', '2024-02-15T08:30:00', '2025-04-20T14:00:00', true, 102, '09:00:00', '21:00:00', '456 Green St', '555-5678', '6fa459ea-ee8a-3ca4-894e-db77e1603551', '7fa459ea-ee8a-3ca4-894e-db77e1603552', '#4CAF50', 2),

  ('550e8400-e29b-41d4-a716-446655440002', 'Ocean Breeze', 4.2, 'Seafood by the bay.', '2023-11-11T12:00:00', '2025-03-30T11:00:00', true, 103, '11:00:00', '23:00:00', '789 Ocean Drive', '555-8765', '6fa459ea-ee8a-3ca4-894e-db77e1603553', '7fa459ea-ee8a-3ca4-894e-db77e1603554', '#00BCD4', 3),

  ('550e8400-e29b-41d4-a716-446655440003', 'Night Owl Diner', 3.9, 'Open late for all-night cravings.', '2024-05-01T19:00:00', '2025-05-16T21:00:00', true, 104, '18:00:00', '03:00:00', '321 Moonlight Blvd', '555-0001', '6fa459ea-ee8a-3ca4-894e-db77e1603555', '7fa459ea-ee8a-3ca4-894e-db77e1603556', '#9C27B0', 4),

  ('550e8400-e29b-41d4-a716-446655440004', 'The Burger Spot', 4.0, 'Burgers and fries all day.', '2024-03-10T10:00:00', '2025-05-10T18:00:00', true, 105, '10:00:00', '22:00:00', '987 Burger Lane', '555-3333', '6fa459ea-ee8a-3ca4-894e-db77e1603557', '7fa459ea-ee8a-3ca4-894e-db77e1603558', '#FFC107', 5),

  ('550e8400-e29b-41d4-a716-446655440005', 'Pasta Palace', 4.6, 'Authentic Italian pasta dishes.', '2023-12-01T11:30:00', '2025-05-01T12:00:00', true, 106, '11:30:00', '22:30:00', '111 Rome Ave', '555-1122', '6fa459ea-ee8a-3ca4-894e-db77e1603559', '7fa459ea-ee8a-3ca4-894e-db77e1603550', '#E91E63', 1),

  ('550e8400-e29b-41d4-a716-446655440006', 'Taco Fiesta', 4.3, 'Mexican street food favorites.', '2024-04-05T12:00:00', '2025-04-25T15:00:00', true, 107, '12:00:00', '20:00:00', '222 Salsa St', '555-4455', '6fa459ea-ee8a-3ca4-894e-db77e160355a', '7fa459ea-ee8a-3ca4-894e-db77e160355b', '#FF9800', 2),

  ('550e8400-e29b-41d4-a716-446655440007', 'Sushi World', 4.8, 'Fresh sushi and sashimi.', '2024-01-20T09:00:00', '2025-05-10T14:00:00', true, 108, '09:00:00', '21:30:00', '333 Tokyo Lane', '555-7788', '6fa459ea-ee8a-3ca4-894e-db77e160355c', '7fa459ea-ee8a-3ca4-894e-db77e160355d', '#03A9F4', 3),

  ('550e8400-e29b-41d4-a716-446655440008', 'Cafe Morning', 4.1, 'Perfect for breakfast and brunch.', '2024-02-05T07:00:00', '2025-03-15T10:30:00', true, 109, '07:00:00', '14:00:00', '444 Sunrise Way', '555-9911', '6fa459ea-ee8a-3ca4-894e-db77e160355e', '7fa459ea-ee8a-3ca4-894e-db77e160355f', '#8BC34A', 4),

  ('550e8400-e29b-41d4-a716-446655440009', 'BBQ Brothers', 4.4, 'Barbecue meats and smoked dishes.', '2024-01-12T15:00:00', '2025-05-01T17:00:00', true, 110, '15:00:00', '23:00:00', '555 Smokehouse Ave', '555-5566', '6fa459ea-ee8a-3ca4-894e-db77e1603551', '7fa459ea-ee8a-3ca4-894e-db77e1603552', '#795548', 5);

