package model;

import com.googlecode.objectify.Key;
import model.dao.DAO;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: lubos
 * Date: 3/12/12
 * Time: 11:37 AM
 * To change this template use File | Settings | File Templates.
 */

@Entity
public class ArtistSnapshotMap implements Serializable {
    @Id
    private Long id;
    private Key<Artist> artist;
    private Key<SnapshotDB> snapshot;

    public ArtistSnapshotMap() {
    }

    public ArtistSnapshotMap(Key<Artist> artist, Key<SnapshotDB> snapshot) {
        this.artist = artist;
        this.snapshot = snapshot;
    }

    public Key<ArtistSnapshotMap> getKey() {
        return DAO.getInstance().getKey(ArtistSnapshotMap.class, id);
    }

    public Key<Artist> getArtist() {
        return artist;
    }

    public Key<SnapshotDB> getSnapshot() {
        return snapshot;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ArtistSnapshotMap{" +
                "artist=" + artist +
                ", snapshot=" + snapshot +
                ", id=" + id +
                '}';
    }
}
