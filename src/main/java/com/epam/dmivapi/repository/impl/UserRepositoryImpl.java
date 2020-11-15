package com.epam.dmivapi.repository.impl;

import com.epam.dmivapi.dto.Role;
import com.epam.dmivapi.repository.impl.db.DBManager;
import com.epam.dmivapi.repository.impl.db.EntityMapper;
import com.epam.dmivapi.model.User;

import java.sql.*;
import java.util.List;

import com.epam.dmivapi.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private static final Logger log = Logger.getLogger(UserRepositoryImpl.class);

    private static final String FLD_ID = "id";
    private static final String FLD_EMAIL = "email";
    private static final String FLD_PASSWORD = "password";
    private static final String FLD_FIRST_NAME = "first_name";
    private static final String FLD_LAST_NAME = "last_name";
    private static final String FLD_LOCALE_NAME = "locale_name";
    private static final String FLD_USER_ROLE = "user_role";
    private static final String FLD_BLOCKED = "blocked";

    private static final String SQL__FIND_USER_BY_LOGIN ="SELECT * FROM user WHERE email=?";
    private static final String SQL__FIND_ALL_USERS ="SELECT * FROM user";
    private static final String SQL__COUNT_ALL_USERS ="SELECT COUNT(id) FROM user";
    private static final String LIMIT_FRAGMENT = " LIMIT ?, ?";

    private static final String SQL_NEW_USER = "INSERT INTO user ("
                    + FLD_EMAIL + ", " + FLD_PASSWORD + ", " + FLD_FIRST_NAME + ", "
                    + FLD_LAST_NAME + ", " + FLD_USER_ROLE + ") VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE_USER_INFO =
            "UPDATE user SET "+ FLD_PASSWORD + "=?, " + FLD_FIRST_NAME + "=?, "
                    + FLD_LAST_NAME + "=? WHERE " + FLD_ID + "=?";

    private static final String SQL_UPDATE_USER_BLOCKED_STATUS =
            "UPDATE user SET " + FLD_BLOCKED + "=?	WHERE " + FLD_ID + "=?";

    private static final String SQL_DELETE_USER =
            "DELETE FROM user WHERE " + FLD_ID + "=?";

    // -------------------------------------------------------------------------
    @Override
    public List<User> findUsersByRole(Role role, int currentPage, int recordsPerPage) {
        return findUsers(role, null, currentPage, recordsPerPage);
    }

    @Override
    public int countUsersByRole(Role role) {
        return countUsers(role, null);
    }
    // -------------------------------------------------------------------------

    private static String getSqlWithCriteria(Role role, Boolean isBlocked,
                                             int recordsPerPage,
                                             boolean countOnly) {
        StringBuilder sb = new StringBuilder(countOnly ?
                SQL__COUNT_ALL_USERS : SQL__FIND_ALL_USERS);

        boolean shouldLimit = !countOnly && recordsPerPage != 0;

        if (role != null || isBlocked != null)
            sb.append(" WHERE");

        if (role != null) {
            sb.append(" ").append(FLD_USER_ROLE).append("=?");
            if (isBlocked != null)
                sb.append(" AND");
        }

        if (isBlocked != null)
            sb.append(" ").append(FLD_BLOCKED).append("=?");

        if (!countOnly) { // do not need ordering to count records that fit criteria
            sb.append(shouldLimit ? LIMIT_FRAGMENT : "");
        }

        return sb.toString();
    }

    public static int countUsers(Role role, Boolean isBlocked) {
        List<User> userList = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBManager.getInstance().getConnection();

            pstmt = con.prepareStatement(
                    getSqlWithCriteria(role, isBlocked,0,true)
            );

            int k = 1;
            if (role != null)
                pstmt.setString(k++, role.name().toLowerCase());
            if (isBlocked != null)
                pstmt.setInt(k++, isBlocked ? 1 : 0);

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
    public static List<User> findUsers(Role role,
                                       Boolean blocked,
                                       int currentPage,
                                       int recordsPerPage) {
        List<User> userList = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean shouldLimit = recordsPerPage != 0;

        try {
            con = DBManager.getInstance().getConnection();

            pstmt = con.prepareStatement(
                    getSqlWithCriteria(role, blocked, recordsPerPage,false)
            );

            int k = 1;
            if (role != null)
                pstmt.setString(k++, role.name().toLowerCase());
            if (blocked != null)
                pstmt.setInt(k++, blocked ? 1 : 0);
            if (shouldLimit) {
                pstmt.setInt(k++, (currentPage - 1) * recordsPerPage);
                pstmt.setInt(k++, recordsPerPage);
            }

            rs = pstmt.executeQuery();
            userList = new UserMapper().mapRows(rs);
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }
        return userList;
    }

    public static User findUserByLogin(String login) {
        User user = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            UserMapper mapper = new UserMapper();
            pstmt = con.prepareStatement(SQL__FIND_USER_BY_LOGIN);
            pstmt.setString(1, login);
            rs = pstmt.executeQuery();
            if (rs.next())
                user = mapper.mapRow(rs);
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }
        return user;
    }

    public static boolean updateUserBlockStatus(int userId, boolean isBlocked) {
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            PreparedStatement pstmt = con.prepareStatement(SQL_UPDATE_USER_BLOCKED_STATUS);

            int k = 1;
            pstmt.setInt(k++, isBlocked ? 1 : 0);
            pstmt.setInt(k++, userId);

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

    public static boolean removeUser(int userId) {
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            PreparedStatement pstmt = con.prepareStatement(SQL_DELETE_USER);

            int k = 1;
            pstmt.setInt(k++, userId);

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

    /**
     * @param user user.id is modified with db-generated value
     * in case this is a new record creation
     */
    public static boolean updateUser(User user, boolean isNew) {
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            writeUser(con, user, isNew);
            return true;
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
            return false;
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }
    }

    private static void writeUser(Connection con, User user, boolean isNew) throws SQLException {
        String sqlString = isNew ? SQL_NEW_USER : SQL_UPDATE_USER_INFO;

        PreparedStatement pstmt = isNew ?
                con.prepareStatement(sqlString, new String[] {FLD_ID}) :
                con.prepareStatement(sqlString);

        int k = 1;
        if (isNew)
            pstmt.setString(k++, user.getEmail());
        pstmt.setString(k++, user.getPassword());
        pstmt.setString(k++, user.getFirstName());
        pstmt.setString(k++, user.getLastName());
        pstmt.setString(k++, user.getUserRole().toString().toLowerCase());

        if (!isNew)
            pstmt.setInt(k++, user.getId());
        pstmt.executeUpdate();

        if (isNew) { // get user Id generated by DB
            ResultSet gk = pstmt.getGeneratedKeys();
            if(gk.next())
                user.setId(gk.getInt(1));
        }
        pstmt.close();
    }

    private static class UserMapper extends EntityMapper<User> {

        @Override
        public User mapRow(ResultSet rs) {
            try {
                User user = new User();
                user.setId(rs.getInt(FLD_ID));
                user.setEmail(rs.getString(FLD_EMAIL));
                user.setPassword(rs.getString(FLD_PASSWORD));
                user.setFirstName(rs.getString(FLD_FIRST_NAME));
                user.setLastName(rs.getString(FLD_LAST_NAME));
                user.setLocaleName(rs.getString(FLD_LOCALE_NAME));
                user.setUserRole(Role.valueOf(rs.getString(FLD_USER_ROLE).toUpperCase()));
                user.setBlocked(rs.getInt(FLD_BLOCKED) == 1);
                return user;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
