package br.com.fiap.postech.techchallenge.lambda;

import br.com.fiap.postech.techchallenge.model.WeeklyReportResponse;
import br.com.fiap.postech.techchallenge.service.NotificationService;
import br.com.fiap.postech.techchallenge.service.ReportService;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import jakarta.inject.Named;

import java.util.Map;

@Named("weeklyReport")
public class WeeklyReportHandler implements RequestHandler<Map<String, Object>, WeeklyReportResponse> {

  private final ReportService reportService;
  private final NotificationService notificationService;

  public WeeklyReportHandler(ReportService reportService, NotificationService notificationService) {
    this.reportService = reportService;
    this.notificationService = notificationService;
  }

  @Override
  public WeeklyReportResponse handleRequest(Map<String, Object> input, Context context) {
    WeeklyReportResponse report = reportService.generateWeeklyReport();
    notificationService.notifyWeeklyReport(reportService.formatAsText(report));
    return report;
  }
}
