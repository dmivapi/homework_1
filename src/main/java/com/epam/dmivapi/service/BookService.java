package com.epam.dmivapi.service;

import com.epam.dmivapi.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> findBookByTitleAndAuthor(
            String title,
            String author,
            String genreLanguageCode,
            String orderByField,
            boolean isAscending,
            int currentPage,
            int recordsPerPage
    );

    Integer countBookPageByTitleAndAuthor(
            String title,
            String author,
            String genreLanguageCode,
            int recordsPerPage
    );
}
