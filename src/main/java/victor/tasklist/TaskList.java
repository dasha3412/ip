package victor.tasklist;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;

import victor.tasks.Task;

public class TaskList {
    private ArrayList<Task> tasks;
    private Path filePath;

    public TaskList(Path filePath) {
        this.tasks = new ArrayList<Task>();
        this.filePath = filePath;
    }

    /**
     * Adds a task to the internal ArrayList of tasks keeping track of all tasks in the program.
     * Returns a string array of the output to be shown by the UI component to the user.
     * @param task A Task object to be added to the task list.
     * @return A string array with output to be shown to the user following adding the task.
     */
    public String[] addTask(Task task) {
        this.tasks.add(task);
        return new String[] {"  ~  Cool! I added this task:",
            "  ~  " + task,
            "  ~  You now have " + this.size() + ((this.size() == 1) ? " task" : " tasks") + " in your list."};
    }

    /**
     * Deletes a task from the internal ArrayList of tasks keeping track of all tasks in the program.
     * Returns a string array of the output to be shown by the UI component to the user.
     * @param taskNumber An integer representing the number of the task meant to be deleted.
     * @return A string array with output to be shown to the user following deleting the task.
     * Indicates success status of the deletion task.
     */
    public String[] deleteTask(int taskNumber) {
        try {
            Task removed = tasks.remove(taskNumber - 1);
            return new String[] {"  ~  Deleting the task below now!",
                "  ~  " + removed, "  ~  You now have " + this.size() + ((this.size() == 1) ? " task" : " tasks")
                    + " in your list."};
        } catch (NumberFormatException e) {
            return new String[] {"  ~  Sorry, I don't think you entered a number for which task to delete!"};
        } catch (IndexOutOfBoundsException e) {
            return new String[] {"  ~  I don't think there's a task with that number!"};
        }
    }

    /**
     * Marks a task as done in the internal ArrayList of tasks keeping track of all tasks in the program.
     * Returns a string array of the output to be shown by the UI component to the user.
     * @param taskNumber An integer representing the number of the task meant to be marked as done.
     * @return A string array with output to be shown to the user following marking the task as done.
     * Indicates success status of the marking as done task.
     */
    public String[] markDone(int taskNumber) {
        try {
            Task task = tasks.get(taskNumber - 1);
            task.markDone();
            return new String[] {"  ~  You finished a task! Well done! I marked this task as done:",
                "  ~  " + task};
        } catch (IndexOutOfBoundsException e) {
            return new String[] {"  ~  I don't think there's a task with that number!"};
        }
    }

    /**
     * Unmarks a task as done in the internal ArrayList of tasks keeping track of all tasks in the program.
     * Returns a string array of the output to be shown by the UI component to the user.
     * @param taskNumber An integer representing the number of the task meant to be unmarked as done.
     * @return A string array with output to be shown to the user following unmarking the task as done.
     * Indicates success status of the unmarking as done task.
     */
    public String[] unmarkDone(int taskNumber) {
        try {
            Task task = tasks.get(taskNumber - 1);
            task.markUndone();
            return new String[] {"  ~  Oops, I guess you didn't finish the task! I marked this task as undone:",
                "  ~  " + task};
        } catch (IndexOutOfBoundsException e) {
            return new String[] {"  ~  I don't think there's a task with that number!"};
        }
    }

    /**
     * Writes the entire task list to the file at the file path provided.
     * @param filePath A Path object - a relative file path (relative to project root directory)
     *                 where data should be saved.
     * @param shouldOverwrite A boolean specifying whether the current data in the file at the file
     *                        path should be overwritten.
     */
    public void writeToFile(Path filePath, boolean shouldOverwrite) {
        try {
            // Clear output file if overwriting
            if (shouldOverwrite) {
                PrintWriter writer = new PrintWriter(String.valueOf(filePath));
                writer.print("");
                writer.close();
            }
            FileWriter fw = new FileWriter(String.valueOf(filePath), shouldOverwrite);
            for (Task task : tasks) {
                task.writeToFile(fw);
            }
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a string array listing the String versions of all the tasks currently in the
     * task list, along with their number and order.
     * @return A string array of all current tasks in the task list.
     */
    public String[] enumerateTasks() {
        String[] outputList = new String[tasks.size() + 1];
        outputList[0] = "  ~  Sure! Here are all of your tasks:";
        for (int i = 0; i < tasks.size(); i++) {
            outputList[i + 1] = "     " + (i + 1) + ". " + tasks.get(i);
        }
        return outputList;
    }

    public int size() {
        return this.tasks.size();
    }

    public ArrayList<Task> getTaskList() {
        return this.tasks;
    }
}
