package io.quarkus.qe.core;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

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
    @Path("/image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] postFormReturnFile(@MultipartForm MultipartBody multipartBody) throws IOException {
        return IOUtils.toByteArray(multipartBody.image);
    }

    @POST
    @Path("/data")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] postFormReturnData(@MultipartForm MultipartBody multipartBody) throws IOException {
        return IOUtils.toByteArray(multipartBody.data);
    }

    @POST
    @Path("/echo")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public String echo(String requestBody) throws Exception {
        return requestBody;
    }
}
