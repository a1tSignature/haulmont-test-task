package com.haulmont.testtask.ui.windows;

import com.haulmont.testtask.MyTheme;
import com.haulmont.testtask.db.daos.BookDaoInterface;
import com.haulmont.testtask.db.hsqlDao.DaoFactory;
import com.haulmont.testtask.dto.AuthorDTO;
import com.haulmont.testtask.dto.BookDTO;
import com.haulmont.testtask.dto.GenreDTO;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Класс информации о пациентах
 *
 * @author a1tSign
 * @version 1.0
 * @since 2020-12-24
 */
public class BookWindow extends Window {
    private Grid<BookDTO> grid;
    private boolean add;

    private Button okButton;
    private Button cancelButton;

    private TextField titleText;
    private ComboBox<AuthorDTO> authors;
    private ComboBox<GenreDTO> genres;
    private ComboBox<String> publishers;
    private TextField yearText;
    private TextField cityText;

    private Binder<BookDTO> binder = new Binder<>(BookDTO.class);

    private BookDTO bookDTO;

    private Logger logger = Logger.getLogger(BookDTO.class.getName());

    /**
     * Конструктор диалога изменения/добавления новой книги
     *
     * @param grid таблица книг
     * @param add  флаг, который будет определять действие диалога
     *             (add = true - добавить, add = false - изменить)
     */

    public BookWindow(Grid<BookDTO> grid, boolean add) {
        this.grid = grid;
        this.add = add;
        build();
        fillAuthors();
        fillGenres();
        fillPublishers();
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

        titleText = new TextField("Название книги");
        titleText.setMaxLength(100);
        titleText.setWidth("100%");
        titleText.setRequiredIndicatorVisible(true);
        binder.forField(titleText)
                .withValidator(str -> str != null && !str.isEmpty(),
                        "Необходимо ввести название книги!")
                .asRequired()
                .bind(BookDTO::getTitle, BookDTO::setTitle);

        authors = new ComboBox<>("Автор");
        authors.setTextInputAllowed(false);
        authors.setPlaceholder("Выберите автора");
        authors.setWidth("100%");
        binder.forField(authors)
                .withValidator(Objects::nonNull, "Необходимо выбрать автора!")
                .asRequired()
                .bind(BookDTO::getAuthorId, BookDTO::setAuthorId);

        genres = new ComboBox<>("Жанр");
        genres.setTextInputAllowed(false);
        genres.setPlaceholder("Выберите жанр");
        genres.setWidth("100%");
        binder.forField(genres)
                .withValidator(Objects::nonNull, "Необходимо выбрать жанр!")
                .asRequired()
                .bind(BookDTO::getGenreId, BookDTO::setGenreId);

        publishers = new ComboBox<>("Издатель");
        publishers.setTextInputAllowed(false);
        publishers.setPlaceholder("Выберите издателя");
        publishers.setWidth("100%");
        binder.forField(publishers)
                .withValidator(Objects::nonNull, "Необходимо выбрать издателя!")
                .asRequired()
                .bind(BookDTO::getPublisher, BookDTO::setPublisher);

        yearText = new TextField("Год издания");
        yearText.setMaxLength(4);
        yearText.setWidth("100%");
        yearText.setRequiredIndicatorVisible(true);
        binder.forField(yearText)
                .withValidator(str -> str != null && !str.isEmpty(),
                        "Необходимо ввести год издания!")
                .withValidator(new RegexpValidator("Введите корректный год издания!",
                        "\\d+"))
                .asRequired()
                .bind(e -> String.valueOf(e.getYear()), BookDTO::setYear);

        cityText = new TextField("Город");
        cityText.setMaxLength(40);
        cityText.setWidth("100%");
        cityText.setRequiredIndicatorVisible(true);
        binder.forField(cityText)
                .withValidator(str -> str != null && !str.isEmpty(),
                        "Необходимо ввести название книги!")
                .withValidator(new RegexpValidator("Некорректное название города!",
                        "[a-zA-Zа-я-А-я\\-]+"))
                .asRequired()
                .bind(BookDTO::getCity, BookDTO::setCity);

//        Pattern pattern = Pattern.compile("[a-zA-Zа-я-А-я\\-]+");

        formLayout.addComponents(titleText, authors, genres, publishers, yearText, cityText);

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
            setCaption("Редактирование книги");
            if (!grid.asSingleSelect().isEmpty()) {
                try {
                    bookDTO = grid.asSingleSelect().getValue();
                    binder.setBean(bookDTO);
                } catch (Exception e) {
                    logger.severe(e.getMessage());
                }
            }
        } else {
            setCaption("Добавление жанра");
            titleText.focus();
        }

        okButton.addClickListener(clickEvent -> {
            if (binder.validate().isOk()) {
                try {
                    BookDTO newBook = new BookDTO();
                    newBook.setTitle(titleText.getValue());
                    newBook.setAuthorId(authors.getValue());
                    newBook.setGenreId(genres.getValue());
                    newBook.setPublisher(publishers.getValue());
                    newBook.setYear(Integer.parseInt(yearText.getValue()));
                    newBook.setCity(cityText.getValue());

                    BookDaoInterface bookDao = DaoFactory.getInstance().getBookDao();
                    if (!add) {
                        bookDao.updateBook(bookDTO);
                    } else {
                        System.out.println(newBook);
                        bookDao.insertBook(newBook);
                    }
                    List<BookDTO> books = DaoFactory.getInstance().getBookDao().findAllBooks();
                    grid.setItems(books);
                } catch (SQLException e) {
                    logger.severe(e.getMessage());
                }
                close();
            }
        });

        cancelButton.addClickListener(clickEvent -> close());
    }

    private void fillPublishers() {
        try {
            List<String> publishersList = new ArrayList<>();
            publishersList.add("O’Reilly");
            publishersList.add("Москва");
            publishersList.add("Питер");
            publishers.setItems(publishersList);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void fillGenres() {
        try {
            List<GenreDTO> genresList = DaoFactory.getInstance().getGenreDao().findAllGenres();
            genres.setItems(genresList);
            genres.setItemCaptionGenerator(GenreDTO::getName);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillAuthors() {
        try {
            List<AuthorDTO> authorList = DaoFactory.getInstance().getAuthorDao().getAllAuthors();
            authors.setItems(authorList);
            authors.setItemCaptionGenerator(caption ->
                    caption.getName() + " " + caption.getSurname());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

