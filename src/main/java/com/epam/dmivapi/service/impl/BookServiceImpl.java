package com.epam.dmivapi.service.impl;

import com.epam.dmivapi.model.Book;
import com.epam.dmivapi.repository.BookRepository;
import com.epam.dmivapi.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getBooksByTitleAndAuthor(
            String title,
            String author,
            String genreLanguageCode,
            String orderByField,
            boolean isAscending,
            int currentPage,
            int recordsPerPage
    ) {
        return bookRepository.findBooksByTitleAndAuthor(
                title,
                author,
                genreLanguageCode,
                orderByField,
                isAscending,
                currentPage,
                recordsPerPage
        );
    }

    @Override
    public int countBooksPageByTitleAndAuthor(
            String title,
            String author,
            String genreLanguageCode,
            int recordsPerPage
    ) {
        int rows = bookRepository.countBooksByTitleAndAuthor(title, author, genreLanguageCode);

        int nOfPages = rows / recordsPerPage;
        if (rows % recordsPerPage > 0)
            nOfPages++;

        return nOfPages;
    }
}
