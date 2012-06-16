package model;

import com.googlecode.objectify.Key;
import model.dao.DAO;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: lubos
 * Date: 3/11/12
 * Time: 4:27 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
public class UserSnapshotMap implements Serializable {
    @Id
    private Long            id;
    private Key<User>       user;
    private Key<SnapshotDB> snapshot;

    public UserSnapshotMap() {
    }

    public UserSnapshotMap(Key<User> user, Key<SnapshotDB> snapshot) {
        this.user = user;
        this.snapshot = snapshot;
    }

    public Key<UserSnapshotMap> getKey() {
        return DAO.getInstance().getKey(UserSnapshotMap.class, id);
    }

    public Key<SnapshotDB> getSnapshot() {
        return snapshot;
    }

    public Key<User> getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "UserSnapshotMap{" +
                "id=" + id +
                ", user=" + user +
                ", snapshot=" + snapshot +
                '}';
    }
}
