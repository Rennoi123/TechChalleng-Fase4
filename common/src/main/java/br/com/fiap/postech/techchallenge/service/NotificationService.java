package br.com.fiap.postech.techchallenge.service;

import br.com.fiap.postech.techchallenge.model.FeedbackItem;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.time.Instant;

@ApplicationScoped
public class NotificationService {

  private static final Logger log = Logger.getLogger(NotificationService.class);

  @ConfigProperty(name = "app.sns-topic-arn", defaultValue = "")
  String topicArn;

  private final SnsClient sns;
  @Inject
  public NotificationService(SnsClient sns) {
    this.sns = sns;
  }

  public void notifyCritical(FeedbackItem item) {
    if (topicArn == null || topicArn.isBlank()) {
      log.warn("SNS_TOPIC_ARN não configurado; pulando notificação crítica.");
      return;
    }

    String subject = "[URGENTE] Feedback crítico recebido";
    String message =
            "Um feedback crítico foi registrado.\n\n" +
                    "Descrição: " + item.getDescricao() + "\n" +
                    "Urgência: " + item.getUrgencia() + "\n" +
                    "Data de envio (UTC): " + item.getCreatedAt() + "\n" +
                    "ID: " + item.getId();


    sns.publish(PublishRequest.builder()
        .topicArn(topicArn)
        .subject(subject)
        .message(message)
        .build());

    log.infov("Notificação crítica enviada via SNS. id={0}, at={1}", item.getId(), Instant.now());
  }

  public void notifyWeeklyReport(String reportText) {
    if (topicArn == null || topicArn.isBlank()) {
      log.warn("SNS_TOPIC_ARN não configurado; pulando envio do relatório.");
      return;
    }

    sns.publish(PublishRequest.builder()
        .topicArn(topicArn)
        .subject("[RELATORIO] Resumo semanal de feedbacks")
        .message(reportText)
        .build());

    log.info("Relatório semanal publicado no SNS.");
  }
}