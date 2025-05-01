import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

// This class reads and parses Tasks.txt and Wishes.txt, creates their corresponding 
// objects, and then assigns them to the appropriate entities.
// Also reads each command from the Commands.txt file 
// and forwards them to the CommandHandler class for execution.

public class FileReader {
	
    private static Child child = null;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public FileReader(Child child) {
        FileReader.child = child;
    }

    public void loadTasks(String fileName) {

        try (Scanner scannerT = new Scanner(new File(fileName))) {
            while (scannerT.hasNextLine()) {
                String[] parts = scannerT.nextLine().split(",");
                int taskId = Integer.parseInt(parts[0]);
                String title = parts[1].replace("\"", "");  // Remove quotes
                String description = parts[2].replace("\"", "");
                int points = Integer.parseInt(parts[3].trim());
                String type = parts[4];

                // Check if the last field contains a time interval.
                if (parts[5].contains(" - ")) {
                    // ADD_TASK2 case (time interval)
                    String[] timeParts = parts[5].split(" - ");
                    if (timeParts.length != 2) {
                        System.out.println("Incorrect task date format");
                        continue;
                    }

                    LocalDateTime startDateTime = LocalDateTime.parse(timeParts[0].trim(), FORMATTER);
                    LocalDateTime endDateTime = LocalDateTime.parse(timeParts[1].trim(), FORMATTER);

                    child.tasks.put(taskId, new Task(taskId, title, description, points, type, startDateTime, endDateTime));

                } else {
                    // ADD_TASK1 case (deadline).
                    LocalDateTime dueDate = LocalDateTime.parse(parts[5].trim(), FORMATTER);
                    child.tasks.put(taskId, new Task(taskId, title, description, points, type, dueDate));
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("Tasks file not found.");
        }
    }

    public void loadWishes(String fileName) {
        try (Scanner scannerW = new Scanner(new File(fileName))) {
            while (scannerW.hasNextLine()) {
                String[] parts = scannerW.nextLine().split(",");
                addWish(parts); // Delegate to helper
                System.out.println("Wish: " + parts[0] + " added");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Wish file not found.");
        }
    }

    // Adds a wish based on the number of parameters
    public void addWish(String[] parts) {
        int id = Integer.parseInt(parts[0]);
        String title = parts[1].replace("\"", "");  // Remove quotes
        String description = parts[2].replace("\"", "");
        if (parts.length == 3) {
            // Wish without specific time
            child.wishes.put(id, new Wish(id, title, description));
        } else if (parts.length == 4) {
            // Wish with date and hour
            String[] timeParts = parts[3].split(" - ");
            
            // Ensure correct date format entered.
            if (timeParts.length != 2) {
                System.out.println("Incorrect wish date");
                return;
            }

            LocalDateTime startDateTime = LocalDateTime.parse(timeParts[0].trim(), FORMATTER);
            LocalDateTime endDateTime = LocalDateTime.parse(timeParts[1].trim(), FORMATTER);
            child.wishes.put(id, new Wish(id, title, description, startDateTime, endDateTime));
        }
    }

    // Reads Commands.txt file and sends each command to CommandHandler class.
    public void processCommands() {
        TaskWishManager handler = new TaskWishManager(child);
        handler.processCommands("Commands.txt");
    }
}
