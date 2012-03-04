package org.cloudofcontrol.service.messages;

import org.cloudofcontrol.nodeconfigurator.model.NodeComponentConfiguration;
import org.cloudofcontrol.nodeconfigurator.model.NodeConfiguration;

/**
 * Created by IntelliJ IDEA.
 * User: Olli
 * Date: 05.12.11
 * Time: 11:33
 * To change this template use File | Settings | File Templates.
 */
public class ConfigureNodeRequest extends GeneralRequest {

    private NodeConfiguration nodeConfiguration;

    public ConfigureNodeRequest(NodeConfiguration nodeConfiguration) {
        this.nodeConfiguration = nodeConfiguration;
    }

    public NodeConfiguration getNodeConfiguration() {
        return nodeConfiguration;
    }

    public void setNodeConfiguration(NodeConfiguration nodeConfiguration) {
        this.nodeConfiguration = nodeConfiguration;
    }
}
