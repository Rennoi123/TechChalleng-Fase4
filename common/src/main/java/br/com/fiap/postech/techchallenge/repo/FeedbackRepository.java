package br.com.fiap.postech.techchallenge.repo;

import br.com.fiap.postech.techchallenge.model.FeedbackItem;
import jakarta.inject.Inject;

import java.time.Instant;
import java.util.List;
public interface FeedbackRepository {
  void save(FeedbackItem item);
  List<FeedbackItem> findSince(Instant sinceUtc);
}
