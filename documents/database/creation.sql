CREATE TABLE pieces(
   id INT AUTO_INCREMENT,
   piece_name VARCHAR(255)  NOT NULL,
   enterprise_name VARCHAR(255)  NOT NULL,
   external_ref VARCHAR(255)  NOT NULL,
   internal_ref VARCHAR(255)  NOT NULL,
   location VARCHAR(255)  NOT NULL,
   quantity INT NOT NULL,
   buy_price DECIMAL(15,2)   NOT NULL,
   sell_price DECIMAL(15,2)   NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE users(
   id INT AUTO_INCREMENT,
   username VARCHAR(50)  NOT NULL,
   password VARCHAR(100)  NOT NULL,
   role TINYINT NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(username)
);

CREATE TABLE missions(
   id INT AUTO_INCREMENT,
   description TEXT NOT NULL,
   moment VARCHAR(20)  NOT NULL,
   technician VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE help_categories(
   id INT AUTO_INCREMENT,
   category_name VARCHAR(50)  NOT NULL,
   help_content TEXT NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(category_name)
);
insert into users values(1,'nesrialex','$2a$10$qM2UW8iGECq2xttH2sFOM.NxJXGZ7Lj5Jf5O8enVqnhb5WjG5iTwu',1);
