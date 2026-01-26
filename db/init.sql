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
('550e8400-e29b-41d4-a716-446655440000','Sunset Grill',4.5,4,'Cozy rooftop grill with city views.','2024-01-01 10:00:00','2025-05-01 09:30:00',TRUE,101,'09:00:00','22:00:00','Jl. Sudirman No. 123, Lantai 15, Jakarta Selatan 12190','085789326155','https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?fm=jpg&q=60&w=3000','https://images.unsplash.com/photo-1595079676339-1534801ad6cf?q=80&w=2070&auto=format&fit=crop','#FF5733',1,TRUE),

('550e8400-e29b-41d4-a716-446655440001','Green Garden',4.25,4,'Organic vegan restaurant.','2024-02-15 08:30:00','2025-04-20 14:00:00',TRUE,102,'08:00:00','21:00:00','Jl. Kemang Raya No. 45, Jakarta Selatan 12730','081234567890','https://images.unsplash.com/photo-1466978913421-dad2ebd01d17?q=80&w=2074&auto=format&fit=crop','https://images.unsplash.com/photo-1595079676339-1534801ad6cf?q=80&w=2070&auto=format&fit=crop','#4CAF50',2,TRUE),

('550e8400-e29b-41d4-a716-446655440002','Ocean Breeze',4.6,5,'Seafood by the bay.','2023-11-11 12:00:00','2025-03-30 11:00:00',TRUE,103,'07:00:00','23:00:00','Jl. Pantai Indah Kapuk No. 88, Jakarta Utara 14470','087856432198','https://images.unsplash.com/photo-1514933651103-005eec06c04b?q=80&w=2074&auto=format&fit=crop','https://images.unsplash.com/photo-1595079676339-1534801ad6cf?q=80&w=2070&auto=format&fit=crop','#00BCD4',3,TRUE),

('550e8400-e29b-41d4-a716-446655440003','Night Owl Diner',4.0,3,'Open late for all-night cravings.','2024-05-01 19:00:00','2025-05-16 21:00:00',TRUE,104,'09:00:00','23:00:00','Jl. Senopati No. 72, Jakarta Selatan 12110','082145678932','https://images.unsplash.com/photo-1552566626-52f8b828add9?q=80&w=2070&auto=format&fit=crop','https://images.unsplash.com/photo-1595079676339-1534801ad6cf?q=80&w=2070&auto=format&fit=crop','#9C27B0',4,TRUE),

('550e8400-e29b-41d4-a716-446655440004','The Burger Spot',4.25,4,'Burgers and fries all day.','2024-03-10 10:00:00','2025-05-10 18:00:00',TRUE,105,'09:00:00','22:00:00','Jl. Gandaria No. 99, Jakarta Selatan 12420','089512347865','https://images.unsplash.com/photo-1466978913421-dad2ebd01d17?q=80&w=2074&auto=format&fit=crop','https://images.unsplash.com/photo-1595079676339-1534801ad6cf?q=80&w=2070&auto=format&fit=crop','#FFC107',5,TRUE),

('550e8400-e29b-41d4-a716-446655440005','Pasta Palace',4.6,5,'Authentic Italian pasta dishes.','2023-12-01 11:30:00','2025-05-01 12:00:00',TRUE,106,'08:30:00','22:30:00','Jl. Gunawarman No. 56, Jakarta Selatan 12110','081398765432','https://images.unsplash.com/photo-1555396273-367ea4eb4db5?q=80&w=2074&auto=format&fit=crop','https://images.unsplash.com/photo-1595079676339-1534801ad6cf?q=80&w=2070&auto=format&fit=crop','#E91E63',1,TRUE),

('550e8400-e29b-41d4-a716-446655440006','Taco Fiesta',4.0,3,'Mexican street food favorites.','2024-04-05 12:00:00','2025-04-25 15:00:00',TRUE,107,'09:00:00','20:00:00','Jl. Cipete Raya No. 33, Jakarta Selatan 12410','085632149875','https://media-cdn.tripadvisor.com/media/photo-s/1a/27/60/24/soft-and-crispy-taco.jpg','https://images.unsplash.com/photo-1595079676339-1534801ad6cf?q=80&w=2070&auto=format&fit=crop','#FF9800',2,TRUE),

('550e8400-e29b-41d4-a716-446655440007','Sushi World',4.75,4,'Fresh sushi and sashimi.','2024-01-20 09:00:00','2025-05-10 14:00:00',TRUE,108,'09:00:00','21:30:00','Jl. Senayan No. 18, Jakarta Pusat 10270','087745632189','https://images.unsplash.com/photo-1579027989536-b7b1f875659b?q=80&w=2070&auto=format&fit=crop','https://images.unsplash.com/photo-1595079676339-1534801ad6cf?q=80&w=2070&auto=format&fit=crop','#03A9F4',3,TRUE),

('550e8400-e29b-41d4-a716-446655440008','Cafe Morning',4.5,4,'Perfect for breakfast and brunch.','2024-02-05 07:00:00','2025-03-15 10:30:00',TRUE,109,'07:00:00','14:00:00','Jl. Cikini Raya No. 25, Jakarta Pusat 10330','082198765431','https://images.unsplash.com/photo-1554118811-1e0d58224f24?q=80&w=2047&auto=format&fit=crop','https://images.unsplash.com/photo-1595079676339-1534801ad6cf?q=80&w=2070&auto=format&fit=crop','#8BC34A',4,TRUE),

('550e8400-e29b-41d4-a716-446655440009','BBQ Brothers',4.5,4,'Barbecue meats and smoked dishes.','2024-01-12 15:00:00','2025-05-01 17:00:00',TRUE,110,'08:00:00','23:00:00','Jl. Kelapa Gading Boulevard No. 77, Jakarta Utara 14240','089876543210','https://images.unsplash.com/photo-1555992336-fb0d29498b13?q=80&w=2064&auto=format&fit=crop','https://images.unsplash.com/photo-1595079676339-1534801ad6cf?q=80&w=2070&auto=format&fit=crop','#795548',5,TRUE);

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

-- Review seed data
INSERT INTO review (id, review_content, created_at, updated_at, is_active, restaurant_id, rating, customer_name, review_image_url, order_id, customer_id) VALUES
-- Sunset Grill (4 reviews, avg 4.5)
('b0010000-0000-0000-0000-000000000001', 'Makanannya enak banget! Brisket-nya lembut dan smoky. Pemandangan dari rooftop juga keren.', '2024-12-15 19:30:00', '2024-12-15 19:30:00', true, '550e8400-e29b-41d4-a716-446655440000', 5.0, 'Budi Santoso', 'https://images.unsplash.com/photo-1544025162-d76694265947?q=80&w=500&auto=format&fit=crop', NULL, 'cust_001'),
('b0010000-0000-0000-0000-000000000002', 'Salmon panggang-nya fresh dan perfectly cooked. Harga agak mahal tapi worth it.', '2024-12-20 20:00:00', '2024-12-20 20:00:00', true, '550e8400-e29b-41d4-a716-446655440000', 4.0, 'Siti Rahayu', NULL, NULL, 'cust_002'),
('b0010000-0000-0000-0000-000000000003', 'Tempatnya romantic, cocok buat dinner date. Pelayanannya ramah.', '2025-01-05 21:00:00', '2025-01-05 21:00:00', true, '550e8400-e29b-41d4-a716-446655440000', 5.0, 'Andi Wijaya', NULL, NULL, 'cust_003'),
('b0010000-0000-0000-0000-000000000004', 'Burger-nya juicy! Pasti balik lagi kesini.', '2025-01-10 18:45:00', '2025-01-10 18:45:00', true, '550e8400-e29b-41d4-a716-446655440000', 4.0, 'Dewi Lestari', 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?q=80&w=500&auto=format&fit=crop', NULL, 'cust_004'),

-- Green Garden (4 reviews, avg 4.25)
('b0020000-0000-0000-0000-000000000001', 'Akhirnya nemu resto vegan yang enak di Jakarta! Buddha bowl-nya recommended.', '2024-11-20 12:30:00', '2024-11-20 12:30:00', true, '550e8400-e29b-41d4-a716-446655440001', 5.0, 'Maya Putri', 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?q=80&w=500&auto=format&fit=crop', NULL, 'cust_005'),
('b0020000-0000-0000-0000-000000000002', 'Smoothie bowl-nya seger dan presentasinya cantik. Cocok buat foto-foto.', '2024-12-01 10:00:00', '2024-12-01 10:00:00', true, '550e8400-e29b-41d4-a716-446655440001', 4.0, 'Rina Susanti', NULL, NULL, 'cust_006'),
('b0020000-0000-0000-0000-000000000003', 'Makanan sehat tapi tetap enak. Avocado toast-nya the best!', '2024-12-28 13:15:00', '2024-12-28 13:15:00', true, '550e8400-e29b-41d4-a716-446655440001', 4.0, 'Fajar Nugroho', NULL, NULL, 'cust_007'),
('b0020000-0000-0000-0000-000000000004', 'Porsinya agak kecil untuk harganya, tapi rasanya oke.', '2025-01-08 11:30:00', '2025-01-08 11:30:00', true, '550e8400-e29b-41d4-a716-446655440001', 4.0, 'Linda Kusuma', NULL, NULL, 'cust_008'),

-- Ocean Breeze (5 reviews, avg 4.6)
('b0030000-0000-0000-0000-000000000001', 'Lobster-nya super fresh! Best seafood restaurant in town.', '2024-10-15 19:00:00', '2024-10-15 19:00:00', true, '550e8400-e29b-41d4-a716-446655440002', 5.0, 'Hendra Gunawan', 'https://images.unsplash.com/photo-1553247407-23251ce81f59?q=80&w=500&auto=format&fit=crop', NULL, 'cust_009'),
('b0030000-0000-0000-0000-000000000002', 'Seafood paella-nya porsi besar, bisa sharing berdua. Rasanya authentic!', '2024-11-22 20:30:00', '2024-11-22 20:30:00', true, '550e8400-e29b-41d4-a716-446655440002', 5.0, 'Yuni Kartika', NULL, NULL, 'cust_010'),
('b0030000-0000-0000-0000-000000000003', 'Fish and chips-nya crispy dan tidak berminyak. Recommended!', '2024-12-10 18:00:00', '2024-12-10 18:00:00', true, '550e8400-e29b-41d4-a716-446655440002', 4.0, 'Rizky Pratama', NULL, NULL, 'cust_011'),
('b0030000-0000-0000-0000-000000000004', 'View lautnya bagus, suasana romantis. Clam chowder-nya creamy.', '2025-01-02 19:45:00', '2025-01-02 19:45:00', true, '550e8400-e29b-41d4-a716-446655440002', 5.0, 'Anisa Rahma', 'https://images.unsplash.com/photo-1594756202469-9ff9799b2e4e?q=80&w=500&auto=format&fit=crop', NULL, 'cust_012'),
('b0030000-0000-0000-0000-000000000005', 'Harga lumayan mahal tapi kualitas seafood-nya premium.', '2025-01-15 21:00:00', '2025-01-15 21:00:00', true, '550e8400-e29b-41d4-a716-446655440002', 4.0, 'Tommy Setiawan', NULL, NULL, 'cust_013'),

-- Night Owl Diner (3 reviews, avg 4.0)
('b0040000-0000-0000-0000-000000000001', 'Tempat nongkrong malam yang asyik. Pancake-nya fluffy!', '2024-12-05 23:30:00', '2024-12-05 23:30:00', true, '550e8400-e29b-41d4-a716-446655440003', 4.0, 'Dimas Arya', NULL, NULL, 'cust_014'),
('b0040000-0000-0000-0000-000000000002', 'Milkshake-nya thick dan creamy. Loaded fries juga enak!', '2024-12-25 01:00:00', '2024-12-25 01:00:00', true, '550e8400-e29b-41d4-a716-446655440003', 5.0, 'Nadia Safitri', 'https://images.unsplash.com/photo-1572490122747-3968b75cc699?q=80&w=500&auto=format&fit=crop', NULL, 'cust_015'),
('b0040000-0000-0000-0000-000000000003', 'Comfort food yang pas buat lapar tengah malam. Pelayanan agak lama.', '2025-01-12 00:30:00', '2025-01-12 00:30:00', true, '550e8400-e29b-41d4-a716-446655440003', 3.0, 'Kevin Halim', NULL, NULL, 'cust_016'),

-- The Burger Spot (4 reviews, avg 4.25)
('b0050000-0000-0000-0000-000000000001', 'Smash burger terenak yang pernah gue coba! Patty-nya juicy banget.', '2024-11-10 13:00:00', '2024-11-10 13:00:00', true, '550e8400-e29b-41d4-a716-446655440004', 5.0, 'Bayu Firmansyah', 'https://images.unsplash.com/photo-1586190848861-99aa4a171e90?q=80&w=500&auto=format&fit=crop', NULL, 'cust_017'),
('b0050000-0000-0000-0000-000000000002', 'Bacon BBQ burger-nya mantap! Onion rings juga crispy.', '2024-12-03 12:30:00', '2024-12-03 12:30:00', true, '550e8400-e29b-41d4-a716-446655440004', 4.0, 'Citra Dewi', NULL, NULL, 'cust_018'),
('b0050000-0000-0000-0000-000000000003', 'Porsi pas, harga reasonable. Cheese fries-nya cheesy abis!', '2024-12-18 19:00:00', '2024-12-18 19:00:00', true, '550e8400-e29b-41d4-a716-446655440004', 4.0, 'Agus Prasetyo', NULL, NULL, 'cust_019'),
('b0050000-0000-0000-0000-000000000004', 'Mushroom swiss burger enak, tapi antrian agak panjang pas weekend.', '2025-01-06 14:00:00', '2025-01-06 14:00:00', true, '550e8400-e29b-41d4-a716-446655440004', 4.0, 'Fitri Handayani', NULL, NULL, 'cust_020'),

-- Pasta Palace (5 reviews, avg 4.6)
('b0060000-0000-0000-0000-000000000001', 'Carbonara-nya authentic Italian taste! Creamy tapi tidak eneg.', '2024-10-28 20:00:00', '2024-10-28 20:00:00', true, '550e8400-e29b-41d4-a716-446655440005', 5.0, 'Jessica Tan', 'https://images.unsplash.com/photo-1612874742237-6526221588e3?q=80&w=500&auto=format&fit=crop', NULL, 'cust_021'),
('b0060000-0000-0000-0000-000000000002', 'Lasagna-nya cheesy dan saucy. Porsi besar!', '2024-11-15 19:30:00', '2024-11-15 19:30:00', true, '550e8400-e29b-41d4-a716-446655440005', 5.0, 'Rahmat Hidayat', NULL, NULL, 'cust_022'),
('b0060000-0000-0000-0000-000000000003', 'Pizza margherita-nya simple tapi enak. Tiramisu juga recommended.', '2024-12-08 21:00:00', '2024-12-08 21:00:00', true, '550e8400-e29b-41d4-a716-446655440005', 4.0, 'Wulan Sari', NULL, NULL, 'cust_023'),
('b0060000-0000-0000-0000-000000000004', 'Fettuccine alfredo-nya creamy dan rich. Love it!', '2025-01-03 20:15:00', '2025-01-03 20:15:00', true, '550e8400-e29b-41d4-a716-446655440005', 5.0, 'Daniel Wijaya', 'https://images.unsplash.com/photo-1645112411341-6c4fd023714a?q=80&w=500&auto=format&fit=crop', NULL, 'cust_024'),
('b0060000-0000-0000-0000-000000000005', 'Suasana cozy, cocok untuk dinner. Pelayanan cepat.', '2025-01-18 19:00:00', '2025-01-18 19:00:00', true, '550e8400-e29b-41d4-a716-446655440005', 4.0, 'Eka Putri', NULL, NULL, 'cust_025'),

-- Taco Fiesta (3 reviews, avg 4.0)
('b0070000-0000-0000-0000-000000000001', 'Street tacos-nya authentic Mexican! Salsa-nya pedes mantap.', '2024-12-12 13:30:00', '2024-12-12 13:30:00', true, '550e8400-e29b-41d4-a716-446655440006', 5.0, 'Rio Saputra', 'https://images.unsplash.com/photo-1551504734-5ee1c4a1479b?q=80&w=500&auto=format&fit=crop', NULL, 'cust_026'),
('b0070000-0000-0000-0000-000000000002', 'Nachos supreme-nya loaded banget! Enak buat sharing.', '2024-12-28 14:00:00', '2024-12-28 14:00:00', true, '550e8400-e29b-41d4-a716-446655440006', 4.0, 'Mega Ayu', NULL, NULL, 'cust_027'),
('b0070000-0000-0000-0000-000000000003', 'Quesadilla-nya oke, tapi churros agak kurang manis.', '2025-01-14 12:45:00', '2025-01-14 12:45:00', true, '550e8400-e29b-41d4-a716-446655440006', 3.0, 'Irfan Maulana', NULL, NULL, 'cust_028'),

-- Sushi World (4 reviews, avg 4.75)
('b0080000-0000-0000-0000-000000000001', 'Salmon sashimi-nya melt in your mouth! Super fresh.', '2024-11-05 19:00:00', '2024-11-05 19:00:00', true, '550e8400-e29b-41d4-a716-446655440007', 5.0, 'Yuki Tanaka', 'https://images.unsplash.com/photo-1579871494447-9811cf80d66c?q=80&w=500&auto=format&fit=crop', NULL, 'cust_029'),
('b0080000-0000-0000-0000-000000000002', 'Dragon roll-nya kreatif dan enak! Presentation juga bagus.', '2024-12-02 20:30:00', '2024-12-02 20:30:00', true, '550e8400-e29b-41d4-a716-446655440007', 5.0, 'Andre Lim', NULL, NULL, 'cust_030'),
('b0080000-0000-0000-0000-000000000003', 'Miso ramen-nya rich dan flavorful. Chashu-nya tender.', '2024-12-22 21:00:00', '2024-12-22 21:00:00', true, '550e8400-e29b-41d4-a716-446655440007', 5.0, 'Stephanie Chen', 'https://images.unsplash.com/photo-1569718212165-3a8278d5f624?q=80&w=500&auto=format&fit=crop', NULL, 'cust_031'),
('b0080000-0000-0000-0000-000000000004', 'Tempura-nya crispy, matcha ice cream juga enak. Harga premium.', '2025-01-11 19:30:00', '2025-01-11 19:30:00', true, '550e8400-e29b-41d4-a716-446655440007', 4.0, 'Michael Tanuwijaya', NULL, NULL, 'cust_032'),

-- Cafe Morning (4 reviews, avg 4.5)
('b0090000-0000-0000-0000-000000000001', 'Best breakfast spot! Full breakfast-nya lengkap dan enak.', '2024-11-18 08:30:00', '2024-11-18 08:30:00', true, '550e8400-e29b-41d4-a716-446655440008', 5.0, 'Sarah Amelia', 'https://images.unsplash.com/photo-1533089860892-a7c6f0a88666?q=80&w=500&auto=format&fit=crop', NULL, 'cust_033'),
('b0090000-0000-0000-0000-000000000002', 'Belgian waffles-nya fluffy! Cappuccino juga perfect.', '2024-12-06 09:00:00', '2024-12-06 09:00:00', true, '550e8400-e29b-41d4-a716-446655440008', 5.0, 'Rendi Pratama', NULL, NULL, 'cust_034'),
('b0090000-0000-0000-0000-000000000003', 'Eggs benedict-nya oke, hollandaise sauce-nya creamy.', '2024-12-30 10:15:00', '2024-12-30 10:15:00', true, '550e8400-e29b-41d4-a716-446655440008', 4.0, 'Tiara Kusumawati', NULL, NULL, 'cust_035'),
('b0090000-0000-0000-0000-000000000004', 'French toast-nya enak tapi agak manis. Tempatnya cozy.', '2025-01-09 08:00:00', '2025-01-09 08:00:00', true, '550e8400-e29b-41d4-a716-446655440008', 4.0, 'Gilang Ramadhan', NULL, NULL, 'cust_036'),

-- BBQ Brothers (4 reviews, avg 4.5)
('b0100000-0000-0000-0000-000000000001', 'Baby back ribs-nya fall off the bone! Smoky dan tender.', '2024-10-22 18:00:00', '2024-10-22 18:00:00', true, '550e8400-e29b-41d4-a716-446655440009', 5.0, 'Bambang Sutrisno', 'https://images.unsplash.com/photo-1544025162-d76694265947?q=80&w=500&auto=format&fit=crop', NULL, 'cust_037'),
('b0100000-0000-0000-0000-000000000002', 'Pulled pork-nya juicy! Mac and cheese juga creamy.', '2024-11-28 19:30:00', '2024-11-28 19:30:00', true, '550e8400-e29b-41d4-a716-446655440009', 5.0, 'Diana Permata', NULL, NULL, 'cust_038'),
('b0100000-0000-0000-0000-000000000003', 'Brisket sandwich-nya smoky dan flavorful. Cornbread juga enak.', '2024-12-15 20:00:00', '2024-12-15 20:00:00', true, '550e8400-e29b-41d4-a716-446655440009', 4.0, 'Eko Prasetyo', NULL, NULL, 'cust_039'),
('b0100000-0000-0000-0000-000000000004', 'BBQ sauce-nya homemade dan rich. Worth the price!', '2025-01-07 17:30:00', '2025-01-07 17:30:00', true, '550e8400-e29b-41d4-a716-446655440009', 4.0, 'Novita Sari', 'https://images.unsplash.com/photo-1529193591184-b1d58069ecdd?q=80&w=500&auto=format&fit=crop', NULL, 'cust_040');

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
-- Sunset Grill (Indonesian/Grill)
('a0010000-0000-0000-0000-000000000100', 'Smoked Brisket', 'Slow-smoked beef brisket.', 88000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440000', 'https://images.unsplash.com/photo-1544025162-d76694265947?q=80&w=1990&auto=format&fit=crop'),
('a0010000-0000-0000-0000-000000000101', 'Grilled Salmon', 'Fresh Atlantic salmon with herbs.', 82000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440000', 'https://images.unsplash.com/photo-1467003909585-2f8a72700288?q=80&w=1974&auto=format&fit=crop'),
('a0010000-0000-0000-0000-000000000103', 'Grilled Shrimp Skewers', 'Marinated shrimp on skewers.', 75000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440000', 'https://images.unsplash.com/photo-1565557623262-b51c2513a641?q=80&w=1974&auto=format&fit=crop'),
('a0010000-0000-0000-0000-000000000104', 'Sunset Burger', 'Premium beef burger with special sauce.', 62000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440000', 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?q=80&w=1974&auto=format&fit=crop'),
('a0010000-0000-0000-0000-000000000105', 'Grilled Veggie Platter', 'Assorted grilled vegetables.', 55000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440000', 'https://images.unsplash.com/photo-1540420773420-3366772f4999?q=80&w=1974&auto=format&fit=crop'),
('a0010000-0000-0000-0000-000000000106', 'Chicken Wings', 'Spicy buffalo wings.', 50000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440000', 'https://images.unsplash.com/photo-1608039755401-742074f0548d?q=80&w=1974&auto=format&fit=crop'),

-- Green Garden (Organic Vegan - Western)
('a0020000-0000-0000-0000-000000000200', 'Buddha Bowl', 'Quinoa, roasted veggies, avocado, tahini dressing.', 65000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440001', 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?q=80&w=2070&auto=format&fit=crop'),
('a0020000-0000-0000-0000-000000000201', 'Avocado Toast', 'Sourdough with smashed avocado and seeds.', 45000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440001', 'https://images.unsplash.com/photo-1541519227354-08fa5d50c44d?q=80&w=2072&auto=format&fit=crop'),
('a0020000-0000-0000-0000-000000000202', 'Garden Salad', 'Fresh mixed greens with balsamic vinaigrette.', 40000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440001', 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?q=80&w=2070&auto=format&fit=crop'),
('a0020000-0000-0000-0000-000000000203', 'Vegan Burger', 'Plant-based patty with fresh vegetables.', 58000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440001', 'https://images.unsplash.com/photo-1520072959219-c595dc870360?q=80&w=1990&auto=format&fit=crop'),
('a0020000-0000-0000-0000-000000000204', 'Smoothie Bowl', 'Acai bowl with fresh fruits and granola.', 48000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440001', 'https://images.unsplash.com/photo-1590301157890-4810ed352733?q=80&w=1974&auto=format&fit=crop'),

-- Ocean Breeze (Seafood - Japanese)
('a0030000-0000-0000-0000-000000000300', 'Grilled Lobster', 'Fresh lobster with garlic butter sauce.', 180000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440002', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTZRrF8jCvuPJuO43Sc4P8uMcHbbDndTaKlFQ&s'),
('a0030000-0000-0000-0000-000000000301', 'Fish and Chips', 'Beer-battered cod with crispy fries.', 75000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440002', 'https://www.thespruceeats.com/thmb/sdVTq0h7xZvJjPr6bE2fhh5M3NI=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/SES-best-fish-and-chips-recipe-434856-hero-01-27d8b57008414972822b866609d0af9b.jpg'),
('a0030000-0000-0000-0000-000000000302', 'Seafood Paella', 'Spanish rice with mixed seafood.', 120000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440002', 'https://images.unsplash.com/photo-1534080564583-6be75777b70a?q=80&w=2070&auto=format&fit=crop'),
('a0030000-0000-0000-0000-000000000303', 'Shrimp Cocktail', 'Chilled shrimp with cocktail sauce.', 65000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440002', 'https://images.unsplash.com/photo-1565680018434-b513d5e5fd47?q=80&w=2070&auto=format&fit=crop'),
('a0030000-0000-0000-0000-000000000304', 'Clam Chowder', 'Creamy New England style soup.', 50000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440002', 'https://images.unsplash.com/photo-1594756202469-9ff9799b2e4e?q=80&w=1974&auto=format&fit=crop'),

-- Night Owl Diner (Late Night - Chinese)
('a0040000-0000-0000-0000-000000000400', 'Midnight Pancakes', 'Fluffy pancakes with maple syrup.', 42000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440003', 'https://images.unsplash.com/photo-1567620905732-2d1ec7ab7445?q=80&w=1980&auto=format&fit=crop'),
('a0040000-0000-0000-0000-000000000401', 'Classic Cheeseburger', 'Juicy beef patty with melted cheese.', 55000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440003', 'https://images.unsplash.com/photo-1571091718767-18b5b1457add?q=80&w=2072&auto=format&fit=crop'),
('a0040000-0000-0000-0000-000000000402', 'Loaded Fries', 'Crispy fries with cheese and bacon.', 45000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440003', 'https://images.unsplash.com/photo-1573080496219-bb080dd4f877?q=80&w=1974&auto=format&fit=crop'),
('a0040000-0000-0000-0000-000000000403', 'Milkshake', 'Thick and creamy vanilla milkshake.', 35000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440003', 'https://images.unsplash.com/photo-1572490122747-3968b75cc699?q=80&w=1974&auto=format&fit=crop'),
('a0040000-0000-0000-0000-000000000404', 'Club Sandwich', 'Triple-decker with turkey and bacon.', 52000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440003', 'https://images.unsplash.com/photo-1528735602780-2552fd46c7af?q=80&w=2073&auto=format&fit=crop'),

-- The Burger Spot (Burgers - Korean)
('a0050000-0000-0000-0000-000000000500', 'Classic Smash Burger', 'Double smashed patty with American cheese.', 58000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440004', 'https://images.unsplash.com/photo-1586190848861-99aa4a171e90?q=80&w=1980&auto=format&fit=crop'),
('a0050000-0000-0000-0000-000000000501', 'Bacon BBQ Burger', 'Beef patty with crispy bacon and BBQ sauce.', 68000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440004', 'https://images.unsplash.com/photo-1553979459-d2229ba7433b?q=80&w=1968&auto=format&fit=crop'),
('a0050000-0000-0000-0000-000000000502', 'Mushroom Swiss Burger', 'Beef patty with sauteed mushrooms and swiss.', 65000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440004', 'https://images.unsplash.com/photo-1594212699903-ec8a3eca50f5?q=80&w=2071&auto=format&fit=crop'),
('a0050000-0000-0000-0000-000000000503', 'Onion Rings', 'Crispy beer-battered onion rings.', 35000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440004', 'https://images.unsplash.com/photo-1639024471283-03518883512d?q=80&w=1974&auto=format&fit=crop'),
('a0050000-0000-0000-0000-000000000504', 'Cheese Fries', 'Golden fries with melted cheddar.', 38000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440004', 'https://images.unsplash.com/photo-1585109649139-366815a0d713?q=80&w=2070&auto=format&fit=crop'),

-- Pasta Palace (Italian - Indonesian)
('a0060000-0000-0000-0000-000000000600', 'Spaghetti Carbonara', 'Classic Roman pasta with egg and pancetta.', 72000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440005', 'https://images.unsplash.com/photo-1612874742237-6526221588e3?q=80&w=2071&auto=format&fit=crop'),
('a0060000-0000-0000-0000-000000000601', 'Fettuccine Alfredo', 'Creamy parmesan sauce with fettuccine.', 68000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440005', 'https://images.unsplash.com/photo-1645112411341-6c4fd023714a?q=80&w=2070&auto=format&fit=crop'),
('a0060000-0000-0000-0000-000000000602', 'Lasagna', 'Layered pasta with meat sauce and cheese.', 78000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440005', 'https://images.unsplash.com/photo-1574894709920-11b28e7367e3?q=80&w=2070&auto=format&fit=crop'),
('a0060000-0000-0000-0000-000000000603', 'Margherita Pizza', 'Classic tomato, mozzarella and basil.', 65000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440005', 'https://images.unsplash.com/photo-1574071318508-1cdbab80d002?q=80&w=2069&auto=format&fit=crop'),
('a0060000-0000-0000-0000-000000000604', 'Tiramisu', 'Classic Italian coffee dessert.', 45000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440005', 'https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?q=80&w=1972&auto=format&fit=crop'),

-- Taco Fiesta (Mexican - Western)
('a0070000-0000-0000-0000-000000000700', 'Street Tacos', 'Three soft corn tortilla tacos with cilantro.', 48000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440006', 'https://images.unsplash.com/photo-1551504734-5ee1c4a1479b?q=80&w=2070&auto=format&fit=crop'),
('a0070000-0000-0000-0000-000000000701', 'Burrito Bowl', 'Rice, beans, meat, salsa and guacamole.', 62000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440006', 'https://images.unsplash.com/photo-1626700051175-6818013e1d4f?q=80&w=1964&auto=format&fit=crop'),
('a0070000-0000-0000-0000-000000000702', 'Quesadilla', 'Grilled tortilla with melted cheese and chicken.', 52000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440006', 'https://images.unsplash.com/photo-1618040996337-56904b7850b9?q=80&w=2070&auto=format&fit=crop'),
('a0070000-0000-0000-0000-000000000703', 'Nachos Supreme', 'Loaded nachos with all toppings.', 55000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440006', 'https://images.unsplash.com/photo-1513456852971-30c0b8199d4d?q=80&w=2070&auto=format&fit=crop'),
('a0070000-0000-0000-0000-000000000704', 'Churros', 'Fried dough with cinnamon sugar and chocolate.', 35000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440006', 'https://images.unsplash.com/photo-1624371414361-e670edf4898c?q=80&w=2070&auto=format&fit=crop'),

-- Sushi World (Japanese)
('a0080000-0000-0000-0000-000000000800', 'Salmon Sashimi', 'Fresh sliced salmon, 8 pieces.', 85000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440007', 'https://images.unsplash.com/photo-1579871494447-9811cf80d66c?q=80&w=2070&auto=format&fit=crop'),
('a0080000-0000-0000-0000-000000000801', 'Dragon Roll', 'Shrimp tempura with eel and avocado.', 95000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440007', 'https://images.unsplash.com/photo-1617196034796-73dfa7b1fd56?q=80&w=2070&auto=format&fit=crop'),
('a0080000-0000-0000-0000-000000000802', 'Miso Ramen', 'Rich miso broth with chashu pork.', 72000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440007', 'https://images.unsplash.com/photo-1569718212165-3a8278d5f624?q=80&w=2080&auto=format&fit=crop'),
('a0080000-0000-0000-0000-000000000803', 'Tempura Platter', 'Assorted shrimp and vegetable tempura.', 68000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440007', 'https://images.unsplash.com/photo-1581781870027-04212e231e96?q=80&w=2070&auto=format&fit=crop'),
('a0080000-0000-0000-0000-000000000804', 'Matcha Ice Cream', 'Traditional green tea ice cream.', 35000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440007', 'https://images.unsplash.com/photo-1505394033641-40c6ad1178d7?q=80&w=2012&auto=format&fit=crop'),

-- Cafe Morning (Breakfast - Chinese)
('a0090000-0000-0000-0000-000000000900', 'Full Breakfast', 'Eggs, bacon, sausage, toast and hash browns.', 65000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440008', 'https://images.unsplash.com/photo-1533089860892-a7c6f0a88666?q=80&w=2070&auto=format&fit=crop'),
('a0090000-0000-0000-0000-000000000901', 'Belgian Waffles', 'Fluffy waffles with fresh berries.', 52000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440008', 'https://images.unsplash.com/photo-1562376552-0d160a2f238d?q=80&w=1925&auto=format&fit=crop'),
('a0090000-0000-0000-0000-000000000902', 'Eggs Benedict', 'Poached eggs with hollandaise sauce.', 58000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440008', 'https://upload.wikimedia.org/wikipedia/commons/2/24/Traditional_Eggs_Benedict.jpg'),
('a0090000-0000-0000-0000-000000000903', 'French Toast', 'Brioche french toast with maple syrup.', 48000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440008', 'https://images.unsplash.com/photo-1484723091739-30a097e8f929?q=80&w=1547&auto=format&fit=crop'),
('a0090000-0000-0000-0000-000000000904', 'Cappuccino', 'Rich espresso with steamed milk foam.', 28000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440008', 'https://images.unsplash.com/photo-1572442388796-11668a67e53d?q=80&w=1935&auto=format&fit=crop'),

-- BBQ Brothers (Barbecue - Korean)
('a0100000-0000-0000-0000-000000001000', 'Pulled Pork Platter', 'Slow-smoked pulled pork with coleslaw.', 78000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440009', 'https://images.unsplash.com/photo-1529193591184-b1d58069ecdd?q=80&w=1935&auto=format&fit=crop'),
('a0100000-0000-0000-0000-000000001001', 'Baby Back Ribs', 'Full rack of tender BBQ ribs.', 125000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440009', 'https://images.unsplash.com/photo-1544025162-d76694265947?q=80&w=2069&auto=format&fit=crop'),
('a0100000-0000-0000-0000-000000001002', 'Brisket Sandwich', 'Smoked brisket on brioche bun.', 68000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440009', 'https://images.unsplash.com/photo-1606755962773-d324e0a13086?q=80&w=1974&auto=format&fit=crop'),
('a0100000-0000-0000-0000-000000001003', 'Mac and Cheese', 'Creamy baked mac and cheese.', 42000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440009', 'https://images.unsplash.com/photo-1543339494-b4cd4f7ba686?q=80&w=2070&auto=format&fit=crop'),
('a0100000-0000-0000-0000-000000001004', 'Cornbread', 'Homemade sweet cornbread.', 25000, NOW(), NOW(), true, '550e8400-e29b-41d4-a716-446655440009', 'https://images.unsplash.com/photo-1585535967475-1e3c8c4f3b7b?q=80&w=1974&auto=format&fit=crop');



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