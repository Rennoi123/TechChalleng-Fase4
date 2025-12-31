package br.com.techchallenge.notification;

import com.amazonaws.services.lambda.runtime.*;
import software.amazon.awssdk.services.sns.*;
import software.amazon.awssdk.services.sns.model.*;

import java.util.Map;

public class NotificationHandler implements RequestHandler<Map<String, Object>, Void> {

    SnsClient sns = SnsClient.create();
    String topicArn = System.getenv("SNS_TOPIC_ARN");

    @Override
    public Void handleRequest(Map<String, Object> event, Context context) {
        sns.publish(PublishRequest.builder()
            .topicArn(topicArn)
            .subject("Feedback Crítico")
            .message(event.toString())
            .build());
        context.getLogger().log("Notificação enviada");
        return null;
    }
}