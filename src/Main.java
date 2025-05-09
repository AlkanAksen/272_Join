
class Main {

    public static void main(String[] args) {
        Child child = new Child(); // Create a child instance (not directly used here, but may be needed for setup)
        FileReader reader = new FileReader(child); 
        loadFiles(reader); // Load data and process commands
        
        // Schedule Test
    }
    
    static void loadFiles(FileReader reader) {
        // Load tasks and wishes from files
    	reader.loadTasks("Tasks.txt");
    	reader.loadWishes("Wishes.txt");

        // Example wish added manually for today's schedule
//        LocalDateTime currentDateTime = LocalDateTime.now();
//        String currentDateString = currentDateTime.toString().substring(0, 10);
//        twm.child.wishes.add(new Wish(7, "Read Book", "Read time for 3 hours", currentDateString, "12:00-15:00"));

        // Process commands from file
    	reader.processCommands();
    	
    }
}