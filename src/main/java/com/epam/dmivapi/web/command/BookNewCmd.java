package com.epam.dmivapi.web.command;

import com.epam.dmivapi.db.dao.BookDao;
import com.epam.dmivapi.db.dao.UserDao;
import com.epam.dmivapi.db.entity.Book;
import com.epam.dmivapi.db.entity.User;
import com.epam.dmivapi.db.entity.User.Role;
import com.epam.dmivapi.web.ContextParam;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.epam.dmivapi.web.ContextParam.getCurrentUserRole;
import static com.epam.dmivapi.web.ContextParam.getErrorPageWithMessage;

public class BookNewCmd extends AbstractCmd {
    private static final Logger log = Logger.getLogger(BookNewCmd.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("BookNewCmd starts");

        Book book = new Book();

        book.setTitle(request.getParameter(ContextParam.BK_TITLE));

        int authorId = Integer.parseInt(request.getParameter(ContextParam.BK_AUTHOR));
        int publisherId = Integer.parseInt(request.getParameter(ContextParam.BK_PUBLISHER));
        int genreId = Integer.parseInt(request.getParameter(ContextParam.BK_GENRE));
        int year = Integer.parseInt(request.getParameter(ContextParam.BK_YEAR));
        int price = Integer.parseInt(request.getParameter(ContextParam.BK_PRICE));
        String libCodeBase = request.getParameter(ContextParam.BK_LIB_CODE_BASE);
        int quantity = Integer.parseInt(request.getParameter(ContextParam.BK_QUANTITY));

        if (!BookDao.createBook(book, authorId, publisherId, genreId, year, price,
                ContextParam.getCurrentLocale(request.getSession()),
                generateLibCodes(libCodeBase, quantity))
        )
                return getErrorPageWithMessage(request,"Something went wrong: book creation failed");

        log.debug("BookNewCmd finished");
        return Command.LIST_BOOKS.getPath();
    }

    String[] generateLibCodes(String base, int quantity) {
        String[] codes = new String[quantity];
        for (int i = 0; i < quantity; i++) {
            codes[i] = base + "." + i;
        }
        return codes;
    }
}