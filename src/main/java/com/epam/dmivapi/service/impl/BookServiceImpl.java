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
        if (recordsPerPage == 0) {
            throw new IllegalArgumentException("The number of records per page can not be 0");
        }
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
    public int countBooksPagesByTitleAndAuthor(
            String title,
            String author,
            String genreLanguageCode,
            int recordsPerPage
    ) {
        if (recordsPerPage == 0) {
            throw new IllegalArgumentException("The number of records per page can not be 0");
        }
        return ServiceUtils.calculateNumOfPages(
                bookRepository.countBooksByTitleAndAuthor(title, author, genreLanguageCode),
                recordsPerPage
        );
    }

    @Override
    public void createBook(Book book, int authorId, int publisherId, int genreId, int year, int price, String languageCode, String libCodes[]) {
        bookRepository.save(book, authorId, publisherId, genreId, year, price, languageCode, libCodes);
    }
}