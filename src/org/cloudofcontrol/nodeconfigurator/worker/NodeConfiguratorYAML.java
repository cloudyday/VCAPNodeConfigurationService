package org.cloudofcontrol.nodeconfigurator.worker;


import org.cloudofcontrol.nodeconfigurator.model.NodeComponent;
import org.cloudofcontrol.nodeconfigurator.model.NodeComponentConfiguration;
import org.cloudofcontrol.nodeconfigurator.model.NodeConfiguration;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Olli
 * Date: 15.11.11
 * Time: 14:38
 * To change this template use File | Settings | File Templates.
 */

public class NodeConfiguratorYAML {

    private String path;
    private NodeConfiguration nodeConfiguration;


    public NodeConfiguratorYAML(String path, NodeConfiguration nodeConfiguration) {
        this.path = path + "/devbox/config";
        this.nodeConfiguration = nodeConfiguration;
    }


    public void changeYMLFiles() throws IOException {

        HashMap<String, String> changeParams = null;

        Set<NodeComponent> components = nodeConfiguration.getComponents();
        Map<NodeComponent, NodeComponentConfiguration> nodeComponentConfigurationMap = nodeConfiguration.getComponentConfigurationMap();

        for (NodeComponent component : components) {

            changeParams = new HashMap<String, String>();

            switch (component) {
                case DEA:
                    changeParams.put("mbus", "nats://" + nodeConfiguration.getNatsUser() + ":" + nodeConfiguration.getNatsPassword() + "@" + nodeConfiguration.getCloudControllerIP() + ":" + nodeConfiguration.getNatsPort() + "/");
                    break;

                case MYSQL_NODE:
                    changeParams.put("mbus", "nats://" + nodeConfiguration.getNatsUser() + ":" + nodeConfiguration.getNatsPassword() + "@" + nodeConfiguration.getCloudControllerIP() + ":" + nodeConfiguration.getNatsPort() + "/");
                    changeParams.put("index", nodeComponentConfigurationMap.get(component).getParam("index"));
                    changeParams.put("node_id", "mysql_node_"+changeParams.get("index"));
                    break;

                case MYSQL_GATEWAY:
                    changeParams.put("mbus", "nats://" + nodeConfiguration.getNatsUser() + ":" + nodeConfiguration.getNatsPassword() + "@" + nodeConfiguration.getCloudControllerIP() + ":" + nodeConfiguration.getNatsPort() + "/");
                    changeParams.put("cloud_controller_uri", nodeConfiguration.getCloudControllerURI());
                    break;

                case MONGODB_NODE:
                    changeParams.put("mbus", "nats://" + nodeConfiguration.getNatsUser() + ":" + nodeConfiguration.getNatsPassword() + "@" + nodeConfiguration.getCloudControllerIP() + ":" + nodeConfiguration.getNatsPort() + "/");
                    changeParams.put("index", nodeComponentConfigurationMap.get(component).getParam("index"));
                    changeParams.put("node_id", "mongodb_node_"+changeParams.get("index"));
                    break;

                case MONGODB_GATEWAY:
                    changeParams.put("mbus", "nats://" + nodeConfiguration.getNatsUser() + ":" + nodeConfiguration.getNatsPassword() + "@" + nodeConfiguration.getCloudControllerIP() + ":" + nodeConfiguration.getNatsPort() + "/");
                    changeParams.put("cloud_controller_uri", nodeConfiguration.getCloudControllerURI());
                    break;

                case REDIS_NODE:
                    changeParams.put("mbus", "nats://" + nodeConfiguration.getNatsUser() + ":" + nodeConfiguration.getNatsPassword() + "@" + nodeConfiguration.getCloudControllerIP() + ":" + nodeConfiguration.getNatsPort() + "/");
                    changeParams.put("index", nodeComponentConfigurationMap.get(component).getParam("index"));
                    changeParams.put("node_id", "redis_node_"+changeParams.get("index"));
                    break;

                case REDIS_GATEWAY:
                    changeParams.put("mbus", "nats://" + nodeConfiguration.getNatsUser() + ":" + nodeConfiguration.getNatsPassword() + "@" + nodeConfiguration.getCloudControllerIP() + ":" + nodeConfiguration.getNatsPort() + "/");
                    changeParams.put("cloud_controller_uri", nodeConfiguration.getCloudControllerURI());
                    break;

                case CLOUD_CONTROLLER:
                    changeParams.put("external_uri", nodeConfiguration.getCloudControllerURI());
                    changeParams.put("external_port", nodeConfiguration.getCloudControllerPort());
                    changeParams.put("mbus", "nats://" + nodeConfiguration.getNatsUser() + ":" + nodeConfiguration.getNatsPassword() + "@" + nodeConfiguration.getCloudControllerIP() + ":" + nodeConfiguration.getNatsPort() + "/");
                    changeParams.put("host", nodeConfiguration.getCloudControllerIP());
                    break;

                case HEALTH_MANAGER:
                    changeParams.put("mbus", "nats://" + nodeConfiguration.getNatsUser() + ":" + nodeConfiguration.getNatsPassword() + "@" + nodeConfiguration.getCloudControllerIP() + ":" + nodeConfiguration.getNatsPort() + "/");
                    changeParams.put("host", nodeConfiguration.getCloudControllerIP());
                    break;

                case ROUTER:
                    changeParams.put("mbus", "nats://" + nodeConfiguration.getNatsUser() + ":" + nodeConfiguration.getNatsPassword() + "@" + nodeConfiguration.getCloudControllerIP() + ":" + nodeConfiguration.getNatsPort() + "/");
                    break;

                case NATS_SERVER:
                    changeParams.put("net", nodeConfiguration.getCloudControllerIP());
                    changeParams.put("port", nodeConfiguration.getNatsPort());
                    changeParams.put("user", nodeConfiguration.getNatsUser());
                    changeParams.put("password", nodeConfiguration.getNatsPassword());
                    break;

                default:
                    break;
            }

            if (nodeComponentConfigurationMap.containsKey(component)) {
                NodeComponentConfiguration nodeComponentConfiguration = nodeComponentConfigurationMap.get(component);
                Map<String, String> settings = nodeComponentConfiguration.getSettings();
                for (String key : settings.keySet()) {
                    changeParams.put(key, settings.get(key));
                }
            }

            if(component == NodeComponent.NATS_SERVER) {
                this.changeFile("nats_server/nats_server", changeParams);
            } else {
                this.changeFile(component.toString().toLowerCase(), changeParams);
            }


        }

    }



    /*
    changes a VCAP YAML configuration file, given the module to change, e.g. cloud_controller
    and the corresponding change config which inhabits all the changeable configuration parameters
     */
    private void changeFile(String moduleToChange, Map<String, String> changeParams) throws IOException {

        String file = path + "/" + moduleToChange + ".yml";
        StringBuffer fileContents = getFile(file);

        for (String param : changeParams.keySet()) {
            fileContents = this.changeAttribute(param, changeParams.get(param), fileContents);
        }

        this.createFile(file, fileContents.toString());
    }


    /*
   change the attribute of a YAML file
    */
    private StringBuffer changeAttribute(String attribute2change, String newValue, StringBuffer fileContent) {

        String result = fileContent.toString();

        try {
            int changeIndexStart = fileContent.indexOf(attribute2change + ":");
            int eol = fileContent.indexOf("\n", changeIndexStart);
            String line2change = fileContent.substring(changeIndexStart, eol + 1);

            String newLine = attribute2change + ": " + newValue + "\n";

            String string2change = fileContent.toString();
            result = string2change.replaceFirst(line2change, newLine);

        } catch (Exception e) {
            System.out.println("Attribute "+attribute2change+" was not found");
        }

        return new StringBuffer(result);
    }


    /*
   read a file into a StringBuffer
    */
    private StringBuffer getFile(String filename) throws IOException {
        File file = new File(filename);
        if (file.isFile() == false) {
            throw new IOException(filename + " IS NOT A FILE");
        }
        FileReader fr = new FileReader(file);
        StringBuffer sb = new StringBuffer(1000);
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = fr.read(buf)) != -1) {
            sb.append(buf, 0, numRead);
        }

        fr.close();

        return sb;
    }

    /*
    write out a String to a file
     */
    private void createFile(String fileDirection, String content) throws IOException {
        File res = new File(fileDirection);
        BufferedWriter out = new BufferedWriter(new FileWriter(res));
        out.write(content);
        out.close();
    }
}
