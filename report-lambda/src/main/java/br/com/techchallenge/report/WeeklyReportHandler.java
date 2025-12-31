package br.com.techchallenge.report;

import com.amazonaws.services.lambda.runtime.*;
import software.amazon.awssdk.services.dynamodb.*;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.sns.*;
import software.amazon.awssdk.services.sns.model.*;

public class WeeklyReportHandler implements RequestHandler<Object, Void> {

    DynamoDbClient dynamo = DynamoDbClient.create();
    SnsClient sns = SnsClient.create();
    String tableName = System.getenv("TABLE_NAME");
    String topicArn = System.getenv("SNS_TOPIC_ARN");

    @Override
    public Void handleRequest(Object input, Context context) {
        ScanResponse scan = dynamo.scan(ScanRequest.builder().tableName(tableName).build());
        String msg = "Relatório semanal - Total de avaliações: " + scan.count();
        sns.publish(PublishRequest.builder()
            .topicArn(topicArn)
            .subject("Relatório Semanal")
            .message(msg)
            .build());
        context.getLogger().log("Relatório enviado");
        return null;
    }
}