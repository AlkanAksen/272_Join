import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

class TaskWishManager {
    static Child child = new Child(); // Single instance of the Child class
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Loads tasks from the specified file


    // Loads wishes from the specified file
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
        if (parts.length == 3) {
            // Wish without specific time
            child.wishes.add(new Wish(
                    Integer.parseInt(parts[0]), parts[1], parts[2]
            ));
        } else if (parts.length == 5) {
            // Wish with date and hour
            child.wishes.add(new Wish(
                    Integer.parseInt(parts[0]), parts[1], parts[2],
                    parts[3], parts[4]
            ));
        }
    }

    // Processes all commands from the command file
    public static void processCommands(String fileName) {
        try (Scanner scannerC = new Scanner(new File(fileName))) {
            while (scannerC.hasNextLine()) {
                String nextLine = scannerC.nextLine();
                String[] command = nextLine.split(" ");
                String[] wtParts = nextLine.split("_");

                switch (command[0]) {
                    case "ADD_TASK":
                        // Format: ADD_TASK_id_title_description_date_hour_points
                        if (wtParts.length == 8) {
                            child.tasks.add(new Task(
                                    Integer.parseInt(wtParts[2]), wtParts[3], wtParts[4],
                                    wtParts[5], wtParts[6], Integer.parseInt(wtParts[7])
                            ));
                            System.out.println("Task added: " + wtParts[3]);
                        }
                        break;
                    case "TASK_DONE":
                        markTaskCompleted(Integer.parseInt(command[1]));
                        break;
                    case "TASK_CHECKED":
                        approveTask(
                                Integer.parseInt(command[1]),
                                Integer.parseInt(command[2])
                        );
                        break;
                    case "ADD_WISH":
                        // Wish without time
                        if (wtParts.length == 5) {
                            child.wishes.add(new Wish(
                                    Integer.parseInt(wtParts[2]),
                                    wtParts[3], wtParts[4]
                            ));
                            System.out.println("Wish added: " + wtParts[3]);
                        }
                        // Wish with time
                        else if (wtParts.length == 7) {
                            child.wishes.add(new Wish(
                                    Integer.parseInt(wtParts[2]),
                                    wtParts[3], wtParts[4],
                                    wtParts[5], wtParts[6]
                            ));
                            System.out.println("Wish added: " + wtParts[3]);
                        }
                        break;

                    case "WISH_CHECKED":
                        approveWish(
                                Integer.parseInt(command[1]),
                                Integer.parseInt(command[2])
                        );
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

                    case "ADD_POINT":
                        child.addPoints(Integer.parseInt(command[1]));
                        System.out.println(command[1] + " points added.");
                        break;

                    default:
                        System.out.println("Unknown command: " + command[0]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Commands file not found.");
        }
    }

    // Marks a task as completed by its ID
    public static void markTaskCompleted(int taskId) {
        for (Task task : child.tasks) {
            if (task.id == taskId) {
                task.markCompleted();
                System.out.println("Task " + taskId + " marked as completed.");
                return;
            }
        }
        System.out.println("Task not found.");
    }

    // Approves a completed task based on the given rating
    public static void approveTask(int taskId, int rating) {
        for (Task task : child.tasks) {
            if (task.id == taskId && task.taskState == stateT.complete) {
                int pointsAwarded = (task.points * rating) / 5;
                child.addPoints(pointsAwarded);
                task.markApproved();
                System.out.println("Task " + taskId + " approved with rating " + rating);
                return;
            }
        }
        System.out.println("Task not found or not completed.");
    }

    // Overloaded method to add wish without time
    public static void addWish(int id, String title, String details) {
        child.wishes.add(new Wish(id, title, details));
        System.out.println("Wish added: " + title);
    }

    // Overloaded method to add wish with date and time
    public static void addWish(int id, String title, String details, String date, String time) {
        child.wishes.add(new Wish(id, title, details, date, time));
        System.out.println("Wish added: " + title);
    }

    // Approves a wish if the child's level is high enough
    public static void approveWish(int wishId, int requiredLevel) {
        for (Wish wish : child.wishes) {
            if (wish.id == wishId && wish.wishState != stateW.approved) {
                if (child.level >= requiredLevel) {
                    wish.approve(requiredLevel);
                    wish.wishState = stateW.approved;
                    System.out.println("Wish " + wishId + " granted: " + wish.title);
                } else {
                    System.out.println("Wish " + wishId + " not granted, required level: " + requiredLevel);
                }
                return;
            }
        }
        System.out.println("Wish not found or already approved.");
    }
}
