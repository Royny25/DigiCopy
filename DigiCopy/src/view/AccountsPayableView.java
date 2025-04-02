package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import controller.AccountsPayableController;
import model.Account;

public class AccountsPayableView extends JFrame {
    private JTextField txtAccountId;
    private JTextField txtAccountDetails;
    private JTextField txtAmount;
    private JTextField txtDueDate;
    private JComboBox<String> cmbStatus;
    private JButton btnAddAccount;
    private JButton btnUpdateAccount;
    private JButton btnDeleteAccount;
    private JButton btnClearFields;
    private JTable tblAccounts;
    private DefaultTableModel tableModel;
    private AccountsPayableController accountsPayableController;

    public AccountsPayableView() {
        // Configuración de la ventana
        setTitle("Cuentas por Pagar");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Inicializar el controlador
        accountsPayableController = new AccountsPayableController();
        
        // Crear componentes
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        // Panel de formulario para datos de la cuenta
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Detalles de la Cuenta"));
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);
        
        // Etiquetas y campos de texto
        addFormField(formPanel, constraints, "ID:", txtAccountId = new JTextField(10), 0);
        txtAccountId.setEditable(false); // El ID es generado automáticamente
        
        addFormField(formPanel, constraints, "Detalles:", txtAccountDetails = new JTextField(30), 1);
        addFormField(formPanel, constraints, "Monto:", txtAmount = new JTextField(10), 2);
        addFormField(formPanel, constraints, "Fecha Vencimiento:", txtDueDate = new JTextField(10), 3);
        
        // ComboBox para el estado
        cmbStatus = new JComboBox<>(new String[]{"Pendiente", "Pagada", "Vencida"});
        addFormField(formPanel, constraints, "Estado:", cmbStatus, 4);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAddAccount = new JButton("Agregar");
        btnUpdateAccount = new JButton("Actualizar");
        btnDeleteAccount = new JButton("Eliminar");
        btnClearFields = new JButton("Limpiar");
        
        buttonPanel.add(btnAddAccount);
        buttonPanel.add(btnUpdateAccount);
        buttonPanel.add(btnDeleteAccount);
        buttonPanel.add(btnClearFields);
        
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        formPanel.add(buttonPanel, constraints);
        
        // Panel de tabla
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Lista de Cuentas por Pagar"));
        
        // Crear modelo de tabla
        String[] columnNames = {"ID", "Detalles", "Monto", "Fecha Vencimiento", "Estado"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla no sea editable
            }
        };
        
        tblAccounts = new JTable(tableModel);
        tblAccounts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblAccounts.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JScrollPane scrollPane = new JScrollPane(tblAccounts);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Agregar paneles al panel principal
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        // Agregar panel principal a la ventana
        add(mainPanel);
        
        // Cargar datos iniciales
        loadAccountsData();
        
        // Agregar listeners a los botones
        btnAddAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!validateFields()) {
                        return;
                    }
                    
                    double amount = 0.0;
                    try {
                        amount = Double.parseDouble(txtAmount.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Por favor ingrese un monto válido.");
                        return;
                    }
                    
                    Account account = new Account(
                        0, // ID será generado por la base de datos
                        txtAccountDetails.getText(),
                        amount,
                        txtDueDate.getText(),
                        cmbStatus.getSelectedItem().toString()
                    );
                    
                    accountsPayableController.addAccount(account);
                    JOptionPane.showMessageDialog(null, "Cuenta agregada exitosamente.");
                    
                    clearFields();
                    loadAccountsData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, 
                        "Error al agregar la cuenta: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        btnUpdateAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (txtAccountId.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, 
                            "Por favor, seleccione una cuenta de la tabla para actualizar.",
                            "Selección requerida", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    if (!validateFields()) {
                        return;
                    }
                    
                    double amount = 0.0;
                    try {
                        amount = Double.parseDouble(txtAmount.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Por favor ingrese un monto válido.");
                        return;
                    }
                    
                    Account account = new Account(
                        Integer.parseInt(txtAccountId.getText()),
                        txtAccountDetails.getText(),
                        amount,
                        txtDueDate.getText(),
                        cmbStatus.getSelectedItem().toString()
                    );
                    
                    accountsPayableController.updateAccount(account);
                    JOptionPane.showMessageDialog(null, "Cuenta actualizada exitosamente.");
                    
                    clearFields();
                    loadAccountsData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, 
                        "Error al actualizar la cuenta: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        btnDeleteAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (txtAccountId.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, 
                            "Por favor, seleccione una cuenta de la tabla para eliminar.",
                            "Selección requerida", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    int confirm = JOptionPane.showConfirmDialog(null, 
                        "¿Está seguro de que desea eliminar esta cuenta?",
                        "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        int accountId = Integer.parseInt(txtAccountId.getText());
                        accountsPayableController.deleteAccount(accountId);
                        JOptionPane.showMessageDialog(null, "Cuenta eliminada exitosamente.");
                        
                        clearFields();
                        loadAccountsData();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, 
                        "Error al eliminar la cuenta: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        btnClearFields.addActionListener(e -> clearFields());
        
        // Listener para selección en la tabla
        tblAccounts.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblAccounts.getSelectedRow();
                if (selectedRow >= 0) {
                    txtAccountId.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    txtAccountDetails.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    txtAmount.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    txtDueDate.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    
                    String status = tableModel.getValueAt(selectedRow, 4).toString();
                    for (int i = 0; i < cmbStatus.getItemCount(); i++) {
                        if (cmbStatus.getItemAt(i).equals(status)) {
                            cmbStatus.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        });
    }
    
    private void addFormField(JPanel panel, GridBagConstraints constraints, String labelText, JComponent component, int row) {
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.gridwidth = 1;
        JLabel label = new JLabel(labelText);
        panel.add(label, constraints);
        
        constraints.gridx = 1;
        constraints.weightx = 1.0;
        panel.add(component, constraints);
        constraints.weightx = 0.0;
    }
    
    private boolean validateFields() {
        if (txtAccountDetails.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Los detalles de la cuenta son obligatorios.");
            txtAccountDetails.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void clearFields() {
        txtAccountId.setText("");
        txtAccountDetails.setText("");
        txtAmount.setText("");
        txtDueDate.setText("");
        cmbStatus.setSelectedIndex(0);
        tblAccounts.clearSelection();
        txtAccountDetails.requestFocus();
    }
    
    private void loadAccountsData() {
        // Limpiar tabla
        tableModel.setRowCount(0);
        
        try {
            // Obtener todas las cuentas
            List<Account> accounts = accountsPayableController.getAllAccounts();
            
            // Llenar tabla con datos
            for (Account account : accounts) {
                Object[] row = {
                    account.getId(),
                    account.getDetails(),
                    account.getAmount(),
                    account.getDueDate(),
                    account.getStatus()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al cargar las cuentas: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            AccountsPayableView view = new AccountsPayableView();
            view.setVisible(true);
        });
    }
} 