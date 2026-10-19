-- BAI KIEM TRA CUOI MODULE: QUAN LY SAN PHAM
-- Chay file nay trong MySQL Workbench hoac: mysql -u root -p < sql/product_management.sql
-- MySQL 8.0+; script co the chay lai ma khong xoa du lieu cu.
CREATE DATABASE IF NOT EXISTS product_management
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE product_management;

CREATE TABLE IF NOT EXISTS products (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    quantity INT NOT NULL,
    CONSTRAINT chk_products_price CHECK (price >= 0),
    CONSTRAINT chk_products_quantity CHECK (quantity >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Chi them du lieu mau khi bang rong, khong chen trung khi chay lai file.
INSERT INTO products (name, price, quantity)
SELECT seed.name, seed.price, seed.quantity
FROM (
    SELECT 'Bàn phím cơ' AS name, 890000.00 AS price, 12 AS quantity
    UNION ALL SELECT 'Chuột không dây', 320000.00, 25
    UNION ALL SELECT 'Màn hình 24 inch', 3290000.00, 8
    UNION ALL SELECT 'Tai nghe Bluetooth', 650000.00, 16
    UNION ALL SELECT 'USB 64GB', 180000.00, 40
) AS seed
WHERE NOT EXISTS (SELECT 1 FROM products LIMIT 1);