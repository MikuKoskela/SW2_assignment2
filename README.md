
### Setup

## Clone the repository:

git clone https://github.com/your-username/localization-app.git
cd localization-app

## Make database and create tables:

CREATE DATABASE IF NOT EXISTS shopping_cart_localization
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE shopping_cart_localization;

CREATE TABLE cart_records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    total_items INT NOT NULL,
    total_cost DOUBLE NOT NULL,
    language VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cart_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cart_record_id INT,
    item_number INT NOT NULL,
    price DOUBLE NOT NULL,
    quantity INT NOT NULL,
    subtotal DOUBLE NOT NULL,
    FOREIGN KEY (cart_record_id) REFERENCES cart_records(id)
        ON DELETE CASCADE
);

CREATE TABLE localization_strings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    `key` VARCHAR(100) NOT NULL,
    value VARCHAR(255) NOT NULL,
    language VARCHAR(10) NOT NULL


## Seed all tables:

-- Example (English)

INSERT INTO localization_strings (`key`, value, language) VALUES
('language', 'Choose a Language:', 'en'),
('calculateButton', 'Calculate', 'en'),
('totalCost', 'Total cost:', 'en'),
('itemAmount', 'Enter quantity for item:', 'en'),
('itemPrice', 'Enter price for item:', 'en'),
('totalAmount', 'Enter number of items to purchase:', 'en');

## Configure Credentials in DatabaseConnection.java
  jdbc:mariadb://localhost:3306/shopping_cart_localization
  -user
  -password


##  Maven clean install

  mvn clean install

## Run the Application
  mvn javafx:run

