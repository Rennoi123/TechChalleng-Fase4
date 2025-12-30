package br.com.fiap.postech.techchallenge.model;

public enum Urgency {
  LOW,
  MEDIUM,
  HIGH;

  public boolean isCritical() {
    return this == HIGH;
  }
}
