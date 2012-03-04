package org.cloudofcontrol.nodeconfigurator.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Olli
 * Date: 26.11.11
 * Time: 13:50
 * To change this template use File | Settings | File Templates.
 */
public class NodeConfiguration {

    private Set<NodeComponent> components;
    private Map<NodeComponent, NodeComponentConfiguration> componentConfigurationMap;
    private boolean isCloudController;
    private String cloudControllerURI;
    private String cloudControllerIP;
    private String cloudControllerPort;
    private String natsPort;
    private String natsUser;
    private String natsPassword;

    public NodeConfiguration(boolean isCloudController, String cloudControllerURI, String cloudControllerIP, String cloudControllerPort, String natsPort, String natsUser, String natsPassword) {
        components = new HashSet<NodeComponent>();
        componentConfigurationMap = new HashMap<NodeComponent, NodeComponentConfiguration>();
        this.isCloudController = isCloudController;
        this.cloudControllerURI = cloudControllerURI;
        this.cloudControllerIP = cloudControllerIP;
        this.cloudControllerPort = cloudControllerPort;
        this.natsPort = natsPort;
        this.natsUser = natsUser;
        this.natsPassword = natsPassword;
    }

    public void addComponent(NodeComponent nodeComponent) throws Exception {

        if (nodeComponent == NodeComponent.MONGODB_NODE || nodeComponent == NodeComponent.MYSQL_NODE || nodeComponent == NodeComponent.REDIS_NODE) {
            throw new Exception("You must configure "+nodeComponent.toString()+" with a org.cloudofcontrol.org.cloudofcontrol.nodeconfigurator.model.NodeComponentConfiguration");
        }

        components.add(nodeComponent);
    }

    public void addComponent(NodeComponentConfiguration nodeComponentConfiguration) throws Exception {
        NodeComponent nodeComponent = nodeComponentConfiguration.getNodeComponent();

        if (nodeComponent == NodeComponent.MONGODB_NODE || nodeComponent == NodeComponent.MYSQL_NODE || nodeComponent == NodeComponent.REDIS_NODE) {
            if (nodeComponentConfiguration.getParam("index") == null) {
                throw new Exception("if adding the " + nodeComponent.toString() + " component, you MUST specifiy the param 'index'");
            }
        }

        components.add(nodeComponent);
        componentConfigurationMap.put(nodeComponent, nodeComponentConfiguration);
    }

    public Set<NodeComponent> getComponents() {
        return components;
    }

    public boolean isCloudController() {
        return isCloudController;
    }


    public String getCloudControllerURI() {
        return cloudControllerURI;
    }

    public void setCloudControllerURI(String cloudControllerURI) {
        this.cloudControllerURI = cloudControllerURI;
    }

    public String getCloudControllerIP() {
        return cloudControllerIP;
    }

    public void setCloudControllerIP(String cloudControllerIP) {
        this.cloudControllerIP = cloudControllerIP;
    }

    public String getCloudControllerPort() {
        return cloudControllerPort;
    }

    public void setCloudControllerPort(String cloudControllerPort) {
        this.cloudControllerPort = cloudControllerPort;
    }

    public String getNatsPort() {
        return natsPort;
    }

    public void setNatsPort(String natsPort) {
        this.natsPort = natsPort;
    }

    public String getNatsUser() {
        return natsUser;
    }

    public void setNatsUser(String natsUser) {
        this.natsUser = natsUser;
    }

    public String getNatsPassword() {
        return natsPassword;
    }

    public void setNatsPassword(String natsPassword) {
        this.natsPassword = natsPassword;
    }

    public Map<NodeComponent, NodeComponentConfiguration> getComponentConfigurationMap() {
        return componentConfigurationMap;
    }

    public void setComponents(Set<NodeComponent> components) {
        this.components = components;
    }

    public void setCloudController(boolean cloudController) {
        isCloudController = cloudController;
    }
}
