package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import controller.InvoiceController;
import controller.ProductController;
import model.Invoice;
import model.Product;

public class BillingView extends JFrame {
    private JTextField txtInvoiceId;
    private JTextField txtCustomerName;
    private JTextField txtDate;
    private JTextField txtTotal;
    private JComboBox<Product> cmbProducts;
    private JTextField txtQuantity;
    private JTextField txtPrice;
    private JButton btnAddItem;
    private JButton btnRemoveItem;
    private JButton btnGenerateInvoice;
    private JButton btnClearForm;
    private JTable tblInvoiceItems;
    private JTable tblInvoices;
    private DefaultTableModel itemsTableModel;
    private DefaultTableModel invoicesTableModel;
    private InvoiceController invoiceController;
    private ProductController productController;
    private List<Product> invoiceItems;
    private double invoiceTotal;

    public BillingView() {
        // Configuración de la ventana
        setTitle("Facturación");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Inicializar controladores
        invoiceController = new InvoiceController();
        productController = new ProductController();
        
        // Inicializar lista de productos para la factura
        invoiceItems = new ArrayList<>();
        invoiceTotal = 0.0;
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel superior para los datos de la factura
        JPanel invoiceDataPanel = new JPanel(new GridBagLayout());
        invoiceDataPanel.setBorder(BorderFactory.createTitledBorder("Datos de la Factura"));
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);
        
        // Etiquetas y campos de texto para los datos de la factura
        addFormField(invoiceDataPanel, constraints, "ID:", txtInvoiceId = new JTextField(10), 0, 0);
        txtInvoiceId.setEditable(false);
        
        addFormField(invoiceDataPanel, constraints, "Cliente:", txtCustomerName = new JTextField(20), 0, 1);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        addFormField(invoiceDataPanel, constraints, "Fecha:", txtDate = new JTextField(dateFormat.format(new Date())), 0, 2);
        txtDate.setEditable(false);
        
        addFormField(invoiceDataPanel, constraints, "Total:", txtTotal = new JTextField("0.00"), 0, 3);
        txtTotal.setEditable(false);
        
        // Panel para agregar productos a la factura
        JPanel addItemsPanel = new JPanel(new GridBagLayout());
        addItemsPanel.setBorder(BorderFactory.createTitledBorder("Agregar Productos"));
        
        // ComboBox para seleccionar productos
        addFormField(addItemsPanel, constraints, "Producto:", cmbProducts = new JComboBox<>(), 0, 0);
        loadProductsCombo();
        
        // Campo para la cantidad
        addFormField(addItemsPanel, constraints, "Cantidad:", txtQuantity = new JTextField("1"), 0, 1);
        
        // Campo para el precio
        addFormField(addItemsPanel, constraints, "Precio:", txtPrice = new JTextField(), 0, 2);
        txtPrice.setEditable(false);
        
        // Botones para agregar y quitar productos
        JPanel itemButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAddItem = new JButton("Agregar Producto");
        btnRemoveItem = new JButton("Quitar Producto");
        
        itemButtonsPanel.add(btnAddItem);
        itemButtonsPanel.add(btnRemoveItem);
        
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        addItemsPanel.add(itemButtonsPanel, constraints);
        
        // Panel para la tabla de productos de la factura actual
        JPanel currentItemsPanel = new JPanel(new BorderLayout());
        currentItemsPanel.setBorder(BorderFactory.createTitledBorder("Productos en esta Factura"));
        
        // Modelo de tabla para los productos de la factura
        String[] itemsColumnNames = {"Producto", "Cantidad", "Precio Unitario", "Total"};
        itemsTableModel = new DefaultTableModel(itemsColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblInvoiceItems = new JTable(itemsTableModel);
        tblInvoiceItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane itemsScrollPane = new JScrollPane(tblInvoiceItems);
        currentItemsPanel.add(itemsScrollPane, BorderLayout.CENTER);
        
        // Panel para los botones de la factura
        JPanel invoiceButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnGenerateInvoice = new JButton("Generar Factura");
        btnClearForm = new JButton("Limpiar");
        
        invoiceButtonsPanel.add(btnGenerateInvoice);
        invoiceButtonsPanel.add(btnClearForm);
        
        // Panel para el historial de facturas
        JPanel invoiceHistoryPanel = new JPanel(new BorderLayout());
        invoiceHistoryPanel.setBorder(BorderFactory.createTitledBorder("Historial de Facturas"));
        
        // Modelo de tabla para el historial de facturas
        String[] invoicesColumnNames = {"ID", "Fecha", "Cliente", "Total", "Estado"};
        invoicesTableModel = new DefaultTableModel(invoicesColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblInvoices = new JTable(invoicesTableModel);
        tblInvoices.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane invoicesScrollPane = new JScrollPane(tblInvoices);
        invoiceHistoryPanel.add(invoicesScrollPane, BorderLayout.CENTER);
        
        // Organizar los paneles en el panel principal
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        topPanel.add(invoiceDataPanel);
        topPanel.add(addItemsPanel);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(currentItemsPanel, BorderLayout.CENTER);
        centerPanel.add(invoiceButtonsPanel, BorderLayout.SOUTH);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(invoiceHistoryPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Cargar datos iniciales
        loadInvoicesData();
        
        // Evento para actualizar el precio cuando se selecciona un producto
        cmbProducts.addActionListener(e -> {
            Product selectedProduct = (Product) cmbProducts.getSelectedItem();
            if (selectedProduct != null) {
                txtPrice.setText(String.valueOf(selectedProduct.getPrice()));
            }
        });
        
        // Evento para agregar un producto a la factura
        btnAddItem.addActionListener(e -> {
            try {
                Product selectedProduct = (Product) cmbProducts.getSelectedItem();
                if (selectedProduct == null) {
                    JOptionPane.showMessageDialog(null, "Por favor seleccione un producto.");
                    return;
                }
                
                int quantity = 1;
                try {
                    quantity = Integer.parseInt(txtQuantity.getText());
                    if (quantity <= 0) {
                        JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor a cero.");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese una cantidad válida.");
                    return;
                }
                
                double price = selectedProduct.getPrice();
                double totalItem = price * quantity;
                
                // Agregar a la tabla
                Object[] row = {
                    selectedProduct.getName(),
                    quantity,
                    price,
                    totalItem
                };
                itemsTableModel.addRow(row);
                
                // Agregar a la lista de productos
                invoiceItems.add(selectedProduct);
                
                // Actualizar el total
                invoiceTotal += totalItem;
                txtTotal.setText(String.format("%.2f", invoiceTotal));
                
                // Limpiar campos
                txtQuantity.setText("1");
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al agregar el producto: " + ex.getMessage());
            }
        });
        
        // Evento para quitar un producto de la factura
        btnRemoveItem.addActionListener(e -> {
            int selectedRow = tblInvoiceItems.getSelectedRow();
            if (selectedRow >= 0) {
                // Restar del total
                double rowTotal = (double) itemsTableModel.getValueAt(selectedRow, 3);
                invoiceTotal -= rowTotal;
                txtTotal.setText(String.format("%.2f", invoiceTotal));
                
                // Quitar de la lista y la tabla
                invoiceItems.remove(selectedRow);
                itemsTableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(null, "Por favor seleccione un producto para quitar.");
            }
        });
        
        // Evento para generar la factura
        btnGenerateInvoice.addActionListener(e -> {
            try {
                if (invoiceItems.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No hay productos en la factura.");
                    return;
                }
                
                if (txtCustomerName.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese el nombre del cliente.");
                    return;
                }
                
                // Crear la factura
                Invoice invoice = new Invoice(
                    0, // ID será generado por la base de datos
                    new Date(),
                    txtCustomerName.getText(),
                    invoiceTotal,
                    "Pagada" // Estado por defecto
                );
                
                // Guardar la factura
                invoiceController.addInvoice(invoice);
                
                // Aquí se deberían guardar los detalles de la factura también
                
                JOptionPane.showMessageDialog(null, "Factura generada exitosamente.");
                
                // Actualizar lista de facturas
                loadInvoicesData();
                
                // Limpiar el formulario
                clearForm();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al generar la factura: " + ex.getMessage());
            }
        });
        
        // Evento para limpiar el formulario
        btnClearForm.addActionListener(e -> clearForm());
        
        // Evento al seleccionar una factura del historial
        tblInvoices.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Doble clic
                    int selectedRow = tblInvoices.getSelectedRow();
                    if (selectedRow >= 0) {
                        int invoiceId = (int) invoicesTableModel.getValueAt(selectedRow, 0);
                        // Aquí se debería cargar la factura seleccionada
                        JOptionPane.showMessageDialog(null, 
                            "La visualización de facturas existentes estará disponible en la próxima versión.", 
                            "En desarrollo", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
    }
    
    private void addFormField(JPanel panel, GridBagConstraints constraints, 
                             String labelText, JComponent component, int gridx, int gridy) {
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = 1;
        JLabel label = new JLabel(labelText);
        panel.add(label, constraints);
        
        constraints.gridx = gridx + 1;
        constraints.weightx = 1.0;
        panel.add(component, constraints);
        constraints.weightx = 0.0;
    }
    
    private void loadProductsCombo() {
        try {
            List<Product> products = productController.getAllProducts();
            for (Product product : products) {
                cmbProducts.addItem(product);
            }
            if (!products.isEmpty()) {
                txtPrice.setText(String.valueOf(products.get(0).getPrice()));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los productos: " + e.getMessage());
        }
    }
    
    private void loadInvoicesData() {
        // Limpiar tabla
        invoicesTableModel.setRowCount(0);
        
        try {
            // Obtener todas las facturas
            List<Invoice> invoices = invoiceController.getAllInvoices();
            
            // Formato para la fecha
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            
            // Llenar tabla con datos
            for (Invoice invoice : invoices) {
                Object[] row = {
                    invoice.getId(),
                    dateFormat.format(invoice.getDate()),
                    invoice.getCustomerName(),
                    String.format("%.2f", invoice.getTotal()),
                    invoice.getStatus()
                };
                invoicesTableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar las facturas: " + e.getMessage());
        }
    }
    
    private void clearForm() {
        txtInvoiceId.setText("");
        txtCustomerName.setText("");
        txtDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        txtTotal.setText("0.00");
        txtQuantity.setText("1");
        
        // Limpiar tabla de productos
        itemsTableModel.setRowCount(0);
        
        // Reiniciar lista de productos y total
        invoiceItems.clear();
        invoiceTotal = 0.0;
        
        // Seleccionar primer producto
        if (cmbProducts.getItemCount() > 0) {
            cmbProducts.setSelectedIndex(0);
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            BillingView view = new BillingView();
            view.setVisible(true);
        });
    }
} 