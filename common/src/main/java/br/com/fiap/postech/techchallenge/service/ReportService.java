package br.com.fiap.postech.techchallenge.service;

import br.com.fiap.postech.techchallenge.model.FeedbackItem;
import br.com.fiap.postech.techchallenge.model.WeeklyReportResponse;
import br.com.fiap.postech.techchallenge.repo.FeedbackRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class ReportService {

  private static final Logger log = Logger.getLogger(ReportService.class);

  private final FeedbackRepository repository;
  @Inject
  public ReportService(FeedbackRepository repository) {
    this.repository = repository;
  }

  public WeeklyReportResponse generateWeeklyReport() {
    Instant now = Instant.now();
    Instant since = now.minusSeconds(7L * 24 * 60 * 60);

    List<FeedbackItem> items = repository.findSince(since);

    DoubleSummaryStatistics stats = items.stream()
        .filter(i -> i.getNota() != null)
        .collect(Collectors.summarizingDouble(i -> i.getNota()));

    Map<String, Long> perUrgency = items.stream()
        .map(FeedbackItem::getUrgencia)
        .filter(u -> u != null && !u.isBlank())
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    Map<String, Long> perDay = items.stream()
        .map(i -> {
          try {
            Instant created = Instant.parse(i.getCreatedAt());
            LocalDate d = created.atZone(ZoneOffset.UTC).toLocalDate();
            return d.toString();
          } catch (Exception e) {
            return "INVALID_DATE";
          }
        })
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    WeeklyReportResponse report = new WeeklyReportResponse(
        now.toString(),
        stats.getCount() == 0 ? 0.0 : round2(stats.getAverage()),
        perDay,
        perUrgency
    );

    log.infov("Relatório semanal gerado. totalAvaliacoes={0}, media={1}", items.size(), report.getAverageScore());
    return report;
  }

  public String formatAsText(WeeklyReportResponse r) {
    return
            "Relatório semanal de feedbacks (UTC)\n" +
                    "Gerado em: " + r.getGeneratedAt() + "\n\n" +
                    "Média das notas: " + r.getAverageScore() + "\n\n" +
                    "Quantidade por dia:\n" + r.getEvaluationsPerDay() + "\n\n" +
                    "Quantidade por urgência:\n" + r.getEvaluationsPerUrgency();
  }

  private static double round2(double v) {
    return Math.round(v * 100.0) / 100.0;
  }
}
