import database.DatabaseConnection;
import gui.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Initialize database
        DatabaseConnection.initializeDatabase();

        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }

        // Run GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });

        // Add shutdown hook to close database connection
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DatabaseConnection.closeConnection();
            System.out.println("Database connection closed.");
        }));
    }
}
