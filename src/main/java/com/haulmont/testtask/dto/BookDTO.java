package com.haulmont.testtask.dto;

import java.util.Objects;

public class BookDTO {
    private long id;
    private String title;
    private AuthorDTO author;
    private GenreDTO genre;
    private String publisher;
    private int year;
    private String city;

    public BookDTO() {
    }

    public BookDTO(long id, String title, String publisher, short year, String city) {
        this.id = id;
        this.title = title;
        this.publisher = publisher;
        this.year = year;
        this.city = city;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AuthorDTO getAuthorId() {
        return author;
    }

    public void setAuthorId(AuthorDTO authorId) {
        this.author = authorId;
    }

    public GenreDTO getGenreId() {
        return genre;
    }

    public void setGenreId(GenreDTO genreId) {
        this.genre = genreId;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setYear(String year) {
        this.year = Integer.parseInt(year);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDTO bookDTO = (BookDTO) o;
        return id == bookDTO.id &&
                year == bookDTO.year &&
                Objects.equals(title, bookDTO.title) &&
                Objects.equals(author, bookDTO.author) &&
                Objects.equals(genre, bookDTO.genre) &&
                Objects.equals(publisher, bookDTO.publisher) &&
                Objects.equals(city, bookDTO.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author, genre, publisher, year, city);
    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", authorId=" + author +
                ", genreId=" + genre +
                ", publisher='" + publisher + '\'' +
                ", year=" + year +
                ", city='" + city + '\'' +
                '}';
    }
}
