package com.command.base.command;

import java.util.HashMap;
import java.util.Map;

public class Command {

    private String commandLine;

    private Map<String, String> params = new HashMap<String, String>();

    public Command(String commandLine) {
        this.commandLine = commandLine;
        parseCommandLine(commandLine);
    }

    private void parseCommandLine(String commandLine) {
        String[] split = commandLine.split(" ");
        for (int i = 0; i < split.length; i++) {
            String str = split[i];
            if (str.startsWith("-") || str.startsWith("--")) {
                params.put(str, split[i + 1]);
            }
        }
    }

    public String getParam(String key) {
        return params.get(key);
    }

    public String getCommandLine() {
        return commandLine;
    }

    public boolean is(String sign) {
        return commandLine.startsWith(sign);
    }

}
