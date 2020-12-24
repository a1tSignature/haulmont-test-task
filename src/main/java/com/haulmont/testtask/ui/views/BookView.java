package com.haulmont.testtask.ui.views;

import com.haulmont.testtask.MyTheme;
import com.haulmont.testtask.db.hsqlDao.DaoFactory;
import com.haulmont.testtask.dto.AuthorDTO;
import com.haulmont.testtask.dto.BookDTO;
import com.haulmont.testtask.ui.windows.BookWindow;
import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Класс информации о книгах
 *
 * @author a1tSign
 * @version 1.0
 * @since 2020-12-24
 */
public class BookView extends VerticalLayout implements View {

    public static final String NAME = "books";

    private Grid<BookDTO> grid = new Grid<>(BookDTO.class);

    private Label filtrationComponentName;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private Button applyFilters;
    private TextField titleFilter;
    private ComboBox<AuthorDTO> authorFilter;
    private ComboBox<String> publisherFilter;

    private Binder<BookDTO> binder = new Binder<>(BookDTO.class);


    private static Logger logger = Logger.getLogger(BookDTO.class.getName());

    public BookView() {
        build();
        listeners();
        fillAuthors();
        fillPublishers();
    }

    private void build() {
        try {
            Panel filterPanel = new Panel();
            HorizontalLayout filterLayout = new HorizontalLayout();
            filterLayout.setMargin(true);
            filterLayout.setSpacing(true);

            filtrationComponentName = new Label();
            filtrationComponentName.setValue("Фильтр:");

            titleFilter = new TextField();

            authorFilter = new ComboBox<>();
            authorFilter.setTextInputAllowed(false);
            authorFilter.setPlaceholder("Выберите автора");

            publisherFilter = new ComboBox<>();
            publisherFilter.setTextInputAllowed(false);
            publisherFilter.setPlaceholder("Выберите издателя");

            applyFilters = new Button("Применить");

            titleFilter.setPlaceholder("по названию");

            filterLayout.addComponents(filtrationComponentName, titleFilter,
                    authorFilter, publisherFilter, applyFilters);
            filterLayout.setComponentAlignment(filtrationComponentName, Alignment.MIDDLE_CENTER);
            filterPanel.setContent(filterLayout);

            grid.removeAllColumns();
            grid.addColumn(BookDTO::getId).setCaption("Номер");
            grid.addColumn(BookDTO::getTitle).setCaption("Название");
            grid.addColumn(bookDTO -> bookDTO.getAuthorId().getName()
                    + " " + bookDTO.getAuthorId().getSurname()).setCaption("Автор");
            grid.addColumn(bookDTO -> bookDTO.getGenreId().getName()).setCaption("Жанр");
            grid.addColumn(BookDTO::getPublisher).setCaption("Издатель");
            grid.addColumn(BookDTO::getYear).setCaption("Год издания");
            grid.addColumn(BookDTO::getCity).setCaption("Город");
            grid.setSizeFull();

            HorizontalLayout buttonsLayout = new HorizontalLayout();
            buttonsLayout.setSpacing(true);

            addButton = new Button("Добавить", new ThemeResource(MyTheme.BUTTON_ADD));
            editButton = new Button("Изменить", new ThemeResource(MyTheme.BUTTON_EDIT));
            deleteButton = new Button("Удалить", new ThemeResource(MyTheme.BUTTON_DELETE));

            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
            buttonsLayout.addComponents(addButton, editButton, deleteButton);

            setSizeFull();
            addComponents(filterPanel, grid, buttonsLayout);
            setExpandRatio(grid, 1f);

        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void listeners() {
        try {
            grid.addSelectionListener(valueChangeEvent -> {
                if (!grid.asSingleSelect().isEmpty()) {
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                } else {
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                }
            });

            addButton.addClickListener(clickEvent ->
                    getUI().addWindow(new BookWindow(grid, true)));

            editButton.addClickListener(clickEvent -> {
                getUI().addWindow(new BookWindow(grid, false));
                fillAuthors();
            });

            deleteButton.addClickListener(clickEvent -> {
                if (!grid.asSingleSelect().isEmpty()) {
                    try {
                        DaoFactory.getInstance().getBookDao().deleteBook(grid.asSingleSelect().getValue().getId());
                        updateGrid();
                    } catch (SQLException e) {
                        logger.severe(e.getMessage());
                    }
                }
            });

            applyFilters.addClickListener(clickEvent -> updateGridByFilters());
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void updateGridByFilters() {
        try {
            List<BookDTO> books;
            String byTitle = titleFilter.getValue();

            String byAuthor;
            if (authorFilter.getValue() != null) {
                byAuthor = authorFilter.getValue().getName()
                        + " " + authorFilter.getValue().getSurname();
            } else byAuthor = "";
            System.out.println(byAuthor);

            String byPublisher;
            if (publisherFilter.getValue() != null) {
                byPublisher = publisherFilter.getValue();
            }
            else byPublisher = "";
            System.out.println(byPublisher);

            books = DaoFactory.getInstance()
                    .getBookDao().findBooksByFilters(byTitle, byAuthor, byPublisher);

            grid.setItems(books);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateGrid() {
        try {
            List<BookDTO> books = DaoFactory.getInstance().getBookDao().findAllBooks();
            grid.setItems(books);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void fillAuthors() {
        try {
            List<String> publishersList = new ArrayList<>();
            publishersList.add("O’Reilly");
            publishersList.add("Москва");
            publishersList.add("Питер");
            publisherFilter.setItems(publishersList);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void fillPublishers() {
        try {
            List<AuthorDTO> authorList = DaoFactory.getInstance().getAuthorDao().getAllAuthors();
            authorFilter.setItems(authorList);
            authorFilter.setItemCaptionGenerator(caption ->
                    caption.getName() + " " + caption.getSurname());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        updateGrid();
    }
}
