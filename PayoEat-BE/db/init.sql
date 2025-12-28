CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Area Insert Restaurant Category
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
    tax BIGINT,
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

INSERT INTO restaurant (id,name,rating,total_rating,description,created_at,updated_at,is_active,tax,user_id,opening_hour,closing_hour,location,telephone_number,restaurant_image_url,qris_image_url,color,restaurant_category,is_open) VALUES
('550e8400-e29b-41d4-a716-446655440000','Sunset Grill',0.0,0,'Cozy rooftop grill with city views.','2024-01-01 10:00:00','2025-05-01 09:30:00',TRUE,10,101,'10:00:00','22:00:00','123 Skyline Ave','555-1234',
'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?fm=jpg&q=60&w=3000&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8cmVzdGF1cmFudHxlbnwwfHwwfHx8MA%3D%3D',
'7fa459ea-ee8a-3ca4-894e-db77e160355f','#FF5733',1,TRUE),

('550e8400-e29b-41d4-a716-446655440001','Green Garden',0.0,0,'Organic vegan restaurant.','2024-02-15 08:30:00','2025-04-20 14:00:00',TRUE,10,102,'09:00:00','21:00:00','456 Green St','555-5678',
'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQJ0EqHI6h5QgFTXGG_1i2FADG1xulRbVtecA&s',
'7fa459ea-ee8a-3ca4-894e-db77e1603552','#4CAF50',2,TRUE),

('550e8400-e29b-41d4-a716-446655440002','Ocean Breeze',0.0,0,'Seafood by the bay.','2023-11-11 12:00:00','2025-03-30 11:00:00',TRUE,10,103,'11:00:00','23:00:00','789 Ocean Drive','555-8765',
'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRWYNFqbPNDfW6HsOhfMJ5de4XwuteI2sV4Hg&s',
'7fa459ea-ee8a-3ca4-894e-db77e1603554','#00BCD4',3,TRUE),

('550e8400-e29b-41d4-a716-446655440003','Night Owl Diner',0.0,0,'Open late for all-night cravings.','2024-05-01 19:00:00','2025-05-16 21:00:00',TRUE,10,104,'18:00:00','03:00:00','321 Moonlight Blvd','555-8765',
'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRJTot5NBFQBa7x7e7HXiWluJT2dEVvd7kzyg&s',
'7fa459ea-ee8a-3ca4-894e-db77e1603556','#9C27B0',4,TRUE),

('550e8400-e29b-41d4-a716-446655440004','The Burger Spot',0.0,0,'Burgers and fries all day.','2024-03-10 10:00:00','2025-05-10 18:00:00',TRUE,10,105,'10:00:00','22:00:00','987 Burger Lane','555-3333',
'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSKTqY5iJ0QxYi6QY-e0Ny1BS-i6-vD2BFSJw&s',
'7fa459ea-ee8a-3ca4-894e-db77e1603558','#FFC107',5,TRUE),

('550e8400-e29b-41d4-a716-446655440005','Pasta Palace',0.0,0,'Authentic Italian pasta dishes.','2023-12-01 11:30:00','2025-05-01 12:00:00',TRUE,10,106,'11:30:00','22:30:00','111 Rome Ave','555-1122',
'https://dynamic-media-cdn.tripadvisor.com/media/photo-o/21/b8/eb/3a/tom-s.jpg',
'7fa459ea-ee8a-3ca4-894e-db77e1603550','#E91E63',1,TRUE),

('550e8400-e29b-41d4-a716-446655440006','Taco Fiesta',0.0,0,'Mexican street food favorites.','2024-04-05 12:00:00','2025-04-25 15:00:00',TRUE,10,107,'12:00:00','20:00:00','222 Salsa St','555-4455',
'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTxOSozKIEG5lTTXV9DXCgv-NWGcWRbpePq8w&s',
'7fa459ea-ee8a-3ca4-894e-db77e160355b','#FF9800',2,TRUE),

('550e8400-e29b-41d4-a716-446655440007','Sushi World',0.0,0,'Fresh sushi and sashimi.','2024-01-20 09:00:00','2025-05-10 14:00:00',TRUE,10,108,'09:00:00','21:30:00','333 Tokyo Lane','555-7788',
'https://dynamic-media-cdn.tripadvisor.com/media/photo-o/2b/09/bb/94/caption.jpg?w=400&h=300&s=1',
'7fa459ea-ee8a-3ca4-894e-db77e160355d','#03A9F4',3,TRUE),

('550e8400-e29b-41d4-a716-446655440008','Cafe Morning',0.0,0,'Perfect for breakfast and brunch.','2024-02-05 07:00:00','2025-03-15 10:30:00',TRUE,10,109,'07:00:00','14:00:00','444 Sunrise Way','555-7788',
'https://dynamic-media-cdn.tripadvisor.com/media/photo-o/22/a2/41/6e/caption.jpg?w=1200&h=1200&s=1',
'7fa459ea-ee8a-3ca4-894e-db77e160355f','#8BC34A',4,TRUE),

('550e8400-e29b-41d4-a716-446655440009','BBQ Brothers',0.0,0,'Barbecue meats and smoked dishes.','2024-01-12 15:00:00','2025-05-01 17:00:00',TRUE,10,110,'15:00:00','23:00:00','555 Smokehouse Ave','555-5566',
'https://dynamic-media-cdn.tripadvisor.com/media/photo-o/25/d5/4e/f6/miss-thu-is-a-lady-of.jpg?w=900&h=500&s=1',
'7fa459ea-ee8a-3ca4-894e-db77e1603552','#795548',5,TRUE);

--

CREATE TABLE IF NOT EXISTS orders (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    created_date DATE,
    order_time TIMESTAMP,
    order_message VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    order_status VARCHAR(50),
    restaurant_id UUID,
    customer_id VARCHAR(100),
    payment_begin_at TIMESTAMP,
    sub_total DOUBLE PRECISION,
    total_price DOUBLE PRECISION,
    tax_price DOUBLE PRECISION,
    cancellation_reason VARCHAR(255),
    dine_in_time TIME,
    payment_image_url TEXT,
    payment_image_rejection_reason VARCHAR(255),
    payment_image_rejection_count BIGINT,
    payment_status VARCHAR(50),
    customer_name VARCHAR(255),
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
    expiry_date TIMESTAMPTZ,
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
(111, 'admin', 'test@gmail.com', 1, 'hashed_test1234', '2024-01-01T10:00:00', '2025-05-01T09:30:00', true, 'admin_token', NULL);-- =====================================================
-- SUNSET GRILL SEED DATA (Dynamic Generation)
-- Restaurant ID: 550e8400-e29b-41d4-a716-446655440000
-- Uses loops and functions for clean data generation
-- =====================================================

DO $$
DECLARE
    v_restaurant_id UUID := '550e8400-e29b-41d4-a716-446655440000';
    v_order_id UUID;
    v_order_item_id UUID;
    v_review_id UUID;
    v_menu_codes UUID[] := ARRAY[
        'a0010000-0000-0000-0000-000000000100'::UUID,  -- Smoked Brisket (88000)
        'a0010000-0000-0000-0000-000000000101'::UUID,  -- Grilled Salmon (82000)
        'a0010000-0000-0000-0000-000000000103'::UUID,  -- Grilled Shrimp Skewers (75000)
        'a0010000-0000-0000-0000-000000000104'::UUID,  -- Sunset Burger (62000)
        'a0010000-0000-0000-0000-000000000105'::UUID,  -- Grilled Veggie Platter (55000)
        'a0010000-0000-0000-0000-000000000106'::UUID   -- Chicken Wings (50000)
    ];
    v_menu_prices NUMERIC[] := ARRAY[88000, 82000, 75000, 62000, 55000, 50000];
    v_customer_names TEXT[] := ARRAY[
        'Michael Johnson', 'Sarah Williams', 'David Chen', 'Emily Rodriguez',
        'James Anderson', 'Lisa Martinez', 'Robert Taylor', 'Amanda White',
        'Jennifer Lee', 'Christopher Brown', 'Patricia Garcia', 'Daniel Wilson',
        'Jessica Thompson', 'Matthew Davis', 'Michelle Harris', 'Kevin Clark',
        'Nancy Lewis', 'Steven Walker', 'Barbara Hall', 'Donald Young',
        'Alex Thompson', 'Maria Santos', 'John Smith', 'Rachel Green',
        'Tom Wilson', 'Sarah Connor', 'Michael Scott', 'Emma Watson',
        'Oliver Brown', 'Sophia Miller', 'William Taylor', 'Ava Johnson'
    ];
    v_messages TEXT[] := ARRAY[
        'Please make it spicy!', 'No onions please', '', 'Extra sauce please!',
        'Celebrating anniversary!', 'Well done please', 'Add extra fries',
        'Vegetarian options only', 'Dinner for family', 'Medium rare',
        'Birthday celebration', 'For takeout', 'Lunch order', 'Rush order please',
        'Table for 4', 'Office lunch', 'Lunch meeting', 'Group order'
    ];
    v_cancellation_reasons TEXT[] := ARRAY[
        'Customer changed mind', 'Payment expired', 'Duplicate order',
        'Dietary restrictions', 'Wrong items ordered'
    ];
    v_reviews TEXT[] := ARRAY[
        'Amazing brisket! Perfectly smoked and tender. The rooftop view was beautiful too.',
        'Great burger, followed my request perfectly. Service was quick!',
        'Excellent seafood! The salmon was incredibly fresh.',
        'Love the chicken wings! Extra sauce made it perfect.',
        'Perfect anniversary dinner! All dishes were outstanding.',
        'Good food, well cooked. Nice atmosphere.',
        'Delicious shrimp skewers! Great value for the price.',
        'Tasty burgers! Will definitely come back.',
        'Best vegetarian options I''ve had! The grilled veggie platter is amazing.',
        'Great family dinner! Portions were generous.',
        'The salmon is always perfect here! My go-to place for seafood.',
        'Perfectly cooked! Just how I like it.',
        'Amazing celebration! Food was excellent and service was top-notch.',
        'Good combo. Everything was fresh.',
        'Perfect for lunch meetings! Service was efficient.'
    ];
    v_days_ago INT;
    v_hours_ago INT;
    v_sub_total NUMERIC;
    v_tax_price NUMERIC;
    v_total_price NUMERIC;
    v_num_items INT;
    v_item_idx INT;
    v_quantity INT;
    v_customer_idx INT;
    v_total_rating NUMERIC := 0;
    v_review_count INT := 0;
BEGIN
    -- =====================================================
    -- PART 1: HISTORICAL FINISHED ORDERS (15 orders over past 30 days)
    -- =====================================================
    FOR i IN 1..15 LOOP
        v_order_id := uuid_generate_v4();
        v_days_ago := 1 + (i * 2);  -- Spread orders: 3, 5, 7, 9... 31 days ago
        v_customer_idx := (i - 1) % array_length(v_customer_names, 1) + 1;

        -- Calculate random order (1-3 items)
        v_num_items := 1 + (i % 3);
        v_sub_total := 0;

        -- Generate order items and calculate subtotal
        FOR j IN 1..v_num_items LOOP
            v_item_idx := ((i + j - 1) % array_length(v_menu_codes, 1)) + 1;
            v_quantity := 1 + (j % 2);  -- 1 or 2 quantity
            v_sub_total := v_sub_total + (v_menu_prices[v_item_idx] * v_quantity);
        END LOOP;

        -- Calculate tax and total
        v_tax_price := v_sub_total * 0.10;
        v_total_price := v_sub_total + v_tax_price;

        -- Insert order
        INSERT INTO orders (
            id, created_date, order_time, order_message, is_active, order_status,
            restaurant_id, customer_id, payment_begin_at, sub_total, total_price,
            tax_price, dine_in_time, payment_status, customer_name
        ) VALUES (
            v_order_id,
            CURRENT_DATE - (v_days_ago || ' days')::INTERVAL,
            NOW() - (v_days_ago || ' days')::INTERVAL,
            v_messages[(i % array_length(v_messages, 1)) + 1],
            TRUE,
            'FINISHED',
            v_restaurant_id,
            'cust_' || LPAD(i::TEXT, 3, '0'),
            NOW() - (v_days_ago || ' days')::INTERVAL + INTERVAL '10 minutes',
            v_sub_total,
            v_total_price,
            v_tax_price,
            ((10 + (i % 12))::TEXT || ':' || ((i % 6) * 10)::TEXT || ':00')::TIME,
            'APPROVED',
            v_customer_names[v_customer_idx]
        );

        -- Insert order items
        FOR j IN 1..v_num_items LOOP
            v_item_idx := ((i + j - 1) % array_length(v_menu_codes, 1)) + 1;
            v_quantity := 1 + (j % 2);

            INSERT INTO order_items (id, menu_code, quantity, order_id)
            VALUES (
                uuid_generate_v4(),
                v_menu_codes[v_item_idx],
                v_quantity,
                v_order_id
            );
        END LOOP;

        -- Insert review (for most finished orders)
        IF i <= 15 THEN  -- Create reviews for all 15 orders
            DECLARE
                v_rating NUMERIC;
            BEGIN
                -- Generate ratings: mostly 4-5 stars
                v_rating := 4.0 + (CASE
                    WHEN i % 4 = 0 THEN 1.0   -- 5 stars
                    WHEN i % 3 = 0 THEN 0.5   -- 4.5 stars
                    ELSE 0.0                   -- 4 stars
                END);

                v_total_rating := v_total_rating + v_rating;
                v_review_count := v_review_count + 1;

                INSERT INTO review (
                    id, review_content, created_at, updated_at, is_active,
                    restaurant_id, rating, customer_name, order_id, customer_id
                ) VALUES (
                    uuid_generate_v4(),
                    v_reviews[((i - 1) % array_length(v_reviews, 1)) + 1],
                    NOW() - ((v_days_ago - 1) || ' days')::INTERVAL,
                    NOW() - ((v_days_ago - 1) || ' days')::INTERVAL,
                    TRUE,
                    v_restaurant_id,
                    v_rating,
                    v_customer_names[v_customer_idx],
                    v_order_id,
                    'cust_' || LPAD(i::TEXT, 3, '0')
                );
            END;
        END IF;
    END LOOP;

    -- =====================================================
    -- PART 2: CANCELLED ORDERS (5 orders over past 30 days)
    -- =====================================================
    FOR i IN 1..5 LOOP
        v_order_id := uuid_generate_v4();
        v_days_ago := 4 + (i * 5);  -- Spread: 9, 14, 19, 24, 29 days ago
        v_customer_idx := (15 + i - 1) % array_length(v_customer_names, 1) + 1;

        -- Calculate order (1-2 items)
        v_num_items := 1 + (i % 2);
        v_sub_total := 0;

        FOR j IN 1..v_num_items LOOP
            v_item_idx := ((i + j + 2) % array_length(v_menu_codes, 1)) + 1;
            v_quantity := 1;
            v_sub_total := v_sub_total + v_menu_prices[v_item_idx];
        END LOOP;

        v_tax_price := v_sub_total * 0.10;
        v_total_price := v_sub_total + v_tax_price;

        -- Insert cancelled order
        INSERT INTO orders (
            id, created_date, order_time, order_message, is_active, order_status,
            restaurant_id, customer_id, payment_begin_at, sub_total, total_price,
            tax_price, cancellation_reason, customer_name
        ) VALUES (
            v_order_id,
            CURRENT_DATE - (v_days_ago || ' days')::INTERVAL,
            NOW() - (v_days_ago || ' days')::INTERVAL,
            CASE WHEN i % 2 = 0 THEN v_messages[i] ELSE '' END,
            TRUE,
            'CANCELLED',
            v_restaurant_id,
            'cust_cancel_' || LPAD(i::TEXT, 3, '0'),
            CASE WHEN i % 2 = 0 THEN NOW() - (v_days_ago || ' days')::INTERVAL + INTERVAL '10 minutes' ELSE NULL END,
            v_sub_total,
            v_total_price,
            v_tax_price,
            v_cancellation_reasons[((i - 1) % array_length(v_cancellation_reasons, 1)) + 1],
            v_customer_names[v_customer_idx]
        );

        -- Insert order items
        FOR j IN 1..v_num_items LOOP
            v_item_idx := ((i + j + 2) % array_length(v_menu_codes, 1)) + 1;

            INSERT INTO order_items (id, menu_code, quantity, order_id)
            VALUES (
                uuid_generate_v4(),
                v_menu_codes[v_item_idx],
                1,
                v_order_id
            );
        END LOOP;
    END LOOP;

    -- =====================================================
    -- PART 3: INCOMING ORDERS (RECEIVED) - 4 orders today
    -- =====================================================
    FOR i IN 1..4 LOOP
        v_order_id := uuid_generate_v4();
        v_hours_ago := i;  -- 1, 2, 3, 4 minutes ago (well within 10 min expiry)
        v_customer_idx := (20 + i - 1) % array_length(v_customer_names, 1) + 1;

        v_num_items := 1 + (i % 3);
        v_sub_total := 0;

        FOR j IN 1..v_num_items LOOP
            v_item_idx := ((i + j + 5) % array_length(v_menu_codes, 1)) + 1;
            v_quantity := 1 + (j % 2);
            v_sub_total := v_sub_total + (v_menu_prices[v_item_idx] * v_quantity);
        END LOOP;

        v_tax_price := v_sub_total * 0.10;
        v_total_price := v_sub_total + v_tax_price;

        -- Insert incoming order
        INSERT INTO orders (
            id, created_date, order_time, order_message, is_active, order_status,
            restaurant_id, customer_id, sub_total, total_price, tax_price, customer_name
        ) VALUES (
            v_order_id,
            CURRENT_DATE,
            NOW() - (v_hours_ago || ' minutes')::INTERVAL,
            v_messages[(i + 10) % array_length(v_messages, 1) + 1],
            TRUE,
            'RECEIVED',
            v_restaurant_id,
            'cust_incoming_' || LPAD(i::TEXT, 3, '0'),
            v_sub_total,
            v_total_price,
            v_tax_price,
            v_customer_names[v_customer_idx]
        );

        -- Insert order items
        FOR j IN 1..v_num_items LOOP
            v_item_idx := ((i + j + 5) % array_length(v_menu_codes, 1)) + 1;
            v_quantity := 1 + (j % 2);

            INSERT INTO order_items (id, menu_code, quantity, order_id)
            VALUES (
                uuid_generate_v4(),
                v_menu_codes[v_item_idx],
                v_quantity,
                v_order_id
            );
        END LOOP;
    END LOOP;

    -- =====================================================
    -- PART 4: AWAITING PAYMENT (PAYMENT) - 3 orders today
    -- =====================================================
    FOR i IN 1..3 LOOP
        v_order_id := uuid_generate_v4();
        v_hours_ago := 15 + (i * 5);  -- 20, 25, 30 minutes ago
        v_customer_idx := (24 + i - 1) % array_length(v_customer_names, 1) + 1;

        v_num_items := 1 + (i % 2);
        v_sub_total := 0;

        FOR j IN 1..v_num_items LOOP
            v_item_idx := ((i + j + 3) % array_length(v_menu_codes, 1)) + 1;
            v_quantity := 1 + ((i + j) % 2);
            v_sub_total := v_sub_total + (v_menu_prices[v_item_idx] * v_quantity);
        END LOOP;

        v_tax_price := v_sub_total * 0.10;
        v_total_price := v_sub_total + v_tax_price;

        -- Insert payment pending order
        INSERT INTO orders (
            id, created_date, order_time, order_message, is_active, order_status,
            restaurant_id, customer_id, payment_begin_at, sub_total, total_price,
            tax_price, payment_status, payment_image_url, customer_name
        ) VALUES (
            v_order_id,
            CURRENT_DATE,
            NOW() - (v_hours_ago || ' minutes')::INTERVAL,
            CASE WHEN i = 2 THEN 'Group order' WHEN i = 3 THEN 'Office lunch' ELSE '' END,
            TRUE,
            'PAYMENT',
            v_restaurant_id,
            'cust_payment_' || LPAD(i::TEXT, 3, '0'),
            NOW() - ((v_hours_ago - 5) || ' minutes')::INTERVAL,
            v_sub_total,
            v_total_price,
            v_tax_price,
            'UPLOADED',
            CASE
                WHEN i = 1 THEN 'https://images.unsplash.com/photo-1554224311-beee4ece0a2f?w=800&h=600&fit=crop'
                WHEN i = 2 THEN 'https://images.unsplash.com/photo-1563013544-824ae1b704d3?w=800&h=600&fit=crop'
                WHEN i = 3 THEN 'https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=800&h=600&fit=crop'
            END,
            v_customer_names[v_customer_idx]
        );

        -- Insert order items
        FOR j IN 1..v_num_items LOOP
            v_item_idx := ((i + j + 3) % array_length(v_menu_codes, 1)) + 1;
            v_quantity := 1 + ((i + j) % 2);

            INSERT INTO order_items (id, menu_code, quantity, order_id)
            VALUES (
                uuid_generate_v4(),
                v_menu_codes[v_item_idx],
                v_quantity,
                v_order_id
            );
        END LOOP;
    END LOOP;

    -- =====================================================
    -- PART 5: CONFIRMED ORDERS (CONFIRMED) - 2 orders today
    -- =====================================================
    FOR i IN 1..2 LOOP
        v_order_id := uuid_generate_v4();
        v_hours_ago := 35 + (i * 3);  -- 38, 41 minutes ago
        v_customer_idx := (27 + i - 1) % array_length(v_customer_names, 1) + 1;

        v_num_items := 1 + (i % 2);
        v_sub_total := 0;

        FOR j IN 1..v_num_items LOOP
            v_item_idx := ((i + j + 4) % array_length(v_menu_codes, 1)) + 1;
            v_quantity := 1 + (i % 2);
            v_sub_total := v_sub_total + (v_menu_prices[v_item_idx] * v_quantity);
        END LOOP;

        v_tax_price := v_sub_total * 0.10;
        v_total_price := v_sub_total + v_tax_price;

        -- Insert confirmed order
        INSERT INTO orders (
            id, created_date, order_time, order_message, is_active, order_status,
            restaurant_id, customer_id, payment_begin_at, sub_total, total_price,
            tax_price, payment_status, customer_name
        ) VALUES (
            v_order_id,
            CURRENT_DATE,
            NOW() - (v_hours_ago || ' minutes')::INTERVAL,
            CASE WHEN i = 1 THEN 'Rush order please' ELSE '' END,
            TRUE,
            'CONFIRMED',
            v_restaurant_id,
            'cust_confirmed_' || LPAD(i::TEXT, 3, '0'),
            NOW() - ((v_hours_ago - 5) || ' minutes')::INTERVAL,
            v_sub_total,
            v_total_price,
            v_tax_price,
            'APPROVED',
            v_customer_names[v_customer_idx]
        );

        -- Insert order items
        FOR j IN 1..v_num_items LOOP
            v_item_idx := ((i + j + 4) % array_length(v_menu_codes, 1)) + 1;
            v_quantity := 1 + (i % 2);

            INSERT INTO order_items (id, menu_code, quantity, order_id)
            VALUES (
                uuid_generate_v4(),
                v_menu_codes[v_item_idx],
                v_quantity,
                v_order_id
            );
        END LOOP;
    END LOOP;

    -- =====================================================
    -- PART 6: ACTIVE ORDERS (ACTIVE) - 3 orders today
    -- =====================================================
    FOR i IN 1..3 LOOP
        v_order_id := uuid_generate_v4();
        v_hours_ago := 45 + (i * 5);  -- 50, 55, 60 minutes ago
        v_customer_idx := (29 + i - 1) % array_length(v_customer_names, 1) + 1;

        v_num_items := 2 + (i % 2);
        v_sub_total := 0;

        FOR j IN 1..v_num_items LOOP
            v_item_idx := ((i + j) % array_length(v_menu_codes, 1)) + 1;
            v_quantity := 1 + (j % 2);
            v_sub_total := v_sub_total + (v_menu_prices[v_item_idx] * v_quantity);
        END LOOP;

        v_tax_price := v_sub_total * 0.10;
        v_total_price := v_sub_total + v_tax_price;

        -- Insert active order
        INSERT INTO orders (
            id, created_date, order_time, order_message, is_active, order_status,
            restaurant_id, customer_id, payment_begin_at, sub_total, total_price,
            tax_price, dine_in_time, payment_status, customer_name
        ) VALUES (
            v_order_id,
            CURRENT_DATE,
            NOW() - (v_hours_ago || ' minutes')::INTERVAL,
            CASE WHEN i = 1 THEN 'Table for 4' WHEN i = 2 THEN 'Well done please' ELSE '' END,
            TRUE,
            'ACTIVE',
            v_restaurant_id,
            'cust_active_' || LPAD(i::TEXT, 3, '0'),
            NOW() - ((v_hours_ago - 5) || ' minutes')::INTERVAL,
            v_sub_total,
            v_total_price,
            v_tax_price,
            ((12 + i)::TEXT || ':' || (i * 15)::TEXT || ':00')::TIME,
            'APPROVED',
            v_customer_names[v_customer_idx]
        );

        -- Insert order items
        FOR j IN 1..v_num_items LOOP
            v_item_idx := ((i + j) % array_length(v_menu_codes, 1)) + 1;
            v_quantity := 1 + (j % 2);

            INSERT INTO order_items (id, menu_code, quantity, order_id)
            VALUES (
                uuid_generate_v4(),
                v_menu_codes[v_item_idx],
                v_quantity,
                v_order_id
            );
        END LOOP;
    END LOOP;

    -- =====================================================
    -- PART 7: READY ORDERS (READY) - 2 orders today
    -- =====================================================
    FOR i IN 1..2 LOOP
        v_order_id := uuid_generate_v4();
        v_hours_ago := 68 + (i * 4);  -- 72, 76 minutes ago
        v_customer_idx := (32 + i - 1) % array_length(v_customer_names, 1) + 1;

        v_num_items := 1 + (i % 2);
        v_sub_total := 0;

        FOR j IN 1..v_num_items LOOP
            v_item_idx := ((i + j + 1) % array_length(v_menu_codes, 1)) + 1;
            v_quantity := 1 + (i % 2);
            v_sub_total := v_sub_total + (v_menu_prices[v_item_idx] * v_quantity);
        END LOOP;

        v_tax_price := v_sub_total * 0.10;
        v_total_price := v_sub_total + v_tax_price;

        -- Insert ready order
        INSERT INTO orders (
            id, created_date, order_time, order_message, is_active, order_status,
            restaurant_id, customer_id, payment_begin_at, sub_total, total_price,
            tax_price, dine_in_time, payment_status, customer_name
        ) VALUES (
            v_order_id,
            CURRENT_DATE,
            NOW() - (v_hours_ago || ' minutes')::INTERVAL,
            CASE WHEN i = 1 THEN 'For takeout' ELSE '' END,
            TRUE,
            'READY',
            v_restaurant_id,
            'cust_ready_' || LPAD(i::TEXT, 3, '0'),
            NOW() - ((v_hours_ago - 5) || ' minutes')::INTERVAL,
            v_sub_total,
            v_total_price,
            v_tax_price,
            ((13 + i)::TEXT || ':' || (i * 20)::TEXT || ':00')::TIME,
            'APPROVED',
            v_customer_names[v_customer_idx]
        );

        -- Insert order items
        FOR j IN 1..v_num_items LOOP
            v_item_idx := ((i + j + 1) % array_length(v_menu_codes, 1)) + 1;
            v_quantity := 1 + (i % 2);

            INSERT INTO order_items (id, menu_code, quantity, order_id)
            VALUES (
                uuid_generate_v4(),
                v_menu_codes[v_item_idx],
                v_quantity,
                v_order_id
            );
        END LOOP;
    END LOOP;

    -- =====================================================
    -- PART 8: UPDATE RESTAURANT RATING
    -- =====================================================
    UPDATE restaurant
    SET rating = ROUND((v_total_rating / v_review_count)::NUMERIC, 2),
        total_rating = v_review_count,
        updated_at = NOW()
    WHERE id = v_restaurant_id;

    RAISE NOTICE 'Seed data generated successfully!';
    RAISE NOTICE 'Historical FINISHED orders: 15';
    RAISE NOTICE 'Historical CANCELLED orders: 5';
    RAISE NOTICE 'Active RECEIVED orders: 4 (1-4 min ago)';
    RAISE NOTICE 'Active PAYMENT orders: 3 (20-30 min ago)';
    RAISE NOTICE 'Active CONFIRMED orders: 2 (38-41 min ago)';
    RAISE NOTICE 'Active ACTIVE orders: 3 (50-60 min ago)';
    RAISE NOTICE 'Active READY orders: 2 (72-76 min ago)';
    RAISE NOTICE 'Total reviews: %', v_review_count;
    RAISE NOTICE 'Average rating: %', ROUND((v_total_rating / v_review_count)::NUMERIC, 2);

END $$;
