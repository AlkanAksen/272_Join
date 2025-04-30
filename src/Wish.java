import java.time.LocalDate;  // Importing LocalDate to manage date-related functionality
import java.time.LocalDateTime;

// Enum representing the state of a wish
enum stateW {
    onHold,    // Wish is pending or on hold
    rejected,
    ready,// Wish has been rejected
    approved   // Wish has been approved
}

class Wish {
    int id;                 // Unique identifier for the wish
    String title;           // Title of the wish
    String details;         // Details describing the wish
    stateW wishState = stateW.onHold;  // Initial state is 'on hold'
    int requiredLevel;      // The level required to approve the wish
    LocalDateTime dateStart;         // Date the wish is scheduled for (if applicable)
    LocalDateTime dateEnd;         // Date the wish is scheduled for (if applicable)
    String hour1;           // The time scheduled for the wish (if applicable)

    // Constructor for wishes WITHOUT a time period
    public Wish(int id, String title, String details) {
        this.id = id;               // Set wish ID
        this.title = title;         // Set wish title
        this.details = details;     // Set wish details
    }

    // Constructor for wishes WITH a time period (using date and time)
    public Wish(int id, String title, String details, LocalDateTime dateStart,LocalDateTime dateEnd) {
        this.id = id;               // Set wish ID
        this.title = title;         // Set wish title
        this.details = details;     // Set wish details
        this.dateStart= dateStart;
        this.dateEnd= dateEnd;
    }

    // Method to approve the wish by setting its state to approved
    public void ready(int reqLev) {
        wishState = stateW.ready;  // Initially set to on hold (though it should be approved)
        this.requiredLevel = reqLev;  // Set the required level for approval
    }
// Method to approve the wish by setting its state to approved
    public void approve() {
        wishState = stateW.approved;  // Initially set to on hold (though it should be approved)
    }

    // Method to reject the wish, changing its state to rejected
    public void rejected() {
        wishState = stateW.rejected;  // Set state to rejected
    }
}
