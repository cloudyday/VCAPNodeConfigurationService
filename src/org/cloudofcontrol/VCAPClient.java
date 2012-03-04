package org.cloudofcontrol;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.thoughtworks.xstream.XStream;
import org.cloudofcontrol.nodeconfigurator.model.NodeComponent;
import org.cloudofcontrol.nodeconfigurator.model.NodeComponentConfiguration;
import org.cloudofcontrol.nodeconfigurator.model.NodeConfiguration;


import javax.ws.rs.core.MediaType;


/**
 * Created by IntelliJ IDEA.
 * User: Olli
 * Date: 02.12.11
 * Time: 10:52
 * To change this template use File | Settings | File Templates.
 */
public class VCAPClient {

    public static void main(String[] args) {

        Client c = Client.create();
        WebResource r = c.resource("http://localhost:8080/rest/configuration");


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


        String xml = "";
        XStream xstream = new XStream();
        xml = xstream.toXML(nodeConfiguration);
        System.out.println(xml);


        ClientResponse response = null;
        try {
            response = r.type(MediaType.TEXT_PLAIN)
                    .accept(MediaType.TEXT_PLAIN)
                    .put(ClientResponse.class, xml);
            System.out.println(response.getStatus());
            System.out.println(response.getEntity(String.class));
        } catch (ClientHandlerException e) {

        }





    }
}
