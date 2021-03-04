package org.acme.spring.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/delete")
public class DeleteController {

    @DeleteMapping(value = "/no-type")
    public String postNoType(@RequestBody String body) {
        return body;
    }

    @DeleteMapping(value = "/text-plain", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String postTextPlain(@RequestBody String body) {
        return body;
    }

    @DeleteMapping(value = "/json", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String postJson(@RequestBody String body) {
        return body;
    }

    @DeleteMapping(value = "/octet-stream", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public String postOctetStream(@RequestBody String body) {
        return body;
    }
}
