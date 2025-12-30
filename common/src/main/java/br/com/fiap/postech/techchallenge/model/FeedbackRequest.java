package br.com.fiap.postech.techchallenge.model;

public class FeedbackRequest {

  private String descricao;
  private Integer nota;

  public FeedbackRequest() {
  }

  public FeedbackRequest(String descricao, Integer nota) {
    this.descricao = descricao;
    this.nota = nota;
  }

  public String getDescricao() {
    return descricao;
  }

  public Integer getNota() {
    return nota;
  }

  public void validate() {
    if (descricao == null || descricao.isBlank()) {
      throw new IllegalArgumentException("descricao é obrigatória");
    }
    if (nota == null || nota < 0 || nota > 10) {
      throw new IllegalArgumentException("nota deve estar entre 0 e 10");
    }
  }
}
