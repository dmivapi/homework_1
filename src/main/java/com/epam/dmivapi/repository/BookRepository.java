package com.epam.dmivapi.repository;

import com.epam.dmivapi.entity.Book;

import java.util.List;

public interface BookRepository {
    List<Book> findBookByTitleAndAuthor(
            String title, String author,
            String genreLanguageCode,
            String orderByField, boolean isAscending,
            int currentPage, int recordsPerPage
    );

    Integer countBookByTitleAndAuthor(
            String title, String author,
            String genreLanguageCode
    );
}
