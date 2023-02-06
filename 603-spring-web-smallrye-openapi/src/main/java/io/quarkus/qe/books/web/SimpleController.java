package io.quarkus.qe.books.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;

@RestController
public class SimpleController {

    @Value("${spring.application.name}")
    String appName;

    @Inject
    Template home;

    @RequestMapping(value = "/", produces = MediaType.TEXT_HTML, method = RequestMethod.GET)
    public TemplateInstance homePage() {
        return home.data("appName", appName);
    }
}
