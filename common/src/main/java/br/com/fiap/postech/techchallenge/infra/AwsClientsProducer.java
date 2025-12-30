package br.com.fiap.postech.techchallenge.infra;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.sns.SnsClient;

@ApplicationScoped
public class AwsClientsProducer {

  @ConfigProperty(name = "app.aws-region", defaultValue = "us-east-1")
  String region;

  @Produces
  @ApplicationScoped
  public DynamoDbEnhancedClient dynamoEnhanced() {
    DynamoDbClient client = DynamoDbClient.builder()
        .region(Region.of(region))
        .build();
    return DynamoDbEnhancedClient.builder()
        .dynamoDbClient(client)
        .build();
  }

  @Produces
  @ApplicationScoped
  public SnsClient snsClient() {
    return SnsClient.builder()
        .region(Region.of(region))
        .build();
  }
}
