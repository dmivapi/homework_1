package com.epam.dmivapi.service;

import com.epam.dmivapi.model.Book;

import java.util.List;

public interface BookService {
    List<Book> getBooksByTitleAndAuthor(
            String title,
            String author,
            String genreLanguageCode,
            String orderByField,
            boolean isAscending,
            int currentPage,
            int recordsPerPage
    );

    int countBooksPageByTitleAndAuthor(
            String title,
            String author,
            String genreLanguageCode,
            int recordsPerPage
    );
}
