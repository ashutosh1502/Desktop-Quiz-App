import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class DisplayResults {

    private Connection connection;
    JFrame ref;

    public DisplayResults(JFrame ref, Connection connection) {
        this.connection = connection;
        this.ref = ref;
        showResultsFrame();
    }

    public void showResultsFrame() {
        JFrame resultsFrame = new JFrame("Quiz Results");
        resultsFrame.setSize(800, 500);
        resultsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultsFrame.setLayout(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);

        tableModel.setColumnIdentifiers(new Object[] { "Username", "Quiz Topic", "Total Questions", "Correct Questions",
                "Wrong Questions", "Result" });

        fetchResultsData(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        resultsFrame.add(scrollPane, BorderLayout.CENTER);
        resultsFrame.setLocationRelativeTo(ref);

        resultsFrame.setVisible(true);
    }

    private void fetchResultsData(DefaultTableModel tableModel) {
        String query = "SELECT* FROM QUIZRESULTS";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String username = rs.getString("username");
                String quizTopic = rs.getString("quiz_topic");
                int totalQuestions = rs.getInt("total_ques");
                int correctQuestions = rs.getInt("correct_ques");
                int wrongQuestions = rs.getInt("wrong_ques");
                String result = rs.getString("result");

                tableModel.addRow(
                        new Object[] { username, quizTopic, totalQuestions, correctQuestions, wrongQuestions, result });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error retrieving data from the database.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
