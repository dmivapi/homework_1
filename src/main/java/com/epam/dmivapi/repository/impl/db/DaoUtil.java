package com.epam.dmivapi.repository.impl.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoUtil {
    private static final String SQL_FIND_LANGUAGE_CODE = "SELECT id FROM languages WHERE code=?";
    public static Integer getLanguageId(Connection con, String languageCode) throws SQLException {
        Integer languageId = null;

        PreparedStatement pstmt = con.prepareStatement(SQL_FIND_LANGUAGE_CODE);
        pstmt.setString(1, languageCode);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next())
            languageId = rs.getInt(1);

        return languageId;
    }

    public static String escapeString(String userInput) {
        return userInput.replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_")
                .replace("[", "![");
    }
}
