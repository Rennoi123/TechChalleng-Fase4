package br.com.techchallenge.feedback;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.inject.Inject;

@Path("/avaliacao")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AvaliacaoResource {

    @Inject
    FeedbackService service;

    @POST
    public Response avaliar(FeedbackItem request) {
        FeedbackItem saved = service.processar(request);
        return Response.status(Response.Status.CREATED).entity(saved).build();
    }
}