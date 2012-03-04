package org.cloudofcontrol.vcap;

import org.cloudofcontrol.nodeconfigurator.model.NodeComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Olli
 * Date: 05.12.11
 * Time: 10:19
 * To change this template use File | Settings | File Templates.
 */
public class VCAPNodeStatus {

    private Map<NodeComponent, Boolean> componentStatus;

    public VCAPNodeStatus() {
        this.componentStatus = new HashMap<NodeComponent, Boolean>();
    }

    public void setComponentStatus(NodeComponent component, boolean status) {
        componentStatus.put(component, status);
    }


    public String getOverallStatus() {

        String status = "RUNNING";
        boolean allStopped = true;
        for (boolean value : componentStatus.values()) {
            if(!value) {
                status =  "ERRONEOUS";
            }
            if(value) {
                allStopped = false;
            }
        }

        if(allStopped) {
            status = "STOPPED";
        }

        return status;
    }

    public Map<NodeComponent, Boolean> getComponentStatus() {
        return componentStatus;
    }
}
