package com.haulmont.testtask.db.daos;

import com.haulmont.testtask.dto.AuthorDTO;
import com.haulmont.testtask.dto.BookDTO;

import java.sql.SQLException;
import java.util.List;

public interface BookDaoInterface {
    String ID = "id";
    String TITLE = "title";
    String AUTHOR_ID = "author_id";
    String GENRE_ID = "genre_id";
    String PUBLISHER = "publisher";
    String YEAR = "year";
    String CITY = "city";

    BookDTO findBook(final long id) throws SQLException;

    void insertBook(final BookDTO bookDTO) throws SQLException;

    void updateBook(final BookDTO bookDTO) throws SQLException;

    void deleteBook(final long id) throws SQLException;

    List<BookDTO> findAllBooks() throws SQLException;

    List<BookDTO> findBooksByTitle(final String title) throws SQLException;

    List<BookDTO> findBooksByAuthor(final long authorId) throws SQLException;

    List<BookDTO> findBooksByFilters(final String title, final String author, final String publisher) throws SQLException;
}
