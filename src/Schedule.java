import java.time.LocalDateTime; // Importing LocalDateTime to manage current date and time
import java.util.HashMap;

class Schedule {

	private static final int TITLE_WIDTH = 25;

	// Method to print the list of tasks
	void printTaskList(HashMap<Integer, Task> tasks) {
		System.out.println("┌────┬───────────────────────────┐"); // Table header
		System.out.println("│ ID │ Name OF Task              │");
		System.out.println("└────┼───────────────────────────┘"); // Table footer
		// Loop through each task
		for (Task t : tasks.values()) {
			// Print task details only if the task is not completed
			if (t.taskState == stateT.undone) {
				String id = String.format(" %-2s", t.id); // Format task ID
				String title = String.format("%-" + TITLE_WIDTH + "s", t.title); // Format task title
				System.out.println("│" + id + " │" + title + " │"); // Print task in table format
			}
		}
		System.out.println("└────┴───────────────────────────┘"); // End of table
	}

	// Method to print the list of wishes
	void printWishList(HashMap<Integer, Wish> wishes) {
		System.out.println("┌────┬───────────────────────────┬──────────┐"); // Table header
		System.out.println("│ ID │ Name OF Wish              │ Status   │");
		System.out.println("└────┼───────────────────────────┼──────────┘"); // Table footer
		// Loop through each wish
		for (Wish w : wishes.values()) {
			// Print wish details only if the wish is not rejected
			if (w.wishState != stateW.rejected) {
				String id = String.format(" %-2s", w.id); // Format wish ID
				String title = String.format(" %-" + TITLE_WIDTH + "s", w.title); // Format wish title
				// Print wish in table format with status (approved or on hold)
				System.out.println("│" + id + " │" + title + " │ "
						+ (w.wishState == stateW.approved ? "Approved " : "On Hold  ") + "│");
			}
		}
		System.out.println("└────┴───────────────────────────┴──────────┘"); // End of table
	}

	// Method to print the daily schedule (tasks and wishes)
	void printScheduleDaily(HashMap<Integer, Task> tasks, HashMap<Integer, Wish> wishes) {
		LocalDateTime currentDateTime = LocalDateTime.now(); // Get current date and time
		String currentDate = currentDateTime.toString().substring(0, 10); // Extract current date (YYYY-MM-DD)

		// Print the header for the daily schedule
		System.out.println("┌───────────────────────────┬────────────┐");
		System.out.println("│ " + currentDate + "                │ Hour       │");
		System.out.println("└───────────────────────────┼────────────┘");

		// Loop through each wish to print approved ones for the current day
		for (Wish w : wishes.values()) {
			// Print wish details only if it's approved and scheduled for today
			if (w.wishState == stateW.approved && w.hour1 != null && w.dateStart != null
					&& currentDateTime.toLocalDate().isEqual(w.dateStart.toLocalDate())) {
				String title = String.format(" %-" + TITLE_WIDTH + "s", w.title); // Format wish title
				// Print the wish scheduled for the current day and hour
				System.out.println("│" + title + " │ " + w.hour1 + "│");
			}
		}

		// Loop through each task to print tasks scheduled for the current day
		for (Task t : tasks.values()) {
			// Print task details only if it's not completed and scheduled for today
			if (t.taskState == stateT.undone && t.hour != null && t.dateStart != null
					&& currentDateTime.toLocalDate().isEqual(t.dateStart.toLocalDate())) {
				String title = String.format(" %-" + TITLE_WIDTH + "s", t.title); // Format task title
				String hour = String.format(" %-11s", t.hour); // Format task hour
				// Print the task scheduled for the current day and hour
				System.out.println("│" + title + " │" + hour + "│");
			}
		}

		System.out.println("└───────────────────────────┴────────────┘"); // End of table
	}
}