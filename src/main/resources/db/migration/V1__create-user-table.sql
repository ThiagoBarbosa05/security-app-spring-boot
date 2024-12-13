CREATE TABLE users (
    id uuid PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULl,
    password VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE
)