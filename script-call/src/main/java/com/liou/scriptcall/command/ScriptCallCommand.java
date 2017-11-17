package com.liou.scriptcall.command;

import com.command.base.command.Command;
import com.command.base.window.Application;

public abstract class ScriptCallCommand extends Command implements ScriptCallRecord {

    private boolean isScript;

    private String scriptDir;

    public ScriptCallCommand(String commandLine) {
        super(commandLine);
        scriptDir = Application.getConfig("script_dir");
        if (!scriptDir.endsWith("/")) {
            scriptDir += "/";
        }
    }

    public boolean isScript() {
        return isScript;
    }

    public void setScript(boolean isScript) {
        this.isScript = isScript;
    }

    public String getScriptDir() {
        return scriptDir;
    }

    public void setScriptDir(String scriptDir) {
        this.scriptDir = scriptDir;
    }

    public abstract String toSystemCall();

}
