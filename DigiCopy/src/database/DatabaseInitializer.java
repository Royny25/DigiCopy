package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Clase encargada de inicializar la base de datos
 */
public class DatabaseInitializer {
    
    /**
     * Inicializa la base de datos creando las tablas necesarias
     * @return true si la inicialización fue exitosa, false en caso contrario
     */
    public static boolean initializeDatabase() {
        try {
            // Verificar si ya existe el archivo de base de datos
            File dbFile = new File("inventory.db");
            boolean isNewDb = !dbFile.exists();
            
            // Obtener conexión
            Connection conn = DatabaseConnection.getConnection();
            
            // Si es nueva base de datos, ejecutar script de inicialización
            if (isNewDb) {
                System.out.println("Creando nueva base de datos...");
                executeScript(conn, "sql/init_database.sql");
                JOptionPane.showMessageDialog(null, 
                    "Base de datos creada exitosamente con datos de muestra.", 
                    "Inicialización", JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.out.println("Base de datos ya existe, verificando estructura...");
                // Aquí podríamos verificar la estructura y actualizarla si es necesario
            }
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error al inicializar la base de datos: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Ejecuta un script SQL desde un archivo
     * @param conn Conexión a la base de datos
     * @param scriptPath Ruta al archivo de script SQL
     */
    private static void executeScript(Connection conn, String scriptPath) throws IOException, SQLException {
        StringBuilder script = new StringBuilder();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(scriptPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Ignorar comentarios
                if (line.startsWith("--")) continue;
                
                script.append(line);
                script.append("\n");
                
                // Si la línea termina con punto y coma, ejecutar como una declaración
                if (line.trim().endsWith(";")) {
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute(script.toString());
                    }
                    script.setLength(0);
                }
            }
        }
    }
} 