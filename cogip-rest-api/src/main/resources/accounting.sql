PRAGMA foreign_keys = ON;

-- COMPANY

CREATE TABLE IF NOT EXISTS 'company' (
  'id' INTEGER PRIMARY KEY AUTOINCREMENT,
  'name' VARCHAR(255) NOT NULL,
  'country' VARCHAR(255) NOT NULL,
  'vat' VARCHAR(255) UNIQUE NOT NULL,
  'type' VARCHAR(255) NOT NULL CHECK ('type' = "provider" OR 'type' = "client"),
  'timestamp' TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

/*
INSERT INTO 'company'
  (name, country, vat, type)
VALUES
  ("BeCode", "Belgium", "1234567890", "client"),
  ("ACME", "United-States", "1234567891", "provider");
*/

-- CONTACT

CREATE TABLE IF NOT EXISTS 'contact' (
  'id' INTEGER PRIMARY KEY AUTOINCREMENT,
  'firstname' VARCHAR(255) NOT NULL,
  'lastname' VARCHAR(255) NOT NULL,
  'phone' VARCHAR(15) NOT NULL,
  'email' VARCHAR(255) NOT NULL,
  'timestamp' TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  'contact_company_id' INTEGER REFERENCES 'company' (id)
);

/*
INSERT INTO 'contact'
  (firstname, lastname, phone, email)
VALUES
  ("John", "Doe", "+32", "johndoe@becode.org"),
  ("", "", "", "");
*/

-- INVOICE

CREATE TABLE IF NOT EXISTS 'invoice' (
  'id' INTEGER PRIMARY KEY AUTOINCREMENT,
  -- 'number' VARCHAR(255) NOT NULL,
  'timestamp' TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  'invoice_company_id' INTEGER REFERENCES 'company' (id),
  'invoice_contact_id' INTEGER REFERENCES 'contact' (id)
);

/*
INSERT INTO
  'invoice' (number)
VALUES
  (),
  ();
*/

-- USER

CREATE TABLE IF NOT EXISTS user (
  'id' INTEGER PRIMARY KEY AUTOINCREMENT,
  'username' VARCHAR(255) NOT NULL,
  'password' VARCHAR(255) NOT NULL,
  'role' VARCHAR(255) NOT NULL CHECK (
    'role' = "admin" OR 'role' = "accountant" OR 'role' = "intern"
  )
);

/*
INSERT INTO 'user'
  (username, password, role)
VALUES
  ("ranu", "ranunu", "admin"),
  ("stacy", "re_alpa55word", "accountant"),
  ("joe", "password", "intern");
*/