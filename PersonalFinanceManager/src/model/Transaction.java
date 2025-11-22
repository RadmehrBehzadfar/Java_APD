package model;

import java.time.LocalDate;

public class Transaction {
    private int id;
    private LocalDate date;
    private double amount;
    private String category;
    private String description;
    private String type; // "income" or "expense"

    public Transaction() {}

    public Transaction(int id, LocalDate date, double amount, String category, String description, String type) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.type = type;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}