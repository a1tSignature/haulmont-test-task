package com.haulmont.testtask;

import com.haulmont.testtask.ui.views.AuthorView;
import com.haulmont.testtask.ui.views.BookView;
import com.haulmont.testtask.ui.views.GenreView;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

@Theme(MyTheme.THEME_NAME)
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        final String title = "Информация о книгах библиотеки";
        getPage().setTitle(title);

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(false);
        mainLayout.setSpacing(false);

        HorizontalLayout header = new HorizontalLayout();
        header.setHeight("50px");
        header.setMargin(false);
        header.setSpacing(true);

        Button bookBtn = new Button("Книги", clickEvent -> getNavigator().navigateTo(BookView.NAME));
        bookBtn.setHeight("100%");

        Button authorBtn = new Button("Авторы", clickEvent -> getNavigator().navigateTo(AuthorView.NAME));
        authorBtn.setHeight("100%");

        Button genreBtn = new Button("Жанры", clickEvent -> getNavigator().navigateTo(GenreView.NAME));
        genreBtn.setHeight("100%");

        Label head = new Label(title);
        header.setWidth(null);

        header.addComponents(bookBtn, authorBtn, genreBtn, head);
        header.setComponentAlignment(head, Alignment.MIDDLE_RIGHT);
        header.setExpandRatio(head, 1f);
        header.setMargin(new MarginInfo(false, false, false, true));
        header.setSpacing(true);

        VerticalLayout viewsLayout = new VerticalLayout();
        viewsLayout.setSizeFull();
        viewsLayout.setMargin(false);

        mainLayout.addComponents(header, viewsLayout);
        mainLayout.setExpandRatio(viewsLayout, 1f);

        ViewDisplay viewDisplay = new Navigator.ComponentContainerViewDisplay(viewsLayout);
        Navigator navigator = new Navigator(this, viewDisplay);
        navigator.addView(BookView.NAME, new BookView());
        navigator.addView(AuthorView.NAME, new AuthorView());
        navigator.addView(GenreView.NAME, new GenreView());

        header.setStyleName(MyTheme.HEADER_LAYOUT);
        mainLayout.setStyleName(MyTheme.VIEW_LAYOUT);
        viewsLayout.setStyleName(MyTheme.VIEW_LAYOUT);


        setContent(mainLayout);
    }
}
