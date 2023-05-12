CREATE TABLE pieces(
   id INTEGER,
   piece_name TEXT NOT NULL,
   enterprise_name TEXT NOT NULL,
   external_ref TEXT NOT NULL,
   internal_ref TEXT NOT NULL,
   location TEXT NOT NULL,
   quantity INTEGER NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE users(
   id INTEGER,
   username TEXT NOT NULL,
   password TEXT NOT NULL,
   role INTEGER NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(username)
);

CREATE TABLE missions(
   id INTEGER,
   description TEXT NOT NULL,
   moment TEXT NOT NULL,
   technician TEXT NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE help_categories(
   id INTEGER,
   category_name TEXT NOT NULL,
   help_content TEXT NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(category_name)
);
