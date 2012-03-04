package org.cloudofcontrol.service;

/**
 * Created by IntelliJ IDEA.
 * User: Olli
 * Date: 02.12.11
 * Time: 12:46
 * To change this template use File | Settings | File Templates.
 */
public class RegistrationInfo {

    private String email;
    private String password;

    public RegistrationInfo(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
