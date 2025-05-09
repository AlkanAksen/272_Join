import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TaskWishManager {
    Child child; // Assuming Child class exists
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public TaskWishManager(Child child) {
    	this.child = child;
    }
    
    public void processCommands(String fileName) {
        try (Scanner scannerC = new Scanner(new File(fileName))) {
            while (scannerC.hasNextLine()) {
                String line = scannerC.nextLine().trim();
                if (line.isEmpty()) continue;

                // Parse the line respecting quoted strings
                String[] tokens = parseCommand(line);

                if (tokens.length == 0) continue;

                String command = tokens[0];

                switch (command) {
                    case "ADD_TASK1":
                        if (tokens.length == 8) {
                            int id = Integer.parseInt(tokens[2]);
                            String title = tokens[3];
                            String description = tokens[4];
                            String dateStr = tokens[5];
                            String timeStr = tokens[6];
                            int points = Integer.parseInt(tokens[7]);

                            LocalDateTime dateTime = LocalDateTime.parse(dateStr + " " + timeStr, FORMATTER);

                            Task task = new Task(id, title, description, points, tokens[1], dateTime);
                            child.tasks.put(id, task);
                            System.out.println("Task added: " + title);
                        }
                        break;

                    case "TASK_DONE":
                        markTaskCompleted(Integer.parseInt(tokens[1]));
                        break;

                    case "TASK_CHECKED":
                        approveTask(
                                Integer.parseInt(tokens[1]),
                                Integer.parseInt(tokens[2])
                        );
                        break;

                    case "ADD_WISH1":
                        if (tokens.length == 4) {
                            int id = Integer.parseInt(tokens[1]);
                            String title = tokens[2];
                            String detail = tokens[3];
                            child.wishes.put(id, new Wish(id, title, detail));
                            System.out.println("Type 1 Wish added: " + title);
                        }
                        break;

                    case "ADD_WISH2":
                        if (tokens.length == 8) {
                            int id = Integer.parseInt(tokens[1]);
                            String title = tokens[2];
                            String detail = tokens[3];
                            LocalDateTime start = LocalDateTime.parse(tokens[4] + " " + tokens[5], FORMATTER);
                            LocalDateTime end = LocalDateTime.parse(tokens[6] + " " + tokens[7], FORMATTER);

                            child.wishes.put(id, new Wish(id, title, detail, start, end));
                            System.out.println("Type 2 Wish added: " +  title);
                        }
                        break;

                    case "WISH_CHECKED":
                        if (tokens.length >= 4 && tokens[2].compareTo("APPROVED") == 0) {
                            int id = Integer.parseInt(tokens[1]);
                            int level = Integer.parseInt(tokens[3]);
                            approveWish(id, level);
                        }
                        else if(tokens.length == 3 && tokens[2].compareTo("REJECTED") == 0){
                            int id = Integer.parseInt(tokens[1]);
                            rejectWish(id);
                        }
                        break;

                    case "PRINT_STATUS":
                        child.printStatus();
                        break;

                    case "PRINT_TASKS":
                        child.printSchedule("taskList");
                        break;

                    case "PRINT_WISHES":
                        child.printSchedule("wishList");
                        break;

                    case "PRINT_SCHEDULE_DAILY":
                        child.printSchedule("daily");
                        break;

                    case "PRINT_SCHEDULE_WEEKLY":
                        child.printSchedule("weekly");
                        break;

                    case "ADD_POINT":
                        child.addPoints(Integer.parseInt(tokens[1]));
                        System.out.println(tokens[1] + " points added.");
                        break;

                    case "PRINT_BUDGET":
                        child.printBudget();
                        break;


                    default:
                        System.out.println("Unknown command: " + command);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Commands file not found.");
        }
    }

    private static String[] parseCommand(String line) {
        Matcher m = Pattern.compile("\"([^\"]*)\"|(\\S+)").matcher(line);
        java.util.List<String> tokens = new java.util.ArrayList<>();
        while (m.find()) {
            if (m.group(1) != null)
                tokens.add(m.group(1)); // quoted string without quotes
            else
                tokens.add(m.group(2)); // normal word
        }
        return tokens.toArray(new String[0]);
    }

    public void markTaskCompleted(int taskId) {
        if (child.tasks.containsKey(taskId)) {
            child.tasks.get(taskId).markCompleted();
            System.out.println("Task " + taskId + " marked as completed.");
        } else {
            System.out.println("Task not found.");
        }
    }

    public void approveTask(int taskId, int rating) {
        if (child.tasks.containsKey(taskId)) {
            Task task = child.tasks.get(taskId);
            if (task.taskState == stateT.complete) {
                int pointsAwarded = (task.points * rating) / 5;
                child.addPoints(pointsAwarded);
                task.markApproved();
                System.out.println("Task " + taskId + " approved with rating " + rating);
                return;
            }
        }
        System.out.println("Task not found or not completed.");
    }

    public void approveWish(int wishId, int requiredLevel) {
        if (child.wishes.containsKey(wishId)) {
            Wish wish = child.wishes.get(wishId);
            if (wish.wishState != stateW.approved) {
                if (child.level >= requiredLevel) {
                    wish.approve();
                    System.out.println("Wish " + wishId + " granted: " + wish.title);
                } else {
                    System.out.println("Wish " + wishId + " not granted, required level: " + requiredLevel);
                }
            } else {
                System.out.println("Wish already approved.");
            }
        } else {
            System.out.println("Wish not found.");
        }
    }

    public void rejectWish(int wishId){
        if (child.wishes.containsKey(wishId)) {
            Wish wish = child.wishes.get(wishId);
            wish.rejected();
            System.out.println("Wish " + wishId + " rejected: " + wish.title);
        }
    }
}
