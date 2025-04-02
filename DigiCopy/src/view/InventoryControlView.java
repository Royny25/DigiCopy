import javax.swing.*;
import java.awt.*;

public class InventoryControlView extends JFrame {
    private JTextField txtSearchProduct;
    private JButton btnSearch;
    private JTextArea txtAreaInventory;

    public InventoryControlView() {
        setTitle("Control de Inventario");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        searchPanel.add(new JLabel("Buscar Producto:"));
        txtSearchProduct = new JTextField(20);
        searchPanel.add(txtSearchProduct);
        btnSearch = new JButton("Buscar");
        searchPanel.add(btnSearch);

        panel.add(searchPanel, BorderLayout.NORTH);

        txtAreaInventory = new JTextArea();
        txtAreaInventory.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtAreaInventory);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InventoryControlView view = new InventoryControlView();
            view.setVisible(true);
        });
    }
} 