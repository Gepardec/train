package com.gepardec.train;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.Tags;

public class AwsCdkApp {
    public static void main(final String[] args) {
        var config = Configuration.load();

        App app = new App();

        new EC2Stack(app, "TestTrainingStack", StackProps.builder()
                .env(Environment.builder()
                        .account(config.account)
                        .region(config.region)
                        .build())
                .build());

        Tags.of(app).add("id", config.id);
        Tags.of(app).add("owner", config.account);
        Tags.of(app).add("training", config.training);

        app.synth();
    }
}

