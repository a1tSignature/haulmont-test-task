package com.haulmont.testtask.db.hsqlDao;

import com.haulmont.testtask.db.dao.AbstractDao;
import com.haulmont.testtask.db.daos.GenreDaoInterface;
import com.haulmont.testtask.dto.GenreDTO;
import com.haulmont.testtask.dto.GenreStatisticDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.haulmont.testtask.db.dao.SqlCommands.*;
import static com.haulmont.testtask.db.daos.DaoFactoryInterface.BOOK;
import static com.haulmont.testtask.db.daos.DaoFactoryInterface.GENRE;

public class HsqlGenreDao extends AbstractDao implements GenreDaoInterface {

    protected HsqlGenreDao() {
        super();
    }

    @Override
    public GenreDTO findBookGenre(long id) throws SQLException {
        connect();

        final String query = String.format("%s * %s %s %s %s = %s", SELECT, FROM, GENRE, WHERE, ID, id);
        ResultSet resultSet = executeQuery(query);

        GenreDTO genre = null;
        while (resultSet.next()) {
            genre = new GenreDTO(
                    resultSet.getLong(ID),
                    resultSet.getString(NAME));
        }

        disconnect();
        return genre;
    }

    @Override
    public void insertBookGenre(GenreDTO genreDTO) throws SQLException {
        connect();

        final String query = String.format("%s %s %s (%s) %s ('%s');",
                INSERT, INTO, GENRE,
                NAME,
                VALUES,
                genreDTO.getName());

        executeUpdate(query);

        disconnect();
    }

    @Override
    public void updateBookGenre(GenreDTO genreDTO) throws SQLException {
        connect();

        final String query = String.format("%s %s %s " +
                        "%s = '%s'" +
                        "%s %s = %s;",
                UPDATE, GENRE, SET,
                NAME, genreDTO.getName(),
                WHERE, ID, genreDTO.getId());

        executeUpdate(query);

        disconnect();
    }

    @Override
    public void deleteBookGenre(long id) throws SQLException {
        connect();

        final String query = String.format("%s %s %s %s %s = %s", DELETE, FROM, GENRE, WHERE, ID, id);
        int changedRowsNum = executeUpdate(query);

        disconnect();
    }

    @Override
    public List<GenreDTO> findAllGenres() throws SQLException {
        connect();

        final String query = String.format("%s * %s %s", SELECT, FROM, GENRE);
        ResultSet resultSet = executeQuery(query);

        List<GenreDTO> genres = getGenreFromResultSet(resultSet);

        disconnect();
        return genres;
    }

    @Override
    public List<GenreDTO> findGenreByName(String name) throws SQLException {
        connect();

        final String query = String.format("%s * %s %s " +
                        "%s %s ( %s ) %s %s ('%s')",
                SELECT, FROM, GENRE,
                WHERE, LOWER, NAME, LIKE, LOWER, '%' + name + '%');

        ResultSet resultSet = executeQuery(query);

        List<GenreDTO> genres = getGenreFromResultSet(resultSet);

        disconnect();

        return genres;
    }

    @Override
    public List<GenreStatisticDTO> getStatistic() throws SQLException {
        connect();

        final String query = String.format("%s g.name %s name, %s(b.id) %s count " +
                        "%s %s b %s %s " +
                        "%s g %s g.id = b.genre_id " +
                        "%s %s g.name",
                SELECT, AS, COUNT, AS, FROM, BOOK, INNER, JOIN,
                GENRE, ON, GROUP, BY);

        ResultSet rs = executeQuery(query);

        List<GenreStatisticDTO> statistic = getGenreStatistics(rs);

        disconnect();

        return statistic;
    }

    private List<GenreDTO> getGenreFromResultSet(ResultSet resultSet) throws SQLException {
        List<GenreDTO> genres = new ArrayList<>();

        GenreDTO genreDTO;
        while (resultSet.next()) {
            genreDTO = new GenreDTO(
                    resultSet.getLong(ID),
                    resultSet.getString(NAME));

            genres.add(genreDTO);
        }

        return genres;
    }

    private List<GenreStatisticDTO> getGenreStatistics(ResultSet resultSet) throws SQLException {
        List<GenreStatisticDTO> statisticDTOS = new ArrayList<>();

        GenreStatisticDTO genreStatisticDTO;
        while (resultSet.next()) {
            genreStatisticDTO = new GenreStatisticDTO(
                    resultSet.getString("name"),
                    resultSet.getInt("count")
            );

            statisticDTOS.add(genreStatisticDTO);
        }

        return statisticDTOS;
    }
}
