package com.haulmont.testtask.db.daos;

public interface DaoFactoryInterface {
    String AUTHOR = "author";
    String BOOK = "book";
    String GENRE = "genre";

    AuthorDaoInterface getAuthorDao();
    BookDaoInterface getBookDao();
    GenreDaoInterface getGenreDao();
}
