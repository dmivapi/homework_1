package com.epam.dmivapi.db.dao;

import com.epam.dmivapi.db.DBManager;
import com.epam.dmivapi.db.EntityMapper;
import com.epam.dmivapi.db.bean.LoanBean;
import com.epam.dmivapi.db.entity.Loan;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static com.epam.dmivapi.db.DaoUtil.getLanguageId;

public class LoanDao {
    private static final String FLD_ID = "id";
    private static final String FLD_USER_ID = "user_id";
    private static final String FLD_BOOK_COPY_ID = "book_copy_id";
    private static final String FLD_DATE_OUT = "date_out";
    private static final String FLD_DUE_DATE = "due_date";
    private static final String FLD_DATE_IN = "date_in";
    private static final String FLD_READING_ROOM = "reading_room";
    private static final String FLD_LIB_CODE = "lib_code";
    private static final String FLD_TITLE = "title";
    private static final String FLD_AUTHORS = "authors";
    private static final String FLD_GENRE = "genre";
    private static final String FLD_PUBLISHER = "publisher";
    private static final String FLD_YEAR = "year";
    private static final String FLD_PRICE = "price";
    private static final String FLD_EMAIL = "email";
    private static final String FLD_FIRST_NAME = "first_name";
    private static final String FLD_LAST_NAME = "last_name";
    private static final String FLD_BLOCKED = "blocked";

    private static final String SQL_FIND_ALL_LOANS = "SELECT * FROM view_book_user_loans WHERE language_id=?";
    private static final String SQL_COUNT_ALL_LOANS = "SELECT COUNT(id) FROM view_book_user_loans WHERE language_id=?";
    private static final String LIMIT_FRAGMENT = " LIMIT ?, ?";

    private static final String SQL_INSERT_LOAN =
            "INSERT INTO book_loan (user_id, book_copy_id) VALUES (?, ?)";

    // using templates allows to add several items to one statement
    private static final String SQL_LOAN_OUT =
            "UPDATE book_loan SET " + FLD_DATE_OUT + "=?, " + FLD_DUE_DATE + "=? WHERE " + FLD_ID + "=?";
    private static final String SQL_LOAN_IN =
            "UPDATE book_loan SET " + FLD_DATE_IN + "=? WHERE " + FLD_ID + "=?";
    private static final String SQL_LOAN_REMOVE =
            "DELETE FROM book_loan WHERE " + FLD_ID + "=?";

    private static final String SQL_BOOK_COPY_ID_ITEM = " book_copy_id=?"; // can be repeated during appending

    // generates SQL fragment for dates filtering based on status given in parameter
    private static String getSQLPredicateFromLoanStatus(Loan.LoanStatus status) {
        switch (status) {
            case NEW: return FLD_DATE_OUT + " IS NULL";
            case RETURNED: return FLD_DATE_IN + " IS NOT NULL";
            case OUT: return FLD_DATE_OUT + " IS NOT NULL AND " + FLD_DATE_IN  + " IS NULL";
            case OVERDUE: return getSQLPredicateFromLoanStatus(Loan.LoanStatus.OUT) +
                    " AND " + FLD_DATE_OUT + ">" + FLD_DUE_DATE;
            default:
                return "";
        }
    }

    // generates full SQL search string for loans search or counting
    private static String getSqlWithSearchCriteria(Loan.LoanStatus status,
                                                   Integer userId,
                                                   int currentPage, int recordsPerPage,
                                                   boolean countOnly
    ) {
        StringBuilder sb = new StringBuilder(countOnly ?
                SQL_COUNT_ALL_LOANS : SQL_FIND_ALL_LOANS);
        boolean shouldLimit = recordsPerPage != 0;

        if (status != null || userId != null)
            sb.append(" AND");

        if (status != null) {
            sb.append("(").append(getSQLPredicateFromLoanStatus(status)).append(")");
            if (userId != null)
                sb.append(" AND");
        }

        if (userId != null)
            sb.append(" ").append(FLD_USER_ID).append("=?");

        if (!countOnly) { // do not need ordering to count records that fit criteria
            sb.append(shouldLimit ? LIMIT_FRAGMENT : "");
        }

        return sb.toString();
    }

    public static int countLoans(Loan.LoanStatus status, Integer userId, String languageId) {
        List<LoanBean> bookLoanList = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBManager.getInstance().getConnection();

            pstmt = con.prepareStatement(
                    getSqlWithSearchCriteria(status, userId, 0, 0, true)
            );

            int k = 1;
            pstmt.setInt(k++, getLanguageId(con, languageId));
            if (userId != null)
                pstmt.setInt(k++, userId);

            rs = pstmt.executeQuery();
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }
        return 0;
    }

    public static List<LoanBean> findLoans(Loan.LoanStatus status, Integer userId, String languageId,
                                           int currentPage, int recordsPerPage) {
        List<LoanBean> bookLoanList = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean shouldLimit = recordsPerPage != 0;

        try {
            con = DBManager.getInstance().getConnection();

            pstmt = con.prepareStatement(
                    getSqlWithSearchCriteria(status, userId,
                            currentPage, recordsPerPage,
                            false)
            );

            int k = 1;
            pstmt.setInt(k++, getLanguageId(con, languageId));
            if (userId != null)
                pstmt.setInt(k++, userId);
            if (shouldLimit) {
                pstmt.setInt(k++, (currentPage - 1) * recordsPerPage);
                pstmt.setInt(k++, recordsPerPage);
            }

            rs = pstmt.executeQuery();
            bookLoanList = new BookLoanMapper().mapRows(rs);
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }
        return bookLoanList;
    }

    public static boolean loanNew(int userId, int[] publicationIds) {
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            con.setAutoCommit(false);
            PreparedStatement pstmt = con.prepareStatement(SQL_INSERT_LOAN);

            for (int pubId : publicationIds) {
                int k = 1;
                pstmt.setInt(k++, userId);

                Integer bookCopyId = BookDao.findBookCopyIdByPublication(con, pubId);
                if (bookCopyId == null)
                    return false;

                pstmt.setInt(k++, bookCopyId);
                pstmt.addBatch();
            }
            int[] affectedRecords = pstmt.executeBatch();
            pstmt.close();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
            return false;
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }

        return true;
    }

    public static boolean loanOut(int loanId, LocalDate dateOut, LocalDate dueDate) {
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            con.setAutoCommit(false);
            PreparedStatement pstmt = con.prepareStatement(SQL_LOAN_OUT);

            int k = 1;
            pstmt.setDate(k++, Date.valueOf(dateOut));
            pstmt.setDate(k++, Date.valueOf(dueDate));
            pstmt.setInt(k++, loanId);

            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
            return false;
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }

        return true;
    }

    public static boolean loanIn(int loanId, LocalDate dateIn) {
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            con.setAutoCommit(false);
            PreparedStatement pstmt = con.prepareStatement(SQL_LOAN_IN);

            int k = 1;
            pstmt.setDate(k++, Date.valueOf(dateIn));
            pstmt.setInt(k++, loanId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
            return false;
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }
        return true;
    }

    public static boolean loanRemove(int loanId) {
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            con.setAutoCommit(false);
            PreparedStatement pstmt = con.prepareStatement(SQL_LOAN_REMOVE);

            int k = 1;
            pstmt.setInt(k++, loanId);

            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
            return false;
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }
        return true;
    }

        private static class BookLoanMapper extends EntityMapper<LoanBean> {
            @Override
            public LoanBean mapRow(ResultSet rs) throws SQLException {
                LoanBean loanBean = new LoanBean();
                loanBean.setId(rs.getInt(FLD_ID));
                loanBean.setUserId(rs.getInt(FLD_USER_ID));
                loanBean.setBookCopyId(rs.getInt(FLD_BOOK_COPY_ID));

                Date date = rs.getDate(FLD_DATE_OUT);
                loanBean.setDateOut(date == null ? null : date.toLocalDate());

                date = rs.getDate(FLD_DUE_DATE);
                loanBean.setDueDate(date == null ? null : date.toLocalDate());

                date = rs.getDate(FLD_DATE_IN);
                loanBean.setDateIn(date == null ? null : date.toLocalDate());

                loanBean.setReadingRoom(rs.getInt(FLD_READING_ROOM) !=0 );
                loanBean.setLibCode(rs.getString(FLD_LIB_CODE));
                loanBean.setBookTitle(rs.getString(FLD_TITLE));
                loanBean.setBookAuthors(rs.getString(FLD_AUTHORS));
                loanBean.setBookGenre(rs.getString(FLD_GENRE));
                loanBean.setBookPublisher(rs.getString(FLD_PUBLISHER));
                loanBean.setBookPublicationYear(rs.getInt(FLD_YEAR));
                loanBean.setPrice(rs.getInt(FLD_PRICE));
                loanBean.setEmail(rs.getString(FLD_EMAIL));
                loanBean.setFirstName(rs.getString(FLD_FIRST_NAME));
                loanBean.setLastName(rs.getString(FLD_LAST_NAME));
                loanBean.setBlocked(rs.getInt(FLD_BLOCKED) !=0 );
                
                return loanBean;
            }
        }
    }
