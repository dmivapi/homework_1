package com.epam.dmivapi.repository;

import com.epam.dmivapi.dto.LoanDtoViewAll;
import com.epam.dmivapi.model.Loan;

import java.time.LocalDate;
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

    void createLoansByUserIdAndPublicationsList(int userId, List<Integer> publicationIds);

    void updateLoanStatusToOutById(int loanId, LocalDate dateOut, LocalDate dueDate);

    void updateLoanStatusToReturnedById(int loanId, LocalDate dateIn);

    void deleteLoanById(int loanId);
}
