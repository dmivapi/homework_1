package com.epam.dmivapi.converter;

import com.epam.dmivapi.dto.LoanDto;
import com.epam.dmivapi.dto.LoanDtoViewAll;
import com.epam.dmivapi.dto.LoanStatus;
import com.epam.dmivapi.model.Loan;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class LoanDtoViewAllConverter extends AbstractConverter<Loan, LoanDtoViewAll> {

    @Override
    public LoanDtoViewAll convertForList(Loan loan) {
        if (Objects.isNull(loan)) {
            throw new IllegalArgumentException("Can't convert loan to loanDtoViewAll, loan is  null");
        }

        LoanDtoViewAll loanDtoViewAll = new LoanDtoViewAll();
        loanDtoViewAll.setId(loan.getId());
        loanDtoViewAll.setDateOut(loan.getDateOut());
        loanDtoViewAll.setDueDate(loan.getDueDate());
        loanDtoViewAll.setDateIn(loan.getDateIn());
        loanDtoViewAll.setReadingRoom(loan.isReadingRoom());
        loanDtoViewAll.setStatus(LoanStatus.of(loanDtoViewAll));

        loanDtoViewAll.setLibCode(loan.getLibCode());
        loanDtoViewAll.setBookTitle(loan.getBookTitle());
        loanDtoViewAll.setBookAuthors(loan.getBookAuthors());
        loanDtoViewAll.setBookGenre(loan.getBookGenre());
        loanDtoViewAll.setBookPublisher(loan.getBookPublisher());
        loanDtoViewAll.setBookPublicationYear(loan.getBookPublicationYear());
        loanDtoViewAll.setPrice(loan.getPrice());

        loanDtoViewAll.setUserId(loan.getUserId());
        loanDtoViewAll.setEmail(loan.getEmail());
        loanDtoViewAll.setFirstName(loan.getFirstName());
        loanDtoViewAll.setLastName(loan.getLastName());
        loanDtoViewAll.setBlocked(loan.isBlocked());

        return loanDtoViewAll;
    }
}
