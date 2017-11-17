package com.liou.scriptcall.command;

public class TestCommand extends ScriptCallCommand {

    public TestCommand(String commandLine) {
        super(commandLine);
    }

    @Override
    public void record(String user, int exitCode) {
    }

    @Override
    public String toSystemCall() {
        return String.format("%stest.bat %s", getScriptDir(), "TEST");
    }

}
