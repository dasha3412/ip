package Parser;

import Commands.*;

public class Parser {
    public Command parseInput(String input) {
        Command command;
        String[] inputWords = input.trim().split(" ");
        command = switch (inputWords[0]) {
            case "todo" -> new ToDoCommand(inputWords);
            case "deadline" -> new DeadlineCommand(inputWords);
            case "event" -> new EventCommand(inputWords);
            case "delete" -> new DeleteCommand(inputWords);
            case "mark" -> new MarkCommand(inputWords);
            case "unmark" -> new UnmarkCommand(inputWords);
            case "list" -> new ListCommand(inputWords);
            case "bye" -> new ExitCommand(inputWords);
            default -> new Command(inputWords);
        };
        return command;
    }
}