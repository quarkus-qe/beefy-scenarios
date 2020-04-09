package io.quarkus.qe.hibernate.validator;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class JpaAndHibernateValidatorResource {
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