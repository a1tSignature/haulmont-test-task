package com.haulmont.testtask.db.hsqlDao;

import com.haulmont.testtask.db.dao.AbstractDao;
import com.haulmont.testtask.db.daos.BookDaoInterface;
import com.haulmont.testtask.dto.AuthorDTO;
import com.haulmont.testtask.dto.BookDTO;
import com.haulmont.testtask.dto.GenreDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.haulmont.testtask.db.dao.SqlCommands.*;
import static com.haulmont.testtask.db.daos.DaoFactoryInterface.AUTHOR;
import static com.haulmont.testtask.db.daos.DaoFactoryInterface.BOOK;

public class HsqlBookDao extends AbstractDao implements BookDaoInterface {
    protected HsqlBookDao() {
        super();
    }

    @Override
    public BookDTO findBook(long id) throws SQLException {
        connect();

        final String query = String.format("%s * %s %s %s %s = %s;",
                SELECT, FROM, BOOK, WHERE, ID, id);

        ResultSet resultSet = executeQuery(query);

        BookDTO bookDTO = null;
        while (resultSet.next()) {

            AuthorDTO author = DaoFactory.getInstance()
                    .getAuthorDao().findAuthor(resultSet.getLong(AUTHOR_ID));
            GenreDTO genre = DaoFactory.getInstance()
                    .getGenreDao().findBookGenre(resultSet.getLong(GENRE_ID));
            bookDTO = new BookDTO(
                    resultSet.getLong(ID),
                    resultSet.getString(TITLE),
                    resultSet.getString(PUBLISHER),
                    resultSet.getShort(YEAR),
                    resultSet.getString(CITY));
            bookDTO.setAuthorId(author);
            bookDTO.setGenreId(genre);
        }

        disconnect();

        return bookDTO;
    }

    @Override
    public void insertBook(BookDTO bookDTO) throws SQLException {
        connect();

        final String query = String.format("%s %s %s (%s, %s, %s, %s, %s, %s) %s ('%s', %s, %s, '%s', %S, '%s');",
                INSERT, INTO, BOOK,
                TITLE, AUTHOR_ID, GENRE_ID, PUBLISHER, YEAR, CITY,
                VALUES,
                bookDTO.getTitle(), bookDTO.getAuthorId().getId(), bookDTO.getGenreId().getId(),
                bookDTO.getPublisher(), bookDTO.getYear(), bookDTO.getCity());

        executeUpdate(query);

        disconnect();
    }

    @Override
    public void updateBook(BookDTO bookDTO) throws SQLException {
        connect();

        final String query = String.format("%s %s %s " +
                        "%s = '%s', " +
                        "%s = %s, " +
                        "%s = %s, " +
                        "%s = '%s', " +
                        "%s = %s, " +
                        "%s = '%s' " +
                        "%s %s = %s;",
                UPDATE, BOOK, SET,
                TITLE, bookDTO.getTitle(),
                AUTHOR_ID, bookDTO.getAuthorId().getId(),
                GENRE_ID, bookDTO.getGenreId().getId(),
                PUBLISHER, bookDTO.getPublisher(),
                YEAR, bookDTO.getYear(),
                CITY, bookDTO.getCity(),
                WHERE, ID, bookDTO.getId());

        executeUpdate(query);

        disconnect();
    }

    @Override
    public void deleteBook(long id) throws SQLException {
        connect();

        final String query = String.format("%s %s %s %s %s = %s",
                DELETE, FROM, BOOK, WHERE, ID, id);

        executeUpdate(query);

        disconnect();
    }

    @Override
    public List<BookDTO> findAllBooks() throws SQLException {
        connect();

        final String query = String.format("%s * %s %s",
                SELECT, FROM, BOOK);

        ResultSet resultSet = executeQuery(query);

        List<BookDTO> books = getBooksFromResultSet(resultSet);

        disconnect();

        return books;
    }

    @Override
    public List<BookDTO> findBooksByTitle(String title) throws SQLException {
        connect();

        final String query = String.format("%s * %s %s " +
                        "%s %s ( %s ) = %s ('%s')",
                SELECT, FROM, BOOK,
                WHERE, LOWER, TITLE, LOWER, title);

        ResultSet resultSet = executeQuery(query);

        List<BookDTO> books = getBooksFromResultSet(resultSet);

        disconnect();
        return books;
    }

    @Override
    public List<BookDTO> findBooksByAuthor(long authorId) {
        return null;
    }

    @Override
    public List<BookDTO> findBooksByFilters(String title, String author, String publisher) throws SQLException {
        connect();

        String[] nameAndSurname = author.split(" ");

        StringBuilder sb = new StringBuilder();
        final String query;

        if (!title.equals("")) {
            final String titleFilter = String.format("%s ( %s ) %s %s ('%s')",
                    LOWER, TITLE, LIKE, LOWER, '%' + title + '%');
            sb.append(titleFilter);

            if (!author.equals("")) {
                //String str = "author_id = (SELECT id FROM author WHERE name = '' AND surname = '')";
                final String authorFilter = String.format("%s = (%s id %s %s %s name = '%s' %s surname = '%s')",
                        AUTHOR_ID, SELECT, FROM, AUTHOR, WHERE, nameAndSurname[0], AND, nameAndSurname[1]);
                sb.append(' ' + AND + ' ');
                sb.append(authorFilter);
            }

            if (!publisher.equals("")) {
                final String publisherFilter = String.format("%s = '%s'", PUBLISHER, publisher);
                sb.append(' ' + AND + ' ');
                sb.append(publisherFilter);
            }

        } else if (!author.equals("")) {
            final String authorFilter = String.format("%s = (%s id %s %s %s name = '%s' %s surname = '%s')",
                    AUTHOR_ID, SELECT, FROM, AUTHOR, WHERE, nameAndSurname[0], AND, nameAndSurname[1]);
            sb.append(authorFilter);

            if (!publisher.equals("")) {
                final String publisherFilter = String.format("%s = '%s'", PUBLISHER, publisher);
                sb.append(' ' + AND + ' ');
                sb.append(publisherFilter);
            }
        } else if (!publisher.equals("")) {
            final String publisherFilter = String.format("%s = '%s'", PUBLISHER, publisher);
            sb.append(publisherFilter);
        } else {
            return findAllBooks();
        }

        query = String.format("%s * %s %s " +
                        "%s %s",
                SELECT, FROM, BOOK,
                WHERE, sb.toString());

        System.out.println("Query: " + query);

        ResultSet resultSet = executeQuery(query);

        List<BookDTO> books = getBooksFromResultSet(resultSet);

        disconnect();
        return books;
    }

    private List<BookDTO> getBooksFromResultSet(ResultSet resultSet) throws SQLException {
        List<BookDTO> medicalPrescriptions = new ArrayList<>();

        BookDTO bookDTO;
        while (resultSet.next()) {
            AuthorDTO author = DaoFactory.getInstance()
                    .getAuthorDao().findAuthor(resultSet.getLong(AUTHOR_ID));
            GenreDTO genre = DaoFactory.getInstance()
                    .getGenreDao().findBookGenre(resultSet.getLong(GENRE_ID));
            bookDTO = new BookDTO(
                    resultSet.getLong(ID),
                    resultSet.getString(TITLE),
                    resultSet.getString(PUBLISHER),
                    resultSet.getShort(YEAR),
                    resultSet.getString(CITY));
            bookDTO.setAuthorId(author);
            bookDTO.setGenreId(genre);

            medicalPrescriptions.add(bookDTO);
        }

        return medicalPrescriptions;
    }
}
