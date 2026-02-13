package gui;

import dao.PassengerDAO;
import model.Passenger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PassengerPanel extends JPanel {
    private PassengerDAO passengerDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField passportField;
    private JTextField searchField;

    private int selectedPassengerId = -1;

    public PassengerPanel() {
        passengerDAO = new PassengerDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(createFormPanel(), BorderLayout.WEST);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createSearchPanel(), BorderLayout.SOUTH);

        loadPassengers();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Passenger Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // First Name
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        firstNameField = new JTextField(15);
        panel.add(firstNameField, gbc);

        // Last Name
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        lastNameField = new JTextField(15);
        panel.add(lastNameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(15);
        panel.add(emailField, gbc);

        // Phone
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(15);
        panel.add(phoneField, gbc);

        // Passport Number
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Passport Number:"), gbc);
        gbc.gridx = 1;
        passportField = new JTextField(15);
        panel.add(passportField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");

        addBtn.addActionListener(e -> addPassenger());
        updateBtn.addActionListener(e -> updatePassenger());
        deleteBtn.addActionListener(e -> deletePassenger());
        clearBtn.addActionListener(e -> clearForm());

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Passengers List"));

        String[] columns = {"ID", "First Name", "Last Name", "Email", "Phone", "Passport"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    populateFormFromTable(row);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        panel.add(searchField);

        JButton searchBtn = new JButton("Search");
        JButton refreshBtn = new JButton("Refresh");

        searchBtn.addActionListener(e -> searchPassengers());
        refreshBtn.addActionListener(e -> loadPassengers());

        panel.add(searchBtn);
        panel.add(refreshBtn);

        return panel;
    }

    private void loadPassengers() {
        try {
            List<Passenger> passengers = passengerDAO.getAll();
            refreshTable(passengers);
        } catch (SQLException e) {
            showError("Error loading passengers: " + e.getMessage());
        }
    }

    private void refreshTable(List<Passenger> passengers) {
        tableModel.setRowCount(0);
        for (Passenger p : passengers) {
            tableModel.addRow(new Object[]{
                p.getPassengerId(), p.getFirstName(), p.getLastName(),
                p.getEmail(), p.getPhone(), p.getPassportNumber()
            });
        }
    }

    private void populateFormFromTable(int row) {
        selectedPassengerId = (int) tableModel.getValueAt(row, 0);
        firstNameField.setText((String) tableModel.getValueAt(row, 1));
        lastNameField.setText((String) tableModel.getValueAt(row, 2));
        emailField.setText((String) tableModel.getValueAt(row, 3));
        phoneField.setText((String) tableModel.getValueAt(row, 4));
        passportField.setText((String) tableModel.getValueAt(row, 5));
    }

    private void addPassenger() {
        if (!validateForm()) return;

        try {
            Passenger passenger = createPassengerFromForm();
            passengerDAO.create(passenger);
            loadPassengers();
            clearForm();
            showInfo("Passenger added successfully!");
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                showError("A passenger with this passport number already exists.");
            } else {
                showError("Error adding passenger: " + e.getMessage());
            }
        }
    }

    private void updatePassenger() {
        if (selectedPassengerId == -1) {
            showError("Please select a passenger to update.");
            return;
        }
        if (!validateForm()) return;

        try {
            Passenger passenger = createPassengerFromForm();
            passenger.setPassengerId(selectedPassengerId);
            passengerDAO.update(passenger);
            loadPassengers();
            clearForm();
            showInfo("Passenger updated successfully!");
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                showError("A passenger with this passport number already exists.");
            } else {
                showError("Error updating passenger: " + e.getMessage());
            }
        }
    }

    private void deletePassenger() {
        if (selectedPassengerId == -1) {
            showError("Please select a passenger to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this passenger?\nThis will also delete all their bookings.",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                passengerDAO.delete(selectedPassengerId);
                loadPassengers();
                clearForm();
                showInfo("Passenger deleted successfully!");
            } catch (SQLException e) {
                showError("Error deleting passenger: " + e.getMessage());
            }
        }
    }

    private void searchPassengers() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadPassengers();
            return;
        }

        try {
            List<Passenger> passengers = passengerDAO.search(keyword);
            refreshTable(passengers);
        } catch (SQLException e) {
            showError("Error searching passengers: " + e.getMessage());
        }
    }

    private void clearForm() {
        selectedPassengerId = -1;
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        passportField.setText("");
        table.clearSelection();
    }

    private boolean validateForm() {
        if (firstNameField.getText().trim().isEmpty() ||
            lastNameField.getText().trim().isEmpty() ||
            emailField.getText().trim().isEmpty() ||
            passportField.getText().trim().isEmpty()) {
            showError("Please fill in all required fields (First Name, Last Name, Email, Passport).");
            return false;
        }

        String email = emailField.getText().trim();
        if (!email.contains("@") || !email.contains(".")) {
            showError("Please enter a valid email address.");
            return false;
        }

        return true;
    }

    private Passenger createPassengerFromForm() {
        Passenger passenger = new Passenger();
        passenger.setFirstName(firstNameField.getText().trim());
        passenger.setLastName(lastNameField.getText().trim());
        passenger.setEmail(emailField.getText().trim());
        passenger.setPhone(phoneField.getText().trim());
        passenger.setPassportNumber(passportField.getText().trim());
        return passenger;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
