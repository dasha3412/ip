package Victor.Commands;

import Victor.Messages.ReturnMessage;
import Victor.Tasks.Event;

import java.io.IOException;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.format.DateTimeParseException;

public class EventCommand extends Command {
    private Event event;
    public EventCommand(String[] additionalInput) {
        super(additionalInput);
    }
    @Override
    public ReturnMessage execute() {
        String taskNameString = "";
        String startString = "";
        String endString = "";

        boolean isStart = false;
        boolean isEnd = false;

        for (int i = 1; i < additionalInput.length; i++) {
            if (additionalInput[i].startsWith("/")) {
                if (isStart) {
                    isEnd = true;
                } else {
                    isStart = true;
                }
                continue;
            }
            if (!isStart) {
                taskNameString += " " + additionalInput[i];
            } else if (!isEnd) {
                startString += " " + additionalInput[i];
            } else {
                endString += "" + additionalInput[i];
            }
        }
        // Trim so that blank space cannot be counted as name for task, start or end
        taskNameString = taskNameString.trim();
        startString = startString.trim();
        endString = endString.trim();

        try {
            if (taskNameString.isBlank()) {
                return new ReturnMessage("  ~  Please give a name for the event.",
                        "  ~  The format should be \"event" +
                        " (description) /from (start, in format yyyy-mm-dd or dd-mm-yyyy) /to" +
                        " (end, in format yyyy-mm-dd or dd-mm-yyyy)\"");
            } else if (startString.isBlank()) {
                return new ReturnMessage("  ~  Please give a start time for the event.",
                        "  ~  The format should be \"event" +
                        " (description) /from (start, in format yyyy-mm-dd or dd-mm-yyyy) /to" +
                        " (end, in format yyyy-mm-dd or dd-mm-yyyy)\"");
            } else if (endString.isBlank()) {
                return new ReturnMessage("  ~  Please give an end time for the event.",
                        "  ~  The format should be \"event" +
                        " (description) /from (start, in format yyyy-mm-dd or dd-mm-yyyy) /to" +
                        " (end, in format yyyy-mm-dd or dd-mm-yyyy)\"");
            } else {
                this.event = new Event(taskNameString, startString, endString);
                return new ReturnMessage(super.taskList.addTask(event));
            }
        } catch (DateTimeParseException parseException) {
            return new ReturnMessage("  ~  Please format the event as \"event" +
                    " (description) /from (start, in format yyyy-mm-dd or dd-mm-yyyy) /to" +
                    " (end, in format yyyy-mm-dd or dd-mm-yyyy)\"");
        } catch (DateTimeException dateException) {
            return new ReturnMessage("  ~  Please ensure end time is later than start time!",
                    "  ~  Format the event as \"event (description) /from" +
                            " (start, in format yyyy-mm-dd or dd-mm-yyyy) /to" +
                            " (end, in format yyyy-mm-dd or dd-mm-yyyy)\"");
        }
    }

    @Override
    public void write(Path filePath) {
        try {
            if (this.event != null) {
                this.event.writeToFile(filePath);
            }
        } catch (IOException writeException) {
            throw new RuntimeException("Problem writing to file.");
        }
    }
}
