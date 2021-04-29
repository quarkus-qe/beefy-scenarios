package quarkus.qe;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import quarkus.qe.config.AntagonistConfiguration;
import quarkus.qe.config.ProtagonistConfiguration;
import quarkus.qe.converter.KrustyEmail;

@Path("/hello")
public class GreetingResource {

    @Inject
    Config config;

    @Inject
    ProtagonistConfiguration protagonist;

    @Inject
    AntagonistConfiguration antagonist;

    @ConfigProperty(name = "restaurant.employees")
    List<KrustyEmail> employeesEmails;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String welcomeMessage() {
        return config.getValue("welcome.message", String.class);
    } // declared in .env

    @GET
    @Path("/protagonist")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloProtagonist() {
        return protagonist.getName() + " says: " + protagonist.getMessage() + ". My hobie is: " + protagonist.getHobby();
    }

    @GET
    @Path("/protagonist/friend")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloProtagonistFriend() {
        return protagonist.getFriendName() + " says: " + protagonist.getFriendMessage();
    }

    @GET
    @Path("/antagonist")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloAntagonist() {
        return antagonist.name + " says: " + antagonist.message;
    }

    @GET
    @Path("/antagonist/wife")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloAntagonistWife() {
        return antagonist.wife.name + " says: " + antagonist.wife.message;
    }

    @GET
    @Path("/emails")
    @Produces(MediaType.TEXT_PLAIN)
    public List<String> printEmails() {
        return employeesEmails.stream()
                .map(employee -> employee.getEmail())
                .collect(Collectors.toList());
    }
}
