package br.com.techchallenge.feedback;

import java.time.Instant;
import java.util.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class FeedbackItem {
    public String id;
    public String descricao;
    public int nota;
    public String urgencia;
    public String dataCriacao;

    public void definirUrgencia() {
        id = UUID.randomUUID().toString();
        dataCriacao = Instant.now().toString();
        if (nota <= 3) urgencia = "ALTA";
        else if (nota <= 6) urgencia = "MEDIA";
        else urgencia = "BAIXA";
    }

    public Map<String, AttributeValue> toMap() {
        return Map.of(
            "id", AttributeValue.fromS(id),
            "descricao", AttributeValue.fromS(descricao),
            "nota", AttributeValue.fromN(String.valueOf(nota)),
            "urgencia", AttributeValue.fromS(urgencia),
            "dataCriacao", AttributeValue.fromS(dataCriacao)
        );
    }

    public String toJson() {
        return "{\"descricao\":\""+descricao+"\",\"urgencia\":\""+urgencia+"\",\"dataCriacao\":\""+dataCriacao+"\"}";
    }
}