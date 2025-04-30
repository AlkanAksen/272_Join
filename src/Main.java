import java.time.LocalDateTime;

class Main {
    static TaskWishManager twm = new TaskWishManager(); // Manager to handle tasks, wishes, and commands

    public static void main(String[] args) {
        Child child = new Child(); // Create a child instance (not directly used here, but may be needed for setup)
        loadFiles(); // Load data and process commands
    }

    static void loadFiles() {
        // Load tasks and wishes from files
        twm.loadTasks("Tasks.txt");
        twm.loadWishes("Wishes.txt");

        // Example wish added manually for today's schedule
        LocalDateTime currentDateTime = LocalDateTime.now();
        String currentDateString = currentDateTime.toString().substring(0, 10);
        twm.child.wishes.add(new Wish(7, "Read Book", "Read time for 3 hours", currentDateString, "12:00-15:00"));

        // Process commands from file
        twm.processCommands("Commands.txt");
    }
}
