package com.haulmont.testtask.dto;

import java.util.Objects;

public class GenreStatisticDTO {
    private final String name;
    private final int count;

    public GenreStatisticDTO(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreStatisticDTO that = (GenreStatisticDTO) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, count);
    }

    @Override
    public String toString() {
        return "GenreStatisticDTO{" +
                "name='" + name + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}
