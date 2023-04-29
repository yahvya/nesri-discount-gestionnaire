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
