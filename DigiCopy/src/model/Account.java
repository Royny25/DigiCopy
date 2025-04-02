package model;

public class Account {
    private int id;
    private String details;
    private double amount;
    private String dueDate;
    private String status;

    public Account(int id, String details) {
        this.id = id;
        this.details = details;
        this.amount = 0.0;
        this.dueDate = "";
        this.status = "Pendiente";
    }
    
    public Account(int id, String details, double amount, String dueDate, String status) {
        this.id = id;
        this.details = details;
        this.amount = amount;
        this.dueDate = dueDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
    
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 