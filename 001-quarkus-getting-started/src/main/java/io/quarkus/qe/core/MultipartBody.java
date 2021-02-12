package io.quarkus.qe.core;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

public class MultipartBody {

    @FormParam("text")
    @PartType(MediaType.TEXT_PLAIN)
    public String text;

    @FormParam("file")
    @PartType("image/png")
    public InputStream file;

    @FormParam("data")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public InputStream data;
}
