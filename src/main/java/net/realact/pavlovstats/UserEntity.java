package net.realact.pavlovstats;

import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonRepresentation;

import java.util.Date;

public class UserEntity
{
    @BsonId
    public long steamId;
    public String steamIdString;
    public String userName;
    public String simplifiedUserName;
    @BsonRepresentation(BsonType.DATE_TIME)
    public Date lastOnline;
    public long kills;
    public long deaths;
    public long assists;

    public UserEntity()
    {
    }

    public UserEntity(final long steamId, final String steamIdString, final String userName, final String simplifiedUserName, final Date lastOnline, final long kills, final long deaths, final long assists)
    {
        this.steamId = steamId;
        this.steamIdString = steamIdString;
        this.userName = userName;
        this.simplifiedUserName = simplifiedUserName;
        this.lastOnline = lastOnline;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
    }
}
