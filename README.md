# eCommerce Service
 Backend service for eCommerce web store

```sql
DROP TABLE IF EXISTS PRODUCT;
CREATE TABLE PRODUCT(
product_id INT AUTO_INCREMENT PRIMARY KEY,
product_name VARCHAR(50) NOT NULL,
product_category VARCHAR(50) NOT NULL,
product_description VARCHAR(200) NOT NULL,
product_price NUMERIC(20, 2),
product_stock INT NOT NULL,
product_image_url VARCHAR(300)
);
```