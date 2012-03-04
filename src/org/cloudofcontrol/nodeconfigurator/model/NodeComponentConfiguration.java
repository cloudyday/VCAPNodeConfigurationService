package org.cloudofcontrol.nodeconfigurator.model;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Olli
 * Date: 29.11.11
 * Time: 17:17
 * To change this template use File | Settings | File Templates.
 */
public class NodeComponentConfiguration {

    private NodeComponent nodeComponent;
    private HashMap<String,String> settings;

    public NodeComponentConfiguration(NodeComponent nodeComponent) {
        this.nodeComponent = nodeComponent;
        this.settings = new HashMap<String, String>();
    }

    public void changeParam(String param, String value) {
        settings.put(param, value);
    }

    public String getParam(String param) {
        return settings.get(param);
    }


    public NodeComponent getNodeComponent() {
        return nodeComponent;
    }

    public void setNodeComponent(NodeComponent nodeComponent) {
        this.nodeComponent = nodeComponent;
    }

    public HashMap<String, String> getSettings() {
        return settings;
    }

    public void setSettings(HashMap<String, String> settings) {
        this.settings = settings;
    }


}
