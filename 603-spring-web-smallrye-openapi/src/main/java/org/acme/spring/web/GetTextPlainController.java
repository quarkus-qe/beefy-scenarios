package org.acme.spring.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/get-text-plain", produces = MediaType.TEXT_PLAIN_VALUE)
public class GetTextPlainController {

    @GetMapping
    public String getTextPlain() {
        return "Hello Spring";
    }
}
