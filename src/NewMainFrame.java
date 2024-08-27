import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class NewMainFrame {
    static Connection connection;
    Font montserratFont = loadFont("resources/fonts/Montserrat-Bold.ttf");

    JFrame mainFrame; // MainFrame.
    JPanel titlePanel;
    JPanel categoryPanel;
    JLabel titleLabel;
    JButton[] languages = new JButton[6];
    JButton quizRecords;

    NewMainFrame() {
        mainFrame = new JFrame();
        mainFrame.setMinimumSize(new Dimension(800, 600));
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        titleLabel = new JLabel("Programming Quizzes !");
        titleLabel.setFont(montserratFont.deriveFont(Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(200, 50, 600, 50);

        quizRecords = new JButton("Quiz Records");
        quizRecords.setFont(new Font("Arial", Font.PLAIN, 20));
        quizRecords.setBackground(new Color(255, 255, 255));
        quizRecords.setForeground(new Color(36, 36, 143));
        quizRecords.setFocusPainted(false);
        quizRecords.addActionListener(displayResults);

        titlePanel = new JPanel();
        titlePanel.setBackground(new Color(36, 36, 143));
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(quizRecords, BorderLayout.EAST);
        mainFrame.add(titlePanel, BorderLayout.NORTH);

        categoryPanel = new JPanel();
        categoryPanel.setLayout(new GridLayout(2, 3));
        categoryPanel.setBorder(new EmptyBorder(200, 300, 200, 300));
        languages[0] = new JButton("C");
        languages[0].setBackground(new Color(255, 102, 102));
        languages[1] = new JButton("C++");
        languages[1].setBackground(new Color(255, 179, 26));
        languages[2] = new JButton("Java");
        languages[2].setBackground(new Color(179, 102, 255));
        languages[3] = new JButton("Python");
        languages[3].setBackground(new Color(0, 204, 163));
        languages[4] = new JButton("JavaScript");
        languages[4].setBackground(new Color(0, 128, 128));
        languages[5] = new JButton("...");
        languages[5].setBackground(new Color(36, 36, 143));
        for (int i = 0; i < 6; i++) {
            languages[i].setSize(50, 50);
            languages[i].setForeground(new Color(255, 255, 255));
            languages[i].setFocusPainted(false);
            if (montserratFont != null) {
                languages[i].setFont(montserratFont.deriveFont(Font.BOLD, 34));
            }
            final int index = i;
            languages[index].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (JOptionPane.showConfirmDialog(mainFrame,
                            "No. of question: 10\nDuration: 5 min\nPress YES to proceed.", "Start Quiz",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                try {
                                    new NewQuizFrame(connection, languages[index].getText().toLowerCase());
                                } catch (SQLException e) {
                                    System.out.println(e);
                                }
                            }
                        });
                    }
                }
            });
            categoryPanel.add(languages[i]);
        }
        languages[5].setEnabled(false);
        mainFrame.add(categoryPanel, BorderLayout.CENTER);

        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    if (connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        mainFrame.setVisible(true);
    }

    ActionListener displayResults = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new DisplayResults(mainFrame, connection);
                }
            });
        }
    };

    private Font loadFont(String fontPath) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            return font;
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "oracle1234");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new NewMainFrame();
            }
        });
    }
}