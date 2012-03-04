package org.cloudofcontrol.nodeconfigurator.worker;

import org.cloudofcontrol.nodeconfigurator.model.NodeComponentConfiguration;
import org.cloudofcontrol.nodeconfigurator.model.NodeConfiguration;
import org.cloudofcontrol.nodeconfigurator.model.NodeComponent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Olli
 * Date: 26.11.11
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public class NodeConfiguratorJSON {

    private String path;
    private NodeConfiguration nodeConfiguration;

    public NodeConfiguratorJSON(String path, NodeConfiguration nodeConfiguration) {
        this.path = path + "/devbox/config";
        this.nodeConfiguration = nodeConfiguration;
    }

    public void doNodeConf() throws Exception {

        String jsonString = null;

        File file = new File(path + "/vcap_components.json");
        if (!file.exists()) {
            throw new Exception(file.getName() + " not found");
        }

        jsonString = this.buildJSONString(file);
        this.createFile(file.getAbsolutePath(), jsonString);

        file = new File(path + "/deploy.json");
        if (!file.exists()) {
            throw new Exception(file.getName() + " not found");
        }

        jsonString = this.buildJSONString(file);
        this.createFile(file.getAbsolutePath(), jsonString);

    }


    private String buildJSONString(File file) {
        String jsonString;
        JSONObject jsonObject = new JSONObject();

        if (file.getName().contains("vcap_components")) {
            JSONArray jsonArray = new JSONArray();

            for (NodeComponent component : nodeConfiguration.getComponents()) {
                if (component == NodeComponent.NATS_SERVER) {
                    continue;
                }
                jsonArray.add(component.toString().toLowerCase());
            }

            jsonObject.put("components", jsonArray);


        } else if (file.getName().contains("deploy")) {

            Map<NodeComponent, NodeComponentConfiguration> nodeComponentConfigurationMap = nodeConfiguration.getComponentConfigurationMap();

            JSONObject home = new JSONObject();
            home.put("home", "/home/ubuntu/cloudfoundry");
            jsonObject.put("cloudfoundry", home);

            JSONObject jobs = new JSONObject();

            JSONObject install = new JSONObject();

            for (NodeComponent component : nodeConfiguration.getComponents()) {
                if (component == NodeComponent.MYSQL_NODE || component == NodeComponent.MONGODB_NODE || component == NodeComponent.REDIS_NODE) {
                    JSONObject index = new JSONObject();
                    index.put("index", nodeComponentConfigurationMap.get(component).getParam("index"));
                    install.put(component.toString().toLowerCase(), index);

                } else {
                    install.put(component.toString().toLowerCase(), null);
                }
            }

            if (nodeConfiguration.isCloudController()) {
                install.put("ccdb", null);
            }

            JSONObject cloudController = new JSONObject();
            JSONArray builtinServices = new JSONArray();
            builtinServices.add("redis");
            builtinServices.add("mongodb");
            builtinServices.add("mysql");
            cloudController.put("builtin_services", builtinServices);

            if (nodeConfiguration.isCloudController()) {
                install.put("cloud_controller", cloudController);
            }

            jobs.put("install", install);

            JSONObject installed = new JSONObject();
            JSONObject natsServer = new JSONObject();
            natsServer.put("port", nodeConfiguration.getNatsPort());
            natsServer.put("host", nodeConfiguration.getCloudControllerIP());
            natsServer.put("user", nodeConfiguration.getNatsUser());
            natsServer.put("password", nodeConfiguration.getNatsPassword());


            if (nodeConfiguration.isCloudController()) {
                installed = null;
            } else {
                installed.put("nats_server", natsServer);
            }

            jobs.put("installed", installed);

            for (NodeComponent component : nodeConfiguration.getComponents()) {
                if (component == NodeComponent.MYSQL_NODE || component == NodeComponent.MONGODB_NODE || component == NodeComponent.REDIS_NODE) {
                    JSONObject index = new JSONObject();
                    index.put("index", nodeComponentConfigurationMap.get(component).getParam("index"));
                    jsonObject.put(component.toString().toLowerCase(), index);
                }
            }

            jsonObject.put("jobs", jobs);

            if (!nodeConfiguration.isCloudController()) {
                jsonObject.put("nats_server", natsServer);
            }

            JSONArray runList = new JSONArray();
            runList.add("role[cloudfoundry]");
            for (NodeComponent component : nodeConfiguration.getComponents()) {
                runList.add("role[" + component.toString().toLowerCase() + "]");
            }
            if (nodeConfiguration.isCloudController()) {
                runList.add("role[ccdb]");
            }

            jsonObject.put("run_list", runList);

            if (nodeConfiguration.isCloudController()) {
                jsonObject.put("cloud_controller", builtinServices);
            }

            JSONObject deployment = new JSONObject();
            deployment.put("group", "1000");
            deployment.put("name", "devbox");
            deployment.put("user", "ubuntu");

            jsonObject.put("deployment", deployment);


        }

        jsonString = jsonObject.toJSONString().replaceAll("\\\\", "");
        return jsonString;


    }

    private void createFile(String fileDirection, String content) throws IOException {
        File res = new File(fileDirection);
        BufferedWriter out = new BufferedWriter(new FileWriter(res));
        out.write(content);
        out.close();
    }


}
