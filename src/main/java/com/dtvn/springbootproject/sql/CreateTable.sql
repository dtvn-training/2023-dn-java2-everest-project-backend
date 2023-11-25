CREATE SCHEMA `everest` ;

USE everest;

CREATE TABLE roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL
);

CREATE TABLE accounts (
    account_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    address VARCHAR(255),
    phone VARCHAR(20),
    avatar VARCHAR(255),
    delete_flag BOOLEAN NOT NULL,
    role_id INT,
	created_by INT,
    updated_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(role_id),
    FOREIGN KEY (created_by) REFERENCES accounts(account_id),
    FOREIGN KEY (updated_by) REFERENCES accounts(account_id)
);

-- insert roles
INSERT INTO roles(role_id, role_name)
VALUES
(1,"ADMIN"),
(2,"DAC");

-- demo user
INSERT INTO accounts(account_id,email, password, firstname, lastname, address,phone,avatar,delete_flag,role_id)
VALUES(1,'quyet@mail.com','$2a$10$.SKirmzGptq/yvOJVPGbv.j2NhQJAOQaQHioJ6cA/kVd2rZ/Gc1s2','Pham','Anh Quyet','Nghe An','0123456789',null,0,1);




