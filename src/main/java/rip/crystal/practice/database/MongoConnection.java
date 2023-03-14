package com.hysteria.practice.database;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;

import java.util.Objects;

/**
 * @author Hysteria Development
 * @project cPractice
 * @date 3/6/2023
 */

@Getter
public class MongoConnection {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public MongoConnection(String uri) {
        try {
            ConnectionString connectionString = new ConnectionString(uri);
            MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString).build();

            this.mongoClient = MongoClients.create(settings);
            this.mongoDatabase = this.mongoClient.getDatabase(Objects.requireNonNull(connectionString.getDatabase()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MongoConnection(String host, int port, String database) {
        try {
            this.mongoDatabase = new com.mongodb.MongoClient(host, port).getDatabase(database);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MongoConnection(String host, int port, String username, String password, String database) {
        try {
            this.mongoDatabase = new com.mongodb.MongoClient(
                new ServerAddress(host, port),
                MongoCredential.createCredential(username, database, password.toCharArray()),
                MongoClientOptions.builder().build()).getDatabase(database);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
