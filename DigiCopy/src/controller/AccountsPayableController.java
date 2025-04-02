package controller;

import model.Account;
import database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountsPayableController {

    public void addAccount(Account account) {
        String sql = "INSERT INTO accounts (details, amount, due_date, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account.getDetails());
            pstmt.setDouble(2, account.getAmount());
            pstmt.setString(3, account.getDueDate());
            pstmt.setString(4, account.getStatus());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAccount(Account account) {
        String sql = "UPDATE accounts SET details = ?, amount = ?, due_date = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account.getDetails());
            pstmt.setDouble(2, account.getAmount());
            pstmt.setString(3, account.getDueDate());
            pstmt.setString(4, account.getStatus());
            pstmt.setInt(5, account.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAccount(int accountId) {
        String sql = "DELETE FROM accounts WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Account getAccountById(int accountId) {
        String sql = "SELECT * FROM accounts WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Account(
                    rs.getInt("id"),
                    rs.getString("details"),
                    rs.getDouble("amount"),
                    rs.getString("due_date"),
                    rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                accounts.add(new Account(
                    rs.getInt("id"),
                    rs.getString("details"),
                    rs.getDouble("amount"),
                    rs.getString("due_date"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }
} 