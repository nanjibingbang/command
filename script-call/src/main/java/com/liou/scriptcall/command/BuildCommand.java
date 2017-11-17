package com.liou.scriptcall.command;

public class BuildCommand extends ScriptCallCommand {

    public BuildCommand(String commandLine) {
        super(commandLine);
    }

    public String getProject() {
        return getParam("-t");
    }

    public String getModel() {
        return getParam("-m");
    }

    @Override
    public void record(String user, int exitCode) {
    }

    @Override
    public String toSystemCall() {
        return String.format("sh %sappbuild/appbuild", getScriptDir());
    }

}