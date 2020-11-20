package com.epam.dmivapi.repository.impl;

import com.epam.dmivapi.repository.impl.db.DBManager;
import com.epam.dmivapi.repository.impl.db.DaoUtil;
import com.epam.dmivapi.repository.impl.db.EntityMapper;
import com.epam.dmivapi.model.Author;
import com.epam.dmivapi.model.Book;
import com.epam.dmivapi.model.Genre;
import com.epam.dmivapi.model.Publisher;
import com.epam.dmivapi.repository.BookRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.epam.dmivapi.repository.impl.db.DaoUtil.getLanguageId;

@Repository
public class BookRepositoryImpl implements BookRepository {
    private static final String SQL_FIND_BOOK_COPY = "SELECT id FROM view_available_book_copies WHERE publication_id=? LIMIT 1";
    private static final String SQL_COUNT_ALL_PUBLICATIONS = "SELECT COUNT(id) FROM view_book_authors_genre WHERE";
    private static final String SQL_FIND_ALL_PUBLICATIONS = "SELECT * FROM view_book_authors_genre WHERE";
    private static final String LANGUAGE_FRAGMENT = " language_id=?";
    private static final String TITLE_FRAGMENT = " title LIKE ?  ESCAPE '!'";
    private static final String AUTHOR_FRAGMENT = " authors LIKE ? ESCAPE '!'";
    private static final String ORDER_BY_FRAGMENT = " ORDER BY ";
    private static final String LIMIT_FRAGMENT = " LIMIT ?, ?";

    private static final Set<String> possibleSortFields = new HashSet<String>();
    static {
        possibleSortFields.add("title");
        possibleSortFields.add("authors");
        possibleSortFields.add("publisher");
        possibleSortFields.add("year");
    }

    @Override
    public List<Book> findBooksByTitleAndAuthor(
            String title, String author,
            String genreLanguageCode,
            String orderByField, boolean isAscending,
            int currentPage, int recordsPerPage
    ){
        return findBooks(genreLanguageCode, title, author, orderByField, isAscending, currentPage, recordsPerPage);
    }

    @Override
    public int countBooksByTitleAndAuthor(String title, String author, String genreLanguageCode) {
        return countBooks(genreLanguageCode, title, author);
    }

    @Override
    public void save(Book book, int authorId, int publisherId, int genreId, int year, int price, String languageCode, String libCodes[]) {
        createBook(book, authorId, publisherId, genreId, year, price, languageCode, libCodes);
    }

// --------------------------------------------------------------------------

    private static PreparedStatement fillPStatement(
            Connection con,
            String languageCode, String title, String author,
            String orderByField, boolean asc,
            int currentPage, int recordsPerPage,
            boolean countOnly) throws SQLException {

        StringBuilder sb = new StringBuilder(countOnly ?
                SQL_COUNT_ALL_PUBLICATIONS : SQL_FIND_ALL_PUBLICATIONS);

        boolean shouldSearchByTitle = title != null && !title.isEmpty();
        boolean shouldSearchByAuthors = author != null && !author.isEmpty();
        boolean shouldOrderBy = possibleSortFields.contains(orderByField);
        boolean shouldLimit = recordsPerPage != 0;

        sb.append(LANGUAGE_FRAGMENT)
                .append(shouldSearchByTitle || shouldSearchByAuthors ? " AND" : "")
                .append(shouldSearchByTitle ? TITLE_FRAGMENT : "")
                .append(shouldSearchByTitle && shouldSearchByAuthors ? " AND" : "")
                .append(shouldSearchByAuthors ? AUTHOR_FRAGMENT : "");

        if (!countOnly) { // do not need ordering to count records that fit criteria
            sb.append(shouldOrderBy ? ORDER_BY_FRAGMENT : "")
                    .append(shouldOrderBy ? orderByField : "")
                    .append(shouldOrderBy && !asc ? " DESC" : "")
                    .append(shouldLimit ? LIMIT_FRAGMENT : "");
        }

        PreparedStatement pstmt = con.prepareStatement(sb.toString());

        int k = 0;
        Integer langId = getLanguageId(con, languageCode);
        pstmt.setInt(++k, langId == null ? 1 : langId);
        if (shouldSearchByTitle)
            pstmt.setString(++k, "%" + DaoUtil.escapeString(title) + "%");

        if (shouldSearchByAuthors)
            pstmt.setString(++k,  "%" + DaoUtil.escapeString(author) + "%");

        if (!countOnly && shouldLimit) {
            pstmt.setInt(++k, (currentPage - 1) * recordsPerPage);
            pstmt.setInt(++k, recordsPerPage);
        }
        return pstmt;
    }

    // for pagination purposes
    private static int countBooks(String languageCode, String title, String author) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();

            pstmt = fillPStatement(con, languageCode, title, author,null, true,0, 0, true);

            rs = pstmt.executeQuery();
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }
        return 0; // something went wrong
    }

    private static List<Book> findBooks(String languageCode, String title, String author,
                                       String orderByField, boolean isAscending,
                                       int currentPage, int recordsPerPage
                                       ) {
        List<Book> bookList = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();

            pstmt = fillPStatement(con, languageCode, title, author, orderByField, isAscending,
                    currentPage, recordsPerPage,false
            );

            rs = pstmt.executeQuery();
            bookList = new BookMapper().mapRows(rs);
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }
        return bookList;
    }

    /**
    * Searches for first available book copy
    * for given publication (which was most likely
    * requested by reader)
    * @param con connection ready to be used for search
    * @param publicationId publication id to search by
    * @return book_copy.id or null if no record found
    */
    public static Integer findBookCopyIdByPublicationId(Connection con, int publicationId) throws SQLException {
        Integer bookCopyId = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        pstmt = con.prepareStatement(SQL_FIND_BOOK_COPY);
        pstmt.setInt(1, publicationId);
        rs = pstmt.executeQuery();
        try {
            if (rs.next())
                bookCopyId = rs.getInt(1);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        return bookCopyId;
    }

    public static List<Author> findAuthors() {
        final String SQL_FIND_ALL_AUTHORS ="SELECT * FROM author";

        List<Author> authors = null;
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_FIND_ALL_AUTHORS);
            authors = new AuthorMapper().mapRows(rs);
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }
        return authors;
    }

    public static List<Publisher> findPublishers() {
        final String SQL_FIND_ALL_PUBLISHERS ="SELECT * FROM publisher";

        List<Publisher> publisher = null;
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_FIND_ALL_PUBLISHERS);
            publisher = new PublisherMapper().mapRows(rs);
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }
        return publisher;
    }

    public static List<Genre> findGenres(String languageCode) {
        final String SQL_FIND_ALL_GENRES ="SELECT * FROM genre_translation WHERE language_id=?";

        List<Genre> genres = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_FIND_ALL_GENRES);
            pstmt.setInt(1, getLanguageId(con, languageCode));
            rs = pstmt.executeQuery();
            genres = new GenreMapper().mapRows(rs);
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }
        return genres;
    }

    public static boolean createBook(Book book, int authorId, int publisherId, int genreId, int year, int price, String languageCode, String libCodes[]) {
        Connection con = null;
        final String SQL_INSERT_BOOK = "INSERT INTO book (title) VALUES (?)";
        final String SQL_INSERT_BOOK_HAS_AUTHOR = "INSERT INTO book_has_author (author_id, book_id) VALUES (?, ?)";
        final String SQL_INSERT_BOOK_HAS_GENRE = "INSERT INTO book_has_genre_translation (book_id, language_id, genre_id) VALUES (?, ?, ?)";
        final String SQL_INSERT_PUBLICATION = "INSERT INTO publication (book_id, publisher_id, year, price) VALUES (?, ?, ?, ?)";
        final String SQL_INSERT_BOOK_COPY = "INSERT INTO book_copy (lib_code, publication_id) VALUES (?, ?)";
        boolean result = false;

        try {
            con = DBManager.getInstance().getConnection();
            con.setAutoCommit(false);

//---------
            PreparedStatement pstmt = con.prepareStatement(SQL_INSERT_BOOK, Statement.RETURN_GENERATED_KEYS);
            int k = 1;
            pstmt.setString(k++, book.getTitle());
            if (pstmt.executeUpdate() != 1)
                throw new SQLException();

            ResultSet gk = pstmt.getGeneratedKeys();
            if(gk.next())
                book.setId(gk.getInt(1));
//---------
            pstmt = con.prepareStatement(SQL_INSERT_BOOK_HAS_AUTHOR);

            k = 1;
            pstmt.setInt(k++, authorId);
            pstmt.setInt(k++, book.getId());

            if (pstmt.executeUpdate() != 1)
                throw new SQLException();
//---------
            pstmt = con.prepareStatement(SQL_INSERT_BOOK_HAS_GENRE);

            k = 1;
            pstmt.setInt(k++, book.getId());
            pstmt.setInt(k++, DaoUtil.getLanguageId(con, languageCode));
            pstmt.setInt(k++, genreId);

            if (pstmt.executeUpdate() != 1)
                throw new SQLException();

//---------
            pstmt = con.prepareStatement(SQL_INSERT_PUBLICATION, Statement.RETURN_GENERATED_KEYS);
            k = 1;
            pstmt.setInt(k++, book.getId());
            pstmt.setInt(k++, publisherId);
            pstmt.setInt(k++, year);
            pstmt.setInt(k++, price);

            if (pstmt.executeUpdate() != 1)
                throw new SQLException();

            gk = pstmt.getGeneratedKeys();
            int publicationId;
            if(gk.next()) {
                publicationId = gk.getInt(1);
            } else {
                throw new SQLException();
            }
//---------
            pstmt = con.prepareStatement(SQL_INSERT_BOOK_COPY);
            for (String libCode : libCodes) {
                k = 1;
                pstmt.setString(k++, libCode);
                pstmt.setInt(k++, publicationId);
                pstmt.addBatch();
            }

            for (int res : pstmt.executeBatch()) {
                if (res != 1)
                    throw new SQLException();
            }
//---------
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

    public static boolean bookRemove(int[] publicationIds) {
        final String SQL_BOOK_REMOVE = "DELETE FROM publication WHERE id=?";
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            con.setAutoCommit(false);
            PreparedStatement pstmt = con.prepareStatement(SQL_BOOK_REMOVE);

            for (int publicationId : publicationIds) {
                pstmt.setInt(1, publicationId);
                pstmt.addBatch();
            }

            pstmt.executeBatch();
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

    private static class AuthorMapper extends EntityMapper<Author> {
        private static final String FLD_ID = "id";
        private static final String FLD_FIRST_NAME = "first_name";
        private static final String FLD_LAST_NAME = "last_name";

        @Override
        public Author mapRow(ResultSet rs) throws SQLException {
            Author author = new Author();
            author.setId(rs.getInt(FLD_ID));
            author.setFirstName(rs.getString(FLD_FIRST_NAME));
            author.setLastName(rs.getString(FLD_LAST_NAME));
            return author;
        }
    }

    private static class PublisherMapper extends EntityMapper<Publisher> {
        private static final String FLD_ID = "id";
        private static final String FLD_NAME = "name";

        @Override
        public Publisher mapRow(ResultSet rs) throws SQLException {
            Publisher publisher = new Publisher();
            publisher.setId(rs.getInt(FLD_ID));
            publisher.setName(rs.getString(FLD_NAME));
            return publisher;
        }
    }

    private static class GenreMapper extends EntityMapper<Genre> {
        private static final String FLD_ID = "id";
        private static final String FLD_GENRE_NAME = "name";

        @Override
        public Genre mapRow(ResultSet rs) throws SQLException {
            Genre genre = new Genre();
            genre.setId(rs.getInt(FLD_ID));
            genre.setName(rs.getString(FLD_GENRE_NAME));
            return genre;
        }
    }

    private static class BookMapper extends EntityMapper<Book> {
        private static final String FLD_ID = "id";
        private static final String FLD_TITLE = "title";
        private static final String FLD_AUTHORS = "authors";
        private static final String FLD_GENRE = "genre";
        private static final String FLD_PUBLISHER = "publisher";
        private static final String FLD_YEAR = "year";
        private static final String FLD_PRICE = "price";

        @Override
        public Book mapRow(ResultSet rs) throws SQLException {
            Book book = new Book();
            book.setId(rs.getInt(FLD_ID));
            book.setTitle(rs.getString(FLD_TITLE));
            book.setAuthors(rs.getString(FLD_AUTHORS));
            book.setGenre(rs.getString(FLD_GENRE));
            book.setPublisher(rs.getString(FLD_PUBLISHER));
            book.setYear(rs.getInt(FLD_YEAR));
            book.setPrice(rs.getInt(FLD_PRICE));
            return book;
        }
    }
}
