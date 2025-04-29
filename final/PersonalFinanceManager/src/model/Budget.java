package model;

public class Budget {
    private int id;
    private String category;
    private double limit;

    public Budget() {}

    public Budget(int id, String category, double limit) {
        this.id = id;
        this.category = category;
        this.limit = limit;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getLimit() { return limit; }
    public void setLimit(double limit) { this.limit = limit; }
}