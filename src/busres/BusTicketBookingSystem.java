package busres;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class BusTicketBookingSystem extends JFrame 
{   
    // Declare instance variables for GUI components
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JComboBox<String> busSelectionComboBox;
    private JButton confirmBookingButton;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField emailField;
    private JTextField contactField;
    private JTextField passengerCountField;
    private JTextArea seatArea;
    private JButton clearSelectionButton;

    // Store seat buttons and selected seats
    private List<JButton> seatButtons = new ArrayList<>();
    private Set<String> selectedSeats = new HashSet<>(); // Use a set to store selected seats

    // Map to store reserved seats for each bus
    private Map<String, int[]> busReservedSeats = new HashMap<>(); // Initialize map

    // Constructor
    public BusTicketBookingSystem() 
    {
        setTitle("Bus Ticket Booking System");
        setSize(550, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Initialize busReservedSeats map
        for (int i = 1; i <= 5; i++) 
        {
            String busName = "Bus" + i; // Default name if source/destination input is not available
            busReservedSeats.put(busName, new int[0]);
        }
        
        // Generate reserved seats for each bus
        createReservedSeats();
        
        // Create main panels
        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        add(mainPanel);

        // Create components for left panel
        createPassengerDetailsPanel();

        // Create components for right panel
        createBusSelectionPanel();
        createSeatSelectionPanel();
        createConfirmationPanel();

        setVisible(true);
    }

    // Method to create panel for passenger details
    private void createPassengerDetailsPanel() 
    {
        JPanel passengerDetailsPanel = new JPanel();
        passengerDetailsPanel.setLayout(new GridLayout(0, 1,20,20)); // 0 rows, 1 column for vertical alignment

        // Add components for source and destination
        JPanel sourcePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sourcePanel.add(new JLabel("Source:"));
        JTextField sourceField = new JTextField(15);
        sourceField.setFont(new Font("Arial", Font.BOLD, 12));
        sourcePanel.add(sourceField);
        passengerDetailsPanel.add(sourcePanel);

        JPanel destinationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        destinationPanel.add(new JLabel("Destination:"));
        JTextField destinationField = new JTextField(15);
        destinationField.setFont(new Font("Arial", Font.BOLD, 12));
        destinationPanel.add(destinationField);
        passengerDetailsPanel.add(destinationPanel);

        // Add other components for passenger details (name, age, email, contact, passenger count)
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(new JLabel("Name:"));
        nameField = new JTextField(20); // Set preferred width
        nameField.setFont(new Font("Arial", Font.BOLD, 12)); // Set font style and size
        namePanel.add(nameField);
        passengerDetailsPanel.add(namePanel);

        JPanel agePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        agePanel.add(new JLabel("Age:"));
        ageField = new JTextField(5); // Set preferred width
        ageField.setFont(new Font("Arial", Font.BOLD, 12)); // Set font style and size
        agePanel.add(ageField);
        passengerDetailsPanel.add(agePanel);

        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailPanel.add(new JLabel("Email:"));
        emailField = new JTextField(20); // Set preferred width
        emailField.setFont(new Font("Arial", Font.BOLD, 12)); // Set font style and size
        emailPanel.add(emailField);
        passengerDetailsPanel.add(emailPanel);

        JPanel contactPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contactPanel.add(new JLabel("Contact:"));
        contactField = new JTextField(15); // Set preferred width
        contactField.setFont(new Font("Arial", Font.BOLD, 12)); // Set font style and size
        contactPanel.add(contactField);
        passengerDetailsPanel.add(contactPanel);
        
        JPanel passengerCountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passengerCountPanel.add(new JLabel("Passenger Count:"));
        passengerCountField = new JTextField(5); // Set preferred width
        passengerCountField.setFont(new Font("Arial", Font.BOLD, 12)); // Set font style and size
        passengerCountPanel.add(passengerCountField);
        passengerDetailsPanel.add(passengerCountPanel);

        passengerDetailsPanel.add(new JLabel("Selected Seat(s):"));
        seatArea = new JTextArea(3, 15);
        seatArea.setFont(new Font("Arial", Font.BOLD, 12)); // Set font style and size
        seatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(seatArea);
        passengerDetailsPanel.add(scrollPane);

        confirmBookingButton = new JButton("Confirm Booking");
        confirmBookingButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                // Code to handle booking confirmation
                if (!isPassengerDetailsFilled() || selectedSeats.isEmpty()) 
                {
                    JOptionPane.showMessageDialog(null, "Please fill in all details and select at least one seat.");
                } 
                else 
                {
                    String source = sourceField.getText();
                    String destination = destinationField.getText();
                    String name = nameField.getText(); // Use the entered name instead of the generated bus name
                    String age = ageField.getText();
                    String email = emailField.getText();
                    String contact = contactField.getText();
                    
                    StringBuilder seatString = new StringBuilder();
                    for (String seat : selectedSeats) 
                    {
                        seatString.append(seat).append(", ");
                    }
                    String selectedBus = generateBusName(source, destination);
                    String bookingDetails = "Booking Confirmed!\n";
                    bookingDetails += "Bus: " + selectedBus + "\n";
                    bookingDetails += "Name: " + name + "\nAge: " + age + "\nEmail: " + email + "\nContact: " + contact + "\nSelected Seat(s): " + seatString.toString();
                    JOptionPane.showMessageDialog(null, bookingDetails);
                    seatString.delete(seatString.length() - 2, seatString.length()); // Remove trailing comma and space
                }
            }
        });
        leftPanel.add(passengerDetailsPanel, BorderLayout.CENTER);
        // Create panel for confirm booking button
        JPanel confirmBookingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        confirmBookingPanel.add(confirmBookingButton);
        leftPanel.add(confirmBookingPanel, BorderLayout.SOUTH);
    }
    
    // Method to generate bus name based on source and destination
    private String generateBusName(String source, String destination) {
        // Ensure that source and destination are at least 3 characters long
        if (source.length() >= 3 && destination.length() >= 3) {
            // Take the first three letters of both source and destination to form the bus name
            return source.substring(0, 3).toUpperCase() + "-" + destination.substring(0, 3).toUpperCase();
        } else {
            // If source or destination is less than 3 characters, return a default bus name
            return "DEFAULT-BUS";
        }
    }

    // Method to create panel for bus selection
    private void createBusSelectionPanel() 
    {
        JPanel busSelectionPanel = new JPanel();
        busSelectionPanel.setLayout(new FlowLayout());

        // Add buses to the combo box
        String[] buses = {"Bus1", "Bus2", "Bus3", "Bus4", "Bus5"}; // 5 buses
        busSelectionComboBox = new JComboBox<>(buses);
        busSelectionPanel.add(new JLabel("Select Bus:"));
        busSelectionPanel.add(busSelectionComboBox);
        busSelectionComboBox.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                updateSeatDisplay();
            }
        });
        rightPanel.add(busSelectionPanel, BorderLayout.NORTH);
    }

    // Method to create panel for seat selection
    private void createSeatSelectionPanel() 
    {
        JPanel seatSelectionPanel = new JPanel();
        seatSelectionPanel.setLayout(new GridLayout(7, 5)); // 7 rows and 5 columns

        // Create seat buttons and add to panel
        for (int i = 0; i < 7; i++) 
        {
            for (int j = 0; j < 5; j++) 
            {
                if ((j == 2 && i != 6) || ((j == 0 || j == 1) && i == 0)) 
                { // Skip adding a button to simulate the aisle
                    seatSelectionPanel.add(new JLabel()); // Placeholder for the aisle
                }
                else 
                {
                    final int seatNumber = i * 5 + j + 1; // Calculate seat number
                    JButton seatButton = new JButton(""+seatNumber);
                    seatButton.setPreferredSize(new Dimension(10, 10)); // Set preferred size to 50x50 pixels
                    
                    // Set background color to red for reserved seats
                    if (isSeatReserved((String) busSelectionComboBox.getSelectedItem(), seatNumber)) 
                    {
                        seatButton.setBackground(Color.RED);
                    }

                    // ActionListener for seat selection
                    seatButton.addActionListener(new ActionListener() 
                    {
                        public void actionPerformed(ActionEvent e) 
                        {
                            JButton clickedButton = (JButton) e.getSource();
                            if (!isPassengerDetailsFilled()) 
                            {
                                JOptionPane.showMessageDialog(null, "Please fill in all passenger details first.");
                                return;
                            }
                            int maxSeats = Integer.parseInt(passengerCountField.getText());
                            String seat = "Seat " + seatNumber;
                            if (selectedSeats.contains(seat)) 
                            {
                                // User is deselecting the seat
                                clickedButton.setBackground(null);
                                selectedSeats.remove(seat);
                            }
                            else 
                            {
                                // User is selecting a new seat
                                if (selectedSeats.size() >= maxSeats) 
                                {
                                    JOptionPane.showMessageDialog(null, "Maximum seat selection reached.");
                                    return;
                                }
                                if (isSeatReserved((String) busSelectionComboBox.getSelectedItem(), seatNumber)) 
                                {
                                    JOptionPane.showMessageDialog(null, "Seat is already reserved.");
                                    return;
                                }
                                clickedButton.setBackground(Color.GREEN);
                                selectedSeats.add(seat);
                            }
                            updateSeatArea();
                        }
                    });

                    seatButtons.add(seatButton);
                    seatSelectionPanel.add(seatButton);
                }
            }
        }
        rightPanel.add(seatSelectionPanel, BorderLayout.CENTER);
    }

    // Method to create panel for confirmation
    private void createConfirmationPanel() 
    {
        JPanel confirmationPanel = new JPanel();
        confirmationPanel.setLayout(new FlowLayout());

        // Button to clear seat selection
        clearSelectionButton = new JButton("Clear Selection");
        clearSelectionButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                clearSelection();
            }
        });
        // Create panel for clear selection button
        JPanel clearSelectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        clearSelectionPanel.add(clearSelectionButton);
        rightPanel.add(clearSelectionPanel, BorderLayout.SOUTH);
    }
 
    // Method to update seat display based on the selected bus
    private void updateSeatDisplay() 
    {
        // Clear selected seats and update seat area
        selectedSeats.clear();
        updateSeatArea();

        // Get the selected bus
        String selectedBus = (String) busSelectionComboBox.getSelectedItem();

        // Update seat buttons color based on reserved seats for the selected bus
        for (JButton seatButton : seatButtons) 
        {
            String buttonText = seatButton.getText();
            int seatNumber = Integer.parseInt(buttonText.substring(buttonText.indexOf(" ") + 1)); // Extract seat number
            if (isSeatReserved(selectedBus, seatNumber)) 
            {
                seatButton.setBackground(Color.RED); // Reserved seat
            }
            else 
            {
                seatButton.setBackground(null); // Available seat
            }
        }
    }
    
    // Method to update seat area
    private void updateSeatArea() 
    {
        StringBuilder seatString = new StringBuilder();
        for (String seat : selectedSeats) 
        {
            seatString.append(seat).append(",");
        }
        seatArea.setText(seatString.toString());
    }

    // Method to clear seat selection
    private void clearSelection() 
    {
        Iterator<String> iterator = selectedSeats.iterator();
        while (iterator.hasNext()) 
        {
            String seat = iterator.next();
            if (!isSeatReserved((String) busSelectionComboBox.getSelectedItem(), Integer.parseInt(seat.substring(seat.indexOf(" ") + 1)))) 
            {
                iterator.remove();
            }
        }
        for (JButton button : seatButtons) 
        {
             if (button.getBackground() != null && button.getBackground().equals(Color.GREEN)) 
             {
                 button.setBackground(null); // Reset background color of green seats
             }
        }
        selectedSeats.clear(); // Clear selected seats
        updateSeatArea(); // Update seat area display
    }

    // Method to check if passenger details are filled
    private boolean isPassengerDetailsFilled() 
    {
        String name = nameField.getText();
        String age = ageField.getText();
        String email = emailField.getText();
        String contact = contactField.getText();
        String passengerCount = passengerCountField.getText();
        return !name.isEmpty() && !age.isEmpty() && !email.isEmpty() && !contact.isEmpty() && !passengerCount.isEmpty();
    }

    // Method to create reserved seats randomly for each bus
    private void createReservedSeats() 
    {
        // Define the total number of seats
        int totalSeats = 35; // 20 seats per bus

        // Define the percentage of seats to reserve for each bus
        double reservedPercentage = 0.2; // 20% of seats reserved for each bus

        // Create Random object
        Random random = new Random();

        // Generate reserved seats for each bus
        for (int bus = 1; bus <= 5; bus++) 
        {
            // Calculate the number of seats to reserve for this bus
            int numReservedSeats = (int) (totalSeats * reservedPercentage);

            // Create an array to store reserved seats for this bus
            int[] reservedSeats = new int[numReservedSeats];

            // Generate random reserved seats for this bus
            for (int j = 0; j < numReservedSeats; j++) 
            {
                int seatNumber;
                do 
                {
                    seatNumber = random.nextInt(totalSeats) + 1; // Generate random seat number
                } while (isSeatReserved("Bus" + bus, seatNumber)); // Check if the seat is already reserved
                reservedSeats[j] = seatNumber; // Add the reserved seat to the array
            }

            // Store reserved seats for this bus
            busReservedSeats.put("Bus" + bus, reservedSeats);
        }
    }

    // Method to check if a seat is reserved
    private boolean isSeatReserved(String bus, int seatNumber) 
    {
        // Retrieve reserved seats for the specified bus
        int[] reservedSeats = busReservedSeats.get(bus);
        // Check if the provided seat number is in the reservedSeats array
        for (int reservedSeat : reservedSeats) 
        {
            if (seatNumber == reservedSeat) 
            {
                return true;
            }
        }
        return false;
    }

    // Main method to run the application
    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
                new BusTicketBookingSystem();
            }
        });
    }
}
