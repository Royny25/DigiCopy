import javax.swing.*;
import java.awt.*;

public class OrderManagementView extends JFrame {
    private JTextField txtOrderId;
    private JTextField txtOrderDetails;
    private JButton btnCreateOrder;
    private JTextArea txtAreaOrderHistory;

    public OrderManagementView() {
        setTitle("Gestión de Órdenes");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel orderPanel = new JPanel();
        orderPanel.setLayout(new GridLayout(3, 2));
        orderPanel.add(new JLabel("ID Orden:"));
        txtOrderId = new JTextField();
        orderPanel.add(txtOrderId);

        orderPanel.add(new JLabel("Detalles de la Orden:"));
        txtOrderDetails = new JTextField();
        orderPanel.add(txtOrderDetails);

        btnCreateOrder = new JButton("Crear Orden");
        orderPanel.add(btnCreateOrder);

        panel.add(orderPanel, BorderLayout.NORTH);

        txtAreaOrderHistory = new JTextArea();
        txtAreaOrderHistory.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtAreaOrderHistory);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OrderManagementView view = new OrderManagementView();
            view.setVisible(true);
        });
    }
} 