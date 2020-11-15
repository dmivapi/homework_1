package com.epam.dmivapi.service.impl;

import com.epam.dmivapi.converter.LoanDtoConverter;
import com.epam.dmivapi.converter.LoanDtoViewAllConverter;
import com.epam.dmivapi.dto.LoanDto;
import com.epam.dmivapi.dto.LoanDtoViewAll;
import com.epam.dmivapi.model.Loan;
import com.epam.dmivapi.repository.LoanRepository;
import com.epam.dmivapi.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final LoanDtoConverter dtoConverter;
    private final LoanDtoViewAllConverter dtoViewAllConverter;

    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository,
                           LoanDtoConverter dtoConverter,
                           LoanDtoViewAllConverter dtoViewAllConverter
    ) {
        this.loanRepository = loanRepository;
        this.dtoConverter = dtoConverter;
        this.dtoViewAllConverter = dtoViewAllConverter;
    }

    @Override
    public List<LoanDtoViewAll> getAllLoan(
            String genreLanguageCode,
            int currentPage,
            int recordsPerPage
    ) {
        List<Loan> loans = loanRepository.findAll(
                genreLanguageCode,
                currentPage,
                recordsPerPage
        );
        return dtoViewAllConverter.convert(loans);
    }

    @Override
    public List<LoanDto> getLoansByUserId(
            int userId,
            String genreLanguageCode,
            int currentPage,
            int recordsPerPage
    ) {
        List<Loan> loans = loanRepository.findLoansByUserId(
                userId,
                genreLanguageCode,
                currentPage,
                recordsPerPage
        );
        return dtoConverter.convert(loans);
    }

    @Override
    public int countLoans(String genreLanguageCode, int recordsPerPage) {
        return ServiceUtils.calculateNumOfPages(
                loanRepository.countLoans(genreLanguageCode),
                recordsPerPage
        );
    }

    @Override
    public int countLoansByUserId(int userId, String genreLanguageCode, int recordsPerPage) {
        return ServiceUtils.calculateNumOfPages(
                loanRepository.countLoansByUserId(userId, genreLanguageCode),
                recordsPerPage
        );
    }
}