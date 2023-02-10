package com.gepardec.train;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

public class AwsCdkApp {
    public static void main(final String[] args) {
        App app = new App();

        new EC2Stack(app, "EC2Stack", StackProps.builder()
                .env(Environment.builder()
                        .account("233723976103") // Should come from argument!!!!!
                        .region("eu-central-1")
                        .build())

                // For more information, see https://docs.aws.amazon.com/cdk/latest/guide/environments.html
                .build());

        app.synth();
    }
}

