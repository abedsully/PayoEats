CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS orders (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    created_date DATE,
    order_time TIMESTAMPTZ,
    order_message VARCHAR(255),
    is_active BOOLEAN,
    order_status VARCHAR(255),
    restaurant_id UUID,
    payment_begin_at TIMESTAMPTZ,
    sub_total DOUBLE PRECISION,
    total_price DOUBLE PRECISION,
    tax_price DOUBLE PRECISION,
    cancellation_reason VARCHAR(255),
    dine_in_time TIME,
    payment_image_url TEXT,
    payment_image_rejection_reason VARCHAR(255),
    payment_image_rejection_count BIGINT,
    payment_status VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS order_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    menu_code UUID NOT NULL,
    quantity BIGINT,
    order_id UUID NOT NULL
);


CREATE TABLE IF NOT EXISTS restaurant_approval (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    restaurant_id UUID NOT NULL,
    restaurant_name VARCHAR(255),
    restaurant_image_url VARCHAR(255),
    user_id BIGINT,
    reason TEXT,
    requested_at TIMESTAMPTZ,
    processed_at TIMESTAMPTZ,
    is_approved BOOLEAN,
    is_active BOOLEAN
);


CREATE TABLE IF NOT EXISTS review (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    review_content TEXT,
    user_id BIGINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_active BOOLEAN,
    restaurant_id UUID,
    rating DOUBLE PRECISION,
    review_image_url VARCHAR(255)
);


CREATE TABLE IF NOT EXISTS user_roles (
    role_id BIGINT PRIMARY KEY,
    role_name VARCHAR(255)
);


CREATE TABLE IF NOT EXISTS verification_token (
    id BIGINT PRIMARY KEY,
    token VARCHAR(255),
    user_id BIGINT,
    expiry_date TIMESTAMPTZ,
    type CHAR(1)
);


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
    total_rating INTEGER,
    description TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    user_id INTEGER,
    opening_hour TIME,
    closing_hour TIME,
    location VARCHAR(255),
    telephone_number VARCHAR(50),
    qris_image_url TEXT,
    color VARCHAR(20),
    restaurant_category INTEGER,
    FOREIGN KEY (restaurant_category) REFERENCES restaurant_category(id),
    tax INTEGER,
    restaurant_image_url TEXT
);

INSERT INTO restaurant (id, name, rating, total_rating, description, created_at, updated_at, is_active, user_id, opening_hour, closing_hour, location, telephone_number, qris_image_url, color, restaurant_category, tax, restaurant_image_url)
VALUES
  ('550e8400-e29b-41d4-a716-446655440000', 'Sunset Grill', 0.0, 0, 'Cozy rooftop grill with city views.', '2024-01-01T10:00:00', '2025-05-01T09:30:00', true, 101, '10:00:00', '22:00:00', '123 Skyline Ave', '555-1234', '7fa459ea-ee8a-3ca4-894e-db77e160355f', '#FF5733', 1, 10, 'https://images.unsplash.com/photo-1579599187352-8d76d8b39d48?w=900&auto=format&fit=crop&q=60'),

  ('550e8400-e29b-41d4-a716-446655440001', 'Green Garden', 0.0, 0, 'Organic vegan restaurant.', '2024-02-15T08:30:00', '2025-04-20T14:00:00', true, 102, '09:00:00', '21:00:00', '456 Green St', '555-5678', '7fa459ea-ee8a-3ca4-894e-db77e1603552', '#4CAF50', 2, 10, 'https://images.unsplash.com/photo-1543353071-873f17a7a084?w=900&auto=format&fit=crop&q=60'),

  ('550e8400-e29b-41d4-a716-446655440002', 'Ocean Breeze', 0.0, 0, 'Seafood by the bay.', '2023-11-11T12:00:00', '2025-03-30T11:00:00', true, 103, '11:00:00', '23:00:00', '789 Ocean Drive', '555-8765', '7fa459ea-ee8a-3ca4-894e-db77e1603554', '#00BCD4', 3, 10, 'https://images.unsplash.com/photo-1555939221-a3f81e36398b?w=900&auto=format&fit=crop&q=60'),

  ('550e8400-e29b-41d4-a716-446655440003', 'Night Owl Diner', 0.0, 0, 'Open late for all-night cravings.', '2024-05-01T19:00:00', '2025-05-16T21:00:00', true, 104, '18:00:00', '03:00:00', '321 Moonlight Blvd', '555-8765', '7fa459ea-ee8a-3ca4-894e-db77e1603556', '#9C27B0', 4, 10, 'https://images.unsplash.com/photo-1541737470557-0ce0c5d79905?w=900&auto=format&fit=crop&q=60'),

  ('550e8400-e29b-41d4-a716-446655440004', 'The Burger Spot', 0.0, 0, 'Burgers and fries all day.', '2024-03-10T10:00:00', '2025-05-10T18:00:00', true, 105, '10:00:00', '22:00:00', '987 Burger Lane', '555-3333', '7fa459ea-ee8a-3ca4-894e-db77e1603558', '#FFC107', 5, 10, 'https://images.unsplash.com/photo-1582234057962-d965e6d8a393?w=900&auto=format&fit=crop&q=60'),

  ('550e8400-e29b-41d4-a716-446655440005', 'Pasta Palace', 0.0, 0, 'Authentic Italian pasta dishes.', '2023-12-01T11:30:00', '2025-05-01T12:00:00', true, 106, '11:30:00', '22:30:00', '111 Rome Ave', '555-1122', '7fa459ea-ee8a-3ca4-894e-db77e1603550', '#E91E63', 1, 10, 'https://images.unsplash.com/photo-1574765691475-430349479b12?w=900&auto=format&fit=crop&q=60'),

  ('550e8400-e29b-41d4-a716-446655440006', 'Taco Fiesta', 0.0, 0, 'Mexican street food favorites.', '2024-04-05T12:00:00', '2025-04-25T15:00:00', true, 107, '12:00:00', '20:00:00', '222 Salsa St', '555-4455', '7fa459ea-ee8a-3ca4-894e-db77e160355b', '#FF9800', 2, 10, 'https://images.unsplash.com/photo-1552331584-383794b63e1e?w=900&auto=format&fit=crop&q=60'),

  ('550e8400-e29b-41d4-a716-446655440007', 'Sushi World', 0.0, 0, 'Fresh sushi and sashimi.', '2024-01-20T09:00:00', '2025-05-10T14:00:00', true, 108, '09:00:00', '21:30:00', '333 Tokyo Lane', '555-7788', '7fa459ea-ee8a-3ca4-894e-db77e160355d', '#03A9F4', 3, 10, 'https://images.unsplash.com/photo-1553621042-f6e147245786?w=900&auto=format&fit=crop&q=60'),

  ('550e8400-e29b-41d4-a716-446655440008', 'Cafe Morning', 0.0, 0, 'Perfect for breakfast and brunch.', '2024-02-05T07:00:00', '2025-03-15T10:30:00', true, 109, '07:00:00', '14:00:00', '444 Sunrise Way', '555-7788', '7fa459ea-ee8a-3ca4-894e-db77e160355f', '#8BC34A', 4, 10, 'https://images.unsplash.com/photo-1533965902409-5435987a032f?w=900&auto=format&fit=crop&q=60'),

  ('550e8400-e29b-41d4-a716-446655440009', 'BBQ Brothers', 0.0, 0, 'Barbecue meats and smoked dishes.', '2024-01-12T15:00:00', '2025-05-01T17:00:00', true, 110, '15:00:00', '23:00:00', '555 Smokehouse Ave', '555-5566', '7fa459ea-ee8a-3ca4-894e-db77e1603552', '#795548', 5, 10, 'https://images.unsplash.com/photo-1610609384795-3c2890656001?w=900&auto=format&fit=crop&q=60');

CREATE TABLE IF NOT EXISTS menu (
    menu_code UUID PRIMARY KEY,
    menu_name VARCHAR(255) NOT NULL,
    menu_detail TEXT,
    menu_price NUMERIC(10, 2),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    restaurant_id UUID,
    menu_image_url TEXT
);

INSERT INTO menu (menu_code, menu_name, menu_detail, menu_price, created_at, updated_at, is_active, restaurant_id, menu_image_url) VALUES
('a0010000-0000-0000-0000-000000000001', 'Grilled Ribeye', 'Juicy ribeye steak grilled to perfection.', 25.99, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440000', 'https://images.unsplash.com/photo-1600891964599-f61ba0e24092'),
('a0010000-0000-0000-0000-000000000002', 'BBQ Chicken', 'Charcoal grilled chicken with BBQ sauce.', 14.99, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440000', 'https://cheflindseyfarr.com/wp-content/uploads/2013/04/baked-bbq-chicken-balck-skillet.jpg'),
('a0010000-0000-0000-0000-000000000003', 'Vegan Buddha Bowl', 'Quinoa, tofu, and fresh vegetables.', 12.50, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440001', 'https://www.crazyvegankitchen.com/wp-content/uploads/2023/11/buddha-bowl-recipe.jpg'),
('a0010000-0000-0000-0000-000000000004', 'Avocado Toast', 'Whole grain bread with smashed avocado.', 9.75, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440001', 'https://www.allrecipes.com/thmb/8NccFzsaq0_OZPDKmf7Yee-aG78=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/AvocadoToastwithEggFranceC4x3-bb87e3bbf1944657b7db35f1383fabdb.jpg'),
('a0010000-0000-0000-0000-000000000005', 'Grilled Salmon', 'Fresh salmon with lemon butter.', 22.00, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440002', 'https://www.pccmarkets.com/wp-content/uploads/2017/08/pcc-rosemary-grilled-salmon-flo.jpg'),
('a0010000-0000-0000-0000-000000000006', 'Shrimp Tacos', 'Soft tacos filled with spicy shrimp.', 13.00, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440002', 'https://cookingformysoul.com/wp-content/uploads/2025/05/feat-bangbang-shrimp-tacos-min.jpg'),
('a0010000-0000-0000-0000-000000000007', 'Midnight Burger', 'All-beef patty with fried egg.', 11.99, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440003', 'https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/5e1bf3c0-c37c-4d88-b112-c6675e46947e_Go-Biz_20250203_210424.jpeg'),
('a0010000-0000-0000-0000-000000000008', 'Loaded Fries', 'Fries topped with cheese and bacon.', 7.99, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440003', 'https://mastersofkitchen.com/wp-content/uploads/2025/06/Chicken-Loaded-Fries-896x896.webp'),
('a0010000-0000-0000-0000-000000000009', 'Classic Cheeseburger', 'Beef patty, cheese, lettuce, and tomato.', 10.50, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440004', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTuBic-snensuX1_i0HDDxRk3iVv0deCUG0gw&s'),
('a0010000-0000-0000-0000-000000000010', 'Bacon Double Burger', 'Double patty with crispy bacon.', 13.25, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440004', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTV6RjLeVG3RUwZUoj7EnvewJhwG_AthW3ygQ&s'),
('a0010000-0000-0000-0000-000000000011', 'Spaghetti Carbonara', 'Creamy pasta with pancetta.', 15.90, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440005', 'https://www.allrecipes.com/thmb/Vg2cRidr2zcYhWGvPD8M18xM_WY=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/11973-spaghetti-carbonara-ii-DDMFS-4x3-6edea51e421e4457ac0c3269f3be5157.jpg'),
('a0010000-0000-0000-0000-000000000012', 'Fettuccine Alfredo', 'Rich Alfredo sauce with mushrooms.', 14.50, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440005', 'https://www.allrecipes.com/thmb/6iFrYmTh80DMqrMAOYTYKfBawvY=/0x512/filters:no_upscale():max_bytes(150000):strip_icc()/AR-23431-to-die-for-fettuccine-alfredo-DDMFS-beauty-3x4-b64d36c7ff314cb39774e261c5b18352.jpg'),
('a0010000-0000-0000-0000-000000000013', 'Beef Tacos', 'Corn tortillas with spicy beef.', 9.99, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440006','https://www.onceuponachef.com/images/2023/08/Beef-Tacos.jpg'),
('a0010000-0000-0000-0000-000000000014', 'Nachos Supreme', 'Loaded nachos with jalapeños.', 8.50, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440006', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTV6RjLeVG3RUwZUoj7EnvewJhwG_AthW3ygQ&s'),
('a0010000-0000-0000-0000-000000000015', 'Salmon Nigiri', 'Fresh salmon over sushi rice.', 6.99, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440007', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQKoiLDP5fJGz1VmNR1b52f-5bTt4KDjRwBSA&s'),
('a0010000-0000-0000-0000-000000000016', 'Sushi Platter', 'Assorted sushi and sashimi.', 18.75, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440007', 'https://mastersofkitchen.com/wp-content/uploads/2025/06/Chicken-Loaded-Fries-896x896.webp'),
('a0010000-0000-0000-0000-000000000017', 'Pancake Stack', 'Fluffy pancakes with maple syrup.', 7.80, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440008', 'https://mastersofkitchen.com/wp-content/uploads/2025/06/Chicken-Loaded-Fries-896x896.webp'),
('a0010000-0000-0000-0000-000000000018', 'Eggs Benedict', 'Poached eggs on English muffin.', 10.25, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440008', 'https://mastersofkitchen.com/wp-content/uploads/2025/06/Chicken-Loaded-Fries-896x896.webp'),
('a0010000-0000-0000-0000-000000000019', 'BBQ Ribs', 'Slow-cooked pork ribs with sauce.', 19.99, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440009', 'https://mastersofkitchen.com/wp-content/uploads/2025/06/Chicken-Loaded-Fries-896x896.webp'),
('a0010000-0000-0000-0000-000000000020', 'Smoked Brisket', 'Tender brisket smoked overnight.', 21.50, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440009', 'https://mastersofkitchen.com/wp-content/uploads/2025/06/Chicken-Loaded-Fries-896x896.webp');


CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    role_id VARCHAR(50) NOT NULL,
    password VARCHAR(256) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_active BOOLEAN DEFAULT false,
    confirmation_token VARCHAR(255),
    restaurant_name VARCHAR(255)
);

INSERT INTO users (id, username, email, role_id, password, created_at, updated_at, is_active, confirmation_token, restaurant_name) VALUES
(101, NULL, 'sunsetgrill@example.com', 2, '$2y$10$KpQLzT3IUA4ZY.eCU8So/OpheuoEPuFxHt/vB244uiN8TSxjlN9dm', '2024-01-01T10:00:00', '2025-05-01T09:30:00', true, 'restaurant_token_1', 'Sunset Grill'),
(102, NULL, 'greengarden@example.com', 2, 'hashed_password_2', '2024-02-15T08:30:00', '2025-04-20T14:00:00', true, 'token_2', 'Green Garden'),
(103, NULL, 'oceanbreeze@example.com', 2, 'hashed_password_3', '2023-11-11T12:00:00', '2025-03-30T11:00:00', true, 'token_3', 'Ocean Breeze'),
(104, NULL, 'nightowldiner@example.com', 2, 'hashed_password_4', '2024-05-01T19:00:00', '2025-05-16T21:00:00', true, 'token_4', 'Night Owl Diner'),
(105, NULL, 'burgerspot@example.com', 2, 'hashed_password_5', '2024-03-10T10:00:00', '2025-05-10T18:00:00', true, 'token_5', 'The Burger Spot'),
(106, NULL, 'pastapalace@example.com', 2, 'hashed_password_6', '2023-12-01T11:30:00', '2025-05-01T12:00:00', true, 'token_6', 'Pasta Palace'),
(107, NULL, 'tacofiesta@example.com', 2, 'hashed_password_7', '2024-04-05T12:00:00', '2025-04-25T15:00:00', true, 'token_7', 'Taco Fiesta'),
(108, NULL, 'sushiworld@example.com', 2, 'hashed_password_8', '2024-01-20T09:00:00', '2025-05-10T14:00:00', true, 'token_8', 'Sushi World'),
(109, NULL, 'cafemorning@example.com', 2, 'hashed_password_9', '2024-02-05T07:00:00', '2025-03-15T10:30:00', true, 'token_9', 'Cafe Morning'),
(110, NULL, 'bbqbrothers@example.com', 2, 'hashed_password_10', '2024-01-12T15:00:00', '2025-05-01T17:00:00', true, 'token_10', 'BBQ Brothers'),
(111, 'admin', 'test@gmail.com', 1, 'hashed_test1234', '2024-01-01T10:00:00', '2025-05-01T09:30:00', true, 'admin_token', NULL);

INSERT INTO orders (id, created_date, order_time, is_active, order_status, restaurant_id, sub_total, total_price, tax_price, payment_status) VALUES
('d0000001', CURRENT_DATE, CURRENT_TIMESTAMP, true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 51.98, 57.18, 5.20, 'PAID'),
('d0000002', CURRENT_DATE, CURRENT_TIMESTAMP, true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 25.99, 28.59, 2.60, 'PAID'),
('d0000003', CURRENT_DATE, CURRENT_TIMESTAMP, true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 44.97, 49.47, 4.50, 'PAID'),
('d0000004', CURRENT_DATE, CURRENT_TIMESTAMP, true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 14.99, 16.49, 1.50, 'PAID'),
('d0000005', CURRENT_DATE, CURRENT_TIMESTAMP, true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 77.97, 85.77, 7.80, 'PAID'),
('d0000006', CURRENT_DATE, CURRENT_TIMESTAMP, true, 'CANCELLED', '550e8400-e29b-41d4-a716-446655440000', 25.99, 28.59, 2.60, 'REFUNDED'),
('d0000007', CURRENT_DATE - 1, CURRENT_TIMESTAMP - INTERVAL '1 day', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 40.98, 45.08, 4.10, 'PAID'),
('d0000008', CURRENT_DATE - 1, CURRENT_TIMESTAMP - INTERVAL '1 day', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 29.98, 32.98, 3.00, 'PAID'),
('d0000009', CURRENT_DATE - 1, CURRENT_TIMESTAMP - INTERVAL '1 day', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 25.99, 28.59, 2.60, 'PAID'),
('d0000010', CURRENT_DATE - 1, CURRENT_TIMESTAMP - INTERVAL '1 day', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 51.98, 57.18, 5.20, 'PAID'),
('d0000011', CURRENT_DATE - 1, CURRENT_TIMESTAMP - INTERVAL '1 day', true, 'CANCELLED', '550e8400-e29b-41d4-a716-446655440000', 14.99, 16.49, 1.50, 'REFUNDED'),
('d0000012', CURRENT_DATE - 2, CURRENT_TIMESTAMP - INTERVAL '2 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 66.96, 73.66, 6.70, 'PAID'),
('d0000013', CURRENT_DATE - 2, CURRENT_TIMESTAMP - INTERVAL '2 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 14.99, 16.49, 1.50, 'PAID'),
('d0000014', CURRENT_DATE - 3, CURRENT_TIMESTAMP - INTERVAL '3 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 51.98, 57.18, 5.20, 'PAID'),
('d0000015', CURRENT_DATE - 3, CURRENT_TIMESTAMP - INTERVAL '3 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 40.98, 45.08, 4.10, 'PAID'),
('d0000016', CURRENT_DATE - 3, CURRENT_TIMESTAMP - INTERVAL '3 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 25.99, 28.59, 2.60, 'PAID'),
('d0000017', CURRENT_DATE - 4, CURRENT_TIMESTAMP - INTERVAL '4 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 29.98, 32.98, 3.00, 'PAID'),
('d0000018', CURRENT_DATE - 4, CURRENT_TIMESTAMP - INTERVAL '4 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 77.97, 85.77, 7.80, 'PAID'),
('d0000019', CURRENT_DATE - 4, CURRENT_TIMESTAMP - INTERVAL '4 days', true, 'CANCELLED', '550e8400-e29b-41d4-a716-446655440000', 25.99, 28.59, 2.60, 'REFUNDED'),
('d0000020', CURRENT_DATE - 5, CURRENT_TIMESTAMP - INTERVAL '5 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 51.98, 57.18, 5.20, 'PAID'),
('d0000021', CURRENT_DATE - 5, CURRENT_TIMESTAMP - INTERVAL '5 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 14.99, 16.49, 1.50, 'PAID'),
('d0000022', CURRENT_DATE - 5, CURRENT_TIMESTAMP - INTERVAL '5 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 25.99, 28.59, 2.60, 'PAID'),
('d0000023', CURRENT_DATE - 6, CURRENT_TIMESTAMP - INTERVAL '6 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 40.98, 45.08, 4.10, 'PAID'),
('d0000024', CURRENT_DATE - 6, CURRENT_TIMESTAMP - INTERVAL '6 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 66.96, 73.66, 6.70, 'PAID'),
('d0000025', CURRENT_DATE - 7, CURRENT_TIMESTAMP - INTERVAL '7 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 51.98, 57.18, 5.20, 'PAID'),
('d0000026', CURRENT_DATE - 7, CURRENT_TIMESTAMP - INTERVAL '7 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 29.98, 32.98, 3.00, 'PAID'),
('d0000027', CURRENT_DATE - 8, CURRENT_TIMESTAMP - INTERVAL '8 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 77.97, 85.77, 7.80, 'PAID'),
('d0000028', CURRENT_DATE - 8, CURRENT_TIMESTAMP - INTERVAL '8 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 40.98, 45.08, 4.10, 'PAID'),
('d0000029', CURRENT_DATE - 14, CURRENT_TIMESTAMP - INTERVAL '14 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 51.98, 57.18, 5.20, 'PAID'),
('d0000030', CURRENT_DATE - 14, CURRENT_TIMESTAMP - INTERVAL '14 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 25.99, 28.59, 2.60, 'PAID'),
('d0000031', CURRENT_DATE - 20, CURRENT_TIMESTAMP - INTERVAL '20 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 66.96, 73.66, 6.70, 'PAID'),
('d0000032', CURRENT_DATE - 20, CURRENT_TIMESTAMP - INTERVAL '20 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 14.99, 16.49, 1.50, 'PAID'),
('d0000033', CURRENT_DATE - 20, CURRENT_TIMESTAMP - INTERVAL '20 days', true, 'CANCELLED', '550e8400-e29b-41d4-a716-446655440000', 51.98, 57.18, 5.20, 'REFUNDED'),
('d0000034', CURRENT_DATE - 25, CURRENT_TIMESTAMP - INTERVAL '25 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 40.98, 45.08, 4.10, 'PAID'),
('d0000035', CURRENT_DATE - 25, CURRENT_TIMESTAMP - INTERVAL '25 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 29.98, 32.98, 3.00, 'PAID'),
('d0000036', CURRENT_DATE - 29, CURRENT_TIMESTAMP - INTERVAL '29 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 51.98, 57.18, 5.20, 'PAID'),
('d0000037', CURRENT_DATE - 29, CURRENT_TIMESTAMP - INTERVAL '29 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 25.99, 28.59, 2.60, 'PAID'),
('d0000038', CURRENT_DATE - 31, CURRENT_TIMESTAMP - INTERVAL '31 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 77.97, 85.77, 7.80, 'PAID'),
('d0000039', CURRENT_DATE - 31, CURRENT_TIMESTAMP - INTERVAL '31 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 40.98, 45.08, 4.10, 'PAID'),
('d0000040', CURRENT_DATE - 35, CURRENT_TIMESTAMP - INTERVAL '35 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 51.98, 57.18, 5.20, 'PAID'),
('d0000041', CURRENT_DATE - 35, CURRENT_TIMESTAMP - INTERVAL '35 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 66.96, 73.66, 6.70, 'PAID'),
('d0000042', CURRENT_DATE - 40, CURRENT_TIMESTAMP - INTERVAL '40 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 29.98, 32.98, 3.00, 'PAID'),
('d0000043', CURRENT_DATE - 40, CURRENT_TIMESTAMP - INTERVAL '40 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 14.99, 16.49, 1.50, 'PAID'),
('d0000044', CURRENT_DATE - 45, CURRENT_TIMESTAMP - INTERVAL '45 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 51.98, 57.18, 5.20, 'PAID'),
('d0000045', CURRENT_DATE - 45, CURRENT_TIMESTAMP - INTERVAL '45 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 40.98, 45.08, 4.10, 'PAID'),
('d0000046', CURRENT_DATE - 45, CURRENT_TIMESTAMP - INTERVAL '45 days', true, 'CANCELLED', '550e8400-e29b-41d4-a716-446655440000', 25.99, 28.59, 2.60, 'REFUNDED'),
('d0000047', CURRENT_DATE - 50, CURRENT_TIMESTAMP - INTERVAL '50 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 77.97, 85.77, 7.80, 'PAID'),
('d0000048', CURRENT_DATE - 50, CURRENT_TIMESTAMP - INTERVAL '50 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 25.99, 28.59, 2.60, 'PAID'),
('d0000049', CURRENT_DATE - 55, CURRENT_TIMESTAMP - INTERVAL '55 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 66.96, 73.66, 6.70, 'PAID'),
('d0000050', CURRENT_DATE - 55, CURRENT_TIMESTAMP - INTERVAL '55 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 51.98, 57.18, 5.20, 'PAID'),
('d0000051', CURRENT_DATE - 59, CURRENT_TIMESTAMP - INTERVAL '59 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 40.98, 45.08, 4.10, 'PAID'),
('d0000052', CURRENT_DATE - 59, CURRENT_TIMESTAMP - INTERVAL '59 days', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 14.99, 16.49, 1.50, 'PAID');

INSERT INTO order_items (id, menu_code, quantity, order_id) VALUES
('i0000001', 'a0010000-0000-0000-0000-000000000001', 2, 'd0000001'),
('i0000002', 'a0010000-0000-0000-0000-000000000001', 1, 'd0000002'),
('i0000003', 'a0010000-0000-0000-0000-000000000002', 3, 'd0000003'),
('i0000004', 'a0010000-0000-0000-0000-000000000002', 1, 'd0000004'),
('i0000005', 'a0010000-0000-0000-0000-000000000001', 3, 'd0000005'),
('i0000006', 'a0010000-0000-0000-0000-000000000001', 1, 'd0000006'),
('i0000007', 'a0010000-0000-0000-0000-000000000002', 2, 'd0000007'),
('i0000008', 'a0010000-0000-0000-0000-000000000001', 1, 'd0000007'),
('i0000009', 'a0010000-0000-0000-0000-000000000002', 2, 'd0000008'),
('i0000010', 'a0010000-0000-0000-0000-000000000001', 1, 'd0000009'),
('i0000011', 'a0010000-0000-0000-0000-000000000001', 2, 'd0000010'),
('i0000012', 'a0010000-0000-0000-0000-000000000002', 1, 'd0000011'),
('i0000013', 'a0010000-0000-0000-0000-000000000002', 2, 'd0000012'),
('i0000014', 'a0010000-0000-0000-0000-000000000001', 1, 'd0000012'),
('i0000015', 'a0010000-0000-0000-0000-000000000002', 1, 'd0000013'),
('i0000016', 'a0010000-0000-0000-0000-000000000001', 2, 'd0000014'),
('i0000017', 'a0010000-0000-0000-0000-000000000002', 1, 'd0000014'),
('i0000018', 'a0010000-0000-0000-0000-000000000002', 2, 'd0000015'),
('i0000019', 'a0010000-0000-0000-0000-000000000001', 1, 'd0000015'),
('i0000020', 'a0010000-0000-0000-0000-000000000001', 1, 'd0000016'),
('i0000021', 'a0010000-0000-0000-0000-000000000002', 2, 'd0000017'),
('i0000022', 'a0010000-0000-0000-0000-000000000001', 3, 'd0000018'),
('i0000023', 'a0010000-0000-0000-0000-000000000001', 1, 'd0000019'),
('i0000024', 'a0010000-0000-0000-0000-000000000001', 2, 'd0000020'),
('i0000025', 'a0010000-0000-0000-0000-000000000002', 1, 'd0000021'),
('i0000026', 'a0010000-0000-0000-0000-000000000001', 1, 'd0000022'),
('i0000027', 'a0010000-0000-0000-0000-000000000002', 2, 'd0000023'),
('i0000028', 'a0010000-0000-0000-0000-000000000001', 1, 'd0000023'),
('i0000029', 'a0010000-0000-0000-0000-000000000002', 2, 'd0000024'),
('i0000030', 'a0010000-0000-0000-0000-000000000001', 1, 'd0000024'),
('i0000031', 'a0010000-0000-0000-0000-000000000001', 2, 'd0000025'),
('i0000032', 'a0010000-0000-0000-0000-000000000002', 2, 'd0000026'),
('i0000033', 'a0010000-0000-0000-0000-000000000001', 3, 'd0000027'),
('i0000034', 'a0010000-0000-0000-0000-000000000002', 2, 'd0000028'),
('i0000035', 'a0010000-0000-0000-0000-000000000001', 1, 'd0000028'),
('i0000036', 'a0010000-0000-0000-0000-000000000001', 2, 'd0000029'),
('i0000037', 'a0010000-0000-0000-0000-000000000001', 1, 'd0000030'),
('i0000038', 'a0010000-0000-0000-0000-000000000002', 2, 'd0000031'),
('i0000039', 'a0010000-0000-0000-0000-000000000001', 1, 'd0000031'),
('i0000040', 'a0010000-0000-0000-0000-000000000002', 1, 'd0000032'),
('i0000041', 'a0010000-0000-0000-0000-000000000001', 2, 'd0000033'),
('i0000042', 'a0010000-0000-0000-0000-000000000002', 2, 'd0000034'),
('i0000043', 'a0010000-0000-0000-0000-000000000001', 1, 'd0000034'),
('i0000044', 'a0010000-0000-0000-0000-000000000002', 2, 'd0000035'),
('i0000045', 'a0010000-0000-0000-0000-000000000001', 2, 'd0000036'),
('i0000046', 'a0010000-0000-0000-0000-000000000001', 1, 'd0000037'),
('i0000047', 'a0010000-0000-0000-0000-000000000001', 3, 'd0000038'),
('i0000048', 'a0010000-0000-0000-0000-000000000002', 2, 'd0000039'),
('i0000049', 'a0010000-0000-0000-0000-000000000001', 1, 'd0000039'),
('i0000050', 'a0010000-0000-0000-0000-000000000001', 2, 'd0000040'),
('i0000051', 'a0010000-0000-0000-0000-000000000002', 2, 'd0000041'),
('i0000052', 'a0010000-0000-0000-0000-000000000001', 1, 'd0000041'),
('i0000053', 'a0010000-0000-0000-0000-000000000002', 2, 'd0000042'),
('i0000054', 'a0010000-0000-0000-0000-000000000002', 1, 'd0000043'),
('i0000055', 'a0010000-0000-0000-0000-000000000001', 2, 'd0000044'),
('i0000056', 'a0010000-0000-0000-0000-000000000002', 2, 'd0000045'),
('i0000057', 'a0010000-0000-0000-0000-000000000001', 1, 'd0000045'),
('i0000058', 'a0010000-0000-0000-0000-000000000001', 1, 'd0000046'),
('i0000059', 'a0010000-0000-0000-0000-000000000001', 3, 'd0000047'),
('i0000060', 'a0010000-0000-0000-0000-000000000001', 1, 'd0000048'),
('i0000061', 'a0010000-0000-0000-0000-000000000002', 2, 'd0000049'),
('i0000062', 'a0010000-0000-0000-0000-000000000001', 1, 'd0000049'),
('i0000063', 'a0010000-0000-0000-0000-000000000001', 2, 'd0000050'),
('i0000064', 'a0010000-0000-0000-0000-000000000002', 2, 'd0000051'),
('i0000065', 'a0010000-0000-0000-0000-000000000001', 1, 'd0000051'),
('i0000066', 'a0010000-0000-0000-0000-000000000002', 1, 'd0000052');
