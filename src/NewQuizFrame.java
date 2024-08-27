import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import javax.imageio.ImageIO;

public class NewQuizFrame {
    final static int MAX_QUESTIONS = 10;
    final static int DELAY = 1000;
    String currLanguage;
    int correctAnswers = 0;
    String[] questions = new String[MAX_QUESTIONS];
    JRadioButton[][] options = new JRadioButton[MAX_QUESTIONS][4];
    ButtonGroup[] optionsGroup = new ButtonGroup[MAX_QUESTIONS];
    int[] answers = new int[MAX_QUESTIONS];
    int queNumber = 0;
    Path resourcePath;
    Font montserratFont = loadFont("resources/fonts/Montserrat-Bold.ttf");

    JFrame quizFrame;
    JPanel titlePanel;
    JLabel timeLabel;
    JPanel questionsPanel;
    JLabel questionLabel;
    JPanel optionsPanel;
    JPanel bottomNavPanel;
    JButton previousBtn;
    JButton nextBtn;
    JButton submitBtn;
    JButton exitBtn;
    public static Timer time;

    public NewQuizFrame(Connection connection, String language) throws SQLException {

        // Retriving data from database.
        String query = "SELECT * FROM programmingQuestions WHERE language = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, language);
        ResultSet resultSet = pstmt.executeQuery();

        while (resultSet.next() && queNumber < MAX_QUESTIONS) {
            questions[queNumber] = resultSet.getInt(2) + ") " + resultSet.getString(3);
            options[queNumber][0] = new JRadioButton();
            options[queNumber][0].setText(processString(resultSet.getString(4)));
            options[queNumber][1] = new JRadioButton();
            options[queNumber][1].setText((processString(resultSet.getString(5))));
            options[queNumber][2] = new JRadioButton();
            options[queNumber][2].setText(processString(resultSet.getString(6)));
            options[queNumber][3] = new JRadioButton();
            options[queNumber][3].setText(processString(resultSet.getString(7)));
            answers[queNumber] = resultSet.getInt(8);
            queNumber++;
        }
        currLanguage = language.toUpperCase();
        queNumber = 0;
        pstmt.close();
        resultSet.close();
        // ----------------------------------------------

        // Components -------------------------------------
        quizFrame = new JFrame();
        quizFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        quizFrame.setMinimumSize(new Dimension(800, 600));
        quizFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        quizFrame.setLayout(new BorderLayout());
        quizFrame.setTitle("Programming Quiz: " + language.toUpperCase());

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Arial", Font.BOLD, 32));
        timeLabel.setForeground(new Color(150, 200, 255));
        timeLabel.setBorder(new EmptyBorder(30, 0, 20, 60));
        // resourcePath = Path.get("resources", language + ".jpg");
        titlePanel = createImageBackgroundPanel(
                "resources/images/" + language + ".jpg");
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setPreferredSize(new Dimension(800, 200));
        titlePanel.add(timeLabel, BorderLayout.EAST);
        // Creating a timeLabel--------->
        time = new Timer(DELAY, performTask);
        time.start();
        // ---------------------------------------------
        quizFrame.add(titlePanel, BorderLayout.NORTH);

        questionLabel = new JLabel(questions[0]); // initializing first question.
        questionLabel.setFont(montserratFont.deriveFont(Font.BOLD, 30));
        questionLabel.setBorder(new EmptyBorder(20, 30, 20, 30));
        questionsPanel = new JPanel(new BorderLayout());
        questionsPanel.add(questionLabel, BorderLayout.NORTH);

        for (int i = 0; i < MAX_QUESTIONS; i++) { // Setting necessary things of the options.
            options[i][0].setFont(montserratFont.deriveFont(Font.BOLD, 28));
            options[i][0].setBorder(new EmptyBorder(100, 100, 100, 100));
            options[i][0].setBackground(new Color(200, 200, 200));
            options[i][0].addChangeListener(new CustomChangeListener());
            options[i][0].setFocusPainted(false);
            options[i][1].setFont(montserratFont.deriveFont(Font.BOLD, 28));
            options[i][1].setBorder(new EmptyBorder(100, 100, 100, 100));
            options[i][1].setBackground(new Color(200, 200, 200));
            options[i][1].addChangeListener(new CustomChangeListener());
            options[i][1].setFocusPainted(false);
            options[i][2].setFont(montserratFont.deriveFont(Font.BOLD, 28));
            options[i][2].setBorder(new EmptyBorder(100, 100, 100, 100));
            options[i][2].setBackground(new Color(200, 200, 200));
            options[i][2].addChangeListener(new CustomChangeListener());
            options[i][2].setFocusPainted(false);
            options[i][3].setFont(montserratFont.deriveFont(Font.BOLD, 28));
            options[i][3].setBorder(new EmptyBorder(100, 100, 100, 100));
            options[i][3].setBackground(new Color(200, 200, 200));
            options[i][3].addChangeListener(new CustomChangeListener());
            options[i][3].setFocusPainted(false);
        }

        for (int i = 0; i < MAX_QUESTIONS; i++) { // grouping up the options.
            optionsGroup[i] = new ButtonGroup();
            optionsGroup[i].add(options[i][0]);
            optionsGroup[i].add(options[i][1]);
            optionsGroup[i].add(options[i][2]);
            optionsGroup[i].add(options[i][3]);
        }

        optionsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        optionsPanel.add(options[0][0]); // initializing with the first
                                         // question's opt's.
        optionsPanel.add(options[0][1]);
        optionsPanel.add(options[0][2]);
        optionsPanel.add(options[0][3]);
        optionsPanel.setBorder(new EmptyBorder(10, 20, 0, 20));
        questionsPanel.add(optionsPanel, BorderLayout.CENTER);
        quizFrame.add(questionsPanel, BorderLayout.CENTER);

        // Bottom Buttons----------------------------------------
        bottomNavPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        bottomNavPanel.setPreferredSize(new Dimension(quizFrame.getWidth(), 150));
        bottomNavPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        previousBtn = new JButton("Previous"); // previous btn.
        previousBtn.setFont(montserratFont.deriveFont(Font.PLAIN, 20));
        previousBtn.setBackground(new Color(150, 200, 255));
        previousBtn.setFocusPainted(false);
        previousBtn.setEnabled(false);
        previousBtn.addActionListener(previousQuestion);
        nextBtn = new JButton("Next"); // next btn.
        nextBtn.setFont(montserratFont.deriveFont(Font.PLAIN, 20));
        nextBtn.setBackground(new Color(150, 200, 255));
        nextBtn.setFocusPainted(false);
        nextBtn.addActionListener(nextQuestion);
        submitBtn = new JButton("Submit"); // submit btn.
        submitBtn.setFont(montserratFont.deriveFont(Font.BOLD, 20));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setBackground(new Color(50, 150, 50));
        submitBtn.setFocusPainted(false);
        submitBtn.addActionListener(submitQuiz);
        exitBtn = new JButton("Exit"); // exit btn.
        exitBtn.setFont(montserratFont.deriveFont(Font.BOLD, 20));
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setBackground(new Color(150, 50, 50));
        exitBtn.setFocusPainted(false);
        exitBtn.addActionListener(closeQuiz);
        bottomNavPanel.add(previousBtn);
        bottomNavPanel.add(nextBtn);
        bottomNavPanel.add(submitBtn);
        bottomNavPanel.add(exitBtn);
        quizFrame.add(bottomNavPanel, BorderLayout.SOUTH);
        // ---------------------------------------------------------------

        // end.
        quizFrame.setVisible(true);
    }

    ActionListener performTask = new ActionListener() {
        int seconds = 60;
        int minutes = 4;

        @Override
        public void actionPerformed(ActionEvent event) {
            if (seconds == 0) {
                if (minutes == 0) {
                    submitBtn.doClick();
                    time.stop();
                } else {
                    minutes--;
                    seconds = 59;
                }
            } else {
                seconds--;
            }
            if (seconds < 10) {
                timeLabel.setText("Time: 0" + minutes + ":0" + seconds);
            } else {
                timeLabel.setText("Time: 0" + minutes + ":" + seconds);
            }
            if (minutes == 0) {
                timeLabel.setForeground(new Color(255, 40, 40));
            }
        }
    };

    ActionListener previousQuestion = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (queNumber > 0) {
                queNumber--;
                questionLabel.setText(questions[queNumber]);
                optionsPanel.removeAll();
                optionsPanel.add(options[queNumber][0]);
                optionsPanel.add(options[queNumber][1]);
                optionsPanel.add(options[queNumber][2]);
                optionsPanel.add(options[queNumber][3]);
                optionsPanel.revalidate();
                optionsPanel.repaint();
            }
            if (queNumber == 0) {
                previousBtn.setEnabled(false);
            }
            nextBtn.setEnabled(true);
        }
    };
    ActionListener nextQuestion = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (queNumber < MAX_QUESTIONS - 1) {
                queNumber++;
                questionLabel.setText(questions[queNumber]);
                optionsPanel.removeAll();
                optionsPanel.add(options[queNumber][0]);
                optionsPanel.add(options[queNumber][1]);
                optionsPanel.add(options[queNumber][2]);
                optionsPanel.add(options[queNumber][3]);
                optionsPanel.revalidate();
                optionsPanel.repaint();
            }
            if (queNumber == MAX_QUESTIONS - 1) {
                nextBtn.setEnabled(false);
            }
            previousBtn.setEnabled(true);
        }
    };

    ActionListener submitQuiz = new ActionListener() {
        public void actionPerformed(ActionEvent s) {
            try {
                evaluateQuiz();
                new ResultSaver(MAX_QUESTIONS, correctAnswers, currLanguage, quizFrame);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    ActionListener closeQuiz = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (JOptionPane.showConfirmDialog(null, "Are you sure want to exit?", "Confirm Exit",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                quizFrame.dispose();
            }
        }
    };

    public void evaluateQuiz() {
        optionsPanel.setFocusable(false);
        correctAnswers = 0;
        for (int qno = 0; qno < MAX_QUESTIONS; qno++) {
            if (options[qno][answers[qno] - 1].isSelected()) {
                correctAnswers++;
            }
        }
    }

    private Font loadFont(String fontPath) {
        try {
            // Load the font from the specified file
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath));
            // Register the font with the graphics environment
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            return font;
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static JPanel createImageBackgroundPanel(String imagePath) {
        JPanel panel = new JPanel() {
            private Image backgroundImage;
            {
                try {
                    backgroundImage = ImageIO.read(new File(imagePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        panel.setLayout(new FlowLayout()); // Set your desired layout
        return panel;
    }

    public static void changeBackgroundColor(ChangeEvent e) {
        JRadioButton rButton = (JRadioButton) e.getSource();
        if (rButton.isSelected()) {
            rButton.setBackground(new Color(150, 150, 255));
        } else {
            rButton.setBackground(new Color(200, 200, 200));
        }
    }

    static class CustomChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            changeBackgroundColor(e);
        }
    }

    String processString(String str) {
        if (str.contains("<"))
            return str;
        int max_len = 35, start = 0, end = max_len;
        String option = "<html>";
        while (end < str.length()) {
            option += str.substring(start, end);
            option += "\n";
            start = end;
            end += end;
        }
        option = option + str.substring(start, str.length()) + "</html>";
        return option;
    }
}
