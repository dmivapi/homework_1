package com.epam.dmivapi.repository.impl;

import com.epam.dmivapi.ContextParam;
import com.epam.dmivapi.dto.LoanStatus;
import com.epam.dmivapi.repository.impl.db.DBManager;
import com.epam.dmivapi.repository.impl.db.EntityMapper;
import com.epam.dmivapi.model.Loan;
import com.epam.dmivapi.repository.LoanRepository;
import com.epam.dmivapi.timed.Timed;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static com.epam.dmivapi.repository.impl.db.DaoUtil.getLanguageId;

@Repository
@Timed
public class LoanRepositoryImpl implements LoanRepository {
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

    @Override
    public List<Loan> findAll(
            String genreLanguageCode,
            int currentPage,
            int recordsPerPage
    ){
        return findLoans(null, null, genreLanguageCode, currentPage, recordsPerPage);
    }

    @Override
    public List<Loan> findLoansByUserId(
            int userId,
            String genreLanguageCode,
            int currentPage,
            int recordsPerPage
    ) {
        return findLoans(null, userId, genreLanguageCode, currentPage, recordsPerPage);
    }

    @Override
    public int countLoans(String genreLanguageCode) {
        return countLoans(null, null, genreLanguageCode);
    }

    @Override
    public int countLoansByUserId(int userId, String genreLanguageCode) {
        return countLoans(null, userId, genreLanguageCode);
    }

    @Override
    public void createLoansByUserIdAndPublicationsList(int userId, List<Integer> publicationIds) {
        loanNew(userId, publicationIds.stream().mapToInt(i->i).toArray());
    }

    @Override
    public void updateLoanStatusToOutById(int loanId, LocalDate dateOut, LocalDate dueDate) {
        loanOut(loanId, dateOut, dueDate);
    }

    @Override
    public void updateLoanStatusToReturnedById(int loanId, LocalDate dateIn){
        loanIn(loanId, dateIn);
    }

    @Override
    public void deleteLoanById(int loanId) {
        loanRemove(loanId);
    }

// ------------------------------------------------------------------------------------------

    // generates SQL fragment for dates filtering based on status given in parameter
    private static String getSQLPredicateFromLoanStatus(LoanStatus status) {
        switch (status) {
            case NEW: return FLD_DATE_OUT + " IS NULL";
            case RETURNED: return FLD_DATE_IN + " IS NOT NULL";
            case OUT: return FLD_DATE_OUT + " IS NOT NULL AND " + FLD_DATE_IN  + " IS NULL";
            case OVERDUE: return getSQLPredicateFromLoanStatus(LoanStatus.OUT) +
                    " AND " + FLD_DATE_OUT + ">" + FLD_DUE_DATE;
            default:
                return "";
        }
    }

    // generates full SQL search string for loans search or counting
    private static String getSqlWithSearchCriteria(LoanStatus status,
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

    public static int countLoans(LoanStatus status, Integer userId, String languageId) {
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

    public static List<Loan> findLoans(
            LoanStatus status,
            Integer userId, String languageId,
            int currentPage,
            int recordsPerPage
    ) {
        List<Loan> loans = null;
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
            loans = new BookLoanMapper().mapRows(rs);
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }
        return loans;
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

                Integer bookCopyId = BookRepositoryImpl.findBookCopyIdByPublicationId(con, pubId);
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

        private static class BookLoanMapper extends EntityMapper<Loan> {
            @Override
            public Loan mapRow(ResultSet rs) throws SQLException {
                Loan loan = new Loan();
                loan.setId(rs.getInt(FLD_ID));
                loan.setUserId(rs.getInt(FLD_USER_ID));
                loan.setBookCopyId(rs.getInt(FLD_BOOK_COPY_ID));

                Date date = rs.getDate(FLD_DATE_OUT);
                loan.setDateOut(date == null ? null : date.toLocalDate());

                date = rs.getDate(FLD_DUE_DATE);
                loan.setDueDate(date == null ? null : date.toLocalDate());

                date = rs.getDate(FLD_DATE_IN);
                loan.setDateIn(date == null ? null : date.toLocalDate());

                loan.setReadingRoom(rs.getInt(FLD_READING_ROOM) !=0 );
                loan.setLibCode(rs.getString(FLD_LIB_CODE));
                loan.setBookTitle(rs.getString(FLD_TITLE));
                loan.setBookAuthors(rs.getString(FLD_AUTHORS));
                loan.setBookGenre(rs.getString(FLD_GENRE));
                loan.setBookPublisher(rs.getString(FLD_PUBLISHER));
                loan.setBookPublicationYear(rs.getInt(FLD_YEAR));
                loan.setPrice(rs.getInt(FLD_PRICE));
                loan.setEmail(rs.getString(FLD_EMAIL));
                loan.setFirstName(rs.getString(FLD_FIRST_NAME));
                loan.setLastName(rs.getString(FLD_LAST_NAME));
                loan.setBlocked(rs.getInt(FLD_BLOCKED) != 0 );
                
                return loan;
            }
        }
    }
