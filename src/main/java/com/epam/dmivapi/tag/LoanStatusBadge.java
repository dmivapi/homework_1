package com.epam.dmivapi.tag;

import com.epam.dmivapi.dto.LoanDtoViewAll;
import com.epam.dmivapi.dto.LoanStatus;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;

/**
 * Custom tag hadnler class provides html string (using bootstrap
 * classes) which shows the loan's state and if overdue -
 * it shows fine for overdue (which is book's price divided by
 * the number of days in a month and multiplied by the number of
 * days after the due date)
 */
public class LoanStatusBadge extends SimpleTagSupport {
    private LoanStatus loanStatus;
    private boolean readingRoom;
    private LocalDate dueDate;
    private int price;


    public LoanStatus getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
    }

    public boolean isReadingRoom() {
        return readingRoom;
    }

    public void setReadingRoom(boolean readingRoom) {
        this.readingRoom = readingRoom;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public void doTag() throws JspException, IOException {
        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();
        StringBuilder sb = new StringBuilder("<span class=\"badge badge-");

        switch (loanStatus){
            case NEW:
                sb.append("primary\">New"); // TODO status localization
                break;
            case RETURNED:
                sb.append("secondary\">Returned");
                break;
            case OUT:
                sb.append(isReadingRoom() ?
                    "success\">Reading room" :
                        ("info\">Out till:" + getDueDate())
                );
                break;
            case OVERDUE:
                final int maxDaysForFine = 30;
                int overDue = Period.between(getDueDate(), LocalDate.now()).getDays();
                int effectiveOverDue = Math.min(overDue, maxDaysForFine);
                sb.append("warning\">Overdue:"+overDue+"day" + (overDue > 1 ? "s" : "")
                        +"(Fine:" +
                        effectiveOverDue* getPrice()/maxDaysForFine +"UAH)");
                break;
        }
        out.println(sb.append("</span>"));
    }
}
