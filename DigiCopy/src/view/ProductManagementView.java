package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import controller.ProductController;
import controller.SupplierController;
import model.Product;

public class ProductManagementView extends JFrame {
    private JTextField txtProductId;
    private JTextField txtProductName;
    private JTextField txtProductDescription;
    private JTextField txtProductPrice;
    private JTextField txtProductStock;
    private JComboBox<String> cmbSupplier;
    private JButton btnAddProduct;
    private JButton btnUpdateProduct;
    private JButton btnDeleteProduct;
    private JButton btnClearFields;
    private JTable tblProducts;
    private DefaultTableModel tableModel;
    private ProductController productController;
    private SupplierController supplierController;

    public ProductManagementView() {
        // Configuración de la ventana
        setTitle("Gestión de Productos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Inicializar el controlador
        productController = new ProductController();
        supplierController = new SupplierController();
        
        // Crear componentes
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        // Panel de formulario para datos del producto
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Detalles del Producto"));
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);
        
        // Etiquetas y campos de texto
        addFormField(formPanel, constraints, "ID:", txtProductId = new JTextField(10), 0);
        txtProductId.setEditable(false); // El ID es generado automáticamente
        
        addFormField(formPanel, constraints, "Nombre:", txtProductName = new JTextField(20), 1);
        addFormField(formPanel, constraints, "Descripción:", txtProductDescription = new JTextField(30), 2);
        addFormField(formPanel, constraints, "Precio:", txtProductPrice = new JTextField(10), 3);
        addFormField(formPanel, constraints, "Stock:", txtProductStock = new JTextField(10), 4);
        
        // ComboBox para el proveedor
        cmbSupplier = new JComboBox<>();
        loadSuppliers();
        addFormField(formPanel, constraints, "Proveedor:", cmbSupplier, 5);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAddProduct = new JButton("Agregar");
        btnUpdateProduct = new JButton("Actualizar");
        btnDeleteProduct = new JButton("Eliminar");
        btnClearFields = new JButton("Limpiar");
        
        buttonPanel.add(btnAddProduct);
        buttonPanel.add(btnUpdateProduct);
        buttonPanel.add(btnDeleteProduct);
        buttonPanel.add(btnClearFields);
        
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 2;
        formPanel.add(buttonPanel, constraints);
        
        // Panel de tabla
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Lista de Productos"));
        
        // Crear modelo de tabla
        String[] columnNames = {"ID", "Nombre", "Descripción", "Precio", "Stock", "Proveedor"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla no sea editable
            }
        };
        
        tblProducts = new JTable(tableModel);
        tblProducts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblProducts.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JScrollPane scrollPane = new JScrollPane(tblProducts);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Agregar paneles al panel principal
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        // Agregar panel principal a la ventana
        add(mainPanel);
        
        // Cargar datos iniciales
        loadProductData();
        
        // Agregar listeners a los botones
        btnAddProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!validateFields()) {
                        return;
                    }
                    
                    Product product = new Product(
                        0, // ID será generado por la base de datos
                        txtProductName.getText(),
                        txtProductDescription.getText(),
                        Double.parseDouble(txtProductPrice.getText()),
                        Integer.parseInt(txtProductStock.getText()),
                        cmbSupplier.getSelectedItem().toString()
                    );
                    
                    productController.addProduct(product);
                    JOptionPane.showMessageDialog(null, "Producto agregado exitosamente.");
                    
                    clearFields();
                    loadProductData();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, 
                        "Por favor, ingrese valores numéricos válidos para precio y stock.",
                        "Error de Formato", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, 
                        "Error al agregar el producto: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        btnUpdateProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (txtProductId.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, 
                            "Por favor, seleccione un producto de la tabla para actualizar.",
                            "Selección requerida", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    if (!validateFields()) {
                        return;
                    }
                    
                    Product product = new Product(
                        Integer.parseInt(txtProductId.getText()),
                        txtProductName.getText(),
                        txtProductDescription.getText(),
                        Double.parseDouble(txtProductPrice.getText()),
                        Integer.parseInt(txtProductStock.getText()),
                        cmbSupplier.getSelectedItem().toString()
                    );
                    
                    productController.updateProduct(product);
                    JOptionPane.showMessageDialog(null, "Producto actualizado exitosamente.");
                    
                    clearFields();
                    loadProductData();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, 
                        "Por favor, ingrese valores numéricos válidos para precio y stock.",
                        "Error de Formato", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, 
                        "Error al actualizar el producto: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        btnDeleteProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (txtProductId.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, 
                            "Por favor, seleccione un producto de la tabla para eliminar.",
                            "Selección requerida", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    int confirm = JOptionPane.showConfirmDialog(null, 
                        "¿Está seguro de que desea eliminar este producto?",
                        "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        int productId = Integer.parseInt(txtProductId.getText());
                        productController.deleteProduct(productId);
                        JOptionPane.showMessageDialog(null, "Producto eliminado exitosamente.");
                        
                        clearFields();
                        loadProductData();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, 
                        "Error al eliminar el producto: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        btnClearFields.addActionListener(e -> clearFields());
        
        // Listener para selección en la tabla
        tblProducts.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblProducts.getSelectedRow();
                if (selectedRow >= 0) {
                    txtProductId.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    txtProductName.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    txtProductDescription.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    txtProductPrice.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    txtProductStock.setText(tableModel.getValueAt(selectedRow, 4).toString());
                    
                    String supplierName = tableModel.getValueAt(selectedRow, 5).toString();
                    for (int i = 0; i < cmbSupplier.getItemCount(); i++) {
                        if (cmbSupplier.getItemAt(i).equals(supplierName)) {
                            cmbSupplier.setSelectedIndex(i);
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
        if (txtProductName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre del producto es obligatorio.");
            txtProductName.requestFocus();
            return false;
        }
        
        try {
            if (!txtProductPrice.getText().trim().isEmpty()) {
                Double.parseDouble(txtProductPrice.getText());
            } else {
                JOptionPane.showMessageDialog(null, "El precio es obligatorio.");
                txtProductPrice.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El precio debe ser un número válido.");
            txtProductPrice.requestFocus();
            return false;
        }
        
        try {
            if (!txtProductStock.getText().trim().isEmpty()) {
                Integer.parseInt(txtProductStock.getText());
            } else {
                JOptionPane.showMessageDialog(null, "El stock es obligatorio.");
                txtProductStock.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El stock debe ser un número entero válido.");
            txtProductStock.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void clearFields() {
        txtProductId.setText("");
        txtProductName.setText("");
        txtProductDescription.setText("");
        txtProductPrice.setText("");
        txtProductStock.setText("");
        if (cmbSupplier.getItemCount() > 0) {
            cmbSupplier.setSelectedIndex(0);
        }
        tblProducts.clearSelection();
        txtProductName.requestFocus();
    }
    
    private void loadProductData() {
        // Limpiar tabla
        tableModel.setRowCount(0);
        
        try {
            // Obtener todos los productos
            List<Product> products = productController.getAllProducts();
            
            // Llenar tabla con datos
            for (Product product : products) {
                Object[] row = {
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStock(),
                    product.getSupplierName()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al cargar los productos: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadSuppliers() {
        try {
            // Limpiar ComboBox
            cmbSupplier.removeAllItems();
            
            // Agregar opción por defecto
            cmbSupplier.addItem("Sin proveedor");
            
            // Obtener todos los proveedores
            List<String> supplierNames = productController.getAllSupplierNames();
            for (String name : supplierNames) {
                cmbSupplier.addItem(name);
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
            ProductManagementView view = new ProductManagementView();
            view.setVisible(true);
        });
    }
} 