package com.haulmont.testtask.db.daos;

import com.haulmont.testtask.db.dao.SqlCommands;
import com.haulmont.testtask.dto.GenreDTO;
import com.haulmont.testtask.dto.GenreStatisticDTO;

import java.sql.SQLException;
import java.util.List;

public interface GenreDaoInterface {
    String ID = "id";
    String NAME = "name";

    GenreDTO findBookGenre(final long id) throws SQLException;

    void insertBookGenre(final GenreDTO genreDTO) throws SQLException;

    void updateBookGenre(final GenreDTO genreDTO) throws SQLException;

    void deleteBookGenre(final long id) throws SQLException;

    List<GenreDTO> findAllGenres() throws SQLException;

    List<GenreDTO> findGenreByName(final String name) throws SQLException;

    List<GenreStatisticDTO> getStatistic() throws SQLException;
}
