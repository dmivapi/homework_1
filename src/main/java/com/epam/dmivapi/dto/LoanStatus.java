package com.epam.dmivapi.dto;

import java.io.Serializable;
import java.time.LocalDate;

public enum LoanStatus  implements Serializable {
    NEW, OUT, OVERDUE, RETURNED;

    public static LoanStatus of(LoanDto loanDto) {
        return getStatus(loanDto.getDateOut(), loanDto.getDueDate(), loanDto.getDateIn());
    }

    public static LoanStatus of(LoanDtoViewAll loanDtoViewAll) {
        return getStatus(loanDtoViewAll.getDateOut(), loanDtoViewAll.getDueDate(), loanDtoViewAll.getDateIn());
    }

    private static LoanStatus getStatus(LocalDate dateOut, LocalDate dueDate, LocalDate dateIn) {
        if (dateOut == null)
            return NEW;

        if (dateIn != null)
            return RETURNED;

        if (LocalDate.now().isAfter(dueDate))
            return OVERDUE;

        return OUT;
    }
}
