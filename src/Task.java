import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Enum representing the state of the task
enum stateT {
    undone,     // Task is not completed yet
    complete,   // Task has been marked as completed
    approved    // Task has been approved
}

class Task {
    int id;                 // Unique identifier for the task
    String title, description;  // Task's title and description
    LocalDateTime deadline;          // Task's deadline (currently unused)
    LocalDateTime dateStart;         // Task's scheduled date
    LocalDateTime dateEnd;         // Task's scheduled date
    int points;             // Points awarded when the task is completed and approved
    stateT taskState = stateT.undone;  // Task's initial state is 'undone'
    String type;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Constructor for creating a task with a specific date and hour
    public Task(int id, String title, String description,int points,String type, LocalDateTime deadline ) {
        this.id = id;                   // Set task ID
        this.title = title;             // Set task title
        this.description = description; // Set task description
        this.points = points;           // Set points for the task
        this.type = type;
        this.deadline= deadline;
    }

    public Task(int id, String title, String description, int points,String type, LocalDateTime dateStart , LocalDateTime dateEnd) {
        this.id = id;                   // Set task ID
        this.title = title;             // Set task title
        this.description = description; // Set task description
        this.points = points;           // Set points for the task
        this.type = type;
        this.dateStart=dateStart;
        this.dateEnd= dateEnd;
    }

    // Method to mark the task as completed
    public void markCompleted() {
        taskState = stateT.complete;  // Change state to 'complete'
    }

    // Method to mark the task as approved
    public void markApproved() {
        taskState = stateT.approved;  // Change state to 'approved'
    }
}