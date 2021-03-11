package org.acme.spring.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/get")
public class GetController {

    private static final String HELLO_SPRING = "Hello Spring";

    @GetMapping(value = "/no-type")
    public String getNoType() {
        return HELLO_SPRING;
    }

    @GetMapping(value = "/text-plain", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getTextPlain() {
        return HELLO_SPRING;
    }

    @GetMapping(value = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getJson() {
        return HELLO_SPRING;
    }

    @GetMapping(value = "/octet-stream", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public String getOctetStream() {
        return HELLO_SPRING;
    }
}
