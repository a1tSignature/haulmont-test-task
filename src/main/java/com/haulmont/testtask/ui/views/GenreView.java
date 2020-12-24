package com.haulmont.testtask.ui.views;

import com.haulmont.testtask.MyTheme;
import com.haulmont.testtask.db.hsqlDao.DaoFactory;
import com.haulmont.testtask.dto.GenreDTO;
import com.haulmont.testtask.ui.windows.GenreStatisticWindow;
import com.haulmont.testtask.ui.windows.GenreWindow;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.VerticalLayout;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class GenreView extends VerticalLayout implements View {

    public static final String NAME = "genres";

    private Grid<GenreDTO> genresGrid = new Grid<>(GenreDTO.class);

    Button addButton;
    Button deleteButton;
    Button editButton;
    Button showStatisticButton;

    private static Logger logger = Logger.getLogger(GenreDTO.class.getName());

    public GenreView() {
        build();
        setupListeners();
    }

    private void build() {
        try {
            genresGrid.removeAllColumns();
            genresGrid.addColumn(GenreDTO::getId).setCaption("Номер жанра");
            genresGrid.addColumn(GenreDTO::getName).setCaption("Название жанра");
            genresGrid.setSizeFull();

            HorizontalLayout btnLayout = new HorizontalLayout();
            btnLayout.setSpacing(true);

            addButton = new Button("Добавить", new ThemeResource(MyTheme.BUTTON_ADD));
            deleteButton = new Button("Удалить", new ThemeResource(MyTheme.BUTTON_DELETE));
            editButton = new Button("Изменить", new ThemeResource(MyTheme.BUTTON_EDIT));
            showStatisticButton = new Button("Статистика", new ThemeResource(MyTheme.BUTTON_STATISTIC));

            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
            btnLayout.addComponents(addButton, editButton, deleteButton, showStatisticButton);

            setMargin(true);
            setSpacing(true);
            setSizeFull();
            addComponents(genresGrid, btnLayout);
            setExpandRatio(genresGrid, 1f);

        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void setupListeners() {
        try {
            genresGrid.addSelectionListener(e -> {
                if (!genresGrid.asSingleSelect().isEmpty()) {
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                } else {
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                }
            });

            addButton.addClickListener(clickEvent ->
                    getUI().addWindow(new GenreWindow(genresGrid, true)));

            editButton.addClickListener(clickEvent ->
                    getUI().addWindow(new GenreWindow(genresGrid, false)));

            showStatisticButton.addClickListener(clickEvent ->
                    getUI().addWindow(new GenreStatisticWindow()));

            deleteButton.addClickListener(clickEvent -> {
                if (!genresGrid.asSingleSelect().isEmpty()) {
                    try {
                        DaoFactory.getInstance().getGenreDao()
                                .deleteBookGenre(genresGrid.asSingleSelect().getValue().getId());
                        updateGrid();
                    } catch (SQLException e) {
                            Notification notification = new Notification(
                                    "Так как существую книги такого жанра, удалить его невозможно"
                            );
                            notification.setDelayMsec(1000);
                            notification.show(Page.getCurrent());
                    }
                }
            });

            showStatisticButton.addClickListener(clickEvent -> {

            });
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void updateGrid() {
        try {
            List<GenreDTO> patients = DaoFactory.getInstance().getGenreDao().findAllGenres();
            genresGrid.setItems(patients);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        updateGrid();
    }
}
