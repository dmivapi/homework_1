package com.epam.dmivapi.repository;

import com.epam.dmivapi.model.Book;

import java.util.List;

public interface BookRepository {
    List<Book> findBooksByTitleAndAuthor(
            String title, String author,
            String genreLanguageCode,
            String orderByField, boolean isAscending,
            int currentPage, int recordsPerPage
    );

    int countBooksByTitleAndAuthor(
            String title, String author,
            String genreLanguageCode
    );

    void save(Book book, int authorId, int publisherId, int genreId, int year, int price, String languageCode, String libCodes[]);
}
