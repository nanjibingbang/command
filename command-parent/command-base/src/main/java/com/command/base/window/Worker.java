package com.command.base.window;

import com.command.base.command.Command;

public interface Worker {

    void execute(Command command) throws Exception;

}
