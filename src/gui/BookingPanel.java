package gui;

import dao.BookingDAO;
import dao.FlightDAO;
import dao.PassengerDAO;
import model.Booking;
import model.Flight;
import model.Passenger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class BookingPanel extends JPanel {
    private BookingDAO bookingDAO;
    private FlightDAO flightDAO;
    private PassengerDAO passengerDAO;

    private JTable table;
    private DefaultTableModel tableModel;

    private JComboBox<FlightItem> flightCombo;
    private JComboBox<PassengerItem> passengerCombo;
    private JTextField bookingDateField;
    private JTextField seatNumberField;
    private JComboBox<String> statusCombo;
    private JTextField searchField;

    private int selectedBookingId = -1;
    private int originalFlightId = -1;
    private String originalStatus = null;

    public BookingPanel() {
        bookingDAO = new BookingDAO();
        flightDAO = new FlightDAO();
        passengerDAO = new PassengerDAO();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(createFormPanel(), BorderLayout.WEST);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createSearchPanel(), BorderLayout.SOUTH);

        loadBookings();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Booking Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Flight Selection
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Flight:"), gbc);
        gbc.gridx = 1;
        flightCombo = new JComboBox<>();
        flightCombo.setPreferredSize(new Dimension(250, 25));
        loadFlightCombo();
        panel.add(flightCombo, gbc);

        // Passenger Selection
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Passenger:"), gbc);
        gbc.gridx = 1;
        passengerCombo = new JComboBox<>();
        passengerCombo.setPreferredSize(new Dimension(250, 25));
        loadPassengerCombo();
        panel.add(passengerCombo, gbc);

        // Booking Date
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Booking Date:"), gbc);
        gbc.gridx = 1;
        bookingDateField = new JTextField(15);
        bookingDateField.setText(LocalDate.now().toString());
        panel.add(bookingDateField, gbc);

        // Seat Number
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Seat Number:"), gbc);
        gbc.gridx = 1;
        seatNumberField = new JTextField(15);
        panel.add(seatNumberField, gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        statusCombo = new JComboBox<>(new String[]{"CONFIRMED", "CANCELLED", "COMPLETED"});
        panel.add(statusCombo, gbc);

        // Refresh combos button
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        JButton refreshCombosBtn = new JButton("Refresh Flights/Passengers");
        refreshCombosBtn.addActionListener(e -> {
            loadFlightCombo();
            loadPassengerCombo();
        });
        panel.add(refreshCombosBtn, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");

        addBtn.addActionListener(e -> addBooking());
        updateBtn.addActionListener(e -> updateBooking());
        deleteBtn.addActionListener(e -> deleteBooking());
        clearBtn.addActionListener(e -> clearForm());

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);

        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Bookings List"));

        String[] columns = {"ID", "Flight", "Passenger", "Date", "Seat", "Status"};
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

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(60);
        table.getColumnModel().getColumn(5).setPreferredWidth(80);

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

        searchBtn.addActionListener(e -> searchBookings());
        refreshBtn.addActionListener(e -> loadBookings());

        panel.add(searchBtn);
        panel.add(refreshBtn);

        return panel;
    }

    private void loadFlightCombo() {
        flightCombo.removeAllItems();
        try {
            List<Flight> flights = flightDAO.getAll();
            for (Flight f : flights) {
                if (f.getAvailableSeats() > 0 && "SCHEDULED".equals(f.getStatus())) {
                    flightCombo.addItem(new FlightItem(f));
                }
            }
        } catch (SQLException e) {
            showError("Error loading flights: " + e.getMessage());
        }
    }

    private void loadPassengerCombo() {
        passengerCombo.removeAllItems();
        try {
            List<Passenger> passengers = passengerDAO.getAll();
            for (Passenger p : passengers) {
                passengerCombo.addItem(new PassengerItem(p));
            }
        } catch (SQLException e) {
            showError("Error loading passengers: " + e.getMessage());
        }
    }

    private void loadBookings() {
        try {
            List<Booking> bookings = bookingDAO.getAll();
            refreshTable(bookings);
        } catch (SQLException e) {
            showError("Error loading bookings: " + e.getMessage());
        }
    }

    private void refreshTable(List<Booking> bookings) {
        tableModel.setRowCount(0);
        for (Booking b : bookings) {
            tableModel.addRow(new Object[]{
                b.getBookingId(), b.getFlightInfo(), b.getPassengerInfo(),
                b.getBookingDate(), b.getSeatNumber(), b.getStatus()
            });
        }
    }

    private void populateFormFromTable(int row) {
        selectedBookingId = (int) tableModel.getValueAt(row, 0);

        try {
            Booking booking = bookingDAO.getById(selectedBookingId);
            if (booking != null) {
                originalFlightId = booking.getFlightId();
                originalStatus = booking.getStatus();

                // Select flight in combo
                for (int i = 0; i < flightCombo.getItemCount(); i++) {
                    if (flightCombo.getItemAt(i).getId() == booking.getFlightId()) {
                        flightCombo.setSelectedIndex(i);
                        break;
                    }
                }

                // Select passenger in combo
                for (int i = 0; i < passengerCombo.getItemCount(); i++) {
                    if (passengerCombo.getItemAt(i).getId() == booking.getPassengerId()) {
                        passengerCombo.setSelectedIndex(i);
                        break;
                    }
                }

                bookingDateField.setText(booking.getBookingDate());
                seatNumberField.setText(booking.getSeatNumber());
                statusCombo.setSelectedItem(booking.getStatus());
            }
        } catch (SQLException e) {
            showError("Error loading booking details: " + e.getMessage());
        }
    }

    private void addBooking() {
        if (!validateForm()) return;

        FlightItem selectedFlight = (FlightItem) flightCombo.getSelectedItem();
        if (selectedFlight == null) {
            showError("Please select a flight.");
            return;
        }

        try {
            Booking booking = createBookingFromForm();
            bookingDAO.create(booking);

            // Decrease available seats
            flightDAO.updateAvailableSeats(booking.getFlightId(), -1);

            loadBookings();
            loadFlightCombo(); // Refresh to update available seats
            clearForm();
            showInfo("Booking created successfully!");
        } catch (SQLException e) {
            showError("Error creating booking: " + e.getMessage());
        }
    }

    private void updateBooking() {
        if (selectedBookingId == -1) {
            showError("Please select a booking to update.");
            return;
        }
        if (!validateForm()) return;

        try {
            Booking booking = createBookingFromForm();
            booking.setBookingId(selectedBookingId);

            String newStatus = (String) statusCombo.getSelectedItem();

            // Handle seat availability changes
            if ("CANCELLED".equals(newStatus) && !"CANCELLED".equals(originalStatus)) {
                // Cancelling a booking - restore seat
                flightDAO.updateAvailableSeats(originalFlightId, 1);
            } else if (!"CANCELLED".equals(newStatus) && "CANCELLED".equals(originalStatus)) {
                // Uncancelling a booking - take seat
                flightDAO.updateAvailableSeats(booking.getFlightId(), -1);
            }

            bookingDAO.update(booking);
            loadBookings();
            loadFlightCombo();
            clearForm();
            showInfo("Booking updated successfully!");
        } catch (SQLException e) {
            showError("Error updating booking: " + e.getMessage());
        }
    }

    private void deleteBooking() {
        if (selectedBookingId == -1) {
            showError("Please select a booking to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this booking?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Restore seat if booking wasn't cancelled
                if (!"CANCELLED".equals(originalStatus)) {
                    flightDAO.updateAvailableSeats(originalFlightId, 1);
                }

                bookingDAO.delete(selectedBookingId);
                loadBookings();
                loadFlightCombo();
                clearForm();
                showInfo("Booking deleted successfully!");
            } catch (SQLException e) {
                showError("Error deleting booking: " + e.getMessage());
            }
        }
    }

    private void searchBookings() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadBookings();
            return;
        }

        try {
            List<Booking> bookings = bookingDAO.search(keyword);
            refreshTable(bookings);
        } catch (SQLException e) {
            showError("Error searching bookings: " + e.getMessage());
        }
    }

    private void clearForm() {
        selectedBookingId = -1;
        originalFlightId = -1;
        originalStatus = null;
        if (flightCombo.getItemCount() > 0) {
            flightCombo.setSelectedIndex(0);
        }
        if (passengerCombo.getItemCount() > 0) {
            passengerCombo.setSelectedIndex(0);
        }
        bookingDateField.setText(LocalDate.now().toString());
        seatNumberField.setText("");
        statusCombo.setSelectedIndex(0);
        table.clearSelection();
    }

    private boolean validateForm() {
        if (flightCombo.getSelectedItem() == null) {
            showError("Please select a flight.");
            return false;
        }
        if (passengerCombo.getSelectedItem() == null) {
            showError("Please select a passenger.");
            return false;
        }
        if (bookingDateField.getText().trim().isEmpty()) {
            showError("Please enter a booking date.");
            return false;
        }
        if (seatNumberField.getText().trim().isEmpty()) {
            showError("Please enter a seat number.");
            return false;
        }
        return true;
    }

    private Booking createBookingFromForm() {
        Booking booking = new Booking();

        FlightItem flightItem = (FlightItem) flightCombo.getSelectedItem();
        PassengerItem passengerItem = (PassengerItem) passengerCombo.getSelectedItem();

        booking.setFlightId(flightItem.getId());
        booking.setPassengerId(passengerItem.getId());
        booking.setBookingDate(bookingDateField.getText().trim());
        booking.setSeatNumber(seatNumberField.getText().trim());
        booking.setStatus((String) statusCombo.getSelectedItem());

        return booking;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    // Helper class for flight combo box
    private static class FlightItem {
        private final Flight flight;

        public FlightItem(Flight flight) {
            this.flight = flight;
        }

        public int getId() {
            return flight.getFlightId();
        }

        @Override
        public String toString() {
            return flight.getFlightNumber() + " - " + flight.getOrigin() + " to " +
                   flight.getDestination() + " (" + flight.getAvailableSeats() + " seats)";
        }
    }

    // Helper class for passenger combo box
    private static class PassengerItem {
        private final Passenger passenger;

        public PassengerItem(Passenger passenger) {
            this.passenger = passenger;
        }

        public int getId() {
            return passenger.getPassengerId();
        }

        @Override
        public String toString() {
            return passenger.getFullName() + " (" + passenger.getPassportNumber() + ")";
        }
    }
}
