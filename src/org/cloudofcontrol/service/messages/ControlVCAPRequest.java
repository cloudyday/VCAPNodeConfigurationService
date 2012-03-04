package org.cloudofcontrol.service.messages;

/**
 * Created by IntelliJ IDEA.
 * User: Olli
 * Date: 05.12.11
 * Time: 12:59
 * To change this template use File | Settings | File Templates.
 */
public class ControlVCAPRequest extends GeneralRequest {

    private String command;

    public ControlVCAPRequest(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
