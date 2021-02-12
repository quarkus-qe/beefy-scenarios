package io.quarkus.qe.core;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Path("/multipart")
public class MultipartResource {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @MultipartForm
    public MultipartBody postForm(@MultipartForm MultipartBody multipartBody) {
        return multipartBody;
    }

    @POST
    @Path("/text")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public String postFormReturnText(@MultipartForm MultipartBody multipartBody) {
        return multipartBody.text;
    }

    @POST
    @Path("/file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public String postFormReturnFile(@MultipartForm MultipartBody multipartBody) throws IOException {
        return IOUtils.toString(multipartBody.file, StandardCharsets.UTF_8);
    }

    @POST
    @Path("/data")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public String postFormReturnData(@MultipartForm MultipartBody multipartBody) throws IOException {
        return IOUtils.toString(multipartBody.data, StandardCharsets.UTF_8);
    }
}
