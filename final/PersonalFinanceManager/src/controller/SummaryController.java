package controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import model.Database;
import model.Transaction;

import java.sql.SQLException;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SummaryController {

    @FXML
    private PieChart categoryPieChart;
    @FXML
    private BarChart<String, Number> monthlyBarChart;

    @FXML
    public void initialize() {
        // Called automatically when FXML loads
    }

    @FXML
    private void handleLoadSummary() {
        // Demonstrate concurrency by loading transactions in a background thread
        Task<Void> loadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                updateMessage("Loading data...");
                buildCharts();
                return null;
            }
        };

        loadTask.setOnSucceeded(e -> {
            // Show a message or do something else when done
            showAlert("Summary Loaded", "Summary charts updated.");
        });

        loadTask.setOnFailed(e -> {
            showAlert("Error", "Failed to load summary data.");
            loadTask.getException().printStackTrace();
        });

        Thread thread = new Thread(loadTask);
        thread.setDaemon(true);
        thread.start();
    }

    private void buildCharts() {
        try {
            List<Transaction> transactions = Database.getAllTransactions();
            // Calculate expenses by category
            Map<String, Double> expenseByCategory = new TreeMap<>();
            for (Transaction t : transactions) {
                if ("expense".equalsIgnoreCase(t.getType())) {
                    expenseByCategory.put(t.getCategory(),
                            expenseByCategory.getOrDefault(t.getCategory(), 0.0) + t.getAmount());
                }
            }

            // JavaFX UI must be updated on the Application Thread
            javafx.application.Platform.runLater(() -> {
                categoryPieChart.getData().clear();
                for (Map.Entry<String, Double> entry : expenseByCategory.entrySet()) {
                    categoryPieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
                }
            });

            // Build monthly net totals (income - expense)
            Map<YearMonth, Double> monthlyTotals = new TreeMap<>();
            for (Transaction t : transactions) {
                YearMonth ym = YearMonth.from(t.getDate());
                double current = monthlyTotals.getOrDefault(ym, 0.0);
                if ("income".equalsIgnoreCase(t.getType())) {
                    current += t.getAmount();
                } else {
                    current -= t.getAmount();
                }
                monthlyTotals.put(ym, current);
            }

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Monthly Net");

            for (Map.Entry<YearMonth, Double> entry : monthlyTotals.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
            }

            javafx.application.Platform.runLater(() -> {
                monthlyBarChart.getData().clear();
                monthlyBarChart.getData().add(series);
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
}