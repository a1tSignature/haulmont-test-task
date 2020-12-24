package com.haulmont.testtask.db.hsqlDao;

import com.haulmont.testtask.db.dao.AbstractDao;
import com.haulmont.testtask.db.daos.AuthorDaoInterface;
import com.haulmont.testtask.db.daos.BookDaoInterface;
import com.haulmont.testtask.db.daos.DaoFactoryInterface;
import com.haulmont.testtask.db.daos.GenreDaoInterface;

/**
 * Реализация фабриик Dao
 *
 * @author a1tSign
 * @version 1.0
 * @since 2020-12-23
 */
public class DaoFactory extends AbstractDao implements DaoFactoryInterface {

    private static DaoFactory instance = null;

    private DaoFactory() {
    }

    public static synchronized DaoFactory getInstance() {
        if (instance == null) {
            instance = new DaoFactory();
        }
        return instance;
    }

    @Override
    public AuthorDaoInterface getAuthorDao() {
        return new HsqlAuthorDao();
    }

    @Override
    public BookDaoInterface getBookDao() {
        return new HsqlBookDao();
    }

    @Override
    public GenreDaoInterface getGenreDao() {
        return new HsqlGenreDao();
    }
}
