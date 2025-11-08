package StudentManagement;

import java.sql.*;
import javax.swing.*;

public class DatabaseHelper {

    private static Connection conn;

    public static Connection connect() {
        try {
            if (conn != null && !conn.isClosed()) {
                return conn;
            }

            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:students.db");

            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS students (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT, age INTEGER, gender TEXT, course TEXT, email TEXT, phone TEXT)");
            stmt.close();

            System.out.println("✅ Database connected successfully!");
            return conn;

        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "❌ SQLite JDBC driver not found.\nEnsure sqlite-jdbc-3.51.0.0.jar is in classpath.",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            return null;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Unable to connect to database:\n" + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
