package org.cloudofcontrol.vcap;

import org.cloudofcontrol.nodeconfigurator.model.NodeComponent;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Olli
 * Date: 04.12.11
 * Time: 11:09
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProcessControl {

    public static VCAPNodeStatus scanVCAPProcess() throws IOException {

        ProcessBuilder processBuilder = new ProcessBuilder("/home/ubuntu/cloudfoundry/vcap/dev_setup/bin/vcap_dev", "status");
        processBuilder.redirectErrorStream(true);

        String line;

        Process process = processBuilder.start();
        InputStream stdout = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));

        VCAPNodeStatus nodeStatus = new VCAPNodeStatus();

        while ((line = reader.readLine()) != null) {

            if (line.contains("RUNNING") || line.contains("STOPPED")) {

                int firstTab = line.indexOf("\t");
                int secondTab = line.indexOf("\t", firstTab);
                String component = line.substring(0, firstTab - 1).trim();
                String status = line.substring(secondTab).trim();

                boolean isRunning;
                if (status.equals("RUNNING")) {
                    isRunning = true;
                } else {
                    isRunning = false;
                }
                System.out.println(component);
                System.out.println(isRunning);

                nodeStatus.setComponentStatus(NodeComponent.valueOf(component.toUpperCase()), isRunning);
            }

        }

        return nodeStatus;
    }

    public static void controlVCAP(String command) throws IOException {

        ProcessBuilder processBuilder = new ProcessBuilder("/home/ubuntu/cloudfoundry/vcap/dev_setup/bin/vcap_dev", command);
        processBuilder.redirectErrorStream(true);

        String line;

        Process process = processBuilder.start();
//        InputStream stdout = process.getInputStream();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
//
//        while ((line = reader.readLine()) != null) {
//            System.out.println(line);
//        }
    }


//    public static void scanProcess2() throws IOException {
//
//        Map<String, Boolean> statusMap = new HashMap<String, Boolean>();
//
//        File f = new File("/Users/Olli/Desktop/VCAPstatusString.txt");
//
//        BufferedReader in = new BufferedReader(new FileReader(f));
//        String line = null;
//
//        while ((line = in.readLine()) != null) {
//
//            if (line.contains("RUNNING") || line.contains("STOPPED")) {
//
//                int firstTab = line.indexOf("\t");
//                int secondTab = line.indexOf("\t", firstTab);
//                String component = line.substring(0, firstTab - 1).trim();
//                String status = line.substring(secondTab).trim();
//
//                boolean isRunning;
//                if (status.equals("RUNNING")) {
//                    isRunning = true;
//                } else {
//                    isRunning = false;
//                }
//                System.out.println(component);
//                System.out.println(isRunning);
//
//                statusMap.put(component, isRunning);
//            }
//
//        }
//
//    }

}
