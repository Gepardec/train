package com.gepardec.train;

import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class Configuration {

    private static Configuration CONFIG;

    public String id = UUID.randomUUID().toString();
    public String account;
    public String region;
    public String training;
    public int instanceCount;
    public String ami;

    public String indexedPublicKey(int idx) {
        try {
            return Files.readString(Paths.get("id_rsa_" + idx + ".pub"));
        } catch (IOException e) {
            throw new RuntimeException("Could not load public key", e);
        }
    }

    public String indexedIdSuffix(String name, int i) {
        return idSuffix(name) + i;
    }

    public String idSuffix(String name) {
        return name + training;
    }

    public static Configuration load() {
        if (CONFIG == null) {
            try (var is = Files.newInputStream(Paths.get("configuration.json"))) {
                var jsonConfig = new JsonbConfig().withNullValues(true).withFormatting(true);
                CONFIG = JsonbBuilder.create(jsonConfig).fromJson(is, Configuration.class);
            } catch (Exception e) {
                throw new RuntimeException("Loading of the configuration failed", e);
            }
        }
        return CONFIG;
    }
}
