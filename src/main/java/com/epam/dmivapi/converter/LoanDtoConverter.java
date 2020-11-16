package com.epam.dmivapi.converter;

import com.epam.dmivapi.dto.LoanDto;
import com.epam.dmivapi.dto.LoanStatus;
import com.epam.dmivapi.model.Loan;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class LoanDtoConverter extends AbstractConverter<Loan, LoanDto> {
    @Override
    public LoanDto convertForList(Loan loan) {
        if (Objects.isNull(loan)) {
            throw new IllegalArgumentException("Can't convert loan to loanDto, loan is  null");
        }

        LoanDto loanDto = new LoanDto();
        loanDto.setId(loan.getId());
        loanDto.setDateOut(loan.getDateOut());
        loanDto.setDueDate(loan.getDueDate());
        loanDto.setDateIn(loan.getDateIn());
        loanDto.setReadingRoom(loan.isReadingRoom());
        loanDto.setStatus(LoanStatus.of(loanDto));

        loanDto.setLibCode(loan.getLibCode());
        loanDto.setBookTitle(loan.getBookTitle());
        loanDto.setBookAuthors(loan.getBookAuthors());
        loanDto.setBookGenre(loan.getBookGenre());
        loanDto.setBookPublisher(loan.getBookPublisher());
        loanDto.setBookPublicationYear(loan.getBookPublicationYear());
        loanDto.setPrice(loan.getPrice());

        loanDto.setUserId(loan.getUserId());
        loanDto.setBlocked(loan.isBlocked());

        return loanDto;
    }
}
