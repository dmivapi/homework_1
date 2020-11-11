package com.epam.dmivapi.web.tag;

import com.epam.dmivapi.db.bean.LoanBean;
import com.epam.dmivapi.db.entity.Loan;

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
    private LoanBean loanBean;

    public LoanBean getLoanBean() {
        return loanBean;
    }

    public void setLoanBean(LoanBean loanBean) {
        this.loanBean = loanBean;
    }

    @Override
    public void doTag() throws JspException, IOException {
        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();
        StringBuilder sb = new StringBuilder("<span class=\"badge badge-");

        switch (loanBean.getStatus()){
            case NEW:
                sb.append("primary\">New"); // TODO status localization
                break;
            case RETURNED:
                sb.append("secondary\">Returned");
                break;
            case OUT:
                sb.append(loanBean.isReadingRoom() ?
                    "success\">Reading room" :
                        ("info\">Out till:" + loanBean.getDueDate())
                );
                break;
            case OVERDUE:
                final int maxDaysForFine = 30;
                int overDue = Period.between(loanBean.getDueDate(), LocalDate.now()).getDays();
                int effectiveOverDue = Math.min(overDue, maxDaysForFine);
                sb.append("warning\">Overdue:"+overDue+"day" + (overDue > 1 ? "s" : "")
                        +"(Fine:" +
                        effectiveOverDue*loanBean.getPrice()/maxDaysForFine +"UAH)");
                break;
        }
        out.println(sb.append("</span>"));
    }
}
