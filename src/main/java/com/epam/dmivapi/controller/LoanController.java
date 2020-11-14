package com.epam.dmivapi.controller;

import com.epam.dmivapi.ContextParam;
import com.epam.dmivapi.Path;
import com.epam.dmivapi.dto.LoanDto;
import com.epam.dmivapi.dto.LoanDtoViewAll;
import com.epam.dmivapi.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/loan")
public class LoanController {
    private LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @RequestMapping("")
    public String getAllLoan(
            @RequestParam(value = ContextParam.PGN_CURRENT_PAGE, defaultValue = "1") int currentPage,
            @RequestParam(value = ContextParam.PGN_RECORDS_PER_PAGE,
                    defaultValue = ContextParam.RECORDS_PER_PAGE) int recordsPerPage,
            Model model
    ) {
        final String GENRE_LANGUAGE_CODE = "ru"; // TODO change this hardcoding later

        List<LoanDtoViewAll> loans = loanService.getAllLoan(
                GENRE_LANGUAGE_CODE,
                currentPage,
                recordsPerPage
        );
        model.addAttribute(ContextParam.LS_LOANS, loans);

        int nOfPages = loanService.countLoans(
                GENRE_LANGUAGE_CODE,
                recordsPerPage
        );

        model.addAttribute(ContextParam.PGN_RECORDS_PER_PAGE, recordsPerPage);
        model.addAttribute(ContextParam.PGN_NUMBER_OF_PAGES, nOfPages);

        return Path.PAGE__LIST_LOANS_MULTIPLE_USERS;
    }

    @RequestMapping("/{userId}")
    public String getLoansByUserId(
            @PathVariable int userId,
            @RequestParam(value = ContextParam.PGN_CURRENT_PAGE, defaultValue = "1") int currentPage,
            @RequestParam(value = ContextParam.PGN_RECORDS_PER_PAGE,
                    defaultValue = ContextParam.RECORDS_PER_PAGE) int recordsPerPage,
            Model model
    ) {
        final String GENRE_LANGUAGE_CODE = "ru"; // TODO change this hardcoding later

        List<LoanDto> loans = loanService.getLoansByUserId(
                userId,
                GENRE_LANGUAGE_CODE,
                currentPage,
                recordsPerPage
        );
        model.addAttribute(ContextParam.LS_LOANS, loans);

        int nOfPages = loanService.countLoansByUserId(
                userId,
                GENRE_LANGUAGE_CODE,
                recordsPerPage
        );

        model.addAttribute(ContextParam.PGN_RECORDS_PER_PAGE, recordsPerPage);
        model.addAttribute(ContextParam.PGN_NUMBER_OF_PAGES, nOfPages);

        return Path.PAGE__LIST_LOANS_SINGLE_USER;
    }
}
