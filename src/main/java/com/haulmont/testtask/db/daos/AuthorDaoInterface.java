package com.haulmont.testtask.db.daos;

import com.haulmont.testtask.dto.AuthorDTO;

import java.sql.SQLException;
import java.util.List;

public interface AuthorDaoInterface {
    String ID = "id";
    String NAME = "name";
    String SURNAME = "surname";
    String PATRONYMIC = "patronymic";

    AuthorDTO findAuthor(final long id) throws SQLException;

    void insertAuthor(final AuthorDTO authorDTO) throws SQLException;

    void updateAuthor(final AuthorDTO authorDTO) throws SQLException;

    void deleteAuthor(final long id) throws SQLException;

    List<AuthorDTO> getAllAuthors() throws SQLException;

    List<AuthorDTO> getAuthorBySurname(final String surname) throws SQLException;

}
