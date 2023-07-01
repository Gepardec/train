package com.gepardec.train;

import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class Configuration {
    private static final String OUT_PATH = System.getenv().getOrDefault("OUT_DIR", "out");
    private static final String CONFIG_PATH = System.getenv().getOrDefault("CONFIGURATION_DIR", "config");
    private static final Path BOOTSTRAP_SCRIPT = Paths.get(CONFIG_PATH).resolve("bootstrap.sh");

    public static Configuration CONFIG = load();

    public String id;
    public String account;
    public String region;
    public int instanceCount;
    public String ami;

    public Optional<String> bootstrapFile() {
        try {
            if (Files.exists(BOOTSTRAP_SCRIPT)) {
                return Optional.of(Files.readString(BOOTSTRAP_SCRIPT, StandardCharsets.UTF_8));
            }
            return Optional.empty();
        } catch (IOException e) {
            throw new RuntimeException("Boostrap files could not be listed", e);
        }
    }

    public String indexedPublicKey(int idx) {
        try {
            return Files.readString(Paths.get(OUT_PATH).resolve("id_rsa_" + idx + ".pub"));
        } catch (IOException e) {
            throw new RuntimeException("Could not load public key", e);
        }
    }
    
    public String indexed(String name, int i) {
        return name + i;
    }

    public String indexedIdSuffix(String name, int i) {
        return indexed(idSuffix(name), i);
    }

    public String idSuffix(String name) {
        return name + id;
    }

    private static Configuration load() {
        try (var is = Files.newInputStream(Paths.get(CONFIG_PATH).resolve("configuration.json"))) {
            var jsonConfig = new JsonbConfig().withNullValues(true).withFormatting(true);
            return JsonbBuilder.create(jsonConfig).fromJson(is, Configuration.class);
        } catch (Exception e) {
            throw new RuntimeException("Loading of the configuration failed", e);
        }
    }
}
