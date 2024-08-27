import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ResultSaver {
    GridBagConstraints gbc = new GridBagConstraints();
    JFrame resultFrame;
    JFrame saveResultFrame;
    JFrame quizFrameObj;
    JLabel totolQuestions, correctAnswers, wrongAnswers, totalScore;
    JButton saveResultBtn, cancelBtn;
    String complement;

    ResultSaver(int maxQuestions, int corrAnswers, String language, JFrame quizFrame) {
        quizFrameObj = quizFrame;

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

        // gbc.gridx = 2;
        // gbc.gridy = 1;
        // resultFrame.add(complement);

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

    public void startSaverFrame() {
        saveResultFrame = new JFrame();
        saveResultFrame.setLayout(new BoxLayout(saveResultFrame.getContentPane(), BoxLayout.Y_AXIS));
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nameLabel = new JLabel("Enter your name: ");
        namePanel.add(nameLabel);
        JTextField nameTextField = new JTextField(20);
        namePanel.add(nameTextField);
        saveResultFrame.add(namePanel);
        JButton saveQuiz = new JButton("SAVE");
        saveQuiz.setSize(70, 40);
        saveResultFrame.add(saveQuiz);
        saveResultFrame.setSize(250, 150);
        saveResultFrame.setLocationRelativeTo(resultFrame);
        saveResultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        saveResultFrame.setVisible(true);
    }
}
