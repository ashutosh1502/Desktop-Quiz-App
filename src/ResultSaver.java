import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.*;
import javax.swing.*;

public class ResultSaver {
    GridBagConstraints gbc = new GridBagConstraints();
    JFrame resultFrame;
    JFrame saveResultFrame;
    JFrame quizFrameObj;
    JLabel totolQuestions, correctAnswers, wrongAnswers, totalScore;
    JButton saveResultBtn, cancelBtn;
    String complement;
    JTextField nameTextField;
    JButton saveQuiz;
    Connection connection;
    String quizTopic;

    ResultSaver(int maxQuestions, int corrAnswers, String language, JFrame quizFrame, Connection connection) {
        quizFrameObj = quizFrame;
        quizTopic = language;
        this.connection = connection;
        resultFrame = new JFrame();
        resultFrame.setLayout(new GridBagLayout());

        float score = ((float) corrAnswers / maxQuestions) * 100f;
        if (score > 75) {
            complement = "Excellent!";
        } else if (score > 50) {
            complement = "Good!";
        } else {
            complement = "Need Practice!";
        }

        totolQuestions = new JLabel(maxQuestions + "");
        correctAnswers = new JLabel(corrAnswers + "");
        wrongAnswers = new JLabel((maxQuestions - corrAnswers) + "");
        totalScore = new JLabel(score + " %");

        gbc.gridx = 1;
        gbc.gridy = 1;
        resultFrame.add(new JLabel("Total Questions: "), gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        resultFrame.add(totolQuestions, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        resultFrame.add(new JLabel("Correct Answers: "), gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        resultFrame.add(correctAnswers, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        resultFrame.add(new JLabel("Wrong Answers: "), gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        resultFrame.add(wrongAnswers, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        resultFrame.add(new JLabel("Score: "), gbc);

        gbc.gridx = 2;
        gbc.gridy = 4;
        resultFrame.add(totalScore, gbc);

        saveResultBtn = new JButton("SAVE");
        saveResultBtn.addActionListener(saveQuizResult);
        cancelBtn = new JButton("CANCEL");
        cancelBtn.addActionListener(exitQuiz);
        gbc.gridx = 1;
        gbc.gridy = 5;
        resultFrame.add(saveResultBtn, gbc);
        gbc.gridx = 2;
        gbc.gridy = 5;
        resultFrame.add(cancelBtn, gbc);

        resultFrame.setTitle(complement);
        resultFrame.setSize(400, 250);
        resultFrame.setLocationRelativeTo(quizFrameObj);
        resultFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        resultFrame.setVisible(true);
    }

    ActionListener exitQuiz = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            resultFrame.dispose();
            quizFrameObj.dispose();
        }
    };

    ActionListener saveQuizResult = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> startSaverFrame());
        }
    };

    ActionListener saveResult = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            Statement stmt = null;
            PreparedStatement pstmt = null;
            try {
                DatabaseMetaData dbm = connection.getMetaData();
                ResultSet rs = dbm.getTables(null, null, "QUIZRESULTS", null);
                if (!rs.next()) {
                    stmt = connection.createStatement();
                    String createTableSQL = "CREATE TABLE QuizResults ("
                            + "username VARCHAR(50), "
                            + "quiz_topic VARCHAR(100), "
                            + "total_ques NUMBER, "
                            + "correct_ques NUMBER, "
                            + "wrong_ques NUMBER, "
                            + "result VARCHAR(10))";
                    stmt.executeUpdate(createTableSQL);
                    System.out.println("Table QuizResults created successfully!");
                }
                String insertSQL = "INSERT INTO QuizResults (username, quiz_topic, total_ques, correct_ques, wrong_ques, result) "
                        + "VALUES (?, ?, ?, ?, ?, ?)";
                pstmt = connection.prepareStatement(insertSQL);
                pstmt.setString(1, nameTextField.getText().trim());
                pstmt.setString(2, quizTopic);
                pstmt.setInt(3, Integer.parseInt(totolQuestions.getText().trim()));
                pstmt.setInt(4, Integer.parseInt(correctAnswers.getText().trim()));
                pstmt.setInt(5, Integer.parseInt(wrongAnswers.getText().trim()));
                pstmt.setString(6, totalScore.getText());

                pstmt.executeUpdate();

                System.out.println("Data inserted successfully!");
                JOptionPane.showMessageDialog(resultFrame,
                        "Result saved successfully",
                        "Done",
                        JOptionPane.INFORMATION_MESSAGE);
                saveResultFrame.dispose();
                cancelBtn.doClick();

            } catch (Exception exception) {
                System.out.println("101");
                exception.printStackTrace();
                exception.getCause();
            } finally {
                try {
                    if (stmt != null)
                        stmt.close();
                    if (pstmt != null)
                        pstmt.close();
                } catch (Exception ex) {
                    System.out.println("102");
                    ex.printStackTrace();
                }
            }
        }
    };

    KeyListener checkEmptyString = new KeyAdapter() {
        public void keyReleased(KeyEvent k) {
            if (!nameTextField.getText().trim().isEmpty()) {
                saveQuiz.setEnabled(true);
            } else {
                saveQuiz.setEnabled(false);
            }
        }
    };

    public void startSaverFrame() {
        saveResultFrame = new JFrame();
        saveResultFrame.setLayout(new BoxLayout(saveResultFrame.getContentPane(), BoxLayout.Y_AXIS));
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nameLabel = new JLabel("Enter your name: ");
        namePanel.add(nameLabel);
        nameTextField = new JTextField(20);
        nameTextField.addKeyListener(checkEmptyString);
        namePanel.add(nameTextField);
        saveResultFrame.add(namePanel);
        saveQuiz = new JButton("   SAVE  ");
        saveQuiz.setEnabled(false);
        saveQuiz.addActionListener(saveResult);
        saveResultFrame.add(saveQuiz);
        saveResultFrame.setSize(400, 250);
        saveResultFrame.setLocationRelativeTo(resultFrame);
        saveResultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        saveResultFrame.setResizable(false);
        saveResultFrame.setVisible(true);
    }
}
