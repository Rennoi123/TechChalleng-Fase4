package br.com.fiap.postech.techchallenge.model;

import java.util.Map;

public class WeeklyReportResponse {

    private String generatedAt;
    private double averageScore;
    private Map<String, Long> evaluationsPerDay;
    private Map<String, Long> evaluationsPerUrgency;

    public WeeklyReportResponse(String generatedAt,
                                double averageScore,
                                Map<String, Long> evaluationsPerDay,
                                Map<String, Long> evaluationsPerUrgency) {
        this.generatedAt = generatedAt;
        this.averageScore = averageScore;
        this.evaluationsPerDay = evaluationsPerDay;
        this.evaluationsPerUrgency = evaluationsPerUrgency;
    }

    public String getGeneratedAt() {
        return generatedAt;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public Map<String, Long> getEvaluationsPerDay() {
        return evaluationsPerDay;
    }

    public Map<String, Long> getEvaluationsPerUrgency() {
        return evaluationsPerUrgency;
    }
}
