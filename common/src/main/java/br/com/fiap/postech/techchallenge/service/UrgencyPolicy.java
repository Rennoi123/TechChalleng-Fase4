package br.com.fiap.postech.techchallenge.service;

import br.com.fiap.postech.techchallenge.model.Urgency;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UrgencyPolicy {
  public Urgency classify(int nota) {
    if (nota <= 3) return Urgency.HIGH;
    if (nota <= 6) return Urgency.MEDIUM;
    return Urgency.LOW;
  }
}
