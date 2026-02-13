package gui;

import dao.FlightDAO;
import model.Flight;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class FlightPanel extends JPanel {
    private FlightDAO flightDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField flightNumberField;
    private JTextField airlineField;
    private JTextField originField;
    private JTextField destinationField;
    private JTextField departureTimeField;
    private JTextField arrivalTimeField;
    private JTextField totalSeatsField;
    private JTextField availableSeatsField;
    private JComboBox<String> statusCombo;
    private JTextField searchField;

    private int selectedFlightId = -1;

    public FlightPanel() {
        flightDAO = new FlightDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(createFormPanel(), BorderLayout.WEST);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createSearchPanel(), BorderLayout.SOUTH);

        loadFlights();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Flight Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Flight Number
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Flight Number:"), gbc);
        gbc.gridx = 1;
        flightNumberField = new JTextField(15);
        panel.add(flightNumberField, gbc);

        // Airline
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Airline:"), gbc);
        gbc.gridx = 1;
        airlineField = new JTextField(15);
        panel.add(airlineField, gbc);

        // Origin
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Origin:"), gbc);
        gbc.gridx = 1;
        originField = new JTextField(15);
        panel.add(originField, gbc);

        // Destination
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Destination:"), gbc);
        gbc.gridx = 1;
        destinationField = new JTextField(15);
        panel.add(destinationField, gbc);

        // Departure Time
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Departure (YYYY-MM-DD HH:MM):"), gbc);
        gbc.gridx = 1;
        departureTimeField = new JTextField(15);
        panel.add(departureTimeField, gbc);

        // Arrival Time
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Arrival (YYYY-MM-DD HH:MM):"), gbc);
        gbc.gridx = 1;
        arrivalTimeField = new JTextField(15);
        panel.add(arrivalTimeField, gbc);

        // Total Seats
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Total Seats:"), gbc);
        gbc.gridx = 1;
        totalSeatsField = new JTextField(15);
        panel.add(totalSeatsField, gbc);

        // Available Seats
        gbc.gridx = 0; gbc.gridy = 7;
        panel.add(new JLabel("Available Seats:"), gbc);
        gbc.gridx = 1;
        availableSeatsField = new JTextField(15);
        panel.add(availableSeatsField, gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 8;
        panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        statusCombo = new JComboBox<>(new String[]{"SCHEDULED", "DEPARTED", "ARRIVED", "CANCELLED"});
        panel.add(statusCombo, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");

        addBtn.addActionListener(e -> addFlight());
        updateBtn.addActionListener(e -> updateFlight());
        deleteBtn.addActionListener(e -> deleteFlight());
        clearBtn.addActionListener(e -> clearForm());

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);

        gbc.gridx = 0; gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Flights List"));

        String[] columns = {"ID", "Flight #", "Airline", "Origin", "Destination",
                           "Departure", "Arrival", "Total", "Available", "Status"};
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

        searchBtn.addActionListener(e -> searchFlights());
        refreshBtn.addActionListener(e -> loadFlights());

        panel.add(searchBtn);
        panel.add(refreshBtn);

        return panel;
    }

    private void loadFlights() {
        try {
            List<Flight> flights = flightDAO.getAll();
            refreshTable(flights);
        } catch (SQLException e) {
            showError("Error loading flights: " + e.getMessage());
        }
    }

    private void refreshTable(List<Flight> flights) {
        tableModel.setRowCount(0);
        for (Flight f : flights) {
            tableModel.addRow(new Object[]{
                f.getFlightId(), f.getFlightNumber(), f.getAirline(),
                f.getOrigin(), f.getDestination(), f.getDepartureTime(),
                f.getArrivalTime(), f.getTotalSeats(), f.getAvailableSeats(),
                f.getStatus()
            });
        }
    }

    private void populateFormFromTable(int row) {
        selectedFlightId = (int) tableModel.getValueAt(row, 0);
        flightNumberField.setText((String) tableModel.getValueAt(row, 1));
        airlineField.setText((String) tableModel.getValueAt(row, 2));
        originField.setText((String) tableModel.getValueAt(row, 3));
        destinationField.setText((String) tableModel.getValueAt(row, 4));
        departureTimeField.setText((String) tableModel.getValueAt(row, 5));
        arrivalTimeField.setText((String) tableModel.getValueAt(row, 6));
        totalSeatsField.setText(String.valueOf(tableModel.getValueAt(row, 7)));
        availableSeatsField.setText(String.valueOf(tableModel.getValueAt(row, 8)));
        statusCombo.setSelectedItem(tableModel.getValueAt(row, 9));
    }

    private void addFlight() {
        if (!validateForm()) return;

        try {
            Flight flight = createFlightFromForm();
            flight.setAvailableSeats(flight.getTotalSeats()); // New flight has all seats available
            flightDAO.create(flight);
            loadFlights();
            clearForm();
            showInfo("Flight added successfully!");
        } catch (SQLException e) {
            showError("Error adding flight: " + e.getMessage());
        }
    }

    private void updateFlight() {
        if (selectedFlightId == -1) {
            showError("Please select a flight to update.");
            return;
        }
        if (!validateForm()) return;

        try {
            Flight flight = createFlightFromForm();
            flight.setFlightId(selectedFlightId);
            flightDAO.update(flight);
            loadFlights();
            clearForm();
            showInfo("Flight updated successfully!");
        } catch (SQLException e) {
            showError("Error updating flight: " + e.getMessage());
        }
    }

    private void deleteFlight() {
        if (selectedFlightId == -1) {
            showError("Please select a flight to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this flight?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                flightDAO.delete(selectedFlightId);
                loadFlights();
                clearForm();
                showInfo("Flight deleted successfully!");
            } catch (SQLException e) {
                showError("Error deleting flight: " + e.getMessage());
            }
        }
    }

    private void searchFlights() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadFlights();
            return;
        }

        try {
            List<Flight> flights = flightDAO.search(keyword);
            refreshTable(flights);
        } catch (SQLException e) {
            showError("Error searching flights: " + e.getMessage());
        }
    }

    private void clearForm() {
        selectedFlightId = -1;
        flightNumberField.setText("");
        airlineField.setText("");
        originField.setText("");
        destinationField.setText("");
        departureTimeField.setText("");
        arrivalTimeField.setText("");
        totalSeatsField.setText("");
        availableSeatsField.setText("");
        statusCombo.setSelectedIndex(0);
        table.clearSelection();
    }

    private boolean validateForm() {
        if (flightNumberField.getText().trim().isEmpty() ||
            airlineField.getText().trim().isEmpty() ||
            originField.getText().trim().isEmpty() ||
            destinationField.getText().trim().isEmpty() ||
            departureTimeField.getText().trim().isEmpty() ||
            arrivalTimeField.getText().trim().isEmpty() ||
            totalSeatsField.getText().trim().isEmpty()) {
            showError("Please fill in all required fields.");
            return false;
        }

        try {
            Integer.parseInt(totalSeatsField.getText().trim());
            if (!availableSeatsField.getText().trim().isEmpty()) {
                Integer.parseInt(availableSeatsField.getText().trim());
            }
        } catch (NumberFormatException e) {
            showError("Seats must be valid numbers.");
            return false;
        }

        return true;
    }

    private Flight createFlightFromForm() {
        Flight flight = new Flight();
        flight.setFlightNumber(flightNumberField.getText().trim());
        flight.setAirline(airlineField.getText().trim());
        flight.setOrigin(originField.getText().trim());
        flight.setDestination(destinationField.getText().trim());
        flight.setDepartureTime(departureTimeField.getText().trim());
        flight.setArrivalTime(arrivalTimeField.getText().trim());
        flight.setTotalSeats(Integer.parseInt(totalSeatsField.getText().trim()));

        String availSeats = availableSeatsField.getText().trim();
        flight.setAvailableSeats(availSeats.isEmpty() ?
            flight.getTotalSeats() : Integer.parseInt(availSeats));

        flight.setStatus((String) statusCombo.getSelectedItem());
        return flight;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
