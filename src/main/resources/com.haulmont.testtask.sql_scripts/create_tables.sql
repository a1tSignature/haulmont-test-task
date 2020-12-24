CREATE TABLE author (
id BIGINT IDENTITY  NOT NULL PRIMARY KEY,
name VARCHAR(50) NOT NULL,
surname VARCHAR(50) NOT NULL,
patronymic VARCHAR(50) NOT NULL
);

CREATE TABLE genre (
id BIGINT IDENTITY NOT NULL PRIMARY KEY,
name VARCHAR(30) NOT NULL
)

CREATE TABLE book (
id BIGINT IDENTITY NOT NULL PRIMARY KEY,
title VARCHAR(100) NOT NULL,
author_id BIGINT NOT NULL,
genre_id BIGINT NOT NULL,
publisher VARCHAR(15) NOT NULL,
year INTEGER NOT NULL,
city VARCHAR(40) NOT NULL
)

ALTER TABLE book ADD FOREIGN KEY (author_id) REFERENCES author (id) ON DELETE RESTRICT
ALTER TABLE book ADD FOREIGN KEY (genre_id) REFERENCES genre (id) ON DELETE RESTRICT

CREATE trigger inserting_book
    BEFORE INSERT ON book
    REFERENCING NEW ROW AS new_book
    FOR EACH ROW
    BEGIN ATOMIC
        SET new_book.author_id = (SELECT id FROM author WHERE id = new_book.author_id);
        SET new_book.genre_id = (SELECT id FROM genre WHERE id = new_book.genre_id);
    END

CREATE trigger updating_book
    BEFORE UPDATE ON book
    REFERENCING NEW ROW AS new_book
    FOR EACH ROW
    BEGIN ATOMIC
        SET new_book.author_id = (SELECT id FROM author WHERE id = new_book.author_id);
        SET new_book.genre_id = (SELECT id FROM genre WHERE id = new_book.genre_id);
    END
