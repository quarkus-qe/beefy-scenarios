package io.quarkus.qe.core;

import java.io.InputStream;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;

public class MultipartBody {

    @FormParam("text")
    @PartType(MediaType.TEXT_PLAIN)
    public String text;

    @FormParam("image")
    @PartType("image/png")
    public InputStream image;

    @FormParam("data")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public InputStream data;
}
