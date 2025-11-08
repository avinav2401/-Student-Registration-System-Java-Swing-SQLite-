package StudentManagement;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TeacherDashboard extends JFrame {

    private DefaultTableModel model;
    private JTable table;

    public TeacherDashboard() {
        setTitle("👨‍🏫 Teacher Dashboard");
        setSize(900, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table model
        model = new DefaultTableModel(
                new String[]{"ID", "Name", "Age", "Gender", "Course", "Email", "Phone"}, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        table.setSelectionBackground(new Color(0, 123, 255));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(52, 152, 219));
        table.getTableHeader().setForeground(Color.WHITE);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Load data
        loadStudents();

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton btnDelete = createStyledButton("🗑 Delete Selected", new Color(220, 53, 69));
        JButton btnRefresh = createStyledButton("🔄 Refresh", new Color(52, 152, 219));
        JButton btnSearch = createStyledButton("🔍 Search", new Color(40, 167, 69));

        btnDelete.addActionListener(e -> deleteStudent());
        btnRefresh.addActionListener(e -> loadStudents());
        btnSearch.addActionListener(e -> searchStudent());

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnDelete);

        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    // ✅ Load all students
    private void loadStudents() {
        model.setRowCount(0);
        Connection conn = DatabaseHelper.connect();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "❌ Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"), rs.getString("name"), rs.getInt("age"),
                        rs.getString("gender"), rs.getString("course"),
                        rs.getString("email"), rs.getString("phone")
                });
            }
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Error loading data:\n" + e.getMessage());
        }
    }

    // ✅ Delete selected student
    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Please select a student to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this student?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        int id = (int) model.getValueAt(row, 0);
        Connection conn = DatabaseHelper.connect();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "❌ Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (PreparedStatement pst = conn.prepareStatement("DELETE FROM students WHERE id = ?")) {
            pst.setInt(1, id);
            pst.executeUpdate();
            model.removeRow(row);
            JOptionPane.showMessageDialog(this, "✅ Student deleted successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Error deleting student:\n" + e.getMessage());
        }
    }

    // ✅ Search by name or course
    private void searchStudent() {
        String keyword = JOptionPane.showInputDialog(this, "Enter name or course to search:");
        if (keyword == null || keyword.trim().isEmpty()) return;

        model.setRowCount(0);
        Connection conn = DatabaseHelper.connect();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "❌ Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (PreparedStatement pst = conn.prepareStatement(
                "SELECT * FROM students WHERE name LIKE ? OR course LIKE ?")) {
            pst.setString(1, "%" + keyword + "%");
            pst.setString(2, "%" + keyword + "%");
            ResultSet rs = pst.executeQuery();
            boolean found = false;
            while (rs.next()) {
                found = true;
                model.addRow(new Object[]{
                        rs.getInt("id"), rs.getString("name"), rs.getInt("age"),
                        rs.getString("gender"), rs.getString("course"),
                        rs.getString("email"), rs.getString("phone")
                });
            }
            rs.close();
            if (!found) {
                JOptionPane.showMessageDialog(this, "No results found for \"" + keyword + "\"");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Error searching:\n" + e.getMessage());
        }
    }

    // 🎨 Styled button helper
    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(color.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(color); }
        });
        return btn;
    }
}
