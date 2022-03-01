package net.realact.pavlovstats;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.net.UnknownHostException;
import java.util.List;
import java.util.UUID;

public final class DatabaseManager
{
    public final MongoClient client;
    public final MongoDatabase database;
    public final MongoCollection<UserEntity> users;
    public final MongoCollection<ScoreboardEntity> scoreboards;

    private DatabaseManager(final MongoClient client, final MongoDatabase database, final MongoCollection<UserEntity> users, final MongoCollection<ScoreboardEntity> scoreboards)
    {
        this.client = client;
        this.database = database;
        this.users = users;
        this.scoreboards = scoreboards;
    }

    public static DatabaseManager create()
    {
        final CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        final CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), CodecRegistries.fromProviders(pojoCodecProvider));

        final MongoClient mongoClient = MongoClients.create(PropertyManager.DatabaseUrl);
        final MongoDatabase database = mongoClient.getDatabase(PropertyManager.DatabaseName).withCodecRegistry(pojoCodecRegistry);
        final MongoCollection<UserEntity> users = database.getCollection(PropertyManager.DatabaseUserTableName, UserEntity.class);
        final MongoCollection<ScoreboardEntity> scoreboards = database.getCollection(PropertyManager.DatabaseScoreboardTableName, ScoreboardEntity.class);

        return new DatabaseManager(mongoClient, database, users, scoreboards);
    }

    public void insertUser(UserEntity user)
    {
        users.insertOne(user);
    }

    public UserEntity getUserById(long steamId)
    {
        return users.find(Filters.eq("_id", steamId), UserEntity.class).first();
    }

    public Iterable<UserEntity> getAllPlayers()
    {
        return users.find().sort(Sorts.orderBy(Sorts.descending("kills"), Sorts.ascending("deaths"), Sorts.descending("assists")));
    }

    public void insertScoreboard(ScoreboardEntity entity)
    {
        scoreboards.insertOne(entity);
    }

    public ScoreboardEntity findScoreboard(UUID id)
    {
        return scoreboards.find(Filters.eq("_id", id), ScoreboardEntity.class).first();
    }

    public Iterable<ScoreboardEntity> findAllScoreboards()
    {
        return scoreboards.find();
    }
}
