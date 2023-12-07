CREATE SCHEMA IF NOT EXISTS`everest` ;

USE everest;

CREATE TABLE IF NOT EXISTS roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS accounts (
    account_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(60) NOT NULL,
    firstname VARCHAR(50),
    lastname VARCHAR(50),
    address VARCHAR(255),
    phone VARCHAR(20),
    avatar VARCHAR(255),
    delete_flag BOOLEAN NOT NULL,
    role_id INT NOT NULL,
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
INSERT INTO accounts(account_id,email, password, firstname, lastname, address,phone,avatar,delete_flag,role_id,created_by)
VALUES(1,'quyet@mail.com','$2a$10$.SKirmzGptq/yvOJVPGbv.j2NhQJAOQaQHioJ6cA/kVd2rZ/Gc1s2','Pham','Anh Quyet','Nghe An','0123456789',null,0,1,1);


CREATE TABLE IF NOT EXISTS  campaigns (
            campaign_id SERIAL PRIMARY KEY,
            campaign_name VARCHAR(255),
            startdate TIMESTAMP,
            endDate TIMESTAMP,
            budget BIGINT,
            bid_amount BIGINT,
            account_id INTEGER NOT NULL,
            create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
            status BOOLEAN,
            delete_flag BOOLEAN NOT NULL,
            usage_rate FLOAT,
            used_amount INTEGER,
            FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);


CREATE TABLE IF NOT EXISTS  creatives (
            creative_id INT AUTO_INCREMENT PRIMARY KEY,
            title VARCHAR(255),
            description TEXT,
            image_url VARCHAR(255),
            final_url VARCHAR(255),
            campaign_id INT,
            create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
            delete_flag BOOLEAN,
            FOREIGN KEY (campaign_id) REFERENCES campaigns(campaign_id)
);



INSERT INTO campaigns (campaign_name, startdate, endDate, budget, bid_amount, account_id, status, delete_flag, usage_rate, used_amount)
VALUES
    ('Campaign A', '2023-01-01', '2023-02-01', 100000, 50000, 1, true, false, 0.75, 20),
    ('Campaign B', '2023-02-15', '2023-03-15', 150000, 75000, 2, true, false, 0.60, 15),
    ('Campaign C', '2023-03-20', '2023-04-20', 120000, 60000, 3, false, false, 0.80, 25),
    ('Campaign D', '2023-04-10', '2023-05-10', 180000, 90000, 1, true, false, 0.65, 18),
    ('Campaign E', '2023-05-05', '2023-06-05', 200000, 100000, 2, false, false, 0.70, 22),
    ('Campaign F', '2023-06-12', '2023-07-12', 160000, 80000, 3, true, false, 0.55, 16);

