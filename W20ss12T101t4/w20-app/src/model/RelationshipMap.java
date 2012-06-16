package model;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import model.dao.DAO;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: lubos
 * Date: 3/9/12
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */


@Entity
public class RelationshipMap {
    @Id
    private Long id;
    private Key<SnapshotDB> snapshot;
    private Key<Artist> artist;
    private Key<User> user;

    @Transient private Map<Key<User>,Boolean> closed = new HashMap<Key<User>, Boolean>();
    @Transient private Map<Key<User>,Boolean> possible = new HashMap<Key<User>, Boolean>();

    public RelationshipMap() {
    }

    public RelationshipMap(Key<SnapshotDB> snapshot, Key<Artist> artist, Key<User> user) {
        this.user = user;
        this.artist = artist;
        this.snapshot = snapshot;
    }

    public Key<RelationshipMap> getKey() {
        return DAO.getInstance().getKey(RelationshipMap.class, id);
    }

    public Key<Artist> getArtist() {
        return artist;
    }

    public Key<User> getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public Key<SnapshotDB> getSnapshot() {
        return snapshot;
    }

    public boolean isClosed(Key<User> key) {
        return closed.containsKey(key);
    }

    public boolean isPossible(Key<User> key) {
        return !possible.containsKey(key) && !key.equals(getUser());
    }

    public void putToClosed(Key<User> key) {
        putToPossible(key);
        closed.put(key,null);
    }
    
    public void putToPossible(Key<User> user) {
        possible.put(user,null);
    }

    @Override
    public String toString() {
        return "RelationshipMap{" +
                "id=" + id +
                "snapshot=" + snapshot +
                "artist=" + artist +
                ", user=" + user +
                '}';
    }
}