// Parent.java
public class Parent extends User {
    public Parent(String id, String name) {
        super(id, name);
    }

    public void approveTask(int taskId, int rating, Child child) {
        for (Task task : child.getTasks()) {
            if (task.getId() == taskId && task.isCompleted()) {
                child.addRating(rating);
                task.setRating(rating);
                PointManager.addPoints(child, task.getPoints());
                PointManager.calculateLevel(child);
            }
        }
    }

    public void approveWish(int wishId, int requiredLevel, Child child) {
        for (Wish wish : child.getWishes()) {
            if (wish.getId() == wishId) {
                if (child.getLevel() >= requiredLevel) {
                    wish.setRequiredLevel(requiredLevel);
                    wish.setStatus("Approved");
                } else {
                    wish.setStatus("Rejected");
                }
            }
        }
    }

    public void assignTask(Task task, Child child) {
        child.addTask(task);
    }

    public void addPoints(Child child, int points) {
        PointManager.addPoints(child, points);
        PointManager.calculateLevel(child);
    }
}
