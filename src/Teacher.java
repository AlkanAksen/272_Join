// Teacher.java
public class Teacher extends User {
    public Teacher(String id, String name) {
        super(id, name);
    }

    public void assignTask(Task task, Child child) {
        child.addTask(task);
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

    public void addPoints(Child child, int points) {
        PointManager.addPoints(child, points);
        PointManager.calculateLevel(child);
    }
}
