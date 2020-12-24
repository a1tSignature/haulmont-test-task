package com.haulmont.testtask.db.hsqlDao;

import com.haulmont.testtask.db.dao.AbstractDao;
import com.haulmont.testtask.db.daos.AuthorDaoInterface;
import com.haulmont.testtask.dto.AuthorDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.haulmont.testtask.db.dao.SqlCommands.*;
import static com.haulmont.testtask.db.daos.DaoFactoryInterface.AUTHOR;

public class HsqlAuthorDao extends AbstractDao implements AuthorDaoInterface {
    protected HsqlAuthorDao() {
        super();
    }

    @Override
    public AuthorDTO findAuthor(long id) throws SQLException {
        connect();

        final String query = String.format("%s * %s %s %s %s = %s;", SELECT, FROM, AUTHOR, WHERE, ID, id);
        ResultSet resultSet = executeQuery(query);

        AuthorDTO authorDTO = null;

        while (resultSet.next()) {
            authorDTO = new AuthorDTO(
                    resultSet.getLong(ID),
                    resultSet.getString(NAME),
                    resultSet.getString(SURNAME),
                    resultSet.getString(PATRONYMIC)
            );
        }
        disconnect();

        return authorDTO;
    }

    @Override
    public void insertAuthor(AuthorDTO authorDTO) throws SQLException {
        connect();

        final String query = String.format("%s %s %s (%s, %s, %s) %s ('%s', '%s', '%s');",
                INSERT, INTO, AUTHOR, NAME, SURNAME, PATRONYMIC,
                VALUES,
                authorDTO.getName(), authorDTO.getSurname(), authorDTO.getPatronymic());

        executeUpdate(query);
        disconnect();
    }

    @Override
    public void updateAuthor(AuthorDTO authorDTO) throws SQLException {
        connect();

        final String query = String.format("%s %s %s " +
                        "%s = '%s', " +
                        "%s = '%s', " +
                        "%s = '%s' " +
                        "%s %s = %s;",
                UPDATE, AUTHOR, SET,
                NAME, authorDTO.getName(),
                SURNAME, authorDTO.getSurname(),
                PATRONYMIC, authorDTO.getPatronymic(),
                WHERE, ID, authorDTO.getId());

        executeUpdate(query);

        disconnect();
    }

    @Override
    public void deleteAuthor(long id) throws SQLException {
        connect();

        final String query = String.format("%s %s %s %s %s = %s", DELETE, FROM, AUTHOR, WHERE, ID, id);
        executeUpdate(query);

        disconnect();
    }

    @Override
    public List<AuthorDTO> getAllAuthors() throws SQLException {
        connect();

        final String query = String.format("%s * %s %s", SELECT, FROM, AUTHOR);
        ResultSet resultSet = executeQuery(query);

        List<AuthorDTO> authors = getAuthorsFromResultSet(resultSet);

        disconnect();

        return authors;
    }

    @Override
    public List<AuthorDTO> getAuthorBySurname(String surname) throws SQLException {
        connect();

        final String query = String.format("%s * %s %s " +
                        "%s %s ( %s ) %s %s ('%s')",
                SELECT, FROM, AUTHOR,
                WHERE, LOWER, SURNAME, LIKE, LOWER, '%' + surname + '%');

        ResultSet resultSet = executeQuery(query);

        List<AuthorDTO> authors = getAuthorsFromResultSet(resultSet);

        disconnect();
        return authors;
    }

    private List<AuthorDTO> getAuthorsFromResultSet(ResultSet resultSet) throws SQLException {
        List<AuthorDTO> authors = new ArrayList<>();

        AuthorDTO authorDTO;
        while (resultSet.next()) {
            authorDTO = new AuthorDTO(
                    resultSet.getLong(ID),
                    resultSet.getString(NAME),
                    resultSet.getString(SURNAME),
                    resultSet.getString(PATRONYMIC));

            authors.add(authorDTO);
        }

        return authors;
    }
}
