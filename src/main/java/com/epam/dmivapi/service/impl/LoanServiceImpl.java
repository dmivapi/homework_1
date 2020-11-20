package com.epam.dmivapi.service.impl;

import com.epam.dmivapi.converter.LoanDtoConverter;
import com.epam.dmivapi.converter.LoanDtoViewAllConverter;
import com.epam.dmivapi.dto.LoanDto;
import com.epam.dmivapi.dto.LoanDtoViewAll;
import com.epam.dmivapi.exception.EntityDoesNotExistException;
import com.epam.dmivapi.model.Loan;
import com.epam.dmivapi.repository.LoanRepository;
import com.epam.dmivapi.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.isNull;

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
        if (recordsPerPage == 0) {
            throw new IllegalArgumentException("The number of records per page can not be 0");
        }

        List<Loan> loans = loanRepository.findAll(
                genreLanguageCode,
                currentPage,
                recordsPerPage
        );
        return dtoViewAllConverter.convert(loans);
    }

    @Override
    public List<LoanDto> getLoansByUserId(
            Integer userId,
            String genreLanguageCode,
            int currentPage,
            int recordsPerPage
    ) {
        if (isNull(userId)) {
            throw new EntityDoesNotExistException(String.format("User with ID=%s does not exist", userId));
        }
        if (recordsPerPage == 0) {
            throw new IllegalArgumentException("The number of records per page can not be 0");
        }
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
        if (recordsPerPage == 0) {
            throw new IllegalArgumentException("The number of records per page can not be 0");
        }
        return ServiceUtils.calculateNumOfPages(
                loanRepository.countLoans(genreLanguageCode),
                recordsPerPage
        );
    }

    @Override
    public int countLoansByUserId(Integer userId, String genreLanguageCode, int recordsPerPage) {
        if (isNull(userId)) {
            throw new EntityDoesNotExistException(String.format("User with ID=%s does not exist", userId));
        }
        return ServiceUtils.calculateNumOfPages(
                loanRepository.countLoansByUserId(userId, genreLanguageCode),
                recordsPerPage
        );
    }

    @Override
    public void createLoansByUserIdAndPublicationsList(Integer userId, List<Integer> publicationIds) {
        if (isNull(userId)) {
            throw new EntityDoesNotExistException(String.format("User with ID=%s does not exist", userId));
        }

        if (isNull(publicationIds) || publicationIds.isEmpty()) {
            throw new EntityDoesNotExistException(
                    String.format("Publication list=%s can not be created", publicationIds)
            );
        }
        loanRepository.createLoansByUserIdAndPublicationsList(userId, publicationIds);
    }

    @Override
    public void updateLoanStatusToOutById(Integer loanId){
        final int DEFAULT_TERM = 14;

        if (isNull(loanId)) {
            throw new EntityDoesNotExistException(String.format("Loan with ID=%s does not exist", loanId));
        }
        loanRepository.updateLoanStatusToOutById(loanId, LocalDate.now(), LocalDate.now().plusDays(DEFAULT_TERM));
    }

    @Override
    public void updateLoanStatusToReturnedById(Integer loanId){
        if (isNull(loanId)) {
            throw new EntityDoesNotExistException(String.format("Loan with ID=%s does not exist", loanId));
        }
        loanRepository.updateLoanStatusToReturnedById(loanId, LocalDate.now());
    }

    @Override
    public void deleteLoanById(Integer loanId){
        if (isNull(loanId)) {
            throw new EntityDoesNotExistException(String.format("Loan with ID=%s does not exist", loanId));
        }
        loanRepository.deleteLoanById(loanId);
    }
}