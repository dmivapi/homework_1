package com.epam.dmivapi.repository;

import com.epam.dmivapi.dto.LoanDtoViewAll;
import com.epam.dmivapi.model.Loan;

import java.util.List;

public interface LoanRepository {
    List<Loan> findAll(
            String genreLanguageCode,
            int currentPage,
            int recordsPerPage
    );

    List<Loan> findLoansByUserId(
            int userId,
            String genreLanguageCode,
            int currentPage,
            int recordsPerPage
    );

    int countLoans(String genreLanguageCode);

    int countLoansByUserId(int userId, String genreLanguageCode);
}
