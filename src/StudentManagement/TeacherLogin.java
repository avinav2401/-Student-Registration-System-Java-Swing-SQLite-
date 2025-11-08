package StudentManagement;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TeacherLogin extends JFrame {

    public TeacherLogin() {
        setTitle("🔐 Teacher Login");
        setSize(420, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main background panel
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(52, 143, 235),
                        getWidth(), getHeight(), new Color(117, 58, 136));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Grid layout for form
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Teacher Login", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        JLabel lblUser = createLabel("Username:");
        gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(lblUser, gbc);

        JTextField txtUser = new JTextField();
        gbc.gridx = 1;
        panel.add(txtUser, gbc);

        JLabel lblPass = createLabel("Password:");
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(lblPass, gbc);

        JPasswordField txtPass = new JPasswordField();
        gbc.gridx = 1;
        panel.add(txtPass, gbc);

        // Buttons
        JButton btnLogin = createButton("Login", new Color(52, 152, 219));
        JButton btnBack = createButton("⬅ Back", new Color(255, 193, 7));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnBack);
        buttonPanel.add(btnLogin);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        add(panel);
        setVisible(true);

        // 🔹 Login Action
        btnLogin.addActionListener(e -> {
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword()).trim();

            if (user.equals("teacher") && pass.equals("admin123")) {
                JOptionPane.showMessageDialog(this, "✅ Welcome Teacher!");
                dispose();
                new TeacherDashboard();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Invalid credentials! Please try again.");
            }
        });

        // 🔹 Back to Role Selection
        btnBack.addActionListener(e -> {
            dispose();
            new RoleSelection();
        });
    }

    // 🎨 Label styling
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(Color.WHITE);
        return label;
    }

    // 🎨 Styled button with hover
    private JButton createButton(String text, Color color) {
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
