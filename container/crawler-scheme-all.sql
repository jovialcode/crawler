CREATE DATABASE crawler;

DROP TABLE IF EXISTS crawler;
CREATE TABLE IF NOT EXISTS crawler (
    id INT AUTO_INCREMENT PRIMARY KEY,
    crawler_name varchar(100) NOT NULL,
    crawler_state varchar(5) NOT NULL,
    description varchar(255),
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modify_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    delete_yn TINYINT DEFAULT 0,
    UNIQUE KEY(crawler_name)
);

--

DROP TABLE IF EXISTS seed;
CREATE TABLE IF NOT EXISTS seed (
    host varchar(255) PRIMARY KEY,
    description varchar(255),
    crawler_id INT,
    crawl_delay int DEFAULT 30,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    delete_yn TINYINT DEFAULT 0
    FOREIGN KEY (crawler_id) REFERENCES crawler(crawler_id)
);

--

DROP TABLE IF EXISTS seed_link;
CREATE TABLE IF NOT EXISTS seed_link (
    url varchar(255) PRIMARY KEY,
    visitDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    createDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleteYn TINYINT DEFAULT 0
);