package org.cloudofcontrol.service;

import org.cloudofcontrol.service.RegistrationInfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Olli
 * Date: 02.12.11
 * Time: 12:49
 * To change this template use File | Settings | File Templates.
 */
public abstract class Registration {


    public static void register(RegistrationInfo registrationInfo) throws Exception {
        File file = new File("./cloudofcontrol.user");

        // idempodent... just overwrite registration
        file.delete();

/*        String pass = registrationInfo.getPassword();
        String md5 = DigestUtils.md5Hex(pass);*/

        if (file.exists()) {
            throw new Exception("already a user registered");
        } else {
            String registration = "" + registrationInfo.getEmail() + "\n" + registrationInfo.getPassword();
            createFile("./cloudofcontrol.user", registration);
        }

    }


    private static void createFile(String fileDirection, String content) throws IOException {
        File res = new File(fileDirection);
        BufferedWriter out = new BufferedWriter(new FileWriter(res));
        out.write(content);
        out.close();
    }
}
