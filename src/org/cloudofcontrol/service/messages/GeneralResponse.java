package org.cloudofcontrol.service.messages;

/**
 * Created by IntelliJ IDEA.
 * User: Olli
 * Date: 05.12.11
 * Time: 11:07
 * To change this template use File | Settings | File Templates.
 */
public abstract class GeneralResponse {

    private boolean success;
    private String failure;

    public GeneralResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFailure() {
        return failure;
    }

    public void setFailure(String failure) {
        this.failure = failure;
    }
}
