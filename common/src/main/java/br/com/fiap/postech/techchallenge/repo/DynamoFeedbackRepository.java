package br.com.fiap.postech.techchallenge.repo;

import br.com.fiap.postech.techchallenge.model.FeedbackItem;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DynamoFeedbackRepository implements FeedbackRepository {

  private static final Logger log = Logger.getLogger(DynamoFeedbackRepository.class);

  private final DynamoDbTable<FeedbackItem> table;

  @Inject
  public DynamoFeedbackRepository(DynamoDbEnhancedClient enhanced,
                                  @ConfigProperty(name = "app.table-name", defaultValue = "FeedbackTable") String tableName) {
    this.table = enhanced.table(tableName, TableSchema.fromBean(FeedbackItem.class));
  }

  @Override
  public void save(FeedbackItem item) {
    table.putItem(item);
    log.infov("Feedback salvo. id={0}, urgencia={1}, createdAt={2}", item.getId(), item.getUrgencia(), item.getCreatedAt());
  }

  @Override
  public List<FeedbackItem> findSince(Instant sinceUtc) {
    List<FeedbackItem> items = new ArrayList<>();
    ScanEnhancedRequest request = ScanEnhancedRequest.builder().build();

    for (FeedbackItem i : table.scan(request).items()) {
      try {
        Instant createdAt = Instant.parse(i.getCreatedAt());
        if (!createdAt.isBefore(sinceUtc)) {
          items.add(i);
        }
      } catch (Exception e) {
        log.warnv("Ignorando item com createdAt inválido. id={0}, createdAt={1}", i.getId(), i.getCreatedAt());
      }
    }
    return items;
  }
}
