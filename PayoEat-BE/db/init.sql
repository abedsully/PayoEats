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
('550e8400-e29b-41d4-a716-446655440000','Sunset Grill',0.0,0,'Cozy rooftop grill with city views.','2024-01-01 10:00:00','2025-05-01 09:30:00',TRUE,10,101,'10:00:00','22:00:00','123 Skyline Ave','555-1234','https://images.unsplash.com/photo-1579599187352-8d76d8b39d48?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','7fa459ea-ee8a-3ca4-894e-db77e160355f','#FF5733',1,TRUE),
('550e8400-e29b-41d4-a716-446655440001','Green Garden',0.0,0,'Organic vegan restaurant.','2024-02-15 08:30:00','2025-04-20 14:00:00',TRUE,10,102,'09:00:00','21:00:00','456 Green St','555-5678','https://images.unsplash.com/photo-1555998188-75c13b2c6df4?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','#4CAF50',2,TRUE),
('550e8400-e29b-41d4-a716-446655440002','Ocean Breeze',0.0,0,'Seafood by the bay.','2023-11-11 12:00:00','2025-03-30 11:00:00',TRUE,10,103,'11:00:00','23:00:00','789 Ocean Drive','555-8765','https://images.unsplash.com/photo-1554679782-b7e271a3962d?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','#00BCD4',3,TRUE),
('550e8400-e29b-41d4-a716-446655440003','Night Owl Diner',0.0,0,'Open late for all-night cravings.','2024-05-01 19:00:00','2025-05-16 21:00:00',TRUE,10,104,'18:00:00','03:00:00','321 Moonlight Blvd','555-8765','https://images.unsplash.com/photo-1557007705-7f4159516801?q=80&w=1935&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','#9C27B0',4,TRUE),
('550e8400-e29b-41d4-a716-446655440004','The Burger Spot',0.0,0,'Burgers and fries all day.','2024-03-10 10:00:00','2025-05-10 18:00:00',TRUE,10,105,'10:00:00','22:00:00','987 Burger Lane','555-3333','https://images.unsplash.com/photo-1568901346375-23c9450c58cd?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','#FFC107',5,TRUE),
('550e8400-e29b-41d4-a716-446655440005','Pasta Palace',0.0,0,'Authentic Italian pasta dishes.','2023-12-01 11:30:00','2025-05-01 12:00:00',TRUE,10,106,'11:30:00','22:30:00','111 Rome Ave','555-1122','https://images.unsplash.com/photo-1549488344-2451bc047b0a?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','#E91E63',1,TRUE),
('550e8400-e29b-41d4-a716-446655440006','Taco Fiesta',0.0,0,'Mexican street food favorites.','2024-04-05 12:00:00','2025-04-25 15:00:00',TRUE,10,107,'12:00:00','20:00:00','222 Salsa St','555-4455','https://images.unsplash.com/photo-1551528628-9109435427d1?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','#FF9800',2,TRUE),
('550e8400-e29b-41d4-a716-446655440007','Sushi World',0.0,0,'Fresh sushi and sashimi.','2024-01-20 09:00:00','2025-05-10 14:00:00',TRUE,10,108,'09:00:00','21:30:00','333 Tokyo Lane','555-7788','https://images.unsplash.com/photo-1553621042-f6e147245786?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','#03A9F4',3,TRUE),
('550e8400-e29b-41d4-a716-446655440008','Cafe Morning',0.0,0,'Perfect for breakfast and brunch.','2024-02-05 07:00:00','2025-03-15 10:30:00',TRUE,10,109,'07:00:00','14:00:00','444 Sunrise Way','555-7788','https://images.unsplash.com/photo-1541167760496-1070773d2f32?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','#8BC34A',4,TRUE),
('550e8400-e29b-41d4-a716-446655440009','BBQ Brothers',0.0,0,'Barbecue meats and smoked dishes.','2024-01-12 15:00:00','2025-05-01 17:00:00',TRUE,10,110,'15:00:00','23:00:00','555 Smokehouse Ave','555-5566','https://images.unsplash.com/photo-1610609384795-3c2890656001?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','#795548',5,TRUE);

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


-- Other Restaurant Menus
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

INSERT INTO restaurant (id,name,rating,total_rating,description,created_at,updated_at,is_active,tax,user_id,opening_hour,closing_hour,location,telephone_number,restaurant_image_url,qris_image_url,color,restaurant_category,is_open) VALUES
('550e8400-e29b-41d4-a716-446655440000','Sunset Grill',0.0,0,'Cozy rooftop grill with city views.','2024-01-01 10:00:00','2025-05-01 09:30:00',TRUE,10,101,'10:00:00','22:00:00','123 Skyline Ave','555-1234','https://images.unsplash.com/photo-1579599187352-8d76d8b39d48?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','7fa459ea-ee8a-3ca4-894e-db77e160355f','#FF5733',1,TRUE),
('550e8400-e29b-41d4-a716-446655440001','Green Garden',0.0,0,'Organic vegan restaurant.','2024-02-15 08:30:00','2025-04-20 14:00:00',TRUE,10,102,'09:00:00','21:00:00','456 Green St','555-5678','https://images.unsplash.com/photo-1555998188-75c13b2c6df4?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','#4CAF50',2,TRUE),
('550e8400-e29b-41d4-a716-446655440002','Ocean Breeze',0.0,0,'Seafood by the bay.','2023-11-11 12:00:00','2025-03-30 11:00:00',TRUE,10,103,'11:00:00','23:00:00','789 Ocean Drive','555-8765','https://images.unsplash.com/photo-1554679782-b7e271a3962d?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','#00BCD4',3,TRUE),
('550e8400-e29b-41d4-a716-446655440003','Night Owl Diner',0.0,0,'Open late for all-night cravings.','2024-05-01 19:00:00','2025-05-16 21:00:00',TRUE,10,104,'18:00:00','03:00:00','321 Moonlight Blvd','555-8765','https://images.unsplash.com/photo-1557007705-7f4159516801?q=80&w=1935&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','#9C27B0',4,TRUE),
('550e8400-e29b-41d4-a716-446655440004','The Burger Spot',0.0,0,'Burgers and fries all day.','2024-03-10 10:00:00','2025-05-10 18:00:00',TRUE,10,105,'10:00:00','22:00:00','987 Burger Lane','555-3333','https://images.unsplash.com/photo-1568901346375-23c9450c58cd?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','#FFC107',5,TRUE),
('550e8400-e29b-41d4-a716-446655440005','Pasta Palace',0.0,0,'Authentic Italian pasta dishes.','2023-12-01 11:30:00','2025-05-01 12:00:00',TRUE,10,106,'11:30:00','22:30:00','111 Rome Ave','555-1122','https://images.unsplash.com/photo-1549488344-2451bc047b0a?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','#E91E63',1,TRUE),
('550e8400-e29b-41d4-a716-446655440006','Taco Fiesta',0.0,0,'Mexican street food favorites.','2024-04-05 12:00:00','2025-04-25 15:00:00',TRUE,10,107,'12:00:00','20:00:00','222 Salsa St','555-4455','https://images.unsplash.com/photo-1551528628-9109435427d1?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','#FF9800',2,TRUE),
('550e8400-e29b-41d4-a716-446655440007','Sushi World',0.0,0,'Fresh sushi and sashimi.','2024-01-20 09:00:00','2025-05-10 14:00:00',TRUE,10,108,'09:00:00','21:30:00','333 Tokyo Lane','555-7788','https://images.unsplash.com/photo-1553621042-f6e147245786?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','#03A9F4',3,TRUE),
('550e8400-e29b-41d4-a716-446655440008','Cafe Morning',0.0,0,'Perfect for breakfast and brunch.','2024-02-05 07:00:00','2025-03-15 10:30:00',TRUE,10,109,'07:00:00','14:00:00','444 Sunrise Way','555-7788','https://images.unsplash.com/photo-1541167760496-1070773d2f32?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','#8BC34A',4,TRUE),
('550e8400-e29b-41d4-a716-446655440009','BBQ Brothers',0.0,0,'Barbecue meats and smoked dishes.','2024-01-12 15:00:00','2025-05-01 17:00:00',TRUE,10,110,'15:00:00','23:00:00','555 Smokehouse Ave','555-5566','https://images.unsplash.com/photo-1610609384795-3c2890656001?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','#795548',5,TRUE);