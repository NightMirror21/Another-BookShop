package ru.nightmirror.bookshop.models;

import java.util.Objects;

public class Book {
    private final String name;
    private final int cost;

    public Book(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", cost=" + cost +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cost);
    }
}
