package com.haulmont.testtask.ui.windows;

import com.haulmont.testtask.MyTheme;
import com.haulmont.testtask.db.daos.AuthorDaoInterface;
import com.haulmont.testtask.db.hsqlDao.DaoFactory;
import com.haulmont.testtask.dto.AuthorDTO;
import com.vaadin.data.Binder;
import com.vaadin.ui.*;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class AuthorWindow extends Window {
    private Grid<AuthorDTO> grid;
    private boolean add;
    private Button okButton;
    private Button cancelButton;
    private TextField nameText;
    private TextField surnameText;
    private TextField patronymicText;

    private AuthorDaoInterface authorDao;

    private AuthorDTO authorDTO;

    private static Logger logger = Logger.getLogger(AuthorDTO.class.getName());

    private Binder<AuthorDTO> binder = new Binder<>(AuthorDTO.class);

    /**
     * Конструктор диалога изменения/редактирования
     *
     * @param grid таблица жанров
     * @param add  флаг, который будет определять действие диалога
     *             (edit = true - изменить, edit = false - добавить)
     */
    public AuthorWindow(Grid<AuthorDTO> grid, boolean add) {
        this.grid = grid;
        this.add = add;
        build();
        listeners();
    }

    private void build() {
        setStyleName(MyTheme.MODAL_WINDOW);
        setWidth("480px");
        setHeight("380px");
        setModal(true);
        setResizable(false);
        center();

        VerticalLayout main = new VerticalLayout();
        main.setSizeFull();
        main.setMargin(true);
        main.setSpacing(true);

        FormLayout formLayout = new FormLayout();
        formLayout.setSizeFull();
        formLayout.setMargin(false);
        formLayout.setSpacing(true);

        nameText = new TextField("Имя");
        nameText.setMaxLength(50);
        nameText.setWidth("100%");
        nameText.setRequiredIndicatorVisible(true);
        binder.forField(nameText)
                .withValidator(string -> string != null && !string.isEmpty(), "Пожалуйста, введите имя.")
                .asRequired()
                .bind(AuthorDTO::getName, AuthorDTO::setName);

        surnameText = new TextField("Фамилия");
        surnameText.setMaxLength(50);
        surnameText.setWidth("100%");
        surnameText.setRequiredIndicatorVisible(true);
        binder.forField(surnameText)
                .withValidator(string -> string != null && !string.isEmpty(), "Пожалуйста, введите фамилию.")
                .asRequired()
                .bind(AuthorDTO::getSurname, AuthorDTO::setSurname);

        patronymicText = new TextField("Отчество");
        patronymicText.setMaxLength(50);
        patronymicText.setWidth("100%");
        patronymicText.setRequiredIndicatorVisible(true);
        binder.forField(patronymicText)
                .withValidator(string -> string != null && !string.isEmpty(), "Пожалуйста, введите отчество")
                .asRequired()
                .bind(AuthorDTO::getPatronymic, AuthorDTO::setPatronymic);

        formLayout.addComponents(nameText, surnameText, patronymicText);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setSpacing(true);

        okButton = new Button("OK");
        okButton.setWidth("120px");
        cancelButton = new Button("Отменить");
        cancelButton.setWidth("120px");

        buttonsLayout.addComponents(okButton, cancelButton);

        main.addComponents(formLayout, buttonsLayout);
        main.setExpandRatio(formLayout, 1f);
        main.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_CENTER);
        setContent(main);
    }

    private void listeners() {

        if (!add) {
            setCaption("Редактирование автора");
            if (!grid.asSingleSelect().isEmpty()) {
                try {
                    authorDTO = grid.asSingleSelect().getValue();
                    binder.setBean(authorDTO);
                } catch (Exception e) {
                    logger.severe(e.getMessage());
                }
            }
        } else {
            setCaption("Добавление автора");
            nameText.focus();
        }

        okButton.addClickListener(clickEvent -> {
            if (binder.validate().isOk()) {
                try {
                    AuthorDTO newAuthor = new AuthorDTO();
                    newAuthor.setName(nameText.getValue());
                    newAuthor.setSurname(surnameText.getValue());
                    newAuthor.setPatronymic(patronymicText.getValue());

                    authorDao = DaoFactory.getInstance().getAuthorDao();

                    if (!add) {
                        authorDao.updateAuthor(authorDTO);
                    } else {
                        authorDao.insertAuthor(newAuthor);
                    }

                    List<AuthorDTO> dtos = authorDao.getAllAuthors();
                    grid.setItems(dtos);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                close();
            }
        });

        cancelButton.addClickListener(clickEvent ->
                close());
    }
}

