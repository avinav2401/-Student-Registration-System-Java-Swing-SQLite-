package StudentManagement;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import javax.swing.*;

public class StudentForm extends JFrame {

    private JTextField txtName, txtAge, txtEmail, txtPhone;
    private JComboBox<String> comboGender, comboCourse;

    public StudentForm() {
        setTitle("👨‍🎓 Student Registration");
        setSize(600, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🌈 Gradient Background
        JPanel bg = createGradientPanel();
        bg.setLayout(new BorderLayout());
        add(bg);

        // 🔙 Top bar (Back + Title)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        JButton btnBack = createBackButton(); // black arrow, no background
        JLabel title = new JLabel("Student Registration Form", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        topPanel.add(btnBack, BorderLayout.WEST);
        topPanel.add(title, BorderLayout.CENTER);
        bg.add(topPanel, BorderLayout.NORTH);

        // 🧾 Form panel
        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));

        form.add(createLabel("Name:")); txtName = new JTextField(); form.add(txtName);
        form.add(createLabel("Age:")); txtAge = new JTextField(); form.add(txtAge);
        form.add(createLabel("Gender:")); comboGender = new JComboBox<>(new String[]{"Select","Male","Female","Other"}); form.add(comboGender);
        form.add(createLabel("Course:")); comboCourse = new JComboBox<>(new String[]{"Select","B.Tech","BCA","MCA","B.Sc","M.Sc"}); form.add(comboCourse);
        form.add(createLabel("Email:")); txtEmail = new JTextField(); form.add(txtEmail);
        form.add(createLabel("Phone:")); txtPhone = new JTextField(); form.add(txtPhone);

        bg.add(form, BorderLayout.CENTER);

        // 🎛 Bottom Buttons (Register + Clear)
        JButton btnRegister = createRoundedButton("Register", new Color(0, 123, 255));
        JButton btnClear = createRoundedButton("Clear", new Color(255, 193, 7));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 15));
        bottom.setOpaque(false);
        bottom.add(btnRegister);
        bottom.add(btnClear);
        bg.add(bottom, BorderLayout.SOUTH);

        // 🖱 Actions
        btnRegister.addActionListener(e -> registerStudent());
        btnClear.addActionListener(e -> clearFields());
        btnBack.addActionListener(e -> {
            dispose();
            new RoleSelection();
        });

        setVisible(true);
    }

    // ✅ Database Handling
    private void registerStudent() {
        Connection conn = DatabaseHelper.connect();
        if (conn == null) {
            JOptionPane.showMessageDialog(this,
                    "❌ Database connection failed.\nCheck sqlite-jdbc-3.51.0.0.jar and DatabaseHelper.java.",
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (txtName.getText().isEmpty() || txtAge.getText().isEmpty() ||
                    txtEmail.getText().isEmpty() || comboGender.getSelectedIndex() == 0 ||
                    comboCourse.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "⚠️ Please fill in all required fields!");
                return;
            }

            PreparedStatement pst = conn.prepareStatement(
                    "INSERT INTO students(name, age, gender, course, email, phone) VALUES (?, ?, ?, ?, ?, ?)");
            pst.setString(1, txtName.getText());
            pst.setInt(2, Integer.parseInt(txtAge.getText()));
            pst.setString(3, comboGender.getSelectedItem().toString());
            pst.setString(4, comboCourse.getSelectedItem().toString());
            pst.setString(5, txtEmail.getText());
            pst.setString(6, txtPhone.getText());
            pst.executeUpdate();
            pst.close();

            JOptionPane.showMessageDialog(this, "✅ Student Registered Successfully!");
            clearFields();

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "⚠️ Age must be a number!", "Input Error", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(this, "❌ Database Error: " + sqle.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unexpected Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 🧹 Clears Fields
    private void clearFields() {
        txtName.setText("");
        txtAge.setText("");
        comboGender.setSelectedIndex(0);
        comboCourse.setSelectedIndex(0);
        txtEmail.setText("");
        txtPhone.setText("");
    }

    // 🌈 Gradient Background
    private JPanel createGradientPanel() {
        return new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(72, 219, 251),
                        getWidth(), getHeight(), new Color(255, 94, 98)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }

    // 🏷 Label Style
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(Color.WHITE);
        return label;
    }

    // 🎨 Rounded Buttons (Register / Clear)
    private JButton createRoundedButton(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            public void setContentAreaFilled(boolean b) { }
        };
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(color.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(color); }
        });

        return btn;
    }

    // 🎨 Transparent Back Button (Black Arrow)
    private JButton createBackButton() {
        JButton btn = new JButton("←");
        btn.setForeground(Color.BLACK); // Black arrow
        btn.setFont(new Font("Segoe UI", Font.BOLD, 26));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Subtle hover effect
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setForeground(new Color(60, 60, 60)); }
            public void mouseExited(MouseEvent e) { btn.setForeground(Color.BLACK); }
        });

        return btn;
    }
}
