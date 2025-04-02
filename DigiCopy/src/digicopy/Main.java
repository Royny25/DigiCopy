package digicopy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import database.DatabaseInitializer;
import view.ProductManagementView;
import view.SupplierManagementView;
import view.BillingView;
import view.AccountsPayableView;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Configuración del look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                // Inicializar la base de datos
                if (!DatabaseInitializer.initializeDatabase()) {
                    JOptionPane.showMessageDialog(null,
                        "No se pudo inicializar la base de datos. La aplicación podría no funcionar correctamente.",
                        "Error de inicialización",
                        JOptionPane.WARNING_MESSAGE);
                }
                
                // Crear el menú principal
                JFrame mainFrame = new JFrame("DigiCopy - Sistema de Inventario y Facturación");
                mainFrame.setSize(800, 600);
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setLocationRelativeTo(null);
                
                // Panel principal con GridBagLayout para mejor organización
                JPanel mainPanel = new JPanel(new GridBagLayout());
                mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                
                // Configuración del GridBag
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(10, 10, 10, 10);
                
                // Título
                JLabel titleLabel = new JLabel("Sistema de Gestión DigiCopy", JLabel.CENTER);
                titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.gridwidth = 2;
                mainPanel.add(titleLabel, gbc);
                
                // Subtítulo
                JLabel subtitleLabel = new JLabel("Inventario y Facturación", JLabel.CENTER);
                subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 16));
                gbc.gridy = 1;
                mainPanel.add(subtitleLabel, gbc);
                
                // Restablecer el ancho de grid
                gbc.gridwidth = 1;
                
                // Botones para cada módulo
                JButton btnProducts = createButton("Gestión de Productos");
                gbc.gridx = 0;
                gbc.gridy = 2;
                mainPanel.add(btnProducts, gbc);
                
                JButton btnSuppliers = createButton("Gestión de Proveedores");
                gbc.gridx = 1;
                mainPanel.add(btnSuppliers, gbc);
                
                JButton btnInventory = createButton("Control de Inventario");
                gbc.gridx = 0;
                gbc.gridy = 3;
                mainPanel.add(btnInventory, gbc);
                
                JButton btnBilling = createButton("Facturación");
                gbc.gridx = 1;
                mainPanel.add(btnBilling, gbc);
                
                JButton btnAccounts = createButton("Cuentas por Pagar");
                gbc.gridx = 0;
                gbc.gridy = 4;
                mainPanel.add(btnAccounts, gbc);
                
                JButton btnReports = createButton("Reportes");
                gbc.gridx = 1;
                mainPanel.add(btnReports, gbc);
                
                // Añadir funcionalidad a los botones
                btnProducts.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            // Abrir la vista de gestión de productos
                            ProductManagementView productView = new ProductManagementView();
                            productView.setVisible(true);
                        } catch (Exception ex) {
                            showError(ex);
                        }
                    }
                });
                
                btnSuppliers.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            // Abrir la vista de gestión de proveedores
                            SupplierManagementView supplierView = new SupplierManagementView();
                            supplierView.setVisible(true);
                        } catch (Exception ex) {
                            showError(ex);
                        }
                    }
                });
                
                btnInventory.addActionListener(e -> JOptionPane.showMessageDialog(mainFrame, 
                    "El control de inventario estará disponible en la próxima versión.", 
                    "En desarrollo", JOptionPane.INFORMATION_MESSAGE));
                
                btnBilling.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            // Abrir la vista de facturación
                            BillingView billingView = new BillingView();
                            billingView.setVisible(true);
                        } catch (Exception ex) {
                            showError(ex);
                        }
                    }
                });
                
                btnAccounts.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            // Abrir la vista de cuentas por pagar
                            AccountsPayableView accountsView = new AccountsPayableView();
                            accountsView.setVisible(true);
                        } catch (Exception ex) {
                            showError(ex);
                        }
                    }
                });
                
                btnReports.addActionListener(e -> JOptionPane.showMessageDialog(mainFrame, 
                    "Los reportes estarán disponibles en la próxima versión.", 
                    "En desarrollo", JOptionPane.INFORMATION_MESSAGE));
                
                // Configurar y mostrar la ventana principal
                mainFrame.add(mainPanel);
                mainFrame.setVisible(true);
                
            } catch (Exception e) {
                showError(e);
            }
        });
    }
    
    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(200, 60));
        return button;
    }
    
    private static void showError(Exception e) {
        JOptionPane.showMessageDialog(null, 
            "Error: " + e.getMessage(), 
            "Error en la aplicación", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
} 