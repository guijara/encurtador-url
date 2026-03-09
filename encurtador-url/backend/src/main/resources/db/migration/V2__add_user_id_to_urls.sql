ALTER TABLE urls
    ADD COLUMN user_id UUID;

ALTER TABLE urls
    ADD CONSTRAINT fk_url_user
        FOREIGN KEY (user_id) REFERENCES users(id);