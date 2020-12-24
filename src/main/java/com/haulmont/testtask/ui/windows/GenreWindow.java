package com.haulmont.testtask.ui.windows;

import com.haulmont.testtask.MyTheme;
import com.haulmont.testtask.db.daos.GenreDaoInterface;
import com.haulmont.testtask.db.hsqlDao.DaoFactory;
import com.haulmont.testtask.dto.GenreDTO;
import com.vaadin.data.Binder;
import com.vaadin.ui.*;

import java.util.List;
import java.util.logging.Logger;

public class GenreWindow extends Window {
    private Grid<GenreDTO> grid;
    private boolean add;
    private Button okBtn;
    private Button cancelBtn;
    private TextField nameText;

    private GenreDTO genreDTO;
    private GenreDaoInterface genreDao;

    private static Logger logger = Logger.getLogger(GenreDTO.class.getName());

    private Binder<GenreDTO> binder = new Binder<>(GenreDTO.class);

    /**
     * Конструктор диалога изменения/редактирования
     *
     * @param grid таблица жанров
     * @param add  флаг, который будет определять действие диалога
     *             (add = true - добавить, add = false - изменить)
     */
    public GenreWindow(Grid<GenreDTO> grid, boolean add) {
        this.grid = grid;
        this.add = add;
        this.genreDao = DaoFactory.getInstance().getGenreDao();
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

        nameText = new TextField("Название жанра");
        nameText.setMaxLength(30);
        nameText.setWidth("100%");
        nameText.setRequiredIndicatorVisible(true);
        binder.forField(nameText)
                .withValidator(str -> str != null && !str.isEmpty(),
                        "Необходимо ввести название жанра!")
                .asRequired()
                .bind(GenreDTO::getName, GenreDTO::setName);

        formLayout.addComponent(nameText);

        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.setSpacing(true);

        okBtn = new Button("OK");
        okBtn.setWidth("120px");
        cancelBtn = new Button("Отменить");
        cancelBtn.setWidth("120px");

        btnLayout.addComponents(okBtn, cancelBtn);

        main.addComponents(formLayout, btnLayout);
        main.setExpandRatio(formLayout, 1f);
        main.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
        setContent(main);
    }

    private void listeners() {
        if (!add) {
            setCaption("Редактирование жанра");
            if (!grid.asSingleSelect().isEmpty()) {
                try {
                    genreDTO = grid.asSingleSelect().getValue();
                    binder.setBean(genreDTO);
                } catch (Exception e) {
                    logger.severe(e.getMessage());
                }
            }
        } else {
            setCaption("Добавление жанра");
            nameText.focus();
        }

        okBtn.addClickListener(clickEvent -> {
            if (binder.validate().isOk()) {
                try {
                    GenreDTO newGenre = new GenreDTO();
                    newGenre.setName(nameText.getValue());

                    if (!add) {
                        genreDao.updateBookGenre(genreDTO);
                    } else {
                        genreDao.insertBookGenre(newGenre);
                    }

                    List<GenreDTO> genres = genreDao.findAllGenres();
                    grid.setItems(genres);
                } catch (Exception e) {
                    logger.severe(e.getMessage());
                }
                close();
            }
        });

        cancelBtn.addClickListener(clickEvent -> close());
    }

}
