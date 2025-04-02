package model;

import java.util.Date;
import java.util.List;

public class Invoice {
    private int id;
    private Date date;
    private String customerName;
    private double total;
    private String status;
    private List<Product> products;

    public Invoice(int id, Date date, List<Product> products, double total) {
        this.id = id;
        this.date = date;
        this.products = products;
        this.total = total;
        this.customerName = "";
        this.status = "Pendiente";
    }
    
    public Invoice(int id, Date date, String customerName, double total, String status) {
        this.id = id;
        this.date = date;
        this.customerName = customerName;
        this.total = total;
        this.status = status;
        this.products = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 