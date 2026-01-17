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