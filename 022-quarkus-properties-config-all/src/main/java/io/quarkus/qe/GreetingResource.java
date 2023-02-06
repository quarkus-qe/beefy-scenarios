package io.quarkus.qe;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.qe.config.AntagonistConfiguration;
import io.quarkus.qe.config.ProtagonistConfiguration;
import io.quarkus.qe.converter.KrustyEmail;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

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
        return antagonist.name() + " says: " + antagonist.message();
    }

    @GET
    @Path("/antagonist/wife")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloAntagonistWife() {
        return antagonist.wife().name() + " says: " + antagonist.wife().message();
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
