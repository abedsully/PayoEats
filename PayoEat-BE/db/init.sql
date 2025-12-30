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
    restaurant_image_url TEXT
);

INSERT INTO restaurant (id, name, rating, total_rating, description, created_at, updated_at, is_active, user_id, opening_hour, closing_hour, location, telephone_number, qris_image_url, color, restaurant_category, restaurant_image_url)
VALUES
  ('550e8400-e29b-41d4-a716-446655440000', 'Sunset Grill', 0.0, 0, 'Cozy rooftop grill with city views.', '2024-01-01T10:00:00', '2025-05-01T09:30:00', true, 101, '10:00:00', '22:00:00', '123 Skyline Ave', '555-1234', '7fa459ea-ee8a-3ca4-894e-db77e160355f', '#FF5733', 1, 'https://images.unsplash.com/photo-1579599187352-8d76d8b39d48?w=900&auto=format&fit=crop&q=60'),

  ('550e8400-e29b-41d4-a716-446655440001', 'Green Garden', 0.0, 0, 'Organic vegan restaurant.', '2024-02-15T08:30:00', '2025-04-20T14:00:00', true, 102, '09:00:00', '21:00:00', '456 Green St', '555-5678', '7fa459ea-ee8a-3ca4-894e-db77e1603552', '#4CAF50', 2, 'https://images.unsplash.com/photo-1543353071-873f17a7a084?w=900&auto=format&fit=crop&q=60'),

  ('550e8400-e29b-41d4-a716-446655440002', 'Ocean Breeze', 0.0, 0, 'Seafood by the bay.', '2023-11-11T12:00:00', '2025-03-30T11:00:00', true, 103, '11:00:00', '23:00:00', '789 Ocean Drive', '555-8765', '7fa459ea-ee8a-3ca4-894e-db77e1603554', '#00BCD4', 3, 'https://images.unsplash.com/photo-1555939221-a3f81e36398b?w=900&auto=format&fit=crop&q=60'),

  ('550e8400-e29b-41d4-a716-446655440003', 'Night Owl Diner', 0.0, 0, 'Open late for all-night cravings.', '2024-05-01T19:00:00', '2025-05-16T21:00:00', true, 104, '18:00:00', '03:00:00', '321 Moonlight Blvd', '555-8765', '7fa459ea-ee8a-3ca4-894e-db77e1603556', '#9C27B0', 4, 'https://images.unsplash.com/photo-1541737470557-0ce0c5d79905?w=900&auto=format&fit=crop&q=60'),

  ('550e8400-e29b-41d4-a716-446655440004', 'The Burger Spot', 0.0, 0, 'Burgers and fries all day.', '2024-03-10T10:00:00', '2025-05-10T18:00:00', true, 105, '10:00:00', '22:00:00', '987 Burger Lane', '555-3333', '7fa459ea-ee8a-3ca4-894e-db77e1603558', '#FFC107', 5, 'https://images.unsplash.com/photo-1582234057962-d965e6d8a393?w=900&auto=format&fit=crop&q=60'),

  ('550e8400-e29b-41d4-a716-446655440005', 'Pasta Palace', 0.0, 0, 'Authentic Italian pasta dishes.', '2023-12-01T11:30:00', '2025-05-01T12:00:00', true, 106, '11:30:00', '22:30:00', '111 Rome Ave', '555-1122', '7fa459ea-ee8a-3ca4-894e-db77e1603550', '#E91E63', 1, 'https://images.unsplash.com/photo-1574765691475-430349479b12?w=900&auto=format&fit=crop&q=60'),

  ('550e8400-e29b-41d4-a716-446655440006', 'Taco Fiesta', 0.0, 0, 'Mexican street food favorites.', '2024-04-05T12:00:00', '2025-04-25T15:00:00', true, 107, '12:00:00', '20:00:00', '222 Salsa St', '555-4455', '7fa459ea-ee8a-3ca4-894e-db77e160355b', '#FF9800', 2, 'https://images.unsplash.com/photo-1552331584-383794b63e1e?w=900&auto=format&fit=crop&q=60'),

  ('550e8400-e29b-41d4-a716-446655440007', 'Sushi World', 0.0, 0, 'Fresh sushi and sashimi.', '2024-01-20T09:00:00', '2025-05-10T14:00:00', true, 108, '09:00:00', '21:30:00', '333 Tokyo Lane', '555-7788', '7fa459ea-ee8a-3ca4-894e-db77e160355d', '#03A9F4', 3, 'https://images.unsplash.com/photo-1553621042-f6e147245786?w=900&auto=format&fit=crop&q=60'),

  ('550e8400-e29b-41d4-a716-446655440008', 'Cafe Morning', 0.0, 0, 'Perfect for breakfast and brunch.', '2024-02-05T07:00:00', '2025-03-15T10:30:00', true, 109, '07:00:00', '14:00:00', '444 Sunrise Way', '555-7788', '7fa459ea-ee8a-3ca4-894e-db77e160355f', '#8BC34A', 4, 'https://images.unsplash.com/photo-1533965902409-5435987a032f?w=900&auto=format&fit=crop&q=60'),

  ('550e8400-e29b-41d4-a716-446655440009', 'BBQ Brothers', 0.0, 0, 'Barbecue meats and smoked dishes.', '2024-01-12T15:00:00', '2025-05-01T17:00:00', true, 110, '15:00:00', '23:00:00', '555 Smokehouse Ave', '555-5566', '7fa459ea-ee8a-3ca4-894e-db77e1603552', '#795548', 5, 'https://images.unsplash.com/photo-1610609384795-3c2890656001?w=900&auto=format&fit=crop&q=60');

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

INSERT INTO menu (
    menu_code, menu_name, menu_detail, menu_price,
    created_at, updated_at, is_active, restaurant_id, menu_image_url
)
VALUES
-- Sunset Grill (0000)
('a0010000-0000-0000-0000-000000000001', 'Grilled Ribeye', 'Juicy ribeye steak grilled to perfection.', 25.99, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440000', 'https://images.unsplash.com/photo-1600891964599-f61ba0e24092'),
('a0010000-0000-0000-0000-000000000002', 'BBQ Chicken', 'Charcoal grilled chicken with BBQ sauce.', 14.99, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440000', 'https://cheflindseyfarr.com/wp-content/uploads/2013/04/baked-bbq-chicken-balck-skillet.jpg'),

-- Green Garden (0001)
('a0010000-0000-0000-0000-000000000003', 'Vegan Buddha Bowl', 'Quinoa, tofu, and fresh vegetables.', 12.50, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440001', 'https://www.crazyvegankitchen.com/wp-content/uploads/2023/11/buddha-bowl-recipe.jpg'),
('a0010000-0000-0000-0000-000000000004', 'Avocado Toast', 'Whole grain bread with smashed avocado.', 9.75, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440001', 'https://www.allrecipes.com/thmb/8NccFzsaq0_OZPDKmf7Yee-aG78=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/AvocadoToastwithEggFranceC4x3-bb87e3bbf1944657b7db35f1383fabdb.jpg'),

-- Ocean Delight (0002)
('a0010000-0000-0000-0000-000000000005', 'Grilled Salmon', 'Fresh salmon with lemon butter.', 22.00, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440002', 'https://www.pccmarkets.com/wp-content/uploads/2017/08/pcc-rosemary-grilled-salmon-flo.jpg'),
('a0010000-0000-0000-0000-000000000006', 'Shrimp Tacos', 'Soft tacos filled with spicy shrimp.', 13.00, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440002', 'https://cookingformysoul.com/wp-content/uploads/2025/05/feat-bangbang-shrimp-tacos-min.jpg'),

-- Midnight Diner (0003)
('a0010000-0000-0000-0000-000000000007', 'Midnight Burger', 'All-beef patty with fried egg.', 11.99, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440003', 'https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/5e1bf3c0-c37c-4d88-b112-c6675e46947e_Go-Biz_20250203_210424.jpeg'),
('a0010000-0000-0000-0000-000000000008', 'Loaded Fries', 'Fries topped with cheese and bacon.', 7.99, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440003', 'https://mastersofkitchen.com/wp-content/uploads/2025/06/Chicken-Loaded-Fries-896x896.webp'),

-- Burger Hub (0004)
('a0010000-0000-0000-0000-000000000009', 'Classic Cheeseburger', 'Beef patty, cheese, lettuce, and tomato.', 10.50, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440004', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTuBic-snensuX1_i0HDDxRk3iVv0deCUG0gw&s'),
('a0010000-0000-0000-0000-000000000010', 'Bacon Double Burger', 'Double patty with crispy bacon.', 13.25, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440004', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTV6RjLeVG3RUwZUoj7EnvewJhwG_AthW3ygQ&s'),

-- Pasta House (0005)
('a0010000-0000-0000-0000-000000000011', 'Spaghetti Carbonara', 'Creamy pasta with pancetta.', 15.90, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440005', 'https://www.allrecipes.com/thmb/Vg2cRidr2zcYhWGvPD8M18xM_WY=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/11973-spaghetti-carbonara-ii-DDMFS-4x3-6edea51e421e4457ac0c3269f3be5157.jpg'),
('a0010000-0000-0000-0000-000000000012', 'Fettuccine Alfredo', 'Rich Alfredo sauce with mushrooms.', 14.50, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440005', 'https://www.allrecipes.com/thmb/6iFrYmTh80DMqrMAOYTYKfBawvY=/0x512/filters:no_upscale():max_bytes(150000):strip_icc()/AR-23431-to-die-for-fettuccine-alfredo-DDMFS-beauty-3x4-b64d36c7ff314cb39774e261c5b18352.jpg'),

-- Taco Fiesta (0006)
('a0010000-0000-0000-0000-000000000013', 'Beef Tacos', 'Corn tortillas with spicy beef.', 9.99, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440006','https://www.onceuponachef.com/images/2023/08/Beef-Tacos.jpg'),
('a0010000-0000-0000-0000-000000000014', 'Nachos Supreme', 'Loaded nachos with jalapeños.', 8.50, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440006', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTV6RjLeVG3RUwZUoj7EnvewJhwG_AthW3ygQ&s'),

-- Sushi World (0007)
('a0010000-0000-0000-0000-000000000015', 'Salmon Nigiri', 'Fresh salmon over sushi rice.', 6.99, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440007', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQKoiLDP5fJGz1VmNR1b52f-5bTt4KDjRwBSA&s'),
('a0010000-0000-0000-0000-000000000016', 'Sushi Platter', 'Assorted sushi and sashimi.', 18.75, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440007', 'https://mastersofkitchen.com/wp-content/uploads/2025/06/Chicken-Loaded-Fries-896x896.webp'),

-- Morning Bliss (0008)
('a0010000-0000-0000-0000-000000000017', 'Pancake Stack', 'Fluffy pancakes with maple syrup.', 7.80, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440008', 'https://mastersofkitchen.com/wp-content/uploads/2025/06/Chicken-Loaded-Fries-896x896.webp'),
('a0010000-0000-0000-0000-000000000018', 'Eggs Benedict', 'Poached eggs on English muffin.', 10.25, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440008', 'https://mastersofkitchen.com/wp-content/uploads/2025/06/Chicken-Loaded-Fries-896x896.webp'),

-- Smokehouse BBQ (0009)
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

INSERT INTO users (id, username, email, role_id, password, created_at, updated_at, is_active, confirmation_token, restaurant_name)
VALUES
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