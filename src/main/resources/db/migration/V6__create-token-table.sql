CREATE TABLE tokens (
    id uuid PRIMARY KEY,
    type VARCHAR(50),
    user_id uuid NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);