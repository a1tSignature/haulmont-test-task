package com.haulmont.testtask.ui.windows;

import com.haulmont.testtask.MyTheme;
import com.haulmont.testtask.db.hsqlDao.DaoFactory;
import com.haulmont.testtask.dto.GenreDTO;
import com.haulmont.testtask.dto.GenreStatisticDTO;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Window;

import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Реализация модального окна для отображения статистики жанров
 *
 * @author a1tSign
 * @version 1.0
 * @since 2020-12-24
 */
public class GenreStatisticWindow extends Window {

    private static Logger logger = Logger.getLogger(GenreWindow.class.getName());

    /**
     * Конструктор модального окна
     */
    public GenreStatisticWindow() {
        build();
    }

    private void build() {
        setCaption("Статистика");

        Grid<GenreStatisticDTO> grid = new Grid<>();
        grid.setSizeFull();

        grid.removeAllColumns();
        grid.addColumn(GenreStatisticDTO::getName).setCaption("Название жанра");
        grid.addColumn(GenreStatisticDTO::getCount).setCaption("Количество книг");

        try {
            grid.setItems(DaoFactory.getInstance().getGenreDao().getStatistic());
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        setStyleName(MyTheme.MODAL_WINDOW);
        setWidth("480px");
        setHeight("480px");
        setModal(true);
        setResizable(false);
        setContent(grid);
        center();
    }

}
