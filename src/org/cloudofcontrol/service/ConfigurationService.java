package org.cloudofcontrol.service;

import com.thoughtworks.xstream.XStream;
import org.cloudofcontrol.nodeconfigurator.model.NodeConfiguration;
import org.cloudofcontrol.nodeconfigurator.worker.NodeConfiguratorJSON;
import org.cloudofcontrol.nodeconfigurator.worker.NodeConfiguratorYAML;
import org.cloudofcontrol.service.messages.*;
import org.cloudofcontrol.vcap.ProcessControl;
import org.cloudofcontrol.vcap.VCAPNodeStatus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Olli
 * Date: 02.12.11
 * Time: 10:38
 * To change this template use File | Settings | File Templates.
 */

@Path("/")
public class ConfigurationService {

//    private final String fileDirection = "/Users/Olli/Desktop/vcapconfigfiles";
    private final String fileDirection = "/home/ubuntu/cloudfoundry/.deployments";


    @Path("/ping")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String ping() {
        return "pong";
    }


    @Path("/register")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String register(String registrationDetailsXML) {

        RegistrationInfo registrationInfo = null;
        XStream xstream = new XStream();
        registrationInfo = (RegistrationInfo) xstream.fromXML(registrationDetailsXML);


        boolean success = true;
        String failure = "";

        try {
            Registration.register(registrationInfo);
        } catch (Exception e) {
            failure = e.toString();
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            success = false;
        }

        RegisterResponse response = new RegisterResponse();
        response.setSuccess(success);

        if (!success) {
            response.setFailure(failure);
        }

        xstream.aliasType("RegisterResponse", RegisterResponse.class);
        String resp = xstream.toXML(response);

        return resp;
    }


    @Path("/configuration")
    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String configureNode(String configureNodeRequestXML) {
        ConfigureNodeRequest configureNodeRequest = null;
        XStream xstream = new XStream();
        xstream.aliasType("ConfigureNodeRequest", ConfigureNodeRequest.class);
        configureNodeRequest = (ConfigureNodeRequest) xstream.fromXML(configureNodeRequestXML);

        NodeConfiguration nodeConfiguration = configureNodeRequest.getNodeConfiguration();

        boolean success = true;
        String failure = "";

        try {
            new NodeConfiguratorYAML(fileDirection, nodeConfiguration).changeYMLFiles();
        } catch (IOException e) {
            failure = e.toString();
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            success = false;
        }

        try {
            new NodeConfiguratorJSON(fileDirection, nodeConfiguration).doNodeConf();
        } catch (Exception e) {
            failure = e.toString();
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            success = false;
        }

        ConfigureNodeResponse response = new ConfigureNodeResponse();
        response.setSuccess(success);

        if (!success) {
            response.setFailure(failure);
        }

        xstream.aliasType("ConfigureNodeResponse", ConfigureNodeResponse.class);
        String resp = xstream.toXML(response);

        return resp;
    }


    @Path("/status")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getNodeStatus() {


        XStream xstream = new XStream();

        VCAPNodeStatus nodeStatus = null;

        boolean success = true;
        String failure = "";

        try {
            nodeStatus = ProcessControl.scanVCAPProcess();
        } catch (IOException e) {
            failure = e.toString();
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            success = false;
        }

        GetNodeStatusResponse response = new GetNodeStatusResponse(nodeStatus);
        response.setSuccess(success);

        if (!success) {
            response.setFailure(failure);
        }

        xstream.aliasType("GetNodeStatusResponse", GetNodeStatusResponse.class);
        String resp = xstream.toXML(response);

        return resp;
    }


    @Path("/process")
    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String controlVCAP(String requestXML) {

        XStream xStream = new XStream();
        xStream.aliasType("ControlVCAPRequest", ControlVCAPRequest.class);
        xStream.aliasType("ControlVCAPResponse", ControlVCAPResponse.class);
        ControlVCAPRequest controlVCAPRequest = (ControlVCAPRequest) xStream.fromXML(requestXML);

        String command = controlVCAPRequest.getCommand();

        if (!command.equals("start") && !command.equals("stop") && !command.equals("restart")) {
            ControlVCAPResponse response = new ControlVCAPResponse();
            response.setFailure("Bad Request");
            response.setSuccess(false);

            return xStream.toXML(response);
        }

        String failure = "";
        boolean success = true;

        try {
            ProcessControl.controlVCAP(command);
        } catch (IOException e) {
            failure = e.toString();
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            success = false;
        }

        GeneralResponse response = new ControlVCAPResponse();
        response.setSuccess(success);

        if (!success) {
            response.setFailure(failure);
        }


        String resp = xStream.toXML(response);

        return resp;
    }


}
