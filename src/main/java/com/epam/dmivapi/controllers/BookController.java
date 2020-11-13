package com.epam.dmivapi.controllers;

import com.epam.dmivapi.entity.Book;
import com.epam.dmivapi.ContextParam;
import com.epam.dmivapi.Path;
import com.epam.dmivapi.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {
    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/list")
    public String getAllBook(
            @RequestParam(value = ContextParam.BK_TITLE, defaultValue = "") String title,
            @RequestParam(value = ContextParam.BK_AUTHOR, defaultValue = "") String author,
            @RequestParam(value = ContextParam.BS_ORDER_BY, defaultValue = "") String orderBy,
            @RequestParam(value = ContextParam.BS_ORDER_BY_DIRECTION, defaultValue = "") String orderByDir,
            @RequestParam(value = ContextParam.PGN_CURRENT_PAGE, defaultValue = "1") int currentPage,
            @RequestParam(value = ContextParam.PGN_RECORDS_PER_PAGE, defaultValue = ContextParam.RECORDS_PER_PAGE) int recordsPerPage,
            Model model) {

        final String GENRE_LANGUAGE_CODE = "ru";

        List<Book> bookList = bookService.findBookByTitleAndAuthor(
                title, author, GENRE_LANGUAGE_CODE,
                orderBy, !"DESC".equalsIgnoreCase(orderByDir),
                currentPage, recordsPerPage
        );
        model.addAttribute(ContextParam.BS_BOOKS, bookList);

        int nOfPages = bookService.countBookPageByTitleAndAuthor(
                title, author,
                GENRE_LANGUAGE_CODE,
                recordsPerPage
        );
        model.addAttribute(ContextParam.PGN_NUMBER_OF_PAGES, nOfPages);

        return Path.PAGE__LIST_BOOKS;
    }
}
