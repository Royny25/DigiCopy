package controller;

import model.Invoice;
import model.Product;
import database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceController {

    public void addInvoice(Invoice invoice) {
        String sql = "INSERT INTO invoices (date, customer_name, total, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setDate(1, new java.sql.Date(invoice.getDate().getTime()));
            pstmt.setString(2, invoice.getCustomerName());
            pstmt.setDouble(3, invoice.getTotal());
            pstmt.setString(4, invoice.getStatus());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int invoiceId = rs.getInt(1);
                if (invoice.getProducts() != null) {
                    addInvoiceDetails(invoiceId, invoice.getProducts());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addInvoiceDetails(int invoiceId, List<Product> products) {
        String sql = "INSERT INTO invoice_details (invoice_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Product product : products) {
                pstmt.setInt(1, invoiceId);
                pstmt.setInt(2, product.getId());
                pstmt.setInt(3, 1); // Por defecto la cantidad es 1 (esto debería mejorarse en el futuro)
                pstmt.setDouble(4, product.getPrice());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                invoices.add(new Invoice(
                    rs.getInt("id"),
                    rs.getDate("date"),
                    rs.getString("customer_name"),
                    rs.getDouble("total"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    public Invoice getInvoiceById(int invoiceId) {
        String sql = "SELECT * FROM invoices WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, invoiceId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Invoice(
                    rs.getInt("id"),
                    rs.getDate("date"),
                    rs.getString("customer_name"),
                    rs.getDouble("total"),
                    rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Product> getProductsByInvoiceId(int invoiceId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, id.quantity FROM products p " +
                     "JOIN invoice_details id ON p.id = id.product_id " +
                     "WHERE id.invoice_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, invoiceId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
} 