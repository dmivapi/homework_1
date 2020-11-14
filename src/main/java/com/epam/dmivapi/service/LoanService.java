package com.epam.dmivapi.service;

import com.epam.dmivapi.dto.LoanDto;
import com.epam.dmivapi.dto.LoanDtoViewAll;

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


}
