import java.util.HashMap;

class Child {
    // Child's current points and level
     int points = 0;
     int level = 1;

     Parent parent;
     Teacher teacher;

    // Lists to store the child's tasks and wishes
     HashMap<Integer, Task> tasks = new HashMap<>();
     HashMap<Integer, Wish> wishes = new HashMap<>();
     
    // Schedule manager for printing
     Schedule schedule = new Schedule();

    // Update the level based on points
     void fixLevel() {
        level = Math.min(4, points / 40 + 1);
    }

    // Add points and recalculate level
    void addPoints(int pointsToAdd) {
        points += pointsToAdd;
        fixLevel();
    }

    // Print current points and level
    void printStatus() {
        System.out.println("[Child Points: " + points + ", Level: " + level + "]");
    }
    void printBudget() {System.out.println("[Child Points: " + points+"]");}

    // Print different parts of the schedule based on type
    void printSchedule(String str) {
        switch (str) {
            case "taskList":
                schedule.printTaskList(tasks); // Print all undone tasks
                break;
            case "wishList":
                schedule.printWishList(wishes); // Print all wishes except rejected
                break;
            case "daily":
                schedule.printScheduleDaily(tasks, wishes); // Print today's schedule
                break;
        }
    }
}