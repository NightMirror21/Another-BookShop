package ru.nightmirror.bookshop.commands;

public enum Command {

    PRINT_BALANCE("print balance"),
    SHOW_STOCK("show books in stock"),
    BUY_BOOK("buy"),
    SHOW_PURCHASED("show bought books"),
    EXIT("exit");

    private final String name;
    private String data;

    Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static Command parseString(String commandStr) {
        try {
            for (Command command : values()) {
                if (command.name.length() > commandStr.length()) continue;

                boolean correct = true;
                for (int i = 0; i < command.name.split(" ").length; i++) {
                    if (!command.name.split(" ")[i].equals(commandStr.split(" ")[i])) {
                        correct = false;
                    }
                }
                if (correct) {
                    command.setData(commandStr.replaceFirst(command.name, "").trim());
                    return command;
                }
            }
        } catch (Exception exception) {
            return null;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
