package br.com.fiap.postech.techchallenge.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.Instant;
import java.util.UUID;

@DynamoDbBean
public class FeedbackItem {
  private String id;
  private String descricao;
  private Integer nota;
  private String urgencia;
  private String createdAt;

  public static FeedbackItem create(String descricao, int nota, Urgency urgency) {
    FeedbackItem i = new FeedbackItem();
    i.setId(UUID.randomUUID().toString());
    i.setDescricao(descricao);
    i.setNota(nota);
    i.setUrgencia(urgency.name());
    i.setCreatedAt(Instant.now().toString());
    return i;
  }

  @DynamoDbPartitionKey
  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getDescricao() { return descricao; }
  public void setDescricao(String descricao) { this.descricao = descricao; }

  public Integer getNota() { return nota; }
  public void setNota(Integer nota) { this.nota = nota; }

  public String getUrgencia() { return urgencia; }
  public void setUrgencia(String urgencia) { this.urgencia = urgencia; }

  public String getCreatedAt() { return createdAt; }
  public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
