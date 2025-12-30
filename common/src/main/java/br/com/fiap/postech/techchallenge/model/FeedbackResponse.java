package br.com.fiap.postech.techchallenge.model;

public class FeedbackResponse {

  private boolean success;
  private String id;
  private String urgency;

  public FeedbackResponse(boolean success, String id, String urgency) {
    this.success = success;
    this.id = id;
    this.urgency = urgency;
  }

  public static FeedbackResponse ok(String id, String urgency) {
    return new FeedbackResponse(true, id, urgency);
  }

  public boolean isSuccess() {
    return success;
  }

  public String getId() {
    return id;
  }

  public String getUrgency() {
    return urgency;
  }
}
