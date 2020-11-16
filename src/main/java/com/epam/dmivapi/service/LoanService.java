package com.epam.dmivapi.service;

import com.epam.dmivapi.ContextParam;
import com.epam.dmivapi.dto.LoanDto;
import com.epam.dmivapi.dto.LoanDtoViewAll;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

public interface LoanService {
    List<LoanDtoViewAll> getAllLoan(
            String genreLanguageCode,
            int currentPage,
            int recordsPerPage
    );

    List<LoanDto> getLoansByUserId(
            int userId,
            String genreLanguageCode,
            int currentPage,
            int recordsPerPage
    );

    int countLoans(
            String genreLanguageCode,
            int recordsPerPage
    );

    int countLoansByUserId(
            int userId,
            String genreLanguageCode,
            int recordsPerPage
    );

    void createLoansByUserIdAndPublicationsList(int userId, List<Integer> publicationIds);

    void updateLoanStatusToOutById(int loanId);

    void updateLoanStatusToReturnedById(int loanId);

    void deleteLoanById(int loanId);
}
