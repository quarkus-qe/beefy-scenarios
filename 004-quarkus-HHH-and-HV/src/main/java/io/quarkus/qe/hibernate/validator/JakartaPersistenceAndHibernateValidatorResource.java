package io.quarkus.qe.hibernate.validator;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class JakartaPersistenceAndHibernateValidatorResource {
    // reproducer for https://github.com/quarkusio/quarkus/issues/8323

    @Inject
    Validator validator;

    @Inject
    EntityManager em;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String hello() {

        String greetingText = "hello";

        Human human = em.find(Human.class, 1L);
        if (human == null) {
            human = new Human();
            human.setName(UUID.randomUUID().toString());
        }

        Hello hello = new Hello();
        hello.setGreetingText(greetingText);
        hello.setSomeEntity(createSomeEntity(4));

        human.addGreeting(hello);

        try {
            validator.validate(human);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return sw.toString();
        }

        if (human.getId() == null) {
            em.persist(human);
        }

        return hello.getGreetingText() + " " + hello.getGreetedHuman().getName();
    }

    private SomeEntity createSomeEntity(int childAmount) {
        SomeEntity someEntity = new SomeEntity();

        for (int i = 0; i < childAmount; i++) {
            someEntity.addSomeChild(new SomeChildEntity());
        }

        return someEntity;
    }
}