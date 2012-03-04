package org.cloudofcontrol;

import org.cloudofcontrol.nodeconfigurator.model.NodeComponentConfiguration;
import org.cloudofcontrol.nodeconfigurator.model.NodeComponent;
import org.cloudofcontrol.nodeconfigurator.model.NodeConfiguration;
import org.cloudofcontrol.nodeconfigurator.worker.NodeConfiguratorJSON;
import org.cloudofcontrol.nodeconfigurator.worker.NodeConfiguratorYAML;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Lukas Lampe
 * Date: 11.11.11
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
public class NodeConfiguratorMain {

    public static void main(String[] args) {

        String fileDirection = "/Users/Olli/Desktop/vcapconfigfiles";

        NodeConfiguration nodeConfiguration = new NodeConfiguration(false, "api.test123.com", "111.111.111.111", "5432", "9876", "username", "password");

        NodeComponentConfiguration dea = new NodeComponentConfiguration(NodeComponent.DEA);
        dea.changeParam("heartbeat", "10");
        try {
            nodeConfiguration.addComponent(dea);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        NodeComponentConfiguration mysql0 = new NodeComponentConfiguration(NodeComponent.MYSQL_NODE);
        mysql0.changeParam("index", "0");
        mysql0.changeParam("available_storage", "1024");
        try {
            nodeConfiguration.addComponent(mysql0);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            nodeConfiguration.addComponent(NodeComponent.NATS_SERVER);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


/*        nodeConfiguration.addComponent(org.cloudofcontrol.org.cloudofcontrol.nodeconfigurator.model.NodeComponent.MONGODB_NODE);
try {
    nodeConfiguration.addNodeIndex(org.cloudofcontrol.org.cloudofcontrol.nodeconfigurator.model.NodeComponent.MONGODB_NODE, 1);
} catch (Exception e) {
    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
}*/


        NodeConfiguratorYAML nodeConfiguratorYAML = new NodeConfiguratorYAML(fileDirection, nodeConfiguration);
        try {
            nodeConfiguratorYAML.changeYMLFiles();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

/*        *//*
        change REST node
         *//*
        Map<String,String> changeParams = new HashMap<String,String>();
        changeParams.put("external_uri", "api.cloudofcontrol.nodeconf.org");
        changeParams.put("mbus", "nats://nats:nats@127.0.0.1:4222/");
        changeParams.put("cloud_controller_uri", "api.cloudofcontrol.nodeconf.org");
        configurator.changeRest(changeParams);


        *//*
        change DEA node
         *//*
        changeParams = new HashMap<String,String>();
        changeParams.put("mbus", "nats://nats:nats@111.222.333.444:4222/");
        configurator.changeDea(changeParams);

        *//*
        change MYSQL-MONGO node
         *//*
        changeParams = new HashMap<String,String>();
        changeParams.put("mbus", "nats://nats:nats@111.222.333.444:4222/");
        changeParams.put("index", "0");
        configurator.changeMysqlMongo(changeParams);*/


        NodeConfiguratorJSON nodeConfiguratorJSON = new NodeConfiguratorJSON(fileDirection, nodeConfiguration);

        try {
            nodeConfiguratorJSON.doNodeConf();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
