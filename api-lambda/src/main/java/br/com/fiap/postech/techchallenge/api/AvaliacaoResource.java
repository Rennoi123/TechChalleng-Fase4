package br.com.fiap.postech.techchallenge.api;

import br.com.fiap.postech.techchallenge.model.*;
import br.com.fiap.postech.techchallenge.repo.FeedbackRepository;
import br.com.fiap.postech.techchallenge.service.NotificationService;
import br.com.fiap.postech.techchallenge.service.UrgencyPolicy;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/avaliacao")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AvaliacaoResource {

  private final FeedbackRepository repository;
  private final NotificationService notificationService;
  private final UrgencyPolicy urgencyPolicy;

  @Inject
  public AvaliacaoResource(FeedbackRepository repository,
                           NotificationService notificationService,
                           UrgencyPolicy urgencyPolicy) {
    this.repository = repository;
    this.notificationService = notificationService;
    this.urgencyPolicy = urgencyPolicy;
  }

  @POST
  public FeedbackResponse avaliar(FeedbackRequest request) {
    request.validate();

    Urgency urgency = urgencyPolicy.classify(request.getNota());
    FeedbackItem item = FeedbackItem.create(request.getDescricao(), request.getNota(), urgency);

    repository.save(item);

    if (urgency.isCritical()) {
      notificationService.notifyCritical(item);
    }

    return FeedbackResponse.ok(item.getId(), urgency.name());
  }
}
