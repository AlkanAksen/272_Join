import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.stream.Stream;

// This class reads and parses Tasks.txt and Wishes.txt, creates their corresponding 
// objects, and then assigns them to the appropriate entities.
// Also reads each command from the Commands.txt file 
// and forwards them to the CommandHandler class for execution.

public class FileReader {
    
    private static Child child = null;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public FileReader(Child child) {
        this.child = child;
    }

    public static void loadTasks(String fileName) {
        try (Scanner scannerT = new Scanner(new File(fileName))) {
            while (scannerT.hasNextLine()) {
                String[] parts = scannerT.nextLine().split(",");
                int taskId = Integer.parseInt(parts[0]);
                String title = parts[1].replace("\"", "");  // Remove quotes
                String description = parts[2].replace("\"", "");
                int points = Integer.parseInt(parts[3]);
                String type = parts[4];


                // Check if the last field contains a time interval.
                if (parts[5].contains(" - ")) {
                    // ADD_TASK2 case (time interval)
                    String[] timeParts = parts[5].split(" - ");
                    if (timeParts.length != 2) System.out.println("Incorrect task date");

                    LocalDateTime startDateTime = LocalDateTime.parse(timeParts[0], FORMATTER);
                    LocalDateTime endDateTime = LocalDateTime.parse(timeParts[1], FORMATTER);

                    child.tasks.put(taskId,new Task(taskId, title, description, points, type, startDateTime,endDateTime);

                } else {
                    // ADD_TASK1 case (deadline).
                    LocalDateTime dueDate = LocalDateTime.parse(parts[5], FORMATTER);
                    child.tasks.put(taskId,new Task(taskId, title, description, points, type, dueDate));
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("Tasks file not found.");
        }
    }

    public static void loadWishes(String fileName) {
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
    public static void addWish(String[] parts) {
        int id = Integer.parseInt(parts[0]);
        String title = parts[1].replace("\"", "");  // Remove quotes
        String description = parts[2].replace("\"", "");
        if (parts.length == 3) {
            // Wish without specific time
            child.wishes.put(id, new Wish(id, parts[1], parts[2]));
        } else if (parts.length == 5) {
            // Wish with date and hour
            String[] timeParts = parts[5].split(" - ");
            if (timeParts.length != 2) System.out.println("Incorrect task date");

            LocalDateTime startDateTime = LocalDateTime.parse(timeParts[0], FORMATTER);
            LocalDateTime endDateTime = LocalDateTime.parse(timeParts[1], FORMATTER);
            child.wishes.put(id, new Wish(id, parts[1], parts[2], startDateTime, endDateTime));
        }
    }

    ////////////////// bundan sonra çıkarılıcak kıssım belirlenecek comment handle task wish manager de halledilecek




    // Reads Commands.txt file and sends each commmand to CommandHandler class.
    public void processCommands() {
        CommandHandler handler = new CommandHandler(child, parent);
        
        try (Stream<String> lines = Files.lines(Paths.get(COMMANDS_FILE_PATH))) {
            lines.forEach(t -> {
				try {
					handler.handleCommand(t);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
        } catch (IOException e) {
            System.err.println("Error reading commands file: " + e.getMessage());
        }
    }
}
