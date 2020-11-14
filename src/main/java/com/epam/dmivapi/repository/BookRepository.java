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
}
