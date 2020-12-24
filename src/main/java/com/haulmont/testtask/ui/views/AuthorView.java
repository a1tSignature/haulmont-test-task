package com.haulmont.testtask.ui.views;

import com.haulmont.testtask.MyTheme;
import com.haulmont.testtask.db.hsqlDao.DaoFactory;
import com.haulmont.testtask.dto.AuthorDTO;
import com.haulmont.testtask.ui.windows.AuthorWindow;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Класс информации о атворах
 *
 * @author a1tSign
 * @version 1.0
 * @since 2020-12-24
 */
public class AuthorView extends VerticalLayout implements View {

    public static final String NAME = "authors";

    private Grid<AuthorDTO> authors = new Grid<>(AuthorDTO.class);

    private Button addBtn;
    private Button editBtn;
    private Button deleteBtn;

    private static Logger logger = Logger.getLogger(AuthorDTO.class.getName());

    public AuthorView() {
        build();
        listeners();
    }

    private void build() {
        try {
            authors.removeAllColumns();
            authors.addColumn(AuthorDTO::getId).setCaption("Номер автора");
            authors.addColumn(AuthorDTO::getName).setCaption("Имя");
            authors.addColumn(AuthorDTO::getSurname).setCaption("Фамилия");
            authors.addColumn(AuthorDTO::getPatronymic).setCaption("Отчество");
            authors.setSizeFull();

            HorizontalLayout buttonsLayout = new HorizontalLayout();
            buttonsLayout.setSpacing(true);

            addBtn = new Button("Добавить", new ThemeResource(MyTheme.BUTTON_ADD));
            deleteBtn = new Button("Удалить", new ThemeResource(MyTheme.BUTTON_DELETE));
            editBtn = new Button("Изменить", new ThemeResource(MyTheme.BUTTON_EDIT));

            editBtn.setEnabled(false);
            deleteBtn.setEnabled(false);
            buttonsLayout.addComponents(addBtn, editBtn, deleteBtn);

            setMargin(true);
            setSpacing(true);
            setSizeFull();
            addComponents(authors, buttonsLayout);
            setExpandRatio(authors, 1f);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void listeners() {
        try {
            authors.addSelectionListener(valueChangeEvent -> {
                if (!authors.asSingleSelect().isEmpty()) {
                    editBtn.setEnabled(true);
                    deleteBtn.setEnabled(true);
                } else {
                    editBtn.setEnabled(false);
                    deleteBtn.setEnabled(false);
                }
            });

            addBtn.addClickListener(clickEvent ->
                    getUI().addWindow(new AuthorWindow(authors, true)));

            editBtn.addClickListener(clickEvent ->
                    getUI().addWindow(new AuthorWindow(authors, false)));

            deleteBtn.addClickListener(clickEvent -> {
                if (!authors.asSingleSelect().isEmpty()) {
                    try {
                        DaoFactory.getInstance().getAuthorDao().deleteAuthor(authors.asSingleSelect().getValue().getId());
                        updateGrid();
                    } catch (SQLException e) {
                        if (e.getCause().getClass().equals(java.sql.SQLIntegrityConstraintViolationException.class)) {
                            Notification notification = new Notification("Удаление автора невозможно, " +
                                    "так есть книги, написанные им");
                            notification.setDelayMsec(1000);
                            notification.show(Page.getCurrent());
                        } else {
                            logger.severe(e.getMessage());
                        }
                    }
                }
            });
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void updateGrid() {
        try {
            List<AuthorDTO> authorDTOS = DaoFactory.getInstance().getAuthorDao().getAllAuthors();
            authors.setItems(authorDTOS);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        updateGrid();
    }
}
