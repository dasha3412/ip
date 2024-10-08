package victor.commands;

import java.io.IOException;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.format.DateTimeParseException;

import victor.messages.ReturnMessage;
import victor.parser.Parser;
import victor.tasks.Event;

/**
 * Event command extends command class, ensures valid input and creates event object.
 */
public class EventCommand extends Command {
    private Event event;
    private String taskNameString;
    private String startString;
    private String endString;
    public EventCommand(String[] additionalInput) {
        super(additionalInput);
    }

    /**
     * Overrides the execute method from the Command class. Processes user input and handles
     * inputs with invalid date formats by prompting the user to enter a valid range and format of dates.
     * Calls the addTask method of task list to add the task to the program-wide task list.
     * @return A return message with the event action summary (successful) or a prompt to the user (unsuccessful).
     */
    @Override
    public ReturnMessage execute() {
        getEventParts();
        try {
            if (taskNameString.isBlank()) {
                return new ReturnMessage("  ~  Your event needs a name!",
                        "  ~  The format should be \"event"
                        + " (description) /from (start, in format yyyy-mm-dd or dd-mm-yyyy) /to"
                        + " (end, in format yyyy-mm-dd or dd-mm-yyyy)\"");
            } else if (startString.isBlank()) {
                return new ReturnMessage("  ~  Your event needs a start date!",
                        "  ~  The format should be \"event"
                        + " (description) /from (start, in format yyyy-mm-dd or dd-mm-yyyy) /to"
                        + " (end, in format yyyy-mm-dd or dd-mm-yyyy)\"");
            } else if (endString.isBlank()) {
                return new ReturnMessage("  ~  Your event needs an end date!",
                        "  ~  The format should be \"event"
                        + " (description) /from (start, in format yyyy-mm-dd or dd-mm-yyyy) /to"
                        + " (end, in format yyyy-mm-dd or dd-mm-yyyy)\"");
            }
            this.event = new Event(taskNameString, startString, endString);
            return new ReturnMessage(super.taskList.addTask(event));
        } catch (DateTimeParseException parseException) {
            return new ReturnMessage("  ~  Please format the event as \"event"
                + " (description) /from (start, in format yyyy-mm-dd or dd-mm-yyyy) /to"
                + " (end, in format yyyy-mm-dd or dd-mm-yyyy)\"");
        } catch (DateTimeException dateException) {
            return new ReturnMessage("  ~  Please check that your end time is later than your start time!",
                    "  ~  Format the event as \"event (description) /from"
                        + " (start, in format yyyy-mm-dd or dd-mm-yyyy) /to"
                        + " (end, in format yyyy-mm-dd or dd-mm-yyyy)\"");
        }
    }

    /**
     * Calls the Parser's parseEventParts method and sets the variables for task name, start, and end
     */
    private void getEventParts() {
        Parser parser = new Parser();
        String[] eventParts = parser.parseEventParts(additionalInput);
        taskNameString = eventParts[0];
        startString = eventParts[1];
        endString = eventParts[2];
    }

    /**
     * Overrides the generic write method in the parent Command class. Handles the case where the event is null
     * (has not been set or incorrectly generated) by not writing anything. Otherwise, calls the writeToFile method
     * from the TaskList class with the given file path. Appends to file instead of overwriting.
     * @param filePath The file path, relative to the project root directory, where to write the changes.
     */
    @Override
    public void write(Path filePath) {
        try {
            if (this.event == null) {
                return;
            }
            this.event.writeToFile(filePath);
        } catch (IOException writeException) {
            throw new RuntimeException("Problem writing to file.");
        }
    }

    /**
     * Returns event associated with event command.
     * @return An event object created by the event command.
     */
    public Event getEvent() {
        return this.event;
    }
}
