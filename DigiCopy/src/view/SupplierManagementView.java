package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import controller.SupplierController;
import model.Supplier;

public class SupplierManagementView extends JFrame {
    private JTextField txtSupplierId;
    private JTextField txtSupplierName;
    private JTextField txtContactPerson;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextField txtAddress;
    private JButton btnAddSupplier;
    private JButton btnUpdateSupplier;
    private JButton btnDeleteSupplier;
    private JButton btnClearFields;
    private JTable tblSuppliers;
    private DefaultTableModel tableModel;
    private SupplierController supplierController;

    public SupplierManagementView() {
        // Configuración de la ventana
        setTitle("Gestión de Proveedores");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Inicializar el controlador
        supplierController = new SupplierController();
        
        // Crear componentes
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        // Panel de formulario para datos del proveedor
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Detalles del Proveedor"));
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);
        
        // Etiquetas y campos de texto
        addFormField(formPanel, constraints, "ID:", txtSupplierId = new JTextField(10), 0);
        txtSupplierId.setEditable(false); // El ID es generado automáticamente
        
        addFormField(formPanel, constraints, "Nombre:", txtSupplierName = new JTextField(20), 1);
        addFormField(formPanel, constraints, "Persona de Contacto:", txtContactPerson = new JTextField(20), 2);
        addFormField(formPanel, constraints, "Teléfono:", txtPhone = new JTextField(15), 3);
        addFormField(formPanel, constraints, "Email:", txtEmail = new JTextField(25), 4);
        addFormField(formPanel, constraints, "Dirección:", txtAddress = new JTextField(30), 5);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAddSupplier = new JButton("Agregar");
        btnUpdateSupplier = new JButton("Actualizar");
        btnDeleteSupplier = new JButton("Eliminar");
        btnClearFields = new JButton("Limpiar");
        
        buttonPanel.add(btnAddSupplier);
        buttonPanel.add(btnUpdateSupplier);
        buttonPanel.add(btnDeleteSupplier);
        buttonPanel.add(btnClearFields);
        
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 2;
        formPanel.add(buttonPanel, constraints);
        
        // Panel de tabla
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Lista de Proveedores"));
        
        // Crear modelo de tabla
        String[] columnNames = {"ID", "Nombre", "Persona de Contacto", "Teléfono", "Email", "Dirección"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla no sea editable
            }
        };
        
        tblSuppliers = new JTable(tableModel);
        tblSuppliers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblSuppliers.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JScrollPane scrollPane = new JScrollPane(tblSuppliers);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Agregar paneles al panel principal
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        // Agregar panel principal a la ventana
        add(mainPanel);
        
        // Cargar datos iniciales
        loadSupplierData();
        
        // Agregar listeners a los botones
        btnAddSupplier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!validateFields()) {
                        return;
                    }
                    
                    Supplier supplier = new Supplier(
                        0, // ID será generado por la base de datos
                        txtSupplierName.getText(),
                        txtContactPerson.getText(),
                        txtPhone.getText(),
                        txtEmail.getText(),
                        txtAddress.getText()
                    );
                    
                    supplierController.addSupplier(supplier);
                    JOptionPane.showMessageDialog(null, "Proveedor agregado exitosamente.");
                    
                    clearFields();
                    loadSupplierData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, 
                        "Error al agregar el proveedor: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        btnUpdateSupplier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (txtSupplierId.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, 
                            "Por favor, seleccione un proveedor de la tabla para actualizar.",
                            "Selección requerida", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    if (!validateFields()) {
                        return;
                    }
                    
                    Supplier supplier = new Supplier(
                        Integer.parseInt(txtSupplierId.getText()),
                        txtSupplierName.getText(),
                        txtContactPerson.getText(),
                        txtPhone.getText(),
                        txtEmail.getText(),
                        txtAddress.getText()
                    );
                    
                    supplierController.updateSupplier(supplier);
                    JOptionPane.showMessageDialog(null, "Proveedor actualizado exitosamente.");
                    
                    clearFields();
                    loadSupplierData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, 
                        "Error al actualizar el proveedor: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        btnDeleteSupplier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (txtSupplierId.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, 
                            "Por favor, seleccione un proveedor de la tabla para eliminar.",
                            "Selección requerida", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    int confirm = JOptionPane.showConfirmDialog(null, 
                        "¿Está seguro de que desea eliminar este proveedor?",
                        "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        int supplierId = Integer.parseInt(txtSupplierId.getText());
                        supplierController.deleteSupplier(supplierId);
                        JOptionPane.showMessageDialog(null, "Proveedor eliminado exitosamente.");
                        
                        clearFields();
                        loadSupplierData();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, 
                        "Error al eliminar el proveedor: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        btnClearFields.addActionListener(e -> clearFields());
        
        // Listener para selección en la tabla
        tblSuppliers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblSuppliers.getSelectedRow();
                if (selectedRow >= 0) {
                    txtSupplierId.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    txtSupplierName.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    txtContactPerson.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    txtPhone.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    txtEmail.setText(tableModel.getValueAt(selectedRow, 4).toString());
                    txtAddress.setText(tableModel.getValueAt(selectedRow, 5).toString());
                }
            }
        });
    }
    
    private void addFormField(JPanel panel, GridBagConstraints constraints, String labelText, JTextField textField, int row) {
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.gridwidth = 1;
        JLabel label = new JLabel(labelText);
        panel.add(label, constraints);
        
        constraints.gridx = 1;
        constraints.weightx = 1.0;
        panel.add(textField, constraints);
        constraints.weightx = 0.0;
    }
    
    private boolean validateFields() {
        if (txtSupplierName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre del proveedor es obligatorio.");
            txtSupplierName.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void clearFields() {
        txtSupplierId.setText("");
        txtSupplierName.setText("");
        txtContactPerson.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        tblSuppliers.clearSelection();
        txtSupplierName.requestFocus();
    }
    
    private void loadSupplierData() {
        // Limpiar tabla
        tableModel.setRowCount(0);
        
        try {
            // Obtener todos los proveedores
            List<Supplier> suppliers = supplierController.getAllSuppliers();
            
            // Llenar tabla con datos
            for (Supplier supplier : suppliers) {
                Object[] row = {
                    supplier.getId(),
                    supplier.getName(),
                    supplier.getContactPerson(),
                    supplier.getPhone(),
                    supplier.getEmail(),
                    supplier.getAddress()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al cargar los proveedores: " + e.getMessage(),
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
            SupplierManagementView view = new SupplierManagementView();
            view.setVisible(true);
        });
    }
} 