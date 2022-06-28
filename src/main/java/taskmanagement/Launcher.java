package taskmanagement;

import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import taskmanagement.api.ApplicationConfig;

import java.io.File;

public class Launcher {

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();

        // configure server port number
        tomcat.setPort(8080);

        // remove defaulted JSP configs
        tomcat.setAddDefaultWebXmlToWebapp(false);

        // add the web app
        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File(".").getAbsolutePath());
        ResourceConfig resourceConfig = new ResourceConfig(ApplicationConfig.class);
        Tomcat.addServlet(ctx, "jersey-container-servlet", new ServletContainer(resourceConfig));
        ctx.addServletMappingDecoded("/*", "jersey-container-servlet");

        // start the server
        tomcat.start();
        System.out.println("Server listening on " + tomcat.getHost().getName() + ":" + tomcat.getConnector().getPort());
        tomcat.getServer().await();
    }
}

