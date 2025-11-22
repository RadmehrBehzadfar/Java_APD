package model;

import java.time.LocalDate;

public class BillReminder {
    private int id;
    private String name;
    private String description;
    private double amount;
    private LocalDate dueDate;
    private String category;
    private boolean isPaid;
    private int reminderDays; // Days before due date to remind

    public BillReminder() {}

    public BillReminder(int id, String name, String description, double amount, 
                       LocalDate dueDate, String category, boolean isPaid, int reminderDays) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.dueDate = dueDate;
        this.category = category;
        this.isPaid = isPaid;
        this.reminderDays = reminderDays;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }

    public int getReminderDays() { return reminderDays; }
    public void setReminderDays(int reminderDays) { this.reminderDays = reminderDays; }

    public boolean isOverdue() {
        return !isPaid && dueDate.isBefore(LocalDate.now());
    }

    public boolean shouldRemind() {
        if (isPaid) return false;
        LocalDate reminderDate = dueDate.minusDays(reminderDays);
        return LocalDate.now().isAfter(reminderDate) || LocalDate.now().isEqual(reminderDate);
    }
}
