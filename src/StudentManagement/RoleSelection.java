package StudentManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RoleSelection extends JFrame {
    public RoleSelection() {
        setTitle("🎓 Choose Your Role");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel() {
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

        panel.setLayout(new GridLayout(3, 1, 10, 10));
        JLabel lbl = new JLabel("Select Your Role", JLabel.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbl.setForeground(Color.WHITE);

        JButton btnTeacher = createStyledButton("👨‍🏫 Teacher", new Color(52, 152, 219));
        JButton btnStudent = createStyledButton("👨‍🎓 Student", new Color(46, 204, 113));

        panel.add(lbl);
        panel.add(btnTeacher);
        panel.add(btnStudent);
        add(panel);
        setVisible(true);

        btnTeacher.addActionListener(e -> {
            dispose();
            new TeacherLogin();
        });

        btnStudent.addActionListener(e -> {
            dispose();
            new StudentForm();
        });
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(color.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(color); }
        });
        return btn;
    }
}
