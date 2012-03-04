package org.cloudofcontrol.service.messages;

import org.cloudofcontrol.vcap.VCAPNodeStatus;

/**
 * Created by IntelliJ IDEA.
 * User: Olli
 * Date: 05.12.11
 * Time: 11:10
 * To change this template use File | Settings | File Templates.
 */
public class GetNodeStatusResponse extends GeneralResponse {

    private VCAPNodeStatus nodeStatus;

    public GetNodeStatusResponse(VCAPNodeStatus nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    public VCAPNodeStatus getNodeStatus() {
        return nodeStatus;
    }

    public void setNodeStatus(VCAPNodeStatus nodeStatus) {
        this.nodeStatus = nodeStatus;
    }
}
