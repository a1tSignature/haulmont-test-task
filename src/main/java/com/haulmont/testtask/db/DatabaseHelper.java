package com.haulmont.testtask.db;

import com.haulmont.testtask.db.dao.AbstractDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper extends AbstractDao {

    public void initial() throws SQLException {
        initDatabase();
    }

    private void initDatabase() throws SQLException {
        connect();

        execute("drop trigger inserting_book");
        execute("drop trigger updating_book");
        execute("drop trigger deleting_book_genre");
        execute("drop trigger deleting_author");

        execute("drop table book");
        execute("drop table author");
        execute("drop table genre");

        execute("create table if not exists genre (" +
                "id bigint generated by default as identity" +
                "(start with 1, increment by 1) not null primary key," +
                "name varchar(30) not null);");

        executeQuery("insert into genre (name) values ('Антиутопия');");
        executeQuery("insert into genre (name) values ('Фэнтези');");
        executeQuery("insert into genre (name) values ('Киберпанк');");
        executeQuery("insert into genre (name) values ('Научная фантастика');");
        executeQuery("insert into genre (name) values ('Ужасы');");



        execute("create table if not exists author (" +
                "id bigint generated by default as identity" +
                "(start with 1, increment by 1) not null primary key," +
                "name varchar(50) not null," +
                "surname varchar(50) not null," +
                "patronymic varchar(50) not null);");
        executeQuery("insert into author(name, surname, patronymic) values" +
                "('Стивен', 'Кинг', 'Эдвин');");
        executeQuery("insert into author(name, surname, patronymic) values" +
                "('Айзек', 'Азимов', 'Юдович');");
        executeQuery("insert into author(name, surname, patronymic) values" +
                "('Герберт', 'Уэллс', 'Джордж');");
        executeQuery("insert into author(name, surname, patronymic) values" +
                "('Уильям', 'Гибсон', 'Форд');");
        executeQuery("insert into author(name, surname, patronymic) values" +
                "('Евгений', 'Замятин', 'Иванович');");
        executeQuery("insert into author(name, surname, patronymic) values" +
                "('Олдос', 'Хаксли', 'Леонард');");

        execute("create table if not exists book (" +
                "id bigint generated by default as identity" +
                "(start with 1, increment by 1) not null primary key," +
                "title varchar(100) not null," +
                "author_id bigint not null," +
                "constraint fk_book_author foreign key (author_id) references author(id) on delete restrict," +
                "genre_id bigint not null," +
                "constraint fk_book_genre foreign key (genre_id) references genre(id) on delete restrict," +
                "publisher varchar(15) not null," +
                "year integer not null," +
                "city varchar(40) not null);");

        executeQuery("insert into book (title, author_id, genre_id, publisher, year, city) values ('Тьма', '1', '5', 'O’Reilly', '2010', 'Нью-Йорк')");
        executeQuery("insert into book (title, author_id, genre_id, publisher, year, city) values ('Основание и Земля', '2', '4', 'Москва', '1975', 'Рига')");
        executeQuery("insert into book (title, author_id, genre_id, publisher, year, city) values ('Человек Невидимка', '3', '4', 'O’Reilly', '1899', 'Нюрнберг')");
        executeQuery("insert into book (title, author_id, genre_id, publisher, year, city) values ('Нейромант', '4', '3', 'Питер', '2015', 'Санкт-Петербург')");
        executeQuery("insert into book (title, author_id, genre_id, publisher, year, city) values ('Мы', '5', '1', 'O’Reilly', '1980', 'Вашингтон')");
        executeQuery("insert into book (title, author_id, genre_id, publisher, year, city) values ('О Дивный Новый Мир', '6', '1', 'Москва', '1999', 'Рига')");

        execute("CREATE trigger inserting_book\n" +
                "    BEFORE INSERT ON book\n" +
                "    REFERENCING NEW ROW AS new_book\n" +
                "    FOR EACH ROW\n" +
                "    BEGIN ATOMIC\n" +
                "        SET new_book.author_id = (SELECT id FROM author WHERE id = new_book.author_id);\n" +
                "        SET new_book.genre_id = (SELECT id FROM genre WHERE id = new_book.genre_id);\n" +
                "    END");

        execute("CREATE trigger updating_book\n" +
                "    BEFORE UPDATE ON book\n" +
                "    REFERENCING NEW ROW AS new_book\n" +
                "    FOR EACH ROW\n" +
                "    BEGIN ATOMIC\n" +
                "        SET new_book.author_id = (SELECT id FROM author WHERE id = new_book.author_id);\n" +
                "        SET new_book.genre_id = (SELECT id FROM genre WHERE id = new_book.genre_id);\n" +
                "    END");

        execute("CREATE trigger deleting_book_genre\n" +
                "    BEFORE DELETE on genre\n" +
                "    REFERENCING OLD ROW AS book_genre_to_delete\n" +
                "    FOR EACH ROW\n" +
                "    BEGIN ATOMIC\n" +
                "        IF EXISTS(select genre_id from book where genre_id = book_genre_to_delete.id)\n" +
                "            THEN SIGNAL SQLSTATE 'HY008' SET MESSAGE_TEXT = 'cannot delete genre because there is at least one book with such one';\n" +
                "        END IF;\n" +
                "    END");
        execute("CREATE trigger deleting_author\n" +
                "    BEFORE DELETE on author\n" +
                "    REFERENCING OLD ROW AS author_to_delete\n" +
                "    FOR EACH ROW\n" +
                "    BEGIN ATOMIC\n" +
                "        IF EXISTS(select author_id from book where author_id = author_to_delete.id)\n" +
                "            THEN SIGNAL SQLSTATE 'HY008' SET MESSAGE_TEXT = 'cannot delete author because there is at least one book with such one';\n" +
                "        END IF;\n" +
                "    END");

        executeQuery("update book set " +
                "title = 'Нейромант'," +
                "author_id = 3," +
                "genre_id = 3," +
                "publisher = 'Питер'," +
                "year = 1996," +
                "city = 'Санкт-Петербург' " +
                "where id = 4");


        disconnect();
    }
}
