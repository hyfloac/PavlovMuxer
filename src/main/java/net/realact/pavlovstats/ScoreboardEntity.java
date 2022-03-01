package net.realact.pavlovstats;

import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonRepresentation;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ScoreboardEntity
{
    @BsonId
    public UUID id;
    public String mapName;
    public String gameMode;
    public String playerCount;
    public int redTeamScore;
    public int blueTeamScore;
    @BsonRepresentation(BsonType.DATE_TIME)
    public Date started;
    @BsonRepresentation(BsonType.DATE_TIME)
    public Date concluded;
    public List<UserEntity> redTeam;
    public List<UserEntity> blueTeam;

    public ScoreboardEntity()
    { }

    public ScoreboardEntity(UUID id, String mapName, String gameMode, String playerCount, int redTeamScore, int blueTeamScore, Date started, Date concluded, List<UserEntity> redTeam, List<UserEntity> blueTeam)
    {
        this.id = id;
        this.mapName = mapName;
        this.gameMode = gameMode;
        this.playerCount = playerCount;
        this.redTeamScore = redTeamScore;
        this.blueTeamScore = blueTeamScore;
        this.started = started;
        this.concluded = concluded;
        this.redTeam = redTeam;
        this.blueTeam = blueTeam;
    }
}
