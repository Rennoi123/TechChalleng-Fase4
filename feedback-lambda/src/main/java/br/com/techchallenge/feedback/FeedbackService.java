package br.com.techchallenge.feedback;

import jakarta.enterprise.context.ApplicationScoped;
import software.amazon.awssdk.services.dynamodb.*;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.eventbridge.*;
import software.amazon.awssdk.services.eventbridge.model.*;

@ApplicationScoped
public class FeedbackService {

    DynamoDbClient dynamo = DynamoDbClient.create();
    EventBridgeClient eventBridge = EventBridgeClient.create();
    String tableName = System.getenv("TABLE_NAME");

    public FeedbackItem processar(FeedbackItem item) {
        item.definirUrgencia();

        dynamo.putItem(PutItemRequest.builder()
            .tableName(tableName)
            .item(item.toMap())
            .build());

        if ("ALTA".equals(item.urgencia)) {
            eventBridge.putEvents(PutEventsRequest.builder()
                .entries(PutEventsRequestEntry.builder()
                    .source("techchallenge.feedback")
                    .detailType("feedback-critico")
                    .detail(item.toJson())
                    .build())
                .build());
        }
        return item;
    }
}