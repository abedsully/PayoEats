CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

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
    requested_at TIMESTAMP,
    processed_at TIMESTAMP,
    is_approved BOOLEAN,
    is_active BOOLEAN
);


CREATE TABLE IF NOT EXISTS user_roles (
    role_id BIGINT PRIMARY KEY,
    role_name VARCHAR(255)
);


CREATE TABLE IF NOT EXISTS verification_token (
    id BIGINT PRIMARY KEY,
    token VARCHAR(255),
    user_id BIGINT,
    expiry_date TIMESTAMP,
    type CHAR(1)
);


-- Area Insert Restaurant Category
CREATE TABLE IF NOT EXISTS restaurant_category (
    id SERIAL PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL,
    added_at TIMESTAMP DEFAULT NOW(),
    is_active BOOLEAN DEFAULT TRUE
);

INSERT INTO restaurant_category (category_name, added_at, is_active)
SELECT * FROM (VALUES
  ('Indonesian', NOW(), true),
  ('Western', NOW(), true),
  ('Japanese', NOW(), true),
  ('Chinese', NOW(), true),
  ('Korean', NOW(), true),
  ('Dessert', NOW(), true),
  ('Drinks', NOW(), true),
  ('Vegetarian', NOW(), true),
  ('Halal', NOW(), true),
  ('Seafood', NOW(), true)
) AS v(category_name, added_at, is_active)
WHERE NOT EXISTS (SELECT 1 FROM restaurant_category LIMIT 1);
--


-- Area Insert Restaurant
CREATE TABLE IF NOT EXISTS restaurant (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    rating DOUBLE PRECISION,
    total_rating BIGINT,
    description TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    user_id BIGINT,
    opening_hour TIME,
    closing_hour TIME,
    location VARCHAR(255),
    telephone_number VARCHAR(50),
    restaurant_image_url TEXT,
    qris_image_url TEXT,
    color VARCHAR(20),
    restaurant_category BIGINT,
    is_open BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_restaurant_category
        FOREIGN KEY (restaurant_category) REFERENCES restaurant_category(id)
);

INSERT INTO restaurant (id,name,rating,total_rating,description,created_at,updated_at,is_active,user_id,opening_hour,closing_hour,location,telephone_number,restaurant_image_url,qris_image_url,color,restaurant_category,is_open) VALUES
('550e8400-e29b-41d4-a716-446655440000','Sunset Grill',0.0,0,'Cozy rooftop grill with city views.','2024-01-01 10:00:00','2025-05-01 09:30:00',TRUE,101,'10:00:00','22:00:00','123 Skyline Ave','555-1234','https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?fm=jpg&q=60&w=3000','7fa459ea-ee8a-3ca4-894e-db77e160355f','#FF5733',1,TRUE),

('550e8400-e29b-41d4-a716-446655440001','Green Garden',0.0,0,'Organic vegan restaurant.','2024-02-15 08:30:00','2025-04-20 14:00:00',TRUE,102,'09:00:00','21:00:00','456 Green St','555-5678',
'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQJ0EqHI6h5QgFTXGG_1i2FADG1xulRbVtecA&s',
'7fa459ea-ee8a-3ca4-894e-db77e1603552','#4CAF50',2,TRUE),

('550e8400-e29b-41d4-a716-446655440002','Ocean Breeze',0.0,0,'Seafood by the bay.','2023-11-11 12:00:00','2025-03-30 11:00:00',TRUE,103,'11:00:00','23:00:00','789 Ocean Drive','555-8765',
'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRWYNFqbPNDfW6HsOhfMJ5de4XwuteI2sV4Hg&s',
'7fa459ea-ee8a-3ca4-894e-db77e1603554','#00BCD4',3,TRUE),

('550e8400-e29b-41d4-a716-446655440003','Night Owl Diner',0.0,0,'Open late for all-night cravings.','2024-05-01 19:00:00','2025-05-16 21:00:00',TRUE,104,'18:00:00','03:00:00','321 Moonlight Blvd','555-8765',
'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRJTot5NBFQBa7x7e7HXiWluJT2dEVvd7kzyg&s',
'7fa459ea-ee8a-3ca4-894e-db77e1603556','#9C27B0',4,TRUE),

('550e8400-e29b-41d4-a716-446655440004','The Burger Spot',0.0,0,'Burgers and fries all day.','2024-03-10 10:00:00','2025-05-10 18:00:00',TRUE,105,'10:00:00','22:00:00','987 Burger Lane','555-3333',
'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSKTqY5iJ0QxYi6QY-e0Ny1BS-i6-vD2BFSJw&s',
'7fa459ea-ee8a-3ca4-894e-db77e1603558','#FFC107',5,TRUE),

('550e8400-e29b-41d4-a716-446655440005','Pasta Palace',0.0,0,'Authentic Italian pasta dishes.','2023-12-01 11:30:00','2025-05-01 12:00:00',TRUE,106,'11:30:00','22:30:00','111 Rome Ave','555-1122',
'https://dynamic-media-cdn.tripadvisor.com/media/photo-o/21/b8/eb/3a/tom-s.jpg',
'7fa459ea-ee8a-3ca4-894e-db77e1603550','#E91E63',1,TRUE),

('550e8400-e29b-41d4-a716-446655440006','Taco Fiesta',0.0,0,'Mexican street food favorites.','2024-04-05 12:00:00','2025-04-25 15:00:00',TRUE,107,'12:00:00','20:00:00','222 Salsa St','555-4455',
'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTxOSozKIEG5lTTXV9DXCgv-NWGcWRbpePq8w&s',
'7fa459ea-ee8a-3ca4-894e-db77e160355b','#FF9800',2,TRUE),

('550e8400-e29b-41d4-a716-446655440007','Sushi World',0.0,0,'Fresh sushi and sashimi.','2024-01-20 09:00:00','2025-05-10 14:00:00',TRUE,108,'09:00:00','21:30:00','333 Tokyo Lane','555-7788',
'https://dynamic-media-cdn.tripadvisor.com/media/photo-o/2b/09/bb/94/caption.jpg?w=400&h=300&s=1',
'7fa459ea-ee8a-3ca4-894e-db77e160355d','#03A9F4',3,TRUE),

('550e8400-e29b-41d4-a716-446655440008','Cafe Morning',0.0,0,'Perfect for breakfast and brunch.','2024-02-05 07:00:00','2025-03-15 10:30:00',TRUE,109,'07:00:00','14:00:00','444 Sunrise Way','555-7788',
'https://dynamic-media-cdn.tripadvisor.com/media/photo-o/22/a2/41/6e/caption.jpg?w=1200&h=1200&s=1',
'7fa459ea-ee8a-3ca4-894e-db77e160355f','#8BC34A',4,TRUE),

('550e8400-e29b-41d4-a716-446655440009','BBQ Brothers',0.0,0,'Barbecue meats and smoked dishes.','2024-01-12 15:00:00','2025-05-01 17:00:00',TRUE,110,'15:00:00','23:00:00','555 Smokehouse Ave','555-5566',
'https://dynamic-media-cdn.tripadvisor.com/media/photo-o/25/d5/4e/f6/miss-thu-is-a-lady-of.jpg?w=900&h=500&s=1',
'7fa459ea-ee8a-3ca4-894e-db77e1603552','#795548',5,TRUE);

--

CREATE TABLE IF NOT EXISTS orders (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    created_date DATE,
    order_time TIMESTAMPTZ,
    order_message VARCHAR(255),
    payment_confirmed_at TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    order_status VARCHAR(50),
    restaurant_id UUID,
    customer_id VARCHAR(100),
    payment_begin_at TIMESTAMP,
    payment_uploaded_at TIMESTAMP,
    sub_total DOUBLE PRECISION,
    total_price DOUBLE PRECISION,
    cancellation_reason VARCHAR(255),
    dine_in_time TIME,
    payment_image_url TEXT,
    payment_image_rejection_reason VARCHAR(255),
    payment_image_rejection_count BIGINT,
    payment_status VARCHAR(50),
    customer_name VARCHAR(255),
    scheduled_check_in_time TIMESTAMP,
    CONSTRAINT fk_orders_restaurant
        FOREIGN KEY (restaurant_id) REFERENCES restaurant(id)
);

CREATE TABLE IF NOT EXISTS order_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    menu_code UUID NOT NULL,
    quantity BIGINT NOT NULL,
    order_id UUID NOT NULL,
    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id) REFERENCES orders(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS review (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    review_content TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    restaurant_id UUID,
    rating DOUBLE PRECISION,
    customer_name VARCHAR(100),
    review_image_url TEXT,
    order_id UUID,
    customer_id VARCHAR(100),
    CONSTRAINT fk_review_restaurant
        FOREIGN KEY (restaurant_id) REFERENCES restaurant(id)
);

CREATE TABLE IF NOT EXISTS user_roles (
    role_id BIGINT PRIMARY KEY,
    role_name VARCHAR(255)
);

INSERT INTO user_roles
(role_id, role_name)
VALUES(1, 'ADMIN');

INSERT INTO user_roles
(role_id, role_name)
VALUES(2, 'RESTAURANT');

CREATE TABLE IF NOT EXISTS verification_token (
    id SERIAL PRIMARY KEY,
    token VARCHAR(255),
    user_id BIGINT,
    expiry_date TIMESTAMP,
    type CHAR(1)
);


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
('a0010000-0000-0000-0000-000000000100', 'Smoked Brisket', 'Slow-smoked beef brisket.', 88000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440000', 'https://images.unsplash.com/photo-1544025162-d76694265947?q=80&w=1990&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'),
('a0010000-0000-0000-0000-000000000101', 'Grilled Salmon', 'Fresh Atlantic salmon with herbs.', 82000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440000', 'https://images.unsplash.com/photo-1467003909585-2f8a72700288?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'),
('a0010000-0000-0000-0000-000000000103', 'Grilled Shrimp Skewers', 'Marinated shrimp on skewers.', 75000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440000', 'https://images.unsplash.com/photo-1565557623262-b51c2513a641?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'),
('a0010000-0000-0000-0000-000000000104', 'Sunset Burger', 'Premium beef burger with special sauce.', 62000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440000', 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'),
('a0010000-0000-0000-0000-000000000105', 'Grilled Veggie Platter', 'Assorted grilled vegetables.', 55000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440000', 'https://images.unsplash.com/photo-1540420773420-3366772f4999?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'),
('a0010000-0000-0000-0000-000000000106', 'Chicken Wings', 'Spicy buffalo wings.', 50000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440000', 'https://images.unsplash.com/photo-1608039755401-742074f0548d?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D');



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
(102, NULL, 'greengarden@example.com', 2, '$2y$10$KpQLzT3IUA4ZY.eCU8So/OpheuoEPuFxHt/vB244uiN8TSxjlN9dm', '2024-02-15T08:30:00', '2025-04-20T14:00:00', true, 'token_2', 'Green Garden'),
(103, NULL, 'oceanbreeze@example.com', 2, 'hashed_password_3', '2023-11-11T12:00:00', '2025-03-30T11:00:00', true, 'token_3', 'Ocean Breeze'),
(104, NULL, 'nightowldiner@example.com', 2, 'hashed_password_4', '2024-05-01T19:00:00', '2025-05-16T21:00:00', true, 'token_4', 'Night Owl Diner'),
(105, NULL, 'burgerspot@example.com', 2, 'hashed_password_5', '2024-03-10T10:00:00', '2025-05-10T18:00:00', true, 'token_5', 'The Burger Spot'),
(106, NULL, 'pastapalace@example.com', 2, 'hashed_password_6', '2023-12-01T11:30:00', '2025-05-01T12:00:00', true, 'token_6', 'Pasta Palace'),
(107, NULL, 'tacofiesta@example.com', 2, 'hashed_password_7', '2024-04-05T12:00:00', '2025-04-25T15:00:00', true, 'token_7', 'Taco Fiesta'),
(108, NULL, 'sushiworld@example.com', 2, 'hashed_password_8', '2024-01-20T09:00:00', '2025-05-10T14:00:00', true, 'token_8', 'Sushi World'),
(109, NULL, 'cafemorning@example.com', 2, 'hashed_password_9', '2024-02-05T07:00:00', '2025-03-15T10:30:00', true, 'token_9', 'Cafe Morning'),
(110, NULL, 'bbqbrothers@example.com', 2, 'hashed_password_10', '2024-01-12T15:00:00', '2025-05-01T17:00:00', true, 'token_10', 'BBQ Brothers'),
(111, 'admin', 'test@gmail.com', 1, 'hashed_test1234', '2024-01-01T10:00:00', '2025-05-01T09:30:00', true, 'admin_token', NULL);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'orders'
        AND column_name = 'payment_uploaded_at'
    ) THEN
ALTER TABLE orders ADD COLUMN payment_uploaded_at TIMESTAMP;
RAISE NOTICE 'Column payment_uploaded_at added to orders table';
ELSE
        RAISE NOTICE 'Column payment_uploaded_at already exists';
END IF;
END $$;

UPDATE orders
SET payment_uploaded_at = payment_begin_at + INTERVAL '5 minutes'
WHERE payment_status = 'UPLOADED'
  AND payment_uploaded_at IS NULL
  AND payment_begin_at IS NOT NULL;

DO $$
BEGIN
    RAISE NOTICE 'Migration completed: payment_uploaded_at column added and existing data migrated';
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'orders'
        AND column_name = 'scheduled_check_in_time'
    ) THEN
        ALTER TABLE orders ADD COLUMN scheduled_check_in_time TIMESTAMP;
        RAISE NOTICE 'Column scheduled_check_in_time added to orders table';
    ELSE
        RAISE NOTICE 'Column scheduled_check_in_time already exists';
    END IF;
END $$;

-- Seed Reviews for All Restaurants (using integer ratings 1-5 for rating breakdown compatibility)
INSERT INTO review (id, review_content, created_at, updated_at, is_active, restaurant_id, rating, customer_name, review_image_url, order_id, customer_id) VALUES
-- Sunset Grill Reviews (restaurant 0) - avg: 4.67
('b0010000-0000-0000-0000-000000000001', 'Amazing rooftop view and the brisket was perfectly smoked! Will definitely come back.', '2025-01-10 14:30:00', '2025-01-10 14:30:00', true, '550e8400-e29b-41d4-a716-446655440000', 5, 'John Doe', NULL, NULL, 'customer_001'),
('b0010000-0000-0000-0000-000000000002', 'Great atmosphere but food took a while to arrive. Salmon was good though.', '2025-01-15 18:45:00', '2025-01-15 18:45:00', true, '550e8400-e29b-41d4-a716-446655440000', 4, 'Jane Smith', NULL, NULL, 'customer_002'),
('b0010000-0000-0000-0000-000000000003', 'The sunset view is breathtaking! Food quality is excellent.', '2025-02-01 19:00:00', '2025-02-01 19:00:00', true, '550e8400-e29b-41d4-a716-446655440000', 5, 'Mike Johnson', NULL, NULL, 'customer_003'),

-- Green Garden Reviews (restaurant 1) - avg: 4.33
('b0010000-0000-0000-0000-000000000004', 'Best vegan restaurant in town! The organic salads are so fresh.', '2025-01-08 12:00:00', '2025-01-08 12:00:00', true, '550e8400-e29b-41d4-a716-446655440001', 5, 'Emily Green', NULL, NULL, 'customer_004'),
('b0010000-0000-0000-0000-000000000005', 'Healthy and delicious options. A bit pricey but worth it.', '2025-01-20 13:30:00', '2025-01-20 13:30:00', true, '550e8400-e29b-41d4-a716-446655440001', 4, 'Sarah Brown', NULL, NULL, 'customer_005'),
('b0010000-0000-0000-0000-000000000006', 'Love the plant-based menu. Great for health-conscious diners.', '2025-02-05 11:15:00', '2025-02-05 11:15:00', true, '550e8400-e29b-41d4-a716-446655440001', 4, 'David Wilson', NULL, NULL, 'customer_006'),

-- Ocean Breeze Reviews (restaurant 2) - avg: 4.67
('b0010000-0000-0000-0000-000000000007', 'Fresh seafood with an amazing bay view. The lobster was incredible!', '2025-01-12 19:30:00', '2025-01-12 19:30:00', true, '550e8400-e29b-41d4-a716-446655440002', 5, 'Chris Marine', NULL, NULL, 'customer_007'),
('b0010000-0000-0000-0000-000000000008', 'Great location but parking is difficult. Food was excellent.', '2025-01-25 20:00:00', '2025-01-25 20:00:00', true, '550e8400-e29b-41d4-a716-446655440002', 4, 'Lisa Ocean', NULL, NULL, 'customer_008'),
('b0010000-0000-0000-0000-000000000009', 'The grilled fish was perfectly cooked. Romantic dinner spot!', '2025-02-10 18:45:00', '2025-02-10 18:45:00', true, '550e8400-e29b-41d4-a716-446655440002', 5, 'Tom Waters', NULL, NULL, 'customer_009'),

-- Night Owl Diner Reviews (restaurant 3) - avg: 4.0
('b0010000-0000-0000-0000-000000000010', 'Perfect for late night cravings! The midnight burger is amazing.', '2025-01-05 01:30:00', '2025-01-05 01:30:00', true, '550e8400-e29b-41d4-a716-446655440003', 5, 'Night Walker', NULL, NULL, 'customer_010'),
('b0010000-0000-0000-0000-000000000011', 'Great that they are open late. Food is decent comfort food.', '2025-01-18 23:45:00', '2025-01-18 23:45:00', true, '550e8400-e29b-41d4-a716-446655440003', 3, 'Late Snacker', NULL, NULL, 'customer_011'),
('b0010000-0000-0000-0000-000000000012', 'My go-to spot after work. Staff is friendly even at 2am!', '2025-02-08 02:15:00', '2025-02-08 02:15:00', true, '550e8400-e29b-41d4-a716-446655440003', 4, 'Shift Worker', NULL, NULL, 'customer_012'),

-- The Burger Spot Reviews (restaurant 4) - avg: 4.67
('b0010000-0000-0000-0000-000000000013', 'Best burgers in the city! The patties are so juicy.', '2025-01-07 12:30:00', '2025-01-07 12:30:00', true, '550e8400-e29b-41d4-a716-446655440004', 5, 'Burger Lover', NULL, NULL, 'customer_013'),
('b0010000-0000-0000-0000-000000000014', 'Huge portions and tasty fries. Great value for money.', '2025-01-22 13:00:00', '2025-01-22 13:00:00', true, '550e8400-e29b-41d4-a716-446655440004', 5, 'Foodie Mike', NULL, NULL, 'customer_014'),
('b0010000-0000-0000-0000-000000000015', 'Classic American burgers done right. Love the cheese options!', '2025-02-03 18:30:00', '2025-02-03 18:30:00', true, '550e8400-e29b-41d4-a716-446655440004', 4, 'Grill Master', NULL, NULL, 'customer_015'),

-- Pasta Palace Reviews (restaurant 5) - avg: 4.33
('b0010000-0000-0000-0000-000000000016', 'Authentic Italian pasta! The carbonara is to die for.', '2025-01-09 19:00:00', '2025-01-09 19:00:00', true, '550e8400-e29b-41d4-a716-446655440005', 5, 'Italian Fan', NULL, NULL, 'customer_016'),
('b0010000-0000-0000-0000-000000000017', 'Reminds me of my trip to Rome. Very authentic flavors.', '2025-01-28 20:30:00', '2025-01-28 20:30:00', true, '550e8400-e29b-41d4-a716-446655440005', 4, 'World Traveler', NULL, NULL, 'customer_017'),
('b0010000-0000-0000-0000-000000000018', 'Great pasta selection. The tiramisu is a must-try!', '2025-02-12 19:45:00', '2025-02-12 19:45:00', true, '550e8400-e29b-41d4-a716-446655440005', 4, 'Dessert Lover', NULL, NULL, 'customer_018'),

-- Taco Fiesta Reviews (restaurant 6) - avg: 4.67
('b0010000-0000-0000-0000-000000000019', 'Authentic Mexican street food! The tacos al pastor are amazing.', '2025-01-11 13:30:00', '2025-01-11 13:30:00', true, '550e8400-e29b-41d4-a716-446655440006', 5, 'Taco Tuesday', NULL, NULL, 'customer_019'),
('b0010000-0000-0000-0000-000000000020', 'Great salsa and fresh ingredients. Love the guacamole!', '2025-01-24 14:00:00', '2025-01-24 14:00:00', true, '550e8400-e29b-41d4-a716-446655440006', 4, 'Spice Lover', NULL, NULL, 'customer_020'),
('b0010000-0000-0000-0000-000000000021', 'Affordable and delicious. The burritos are huge!', '2025-02-06 12:45:00', '2025-02-06 12:45:00', true, '550e8400-e29b-41d4-a716-446655440006', 5, 'Hungry Student', NULL, NULL, 'customer_021'),

-- Sushi World Reviews (restaurant 7) - avg: 4.67
('b0010000-0000-0000-0000-000000000022', 'Freshest sushi in town! The omakase is worth every penny.', '2025-01-13 18:00:00', '2025-01-13 18:00:00', true, '550e8400-e29b-41d4-a716-446655440007', 5, 'Sushi Master', NULL, NULL, 'customer_022'),
('b0010000-0000-0000-0000-000000000023', 'Excellent sashimi quality. The chef really knows his craft.', '2025-01-26 19:30:00', '2025-01-26 19:30:00', true, '550e8400-e29b-41d4-a716-446655440007', 5, 'Japan Lover', NULL, NULL, 'customer_023'),
('b0010000-0000-0000-0000-000000000024', 'Good variety of rolls. The dragon roll is my favorite!', '2025-02-09 20:00:00', '2025-02-09 20:00:00', true, '550e8400-e29b-41d4-a716-446655440007', 4, 'Roll Fan', NULL, NULL, 'customer_024'),

-- Cafe Morning Reviews (restaurant 8) - avg: 4.33
('b0010000-0000-0000-0000-000000000025', 'Perfect breakfast spot! The pancakes are fluffy and delicious.', '2025-01-06 09:00:00', '2025-01-06 09:00:00', true, '550e8400-e29b-41d4-a716-446655440008', 5, 'Early Bird', NULL, NULL, 'customer_025'),
('b0010000-0000-0000-0000-000000000026', 'Great coffee and cozy atmosphere. Love coming here for brunch.', '2025-01-19 10:30:00', '2025-01-19 10:30:00', true, '550e8400-e29b-41d4-a716-446655440008', 4, 'Coffee Addict', NULL, NULL, 'customer_026'),
('b0010000-0000-0000-0000-000000000027', 'The avocado toast is amazing! Fresh ingredients.', '2025-02-02 08:45:00', '2025-02-02 08:45:00', true, '550e8400-e29b-41d4-a716-446655440008', 4, 'Brunch Lover', NULL, NULL, 'customer_027'),

-- BBQ Brothers Reviews (restaurant 9) - avg: 4.67
('b0010000-0000-0000-0000-000000000028', 'Best BBQ in the area! The ribs fall right off the bone.', '2025-01-14 17:00:00', '2025-01-14 17:00:00', true, '550e8400-e29b-41d4-a716-446655440009', 5, 'BBQ King', NULL, NULL, 'customer_028'),
('b0010000-0000-0000-0000-000000000029', 'Smoky and flavorful. The pulled pork is incredible!', '2025-01-27 18:30:00', '2025-01-27 18:30:00', true, '550e8400-e29b-41d4-a716-446655440009', 5, 'Meat Lover', NULL, NULL, 'customer_029'),
('b0010000-0000-0000-0000-000000000030', 'Great portions and reasonable prices. A must-try for BBQ fans.', '2025-02-11 16:45:00', '2025-02-11 16:45:00', true, '550e8400-e29b-41d4-a716-446655440009', 4, 'Smoke Hunter', NULL, NULL, 'customer_030');

-- Update restaurant ratings based on reviews (calculated averages)
-- Sunset Grill: (5+4+5)/3 = 4.67
UPDATE restaurant SET rating = 4.67, total_rating = 3 WHERE id = '550e8400-e29b-41d4-a716-446655440000';
-- Green Garden: (5+4+4)/3 = 4.33
UPDATE restaurant SET rating = 4.33, total_rating = 3 WHERE id = '550e8400-e29b-41d4-a716-446655440001';
-- Ocean Breeze: (5+4+5)/3 = 4.67
UPDATE restaurant SET rating = 4.67, total_rating = 3 WHERE id = '550e8400-e29b-41d4-a716-446655440002';
-- Night Owl Diner: (5+3+4)/3 = 4.0
UPDATE restaurant SET rating = 4.0, total_rating = 3 WHERE id = '550e8400-e29b-41d4-a716-446655440003';
-- The Burger Spot: (5+5+4)/3 = 4.67
UPDATE restaurant SET rating = 4.67, total_rating = 3 WHERE id = '550e8400-e29b-41d4-a716-446655440004';
-- Pasta Palace: (5+4+4)/3 = 4.33
UPDATE restaurant SET rating = 4.33, total_rating = 3 WHERE id = '550e8400-e29b-41d4-a716-446655440005';
-- Taco Fiesta: (5+4+5)/3 = 4.67
UPDATE restaurant SET rating = 4.67, total_rating = 3 WHERE id = '550e8400-e29b-41d4-a716-446655440006';
-- Sushi World: (5+5+4)/3 = 4.67
UPDATE restaurant SET rating = 4.67, total_rating = 3 WHERE id = '550e8400-e29b-41d4-a716-446655440007';
-- Cafe Morning: (5+4+4)/3 = 4.33
UPDATE restaurant SET rating = 4.33, total_rating = 3 WHERE id = '550e8400-e29b-41d4-a716-446655440008';
-- BBQ Brothers: (5+5+4)/3 = 4.67
UPDATE restaurant SET rating = 4.67, total_rating = 3 WHERE id = '550e8400-e29b-41d4-a716-446655440009';

-- Seed Orders for Sunset Grill (38 days: 2025-12-02 to 2026-01-08)
-- Restaurant ID: 550e8400-e29b-41d4-a716-446655440000
-- Menu: Brisket(88k), Salmon(82k), Shrimp(75k), Burger(62k), Veggie(55k), Wings(50k)

INSERT INTO orders (id, created_date, order_time, order_message, payment_confirmed_at, is_active, order_status, restaurant_id, customer_id, payment_begin_at, payment_uploaded_at, sub_total, total_price, cancellation_reason, dine_in_time, payment_image_url, payment_status, customer_name, scheduled_check_in_time) VALUES
-- Day 1: 2025-12-02
('c0010000-0000-0000-0000-000000000001', '2025-12-02', '2025-12-02 11:30:00+07', NULL, '2025-12-02 11:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_001', '2025-12-02 11:32:00', '2025-12-02 11:33:00', 150000, 150000, NULL, '12:00:00', 'payment_001.jpg', 'APPROVED', 'Andi Wijaya', '2025-12-02 12:00:00'),
('c0010000-0000-0000-0000-000000000002', '2025-12-02', '2025-12-02 13:15:00+07', NULL, '2025-12-02 13:20:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_002', '2025-12-02 13:17:00', '2025-12-02 13:18:00', 170000, 170000, NULL, '13:30:00', 'payment_002.jpg', 'APPROVED', 'Budi Santoso', '2025-12-02 13:30:00'),
('c0010000-0000-0000-0000-000000000003', '2025-12-02', '2025-12-02 19:00:00+07', NULL, '2025-12-02 19:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_003', '2025-12-02 19:02:00', '2025-12-02 19:03:00', 238000, 238000, NULL, '19:30:00', 'payment_003.jpg', 'APPROVED', 'Citra Dewi', '2025-12-02 19:30:00'),
-- Day 2: 2025-12-03
('c0010000-0000-0000-0000-000000000004', '2025-12-03', '2025-12-03 12:00:00+07', NULL, '2025-12-03 12:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_004', '2025-12-03 12:02:00', '2025-12-03 12:03:00', 124000, 124000, NULL, '12:30:00', 'payment_004.jpg', 'APPROVED', 'Denny Pratama', '2025-12-03 12:30:00'),
('c0010000-0000-0000-0000-000000000005', '2025-12-03', '2025-12-03 18:30:00+07', NULL, NULL, true, 'CANCELLED', '550e8400-e29b-41d4-a716-446655440000', 'cust_005', '2025-12-03 18:32:00', NULL, 88000, 88000, 'Customer cancelled - changed plans', NULL, NULL, 'EXPIRED', 'Eka Putri', NULL),
('c0010000-0000-0000-0000-000000000006', '2025-12-03', '2025-12-03 20:00:00+07', NULL, '2025-12-03 20:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_006', '2025-12-03 20:02:00', '2025-12-03 20:03:00', 195000, 195000, NULL, '20:30:00', 'payment_006.jpg', 'APPROVED', 'Fajar Rahman', '2025-12-03 20:30:00'),
-- Day 3: 2025-12-04
('c0010000-0000-0000-0000-000000000007', '2025-12-04', '2025-12-04 11:00:00+07', NULL, '2025-12-04 11:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_007', '2025-12-04 11:02:00', '2025-12-04 11:03:00', 137000, 137000, NULL, '11:30:00', 'payment_007.jpg', 'APPROVED', 'Gita Sari', '2025-12-04 11:30:00'),
('c0010000-0000-0000-0000-000000000008', '2025-12-04', '2025-12-04 14:00:00+07', NULL, '2025-12-04 14:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_008', '2025-12-04 14:02:00', '2025-12-04 14:03:00', 262000, 262000, NULL, '14:30:00', 'payment_008.jpg', 'APPROVED', 'Hendra Kusuma', '2025-12-04 14:30:00'),
('c0010000-0000-0000-0000-000000000009', '2025-12-04', '2025-12-04 19:30:00+07', NULL, '2025-12-04 19:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_009', '2025-12-04 19:32:00', '2025-12-04 19:33:00', 100000, 100000, NULL, '20:00:00', 'payment_009.jpg', 'APPROVED', 'Indah Permata', '2025-12-04 20:00:00'),
-- Day 4: 2025-12-05
('c0010000-0000-0000-0000-000000000010', '2025-12-05', '2025-12-05 12:30:00+07', NULL, '2025-12-05 12:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_010', '2025-12-05 12:32:00', '2025-12-05 12:33:00', 176000, 176000, NULL, '13:00:00', 'payment_010.jpg', 'APPROVED', 'Joko Widodo', '2025-12-05 13:00:00'),
('c0010000-0000-0000-0000-000000000011', '2025-12-05', '2025-12-05 17:00:00+07', NULL, '2025-12-05 17:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_011', '2025-12-05 17:02:00', '2025-12-05 17:03:00', 82000, 82000, NULL, '17:30:00', 'payment_011.jpg', 'APPROVED', 'Kartika Sari', '2025-12-05 17:30:00'),
('c0010000-0000-0000-0000-000000000012', '2025-12-05', '2025-12-05 20:15:00+07', NULL, '2025-12-05 20:20:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_012', '2025-12-05 20:17:00', '2025-12-05 20:18:00', 213000, 213000, NULL, '20:45:00', 'payment_012.jpg', 'APPROVED', 'Lukman Hakim', '2025-12-05 20:45:00'),
-- Day 5: 2025-12-06
('c0010000-0000-0000-0000-000000000013', '2025-12-06', '2025-12-06 11:45:00+07', NULL, '2025-12-06 11:50:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_013', '2025-12-06 11:47:00', '2025-12-06 11:48:00', 150000, 150000, NULL, '12:15:00', 'payment_013.jpg', 'APPROVED', 'Maya Anggraini', '2025-12-06 12:15:00'),
('c0010000-0000-0000-0000-000000000014', '2025-12-06', '2025-12-06 15:00:00+07', NULL, NULL, true, 'CANCELLED', '550e8400-e29b-41d4-a716-446655440000', 'cust_014', '2025-12-06 15:02:00', NULL, 164000, 164000, 'Restaurant too busy', NULL, NULL, 'EXPIRED', 'Nanda Putra', NULL),
('c0010000-0000-0000-0000-000000000015', '2025-12-06', '2025-12-06 19:00:00+07', NULL, '2025-12-06 19:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_015', '2025-12-06 19:02:00', '2025-12-06 19:03:00', 300000, 300000, NULL, '19:30:00', 'payment_015.jpg', 'APPROVED', 'Oscar Tanujaya', '2025-12-06 19:30:00'),
-- Day 6: 2025-12-07
('c0010000-0000-0000-0000-000000000016', '2025-12-07', '2025-12-07 12:00:00+07', NULL, '2025-12-07 12:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_016', '2025-12-07 12:02:00', '2025-12-07 12:03:00', 125000, 125000, NULL, '12:30:00', 'payment_016.jpg', 'APPROVED', 'Putri Handayani', '2025-12-07 12:30:00'),
('c0010000-0000-0000-0000-000000000017', '2025-12-07', '2025-12-07 18:00:00+07', NULL, '2025-12-07 18:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_017', '2025-12-07 18:02:00', '2025-12-07 18:03:00', 244000, 244000, NULL, '18:30:00', 'payment_017.jpg', 'APPROVED', 'Qori Ramadhan', '2025-12-07 18:30:00'),
('c0010000-0000-0000-0000-000000000018', '2025-12-07', '2025-12-07 20:30:00+07', NULL, '2025-12-07 20:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_018', '2025-12-07 20:32:00', '2025-12-07 20:33:00', 88000, 88000, NULL, '21:00:00', 'payment_018.jpg', 'APPROVED', 'Rizky Febrian', '2025-12-07 21:00:00'),
-- Day 7: 2025-12-08
('c0010000-0000-0000-0000-000000000019', '2025-12-08', '2025-12-08 11:30:00+07', NULL, '2025-12-08 11:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_019', '2025-12-08 11:32:00', '2025-12-08 11:33:00', 157000, 157000, NULL, '12:00:00', 'payment_019.jpg', 'APPROVED', 'Siti Nurhaliza', '2025-12-08 12:00:00'),
('c0010000-0000-0000-0000-000000000020', '2025-12-08', '2025-12-08 14:30:00+07', NULL, '2025-12-08 14:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_020', '2025-12-08 14:32:00', '2025-12-08 14:33:00', 232000, 232000, NULL, '15:00:00', 'payment_020.jpg', 'APPROVED', 'Tommy Kurniawan', '2025-12-08 15:00:00'),
('c0010000-0000-0000-0000-000000000021', '2025-12-08', '2025-12-08 19:15:00+07', NULL, '2025-12-08 19:20:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_021', '2025-12-08 19:17:00', '2025-12-08 19:18:00', 75000, 75000, NULL, '19:45:00', 'payment_021.jpg', 'APPROVED', 'Ulfa Dwiyanti', '2025-12-08 19:45:00'),
-- Day 8: 2025-12-09
('c0010000-0000-0000-0000-000000000022', '2025-12-09', '2025-12-09 12:15:00+07', NULL, '2025-12-09 12:20:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_022', '2025-12-09 12:17:00', '2025-12-09 12:18:00', 194000, 194000, NULL, '12:45:00', 'payment_022.jpg', 'APPROVED', 'Vina Panduwinata', '2025-12-09 12:45:00'),
('c0010000-0000-0000-0000-000000000023', '2025-12-09', '2025-12-09 17:30:00+07', NULL, NULL, true, 'CANCELLED', '550e8400-e29b-41d4-a716-446655440000', 'cust_023', '2025-12-09 17:32:00', NULL, 112000, 112000, 'Payment timeout', NULL, NULL, 'EXPIRED', 'Wahyu Setiawan', NULL),
('c0010000-0000-0000-0000-000000000024', '2025-12-09', '2025-12-09 20:00:00+07', NULL, '2025-12-09 20:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_024', '2025-12-09 20:02:00', '2025-12-09 20:03:00', 170000, 170000, NULL, '20:30:00', 'payment_024.jpg', 'APPROVED', 'Xena Maharani', '2025-12-09 20:30:00'),
-- Day 9: 2025-12-10
('c0010000-0000-0000-0000-000000000025', '2025-12-10', '2025-12-10 11:00:00+07', NULL, '2025-12-10 11:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_025', '2025-12-10 11:02:00', '2025-12-10 11:03:00', 138000, 138000, NULL, '11:30:00', 'payment_025.jpg', 'APPROVED', 'Yanto Basuki', '2025-12-10 11:30:00'),
('c0010000-0000-0000-0000-000000000026', '2025-12-10', '2025-12-10 13:45:00+07', NULL, '2025-12-10 13:50:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_026', '2025-12-10 13:47:00', '2025-12-10 13:48:00', 226000, 226000, NULL, '14:15:00', 'payment_026.jpg', 'APPROVED', 'Zahra Amelia', '2025-12-10 14:15:00'),
('c0010000-0000-0000-0000-000000000027', '2025-12-10', '2025-12-10 19:30:00+07', NULL, '2025-12-10 19:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_027', '2025-12-10 19:32:00', '2025-12-10 19:33:00', 62000, 62000, NULL, '20:00:00', 'payment_027.jpg', 'APPROVED', 'Ahmad Fauzi', '2025-12-10 20:00:00'),
-- Day 10: 2025-12-11
('c0010000-0000-0000-0000-000000000028', '2025-12-11', '2025-12-11 12:00:00+07', NULL, '2025-12-11 12:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_028', '2025-12-11 12:02:00', '2025-12-11 12:03:00', 182000, 182000, NULL, '12:30:00', 'payment_028.jpg', 'APPROVED', 'Bella Safitri', '2025-12-11 12:30:00'),
('c0010000-0000-0000-0000-000000000029', '2025-12-11', '2025-12-11 18:15:00+07', NULL, '2025-12-11 18:20:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_029', '2025-12-11 18:17:00', '2025-12-11 18:18:00', 257000, 257000, NULL, '18:45:00', 'payment_029.jpg', 'APPROVED', 'Candra Wijaya', '2025-12-11 18:45:00'),
('c0010000-0000-0000-0000-000000000030', '2025-12-11', '2025-12-11 20:45:00+07', NULL, '2025-12-11 20:50:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_030', '2025-12-11 20:47:00', '2025-12-11 20:48:00', 105000, 105000, NULL, '21:15:00', 'payment_030.jpg', 'APPROVED', 'Dewi Lestari', '2025-12-11 21:15:00'),
-- Day 11: 2025-12-12
('c0010000-0000-0000-0000-000000000031', '2025-12-12', '2025-12-12 11:30:00+07', NULL, '2025-12-12 11:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_031', '2025-12-12 11:32:00', '2025-12-12 11:33:00', 176000, 176000, NULL, '12:00:00', 'payment_031.jpg', 'APPROVED', 'Eko Prasetyo', '2025-12-12 12:00:00'),
('c0010000-0000-0000-0000-000000000032', '2025-12-12', '2025-12-12 14:00:00+07', NULL, '2025-12-12 14:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_032', '2025-12-12 14:02:00', '2025-12-12 14:03:00', 144000, 144000, NULL, '14:30:00', 'payment_032.jpg', 'APPROVED', 'Fitriani Dewi', '2025-12-12 14:30:00'),
('c0010000-0000-0000-0000-000000000033', '2025-12-12', '2025-12-12 19:00:00+07', NULL, '2025-12-12 19:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_033', '2025-12-12 19:02:00', '2025-12-12 19:03:00', 220000, 220000, NULL, '19:30:00', 'payment_033.jpg', 'APPROVED', 'Galih Permana', '2025-12-12 19:30:00'),
-- Day 12: 2025-12-13
('c0010000-0000-0000-0000-000000000034', '2025-12-13', '2025-12-13 12:00:00+07', NULL, '2025-12-13 12:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_034', '2025-12-13 12:02:00', '2025-12-13 12:03:00', 88000, 88000, NULL, '12:30:00', 'payment_034.jpg', 'APPROVED', 'Hesti Purwanti', '2025-12-13 12:30:00'),
('c0010000-0000-0000-0000-000000000035', '2025-12-13', '2025-12-13 18:30:00+07', NULL, NULL, true, 'CANCELLED', '550e8400-e29b-41d4-a716-446655440000', 'cust_035', '2025-12-13 18:32:00', NULL, 150000, 150000, 'Customer no show', NULL, NULL, 'EXPIRED', 'Irfan Hakim', NULL),
('c0010000-0000-0000-0000-000000000036', '2025-12-13', '2025-12-13 20:00:00+07', NULL, '2025-12-13 20:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_036', '2025-12-13 20:02:00', '2025-12-13 20:03:00', 269000, 269000, NULL, '20:30:00', 'payment_036.jpg', 'APPROVED', 'Julia Perez', '2025-12-13 20:30:00'),
-- Day 13: 2025-12-14
('c0010000-0000-0000-0000-000000000037', '2025-12-14', '2025-12-14 11:00:00+07', NULL, '2025-12-14 11:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_037', '2025-12-14 11:02:00', '2025-12-14 11:03:00', 137000, 137000, NULL, '11:30:00', 'payment_037.jpg', 'APPROVED', 'Kevin Anggara', '2025-12-14 11:30:00'),
('c0010000-0000-0000-0000-000000000038', '2025-12-14', '2025-12-14 15:00:00+07', NULL, '2025-12-14 15:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_038', '2025-12-14 15:02:00', '2025-12-14 15:03:00', 195000, 195000, NULL, '15:30:00', 'payment_038.jpg', 'APPROVED', 'Linda Kusuma', '2025-12-14 15:30:00'),
('c0010000-0000-0000-0000-000000000039', '2025-12-14', '2025-12-14 19:30:00+07', NULL, '2025-12-14 19:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_039', '2025-12-14 19:32:00', '2025-12-14 19:33:00', 312000, 312000, NULL, '20:00:00', 'payment_039.jpg', 'APPROVED', 'Mario Teguh', '2025-12-14 20:00:00'),
-- Day 14: 2025-12-15
('c0010000-0000-0000-0000-000000000040', '2025-12-15', '2025-12-15 12:30:00+07', NULL, '2025-12-15 12:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_040', '2025-12-15 12:32:00', '2025-12-15 12:33:00', 164000, 164000, NULL, '13:00:00', 'payment_040.jpg', 'APPROVED', 'Nadia Saphira', '2025-12-15 13:00:00'),
('c0010000-0000-0000-0000-000000000041', '2025-12-15', '2025-12-15 17:00:00+07', NULL, '2025-12-15 17:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_041', '2025-12-15 17:02:00', '2025-12-15 17:03:00', 100000, 100000, NULL, '17:30:00', 'payment_041.jpg', 'APPROVED', 'Opick Rahman', '2025-12-15 17:30:00'),
('c0010000-0000-0000-0000-000000000042', '2025-12-15', '2025-12-15 20:15:00+07', NULL, '2025-12-15 20:20:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_042', '2025-12-15 20:17:00', '2025-12-15 20:18:00', 238000, 238000, NULL, '20:45:00', 'payment_042.jpg', 'APPROVED', 'Prilly Latuconsina', '2025-12-15 20:45:00'),
-- Day 15: 2025-12-16
('c0010000-0000-0000-0000-000000000043', '2025-12-16', '2025-12-16 11:45:00+07', NULL, '2025-12-16 11:50:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_043', '2025-12-16 11:47:00', '2025-12-16 11:48:00', 82000, 82000, NULL, '12:15:00', 'payment_043.jpg', 'APPROVED', 'Raffi Ahmad', '2025-12-16 12:15:00'),
('c0010000-0000-0000-0000-000000000044', '2025-12-16', '2025-12-16 14:30:00+07', NULL, '2025-12-16 14:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_044', '2025-12-16 14:32:00', '2025-12-16 14:33:00', 157000, 157000, NULL, '15:00:00', 'payment_044.jpg', 'APPROVED', 'Sarah Azhari', '2025-12-16 15:00:00'),
('c0010000-0000-0000-0000-000000000045', '2025-12-16', '2025-12-16 19:00:00+07', NULL, '2025-12-16 19:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_045', '2025-12-16 19:02:00', '2025-12-16 19:03:00', 282000, 282000, NULL, '19:30:00', 'payment_045.jpg', 'APPROVED', 'Tukul Arwana', '2025-12-16 19:30:00'),
-- Day 16: 2025-12-17
('c0010000-0000-0000-0000-000000000046', '2025-12-17', '2025-12-17 12:00:00+07', NULL, '2025-12-17 12:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_046', '2025-12-17 12:02:00', '2025-12-17 12:03:00', 170000, 170000, NULL, '12:30:00', 'payment_046.jpg', 'APPROVED', 'Ussy Sulistiawaty', '2025-12-17 12:30:00'),
('c0010000-0000-0000-0000-000000000047', '2025-12-17', '2025-12-17 18:00:00+07', NULL, NULL, true, 'CANCELLED', '550e8400-e29b-41d4-a716-446655440000', 'cust_047', '2025-12-17 18:02:00', NULL, 88000, 88000, 'Double booking', NULL, NULL, 'EXPIRED', 'Vicky Prasetyo', NULL),
('c0010000-0000-0000-0000-000000000048', '2025-12-17', '2025-12-17 20:30:00+07', NULL, '2025-12-17 20:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_048', '2025-12-17 20:32:00', '2025-12-17 20:33:00', 213000, 213000, NULL, '21:00:00', 'payment_048.jpg', 'APPROVED', 'Wulan Guritno', '2025-12-17 21:00:00'),
-- Day 17: 2025-12-18
('c0010000-0000-0000-0000-000000000049', '2025-12-18', '2025-12-18 11:30:00+07', NULL, '2025-12-18 11:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_049', '2025-12-18 11:32:00', '2025-12-18 11:33:00', 125000, 125000, NULL, '12:00:00', 'payment_049.jpg', 'APPROVED', 'Xavier Tan', '2025-12-18 12:00:00'),
('c0010000-0000-0000-0000-000000000050', '2025-12-18', '2025-12-18 14:00:00+07', NULL, '2025-12-18 14:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_050', '2025-12-18 14:02:00', '2025-12-18 14:03:00', 194000, 194000, NULL, '14:30:00', 'payment_050.jpg', 'APPROVED', 'Yuki Kato', '2025-12-18 14:30:00'),
('c0010000-0000-0000-0000-000000000051', '2025-12-18', '2025-12-18 19:15:00+07', NULL, '2025-12-18 19:20:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_051', '2025-12-18 19:17:00', '2025-12-18 19:18:00', 300000, 300000, NULL, '19:45:00', 'payment_051.jpg', 'APPROVED', 'Zaskia Gotik', '2025-12-18 19:45:00'),
-- Day 18: 2025-12-19
('c0010000-0000-0000-0000-000000000052', '2025-12-19', '2025-12-19 12:15:00+07', NULL, '2025-12-19 12:20:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_052', '2025-12-19 12:17:00', '2025-12-19 12:18:00', 138000, 138000, NULL, '12:45:00', 'payment_052.jpg', 'APPROVED', 'Anang Hermansyah', '2025-12-19 12:45:00'),
('c0010000-0000-0000-0000-000000000053', '2025-12-19', '2025-12-19 17:30:00+07', NULL, '2025-12-19 17:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_053', '2025-12-19 17:32:00', '2025-12-19 17:33:00', 75000, 75000, NULL, '18:00:00', 'payment_053.jpg', 'APPROVED', 'Bunga Citra', '2025-12-19 18:00:00'),
('c0010000-0000-0000-0000-000000000054', '2025-12-19', '2025-12-19 20:00:00+07', NULL, '2025-12-19 20:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_054', '2025-12-19 20:02:00', '2025-12-19 20:03:00', 257000, 257000, NULL, '20:30:00', 'payment_054.jpg', 'APPROVED', 'Chelsea Islan', '2025-12-19 20:30:00'),
-- Day 19: 2025-12-20
('c0010000-0000-0000-0000-000000000055', '2025-12-20', '2025-12-20 11:00:00+07', NULL, '2025-12-20 11:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_055', '2025-12-20 11:02:00', '2025-12-20 11:03:00', 182000, 182000, NULL, '11:30:00', 'payment_055.jpg', 'APPROVED', 'Dian Sastro', '2025-12-20 11:30:00'),
('c0010000-0000-0000-0000-000000000056', '2025-12-20', '2025-12-20 13:45:00+07', NULL, '2025-12-20 13:50:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_056', '2025-12-20 13:47:00', '2025-12-20 13:48:00', 112000, 112000, NULL, '14:15:00', 'payment_056.jpg', 'APPROVED', 'Ernest Prakasa', '2025-12-20 14:15:00'),
('c0010000-0000-0000-0000-000000000057', '2025-12-20', '2025-12-20 19:30:00+07', NULL, '2025-12-20 19:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_057', '2025-12-20 19:32:00', '2025-12-20 19:33:00', 350000, 350000, NULL, '20:00:00', 'payment_057.jpg', 'APPROVED', 'Feni Rose', '2025-12-20 20:00:00'),
-- Day 20: 2025-12-21
('c0010000-0000-0000-0000-000000000058', '2025-12-21', '2025-12-21 12:00:00+07', NULL, '2025-12-21 12:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_058', '2025-12-21 12:02:00', '2025-12-21 12:03:00', 150000, 150000, NULL, '12:30:00', 'payment_058.jpg', 'APPROVED', 'Glenn Fredly', '2025-12-21 12:30:00'),
('c0010000-0000-0000-0000-000000000059', '2025-12-21', '2025-12-21 18:15:00+07', NULL, NULL, true, 'CANCELLED', '550e8400-e29b-41d4-a716-446655440000', 'cust_059', '2025-12-21 18:17:00', NULL, 170000, 170000, 'Emergency cancellation', NULL, NULL, 'EXPIRED', 'Happy Salma', NULL),
('c0010000-0000-0000-0000-000000000060', '2025-12-21', '2025-12-21 20:45:00+07', NULL, '2025-12-21 20:50:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_060', '2025-12-21 20:47:00', '2025-12-21 20:48:00', 226000, 226000, NULL, '21:15:00', 'payment_060.jpg', 'APPROVED', 'Ivan Gunawan', '2025-12-21 21:15:00'),
-- Day 21: 2025-12-22
('c0010000-0000-0000-0000-000000000061', '2025-12-22', '2025-12-22 11:30:00+07', NULL, '2025-12-22 11:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_061', '2025-12-22 11:32:00', '2025-12-22 11:33:00', 144000, 144000, NULL, '12:00:00', 'payment_061.jpg', 'APPROVED', 'Jessica Mila', '2025-12-22 12:00:00'),
('c0010000-0000-0000-0000-000000000062', '2025-12-22', '2025-12-22 14:00:00+07', NULL, '2025-12-22 14:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_062', '2025-12-22 14:02:00', '2025-12-22 14:03:00', 269000, 269000, NULL, '14:30:00', 'payment_062.jpg', 'APPROVED', 'Krisdayanti', '2025-12-22 14:30:00'),
('c0010000-0000-0000-0000-000000000063', '2025-12-22', '2025-12-22 19:00:00+07', NULL, '2025-12-22 19:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_063', '2025-12-22 19:02:00', '2025-12-22 19:03:00', 88000, 88000, NULL, '19:30:00', 'payment_063.jpg', 'APPROVED', 'Luna Maya', '2025-12-22 19:30:00'),
-- Day 22: 2025-12-23
('c0010000-0000-0000-0000-000000000064', '2025-12-23', '2025-12-23 12:00:00+07', NULL, '2025-12-23 12:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_064', '2025-12-23 12:02:00', '2025-12-23 12:03:00', 195000, 195000, NULL, '12:30:00', 'payment_064.jpg', 'APPROVED', 'Maudy Ayunda', '2025-12-23 12:30:00'),
('c0010000-0000-0000-0000-000000000065', '2025-12-23', '2025-12-23 18:30:00+07', NULL, '2025-12-23 18:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_065', '2025-12-23 18:32:00', '2025-12-23 18:33:00', 312000, 312000, NULL, '19:00:00', 'payment_065.jpg', 'APPROVED', 'Nicholas Saputra', '2025-12-23 19:00:00'),
('c0010000-0000-0000-0000-000000000066', '2025-12-23', '2025-12-23 20:00:00+07', NULL, '2025-12-23 20:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_066', '2025-12-23 20:02:00', '2025-12-23 20:03:00', 137000, 137000, NULL, '20:30:00', 'payment_066.jpg', 'APPROVED', 'Olivia Jensen', '2025-12-23 20:30:00'),
-- Day 23: 2025-12-24 (Christmas Eve - busier)
('c0010000-0000-0000-0000-000000000067', '2025-12-24', '2025-12-24 11:00:00+07', NULL, '2025-12-24 11:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_067', '2025-12-24 11:02:00', '2025-12-24 11:03:00', 350000, 350000, NULL, '11:30:00', 'payment_067.jpg', 'APPROVED', 'Pevita Pearce', '2025-12-24 11:30:00'),
('c0010000-0000-0000-0000-000000000068', '2025-12-24', '2025-12-24 13:00:00+07', NULL, '2025-12-24 13:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_068', '2025-12-24 13:02:00', '2025-12-24 13:03:00', 420000, 420000, NULL, '13:30:00', 'payment_068.jpg', 'APPROVED', 'Reza Rahadian', '2025-12-24 13:30:00'),
('c0010000-0000-0000-0000-000000000069', '2025-12-24', '2025-12-24 18:00:00+07', NULL, '2025-12-24 18:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_069', '2025-12-24 18:02:00', '2025-12-24 18:03:00', 500000, 500000, NULL, '18:30:00', 'payment_069.jpg', 'APPROVED', 'Shandy Aulia', '2025-12-24 18:30:00'),
('c0010000-0000-0000-0000-000000000070', '2025-12-24', '2025-12-24 19:30:00+07', NULL, '2025-12-24 19:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_070', '2025-12-24 19:32:00', '2025-12-24 19:33:00', 380000, 380000, NULL, '20:00:00', 'payment_070.jpg', 'APPROVED', 'Tatjana Saphira', '2025-12-24 20:00:00'),
-- Day 24: 2025-12-25 (Christmas)
('c0010000-0000-0000-0000-000000000071', '2025-12-25', '2025-12-25 12:00:00+07', NULL, '2025-12-25 12:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_071', '2025-12-25 12:02:00', '2025-12-25 12:03:00', 450000, 450000, NULL, '12:30:00', 'payment_071.jpg', 'APPROVED', 'Uli Herdinansyah', '2025-12-25 12:30:00'),
('c0010000-0000-0000-0000-000000000072', '2025-12-25', '2025-12-25 18:00:00+07', NULL, '2025-12-25 18:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_072', '2025-12-25 18:02:00', '2025-12-25 18:03:00', 520000, 520000, NULL, '18:30:00', 'payment_072.jpg', 'APPROVED', 'Vino Bastian', '2025-12-25 18:30:00'),
('c0010000-0000-0000-0000-000000000073', '2025-12-25', '2025-12-25 20:00:00+07', NULL, '2025-12-25 20:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_073', '2025-12-25 20:02:00', '2025-12-25 20:03:00', 300000, 300000, NULL, '20:30:00', 'payment_073.jpg', 'APPROVED', 'Wirda Mansur', '2025-12-25 20:30:00'),
-- Day 25: 2025-12-26
('c0010000-0000-0000-0000-000000000074', '2025-12-26', '2025-12-26 11:30:00+07', NULL, '2025-12-26 11:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_074', '2025-12-26 11:32:00', '2025-12-26 11:33:00', 176000, 176000, NULL, '12:00:00', 'payment_074.jpg', 'APPROVED', 'Yura Yunita', '2025-12-26 12:00:00'),
('c0010000-0000-0000-0000-000000000075', '2025-12-26', '2025-12-26 14:00:00+07', NULL, NULL, true, 'CANCELLED', '550e8400-e29b-41d4-a716-446655440000', 'cust_075', '2025-12-26 14:02:00', NULL, 82000, 82000, 'Customer cancelled', NULL, NULL, 'EXPIRED', 'Zainal Abidin', NULL),
('c0010000-0000-0000-0000-000000000076', '2025-12-26', '2025-12-26 19:00:00+07', NULL, '2025-12-26 19:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_076', '2025-12-26 19:02:00', '2025-12-26 19:03:00', 244000, 244000, NULL, '19:30:00', 'payment_076.jpg', 'APPROVED', 'Ariel Noah', '2025-12-26 19:30:00'),
-- Day 26: 2025-12-27
('c0010000-0000-0000-0000-000000000077', '2025-12-27', '2025-12-27 12:00:00+07', NULL, '2025-12-27 12:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_077', '2025-12-27 12:02:00', '2025-12-27 12:03:00', 157000, 157000, NULL, '12:30:00', 'payment_077.jpg', 'APPROVED', 'Baim Wong', '2025-12-27 12:30:00'),
('c0010000-0000-0000-0000-000000000078', '2025-12-27', '2025-12-27 18:30:00+07', NULL, '2025-12-27 18:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_078', '2025-12-27 18:32:00', '2025-12-27 18:33:00', 282000, 282000, NULL, '19:00:00', 'payment_078.jpg', 'APPROVED', 'Cinta Laura', '2025-12-27 19:00:00'),
('c0010000-0000-0000-0000-000000000079', '2025-12-27', '2025-12-27 20:15:00+07', NULL, '2025-12-27 20:20:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_079', '2025-12-27 20:17:00', '2025-12-27 20:18:00', 100000, 100000, NULL, '20:45:00', 'payment_079.jpg', 'APPROVED', 'Dimas Anggara', '2025-12-27 20:45:00'),
-- Day 27: 2025-12-28
('c0010000-0000-0000-0000-000000000080', '2025-12-28', '2025-12-28 11:00:00+07', NULL, '2025-12-28 11:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_080', '2025-12-28 11:02:00', '2025-12-28 11:03:00', 194000, 194000, NULL, '11:30:00', 'payment_080.jpg', 'APPROVED', 'Enzy Storia', '2025-12-28 11:30:00'),
('c0010000-0000-0000-0000-000000000081', '2025-12-28', '2025-12-28 13:45:00+07', NULL, '2025-12-28 13:50:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_081', '2025-12-28 13:47:00', '2025-12-28 13:48:00', 226000, 226000, NULL, '14:15:00', 'payment_081.jpg', 'APPROVED', 'Feby Putri', '2025-12-28 14:15:00'),
('c0010000-0000-0000-0000-000000000082', '2025-12-28', '2025-12-28 19:30:00+07', NULL, '2025-12-28 19:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_082', '2025-12-28 19:32:00', '2025-12-28 19:33:00', 62000, 62000, NULL, '20:00:00', 'payment_082.jpg', 'APPROVED', 'Gilang Dirga', '2025-12-28 20:00:00'),
-- Day 28: 2025-12-29
('c0010000-0000-0000-0000-000000000083', '2025-12-29', '2025-12-29 12:15:00+07', NULL, '2025-12-29 12:20:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_083', '2025-12-29 12:17:00', '2025-12-29 12:18:00', 170000, 170000, NULL, '12:45:00', 'payment_083.jpg', 'APPROVED', 'Hana Hanifah', '2025-12-29 12:45:00'),
('c0010000-0000-0000-0000-000000000084', '2025-12-29', '2025-12-29 17:30:00+07', NULL, NULL, true, 'CANCELLED', '550e8400-e29b-41d4-a716-446655440000', 'cust_084', '2025-12-29 17:32:00', NULL, 138000, 138000, 'Table not available', NULL, NULL, 'EXPIRED', 'Isyana Sarasvati', NULL),
('c0010000-0000-0000-0000-000000000085', '2025-12-29', '2025-12-29 20:00:00+07', NULL, '2025-12-29 20:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_085', '2025-12-29 20:02:00', '2025-12-29 20:03:00', 257000, 257000, NULL, '20:30:00', 'payment_085.jpg', 'APPROVED', 'Joe Taslim', '2025-12-29 20:30:00'),
-- Day 29: 2025-12-30
('c0010000-0000-0000-0000-000000000086', '2025-12-30', '2025-12-30 11:30:00+07', NULL, '2025-12-30 11:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_086', '2025-12-30 11:32:00', '2025-12-30 11:33:00', 182000, 182000, NULL, '12:00:00', 'payment_086.jpg', 'APPROVED', 'Keisya Levronka', '2025-12-30 12:00:00'),
('c0010000-0000-0000-0000-000000000087', '2025-12-30', '2025-12-30 14:00:00+07', NULL, '2025-12-30 14:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_087', '2025-12-30 14:02:00', '2025-12-30 14:03:00', 112000, 112000, NULL, '14:30:00', 'payment_087.jpg', 'APPROVED', 'Lyodra Ginting', '2025-12-30 14:30:00'),
('c0010000-0000-0000-0000-000000000088', '2025-12-30', '2025-12-30 19:00:00+07', NULL, '2025-12-30 19:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_088', '2025-12-30 19:02:00', '2025-12-30 19:03:00', 300000, 300000, NULL, '19:30:00', 'payment_088.jpg', 'APPROVED', 'Mikha Tambayong', '2025-12-30 19:30:00'),
-- Day 30: 2025-12-31 (New Year Eve - busier)
('c0010000-0000-0000-0000-000000000089', '2025-12-31', '2025-12-31 12:00:00+07', NULL, '2025-12-31 12:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_089', '2025-12-31 12:02:00', '2025-12-31 12:03:00', 380000, 380000, NULL, '12:30:00', 'payment_089.jpg', 'APPROVED', 'Nagita Slavina', '2025-12-31 12:30:00'),
('c0010000-0000-0000-0000-000000000090', '2025-12-31', '2025-12-31 17:00:00+07', NULL, '2025-12-31 17:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_090', '2025-12-31 17:02:00', '2025-12-31 17:03:00', 450000, 450000, NULL, '17:30:00', 'payment_090.jpg', 'APPROVED', 'Omesh', '2025-12-31 17:30:00'),
('c0010000-0000-0000-0000-000000000091', '2025-12-31', '2025-12-31 19:00:00+07', NULL, '2025-12-31 19:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_091', '2025-12-31 19:02:00', '2025-12-31 19:03:00', 520000, 520000, NULL, '19:30:00', 'payment_091.jpg', 'APPROVED', 'Prisia Nasution', '2025-12-31 19:30:00'),
('c0010000-0000-0000-0000-000000000092', '2025-12-31', '2025-12-31 20:30:00+07', NULL, '2025-12-31 20:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_092', '2025-12-31 20:32:00', '2025-12-31 20:33:00', 600000, 600000, NULL, '21:00:00', 'payment_092.jpg', 'APPROVED', 'Raditya Dika', '2025-12-31 21:00:00'),
-- Day 31: 2026-01-01 (New Year)
('c0010000-0000-0000-0000-000000000093', '2026-01-01', '2026-01-01 12:00:00+07', NULL, '2026-01-01 12:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_093', '2026-01-01 12:02:00', '2026-01-01 12:03:00', 350000, 350000, NULL, '12:30:00', 'payment_093.jpg', 'APPROVED', 'Sandra Dewi', '2026-01-01 12:30:00'),
('c0010000-0000-0000-0000-000000000094', '2026-01-01', '2026-01-01 18:00:00+07', NULL, '2026-01-01 18:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_094', '2026-01-01 18:02:00', '2026-01-01 18:03:00', 420000, 420000, NULL, '18:30:00', 'payment_094.jpg', 'APPROVED', 'Titi Kamal', '2026-01-01 18:30:00'),
('c0010000-0000-0000-0000-000000000095', '2026-01-01', '2026-01-01 20:00:00+07', NULL, '2026-01-01 20:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_095', '2026-01-01 20:02:00', '2026-01-01 20:03:00', 280000, 280000, NULL, '20:30:00', 'payment_095.jpg', 'APPROVED', 'Uki Sulistiawaty', '2026-01-01 20:30:00'),
-- Day 32: 2026-01-02
('c0010000-0000-0000-0000-000000000096', '2026-01-02', '2026-01-02 11:30:00+07', NULL, '2026-01-02 11:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_096', '2026-01-02 11:32:00', '2026-01-02 11:33:00', 150000, 150000, NULL, '12:00:00', 'payment_096.jpg', 'APPROVED', 'Via Vallen', '2026-01-02 12:00:00'),
('c0010000-0000-0000-0000-000000000097', '2026-01-02', '2026-01-02 14:00:00+07', NULL, '2026-01-02 14:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_097', '2026-01-02 14:02:00', '2026-01-02 14:03:00', 195000, 195000, NULL, '14:30:00', 'payment_097.jpg', 'APPROVED', 'Widi Mulia', '2026-01-02 14:30:00'),
('c0010000-0000-0000-0000-000000000098', '2026-01-02', '2026-01-02 19:00:00+07', NULL, '2026-01-02 19:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_098', '2026-01-02 19:02:00', '2026-01-02 19:03:00', 244000, 244000, NULL, '19:30:00', 'payment_098.jpg', 'APPROVED', 'Xian Gaza', '2026-01-02 19:30:00'),
-- Day 33: 2026-01-03
('c0010000-0000-0000-0000-000000000099', '2026-01-03', '2026-01-03 12:00:00+07', NULL, '2026-01-03 12:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_099', '2026-01-03 12:02:00', '2026-01-03 12:03:00', 170000, 170000, NULL, '12:30:00', 'payment_099.jpg', 'APPROVED', 'Yuki Angela', '2026-01-03 12:30:00'),
('c0010000-0000-0000-0000-000000000100', '2026-01-03', '2026-01-03 18:30:00+07', NULL, NULL, true, 'CANCELLED', '550e8400-e29b-41d4-a716-446655440000', 'cust_100', '2026-01-03 18:32:00', NULL, 88000, 88000, 'Changed mind', NULL, NULL, 'EXPIRED', 'Zara JKT48', NULL),
('c0010000-0000-0000-0000-000000000101', '2026-01-03', '2026-01-03 20:00:00+07', NULL, '2026-01-03 20:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_101', '2026-01-03 20:02:00', '2026-01-03 20:03:00', 226000, 226000, NULL, '20:30:00', 'payment_101.jpg', 'APPROVED', 'Aqila JKT48', '2026-01-03 20:30:00'),
-- Day 34: 2026-01-04
('c0010000-0000-0000-0000-000000000102', '2026-01-04', '2026-01-04 11:00:00+07', NULL, '2026-01-04 11:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_102', '2026-01-04 11:02:00', '2026-01-04 11:03:00', 137000, 137000, NULL, '11:30:00', 'payment_102.jpg', 'APPROVED', 'Bella Shofie', '2026-01-04 11:30:00'),
('c0010000-0000-0000-0000-000000000103', '2026-01-04', '2026-01-04 13:45:00+07', NULL, '2026-01-04 13:50:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_103', '2026-01-04 13:47:00', '2026-01-04 13:48:00', 262000, 262000, NULL, '14:15:00', 'payment_103.jpg', 'APPROVED', 'Cut Tari', '2026-01-04 14:15:00'),
('c0010000-0000-0000-0000-000000000104', '2026-01-04', '2026-01-04 19:30:00+07', NULL, '2026-01-04 19:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_104', '2026-01-04 19:32:00', '2026-01-04 19:33:00', 75000, 75000, NULL, '20:00:00', 'payment_104.jpg', 'APPROVED', 'Dinda Hauw', '2026-01-04 20:00:00'),
-- Day 35: 2026-01-05
('c0010000-0000-0000-0000-000000000105', '2026-01-05', '2026-01-05 12:15:00+07', NULL, '2026-01-05 12:20:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_105', '2026-01-05 12:17:00', '2026-01-05 12:18:00', 182000, 182000, NULL, '12:45:00', 'payment_105.jpg', 'APPROVED', 'Elvia Cerolline', '2026-01-05 12:45:00'),
('c0010000-0000-0000-0000-000000000106', '2026-01-05', '2026-01-05 17:00:00+07', NULL, '2026-01-05 17:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_106', '2026-01-05 17:02:00', '2026-01-05 17:03:00', 100000, 100000, NULL, '17:30:00', 'payment_106.jpg', 'APPROVED', 'Febby Rastanty', '2026-01-05 17:30:00'),
('c0010000-0000-0000-0000-000000000107', '2026-01-05', '2026-01-05 20:15:00+07', NULL, '2026-01-05 20:20:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_107', '2026-01-05 20:17:00', '2026-01-05 20:18:00', 300000, 300000, NULL, '20:45:00', 'payment_107.jpg', 'APPROVED', 'Gading Marten', '2026-01-05 20:45:00'),
-- Day 36: 2026-01-06
('c0010000-0000-0000-0000-000000000108', '2026-01-06', '2026-01-06 11:30:00+07', NULL, '2026-01-06 11:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_108', '2026-01-06 11:32:00', '2026-01-06 11:33:00', 157000, 157000, NULL, '12:00:00', 'payment_108.jpg', 'APPROVED', 'Hamish Daud', '2026-01-06 12:00:00'),
('c0010000-0000-0000-0000-000000000109', '2026-01-06', '2026-01-06 14:00:00+07', NULL, '2026-01-06 14:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_109', '2026-01-06 14:02:00', '2026-01-06 14:03:00', 213000, 213000, NULL, '14:30:00', 'payment_109.jpg', 'APPROVED', 'Indra Bekti', '2026-01-06 14:30:00'),
('c0010000-0000-0000-0000-000000000110', '2026-01-06', '2026-01-06 19:00:00+07', NULL, '2026-01-06 19:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_110', '2026-01-06 19:02:00', '2026-01-06 19:03:00', 269000, 269000, NULL, '19:30:00', 'payment_110.jpg', 'APPROVED', 'Jerinx SID', '2026-01-06 19:30:00'),
-- Day 37: 2026-01-07
('c0010000-0000-0000-0000-000000000111', '2026-01-07', '2026-01-07 12:00:00+07', NULL, '2026-01-07 12:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_111', '2026-01-07 12:02:00', '2026-01-07 12:03:00', 144000, 144000, NULL, '12:30:00', 'payment_111.jpg', 'APPROVED', 'Kimberly Ryder', '2026-01-07 12:30:00'),
('c0010000-0000-0000-0000-000000000112', '2026-01-07', '2026-01-07 18:30:00+07', NULL, NULL, true, 'CANCELLED', '550e8400-e29b-41d4-a716-446655440000', 'cust_112', '2026-01-07 18:32:00', NULL, 176000, 176000, 'Weather conditions', NULL, NULL, 'EXPIRED', 'Lee Jeong Hoon', NULL),
('c0010000-0000-0000-0000-000000000113', '2026-01-07', '2026-01-07 20:00:00+07', NULL, '2026-01-07 20:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_113', '2026-01-07 20:02:00', '2026-01-07 20:03:00', 232000, 232000, NULL, '20:30:00', 'payment_113.jpg', 'APPROVED', 'Marshanda', '2026-01-07 20:30:00'),
-- Day 38: 2026-01-08 (Today)
('c0010000-0000-0000-0000-000000000114', '2026-01-08', '2026-01-08 11:00:00+07', NULL, '2026-01-08 11:05:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_114', '2026-01-08 11:02:00', '2026-01-08 11:03:00', 195000, 195000, NULL, '11:30:00', 'payment_114.jpg', 'APPROVED', 'Naysila Mirdad', '2026-01-08 11:30:00'),
('c0010000-0000-0000-0000-000000000115', '2026-01-08', '2026-01-08 13:30:00+07', NULL, '2026-01-08 13:35:00', true, 'FINISHED', '550e8400-e29b-41d4-a716-446655440000', 'cust_115', '2026-01-08 13:32:00', '2026-01-08 13:33:00', 88000, 88000, NULL, '14:00:00', 'payment_115.jpg', 'APPROVED', 'Olla Ramlan', '2026-01-08 14:00:00');

-- Order Items for Sunset Grill Orders
-- Menu: Brisket(88k/100), Salmon(82k/101), Shrimp(75k/103), Burger(62k/104), Veggie(55k/105), Wings(50k/106)
INSERT INTO order_items (id, menu_code, quantity, order_id) VALUES
('d0010000-0000-0000-0000-000000000001', 'a0010000-0000-0000-0000-000000000100', 1, 'c0010000-0000-0000-0000-000000000001'),
('d0010000-0000-0000-0000-000000000002', 'a0010000-0000-0000-0000-000000000104', 1, 'c0010000-0000-0000-0000-000000000001'),
('d0010000-0000-0000-0000-000000000003', 'a0010000-0000-0000-0000-000000000100', 1, 'c0010000-0000-0000-0000-000000000002'),
('d0010000-0000-0000-0000-000000000004', 'a0010000-0000-0000-0000-000000000101', 1, 'c0010000-0000-0000-0000-000000000002'),
('d0010000-0000-0000-0000-000000000005', 'a0010000-0000-0000-0000-000000000100', 2, 'c0010000-0000-0000-0000-000000000003'),
('d0010000-0000-0000-0000-000000000006', 'a0010000-0000-0000-0000-000000000104', 1, 'c0010000-0000-0000-0000-000000000003'),
('d0010000-0000-0000-0000-000000000007', 'a0010000-0000-0000-0000-000000000104', 2, 'c0010000-0000-0000-0000-000000000004'),
('d0010000-0000-0000-0000-000000000008', 'a0010000-0000-0000-0000-000000000100', 1, 'c0010000-0000-0000-0000-000000000005'),
('d0010000-0000-0000-0000-000000000009', 'a0010000-0000-0000-0000-000000000103', 1, 'c0010000-0000-0000-0000-000000000006'),
('d0010000-0000-0000-0000-000000000010', 'a0010000-0000-0000-0000-000000000101', 1, 'c0010000-0000-0000-0000-000000000006'),
('d0010000-0000-0000-0000-000000000011', 'a0010000-0000-0000-0000-000000000103', 1, 'c0010000-0000-0000-0000-000000000007'),
('d0010000-0000-0000-0000-000000000012', 'a0010000-0000-0000-0000-000000000104', 1, 'c0010000-0000-0000-0000-000000000007'),
('d0010000-0000-0000-0000-000000000013', 'a0010000-0000-0000-0000-000000000100', 2, 'c0010000-0000-0000-0000-000000000008'),
('d0010000-0000-0000-0000-000000000014', 'a0010000-0000-0000-0000-000000000101', 1, 'c0010000-0000-0000-0000-000000000008'),
('d0010000-0000-0000-0000-000000000015', 'a0010000-0000-0000-0000-000000000106', 2, 'c0010000-0000-0000-0000-000000000009'),
('d0010000-0000-0000-0000-000000000016', 'a0010000-0000-0000-0000-000000000100', 2, 'c0010000-0000-0000-0000-000000000010'),
('d0010000-0000-0000-0000-000000000017', 'a0010000-0000-0000-0000-000000000101', 1, 'c0010000-0000-0000-0000-000000000011'),
('d0010000-0000-0000-0000-000000000018', 'a0010000-0000-0000-0000-000000000100', 1, 'c0010000-0000-0000-0000-000000000012'),
('d0010000-0000-0000-0000-000000000019', 'a0010000-0000-0000-0000-000000000103', 1, 'c0010000-0000-0000-0000-000000000012'),
('d0010000-0000-0000-0000-000000000020', 'a0010000-0000-0000-0000-000000000106', 1, 'c0010000-0000-0000-0000-000000000012'),
('d0010000-0000-0000-0000-000000000021', 'a0010000-0000-0000-0000-000000000100', 1, 'c0010000-0000-0000-0000-000000000013'),
('d0010000-0000-0000-0000-000000000022', 'a0010000-0000-0000-0000-000000000104', 1, 'c0010000-0000-0000-0000-000000000013'),
('d0010000-0000-0000-0000-000000000023', 'a0010000-0000-0000-0000-000000000101', 2, 'c0010000-0000-0000-0000-000000000014'),
('d0010000-0000-0000-0000-000000000024', 'a0010000-0000-0000-0000-000000000100', 2, 'c0010000-0000-0000-0000-000000000015'),
('d0010000-0000-0000-0000-000000000025', 'a0010000-0000-0000-0000-000000000104', 2, 'c0010000-0000-0000-0000-000000000015'),
('d0010000-0000-0000-0000-000000000026', 'a0010000-0000-0000-0000-000000000103', 1, 'c0010000-0000-0000-0000-000000000016'),
('d0010000-0000-0000-0000-000000000027', 'a0010000-0000-0000-0000-000000000106', 1, 'c0010000-0000-0000-0000-000000000016'),
('d0010000-0000-0000-0000-000000000028', 'a0010000-0000-0000-0000-000000000100', 2, 'c0010000-0000-0000-0000-000000000017'),
('d0010000-0000-0000-0000-000000000029', 'a0010000-0000-0000-0000-000000000104', 1, 'c0010000-0000-0000-0000-000000000017'),
('d0010000-0000-0000-0000-000000000030', 'a0010000-0000-0000-0000-000000000100', 1, 'c0010000-0000-0000-0000-000000000018'),
('d0010000-0000-0000-0000-000000000031', 'a0010000-0000-0000-0000-000000000103', 1, 'c0010000-0000-0000-0000-000000000019'),
('d0010000-0000-0000-0000-000000000032', 'a0010000-0000-0000-0000-000000000101', 1, 'c0010000-0000-0000-0000-000000000019'),
('d0010000-0000-0000-0000-000000000033', 'a0010000-0000-0000-0000-000000000100', 2, 'c0010000-0000-0000-0000-000000000020'),
('d0010000-0000-0000-0000-000000000034', 'a0010000-0000-0000-0000-000000000105', 1, 'c0010000-0000-0000-0000-000000000020'),
('d0010000-0000-0000-0000-000000000035', 'a0010000-0000-0000-0000-000000000103', 1, 'c0010000-0000-0000-0000-000000000021'),
('d0010000-0000-0000-0000-000000000036', 'a0010000-0000-0000-0000-000000000101', 1, 'c0010000-0000-0000-0000-000000000022'),
('d0010000-0000-0000-0000-000000000037', 'a0010000-0000-0000-0000-000000000104', 1, 'c0010000-0000-0000-0000-000000000022'),
('d0010000-0000-0000-0000-000000000038', 'a0010000-0000-0000-0000-000000000106', 1, 'c0010000-0000-0000-0000-000000000022'),
('d0010000-0000-0000-0000-000000000039', 'a0010000-0000-0000-0000-000000000104', 1, 'c0010000-0000-0000-0000-000000000023'),
('d0010000-0000-0000-0000-000000000040', 'a0010000-0000-0000-0000-000000000106', 1, 'c0010000-0000-0000-0000-000000000023'),
('d0010000-0000-0000-0000-000000000041', 'a0010000-0000-0000-0000-000000000100', 1, 'c0010000-0000-0000-0000-000000000024'),
('d0010000-0000-0000-0000-000000000042', 'a0010000-0000-0000-0000-000000000101', 1, 'c0010000-0000-0000-0000-000000000024'),
('d0010000-0000-0000-0000-000000000043', 'a0010000-0000-0000-0000-000000000100', 1, 'c0010000-0000-0000-0000-000000000025'),
('d0010000-0000-0000-0000-000000000044', 'a0010000-0000-0000-0000-000000000106', 1, 'c0010000-0000-0000-0000-000000000025'),
('d0010000-0000-0000-0000-000000000045', 'a0010000-0000-0000-0000-000000000100', 2, 'c0010000-0000-0000-0000-000000000026'),
('d0010000-0000-0000-0000-000000000046', 'a0010000-0000-0000-0000-000000000106', 1, 'c0010000-0000-0000-0000-000000000026'),
('d0010000-0000-0000-0000-000000000047', 'a0010000-0000-0000-0000-000000000104', 1, 'c0010000-0000-0000-0000-000000000027'),
('d0010000-0000-0000-0000-000000000048', 'a0010000-0000-0000-0000-000000000100', 1, 'c0010000-0000-0000-0000-000000000028'),
('d0010000-0000-0000-0000-000000000049', 'a0010000-0000-0000-0000-000000000101', 1, 'c0010000-0000-0000-0000-000000000028'),
('d0010000-0000-0000-0000-000000000050', 'a0010000-0000-0000-0000-000000000100', 2, 'c0010000-0000-0000-0000-000000000029'),
('d0010000-0000-0000-0000-000000000051', 'a0010000-0000-0000-0000-000000000101', 1, 'c0010000-0000-0000-0000-000000000029'),
('d0010000-0000-0000-0000-000000000052', 'a0010000-0000-0000-0000-000000000105', 1, 'c0010000-0000-0000-0000-000000000030'),
('d0010000-0000-0000-0000-000000000053', 'a0010000-0000-0000-0000-000000000106', 1, 'c0010000-0000-0000-0000-000000000030');