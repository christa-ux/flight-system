package gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Flight Booking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Add panels
        tabbedPane.addTab("Flights", new FlightPanel());
        tabbedPane.addTab("Passengers", new PassengerPanel());
        tabbedPane.addTab("Bookings", new BookingPanel());

        add(tabbedPane, BorderLayout.CENTER);

        // Add menu bar
        setJMenuBar(createMenuBar());
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
            "Flight Booking System v1.0\n\n" +
            "A complete flight management system with:\n" +
            "- Flight Management\n" +
            "- Passenger Management\n" +
            "- Booking Management\n\n" +
            "Built with Java Swing and SQLite",
            "About",
            JOptionPane.INFORMATION_MESSAGE);
    }
}
