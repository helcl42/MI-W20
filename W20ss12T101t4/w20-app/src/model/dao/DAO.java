package model.dao;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.googlecode.objectify.util.DAOBase;
import model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lubos
 * Date: 3/9/12
 * Time: 5:34 PM
 * To change this template use File | Settings | File Templates.
 */


public class DAO extends DAOBase {
    static {
        ObjectifyService.register(User.class);
        ObjectifyService.register(Artist.class);
        ObjectifyService.register(ArtistSnapshotMap.class);
        ObjectifyService.register(UserSnapshotMap.class);
        ObjectifyService.register(RelationshipMap.class);
        ObjectifyService.register(SnapshotDB.class);
        ObjectifyService.register(Genre.class);
        ObjectifyService.register(GenreMap.class);
        ObjectifyService.register(XMLPath.class);
        ObjectifyService.register(Log.class);
    }

    private Objectify objectify;

    private DAO() {
    }

    public static DAO getInstance() {
        DAO dao = new DAO();
        dao.objectify = ObjectifyService.begin();
        return dao;
    }

    public void saveLog(Log log) {
        saveEntity(log);
    }

    public List<Log> getAllLogs() {
        Query<Log> result = objectify.query(Log.class);
        return result.list();
    }

    public User getUserByUsername(String username) {
        Query<User> result = objectify.query(User.class).filter("username", username);
        List<User> list = result.list();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public Artist getArtistByName(String name) {
        Query<Artist> result = objectify.query(Artist.class).filter("name", name);
        List<Artist> list = result.list();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public Genre getGenreByName(String name) {
        Query<Genre> result = objectify.query(Genre.class).filter("name", name);
        List<Genre> list = result.list();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public Boolean genreExists(String name) {
        Query<Genre> res = objectify.query(Genre.class).filter("name", name);
        List<Genre> genres = res.list();
        if (genres.isEmpty() || genres == null)
            return new Boolean(false);
        else
            return new Boolean(true);
    }

    public SnapshotDB getLastSnapshot() {
        Query<SnapshotDB> res = objectify.query(SnapshotDB.class).order("date");
        if (res.list().size() == 0) return null;
        SnapshotDB lastSnapshot = res.list().get(res.list().size() - 1);
        return lastSnapshot;
    }

    public <T> Integer getCountOfAllByClass(Class<T> type) {
        Query<T> result = objectify.query(type);
        return Integer.valueOf(result.list().size());
    }

    public List<ArtistSnapshotMap> getArtistsInSnapshot(Key<SnapshotDB> snapshot) {
        return objectify.query(ArtistSnapshotMap.class).filter("snapshot", snapshot).list();
    }

    public Integer getUsersInSnapshot(Key<SnapshotDB> snapshot) {
        return objectify.query(UserSnapshotMap.class).filter("snapshot", snapshot).count();
    }

    public List<UserSnapshotMap> getUsersInSnapshot2(Key<SnapshotDB> snapshot) {
        return objectify.query(UserSnapshotMap.class).filter("snapshot", snapshot).list();
    }

    public List<RelationshipMap> getRelationsInSnapshot2(Key<SnapshotDB> snapshot) {
        return objectify.query(RelationshipMap.class).filter("snapshot", snapshot).list();
    }

    public Integer getRelationsInSnapshot(Key<SnapshotDB> snapshot) {
        return objectify.query(RelationshipMap.class).filter("snapshot", snapshot).count();
    }

    public List<Artist> getUsersArtistByUsername(String username) {
        User user = getUserByUsername(username);
        Query<RelationshipMap> artistIds = objectify.query(RelationshipMap.class).filter("user", user);
        if (artistIds.list().isEmpty() || artistIds == null)
            return null;
        Query<Artist> artists = objectify.query(Artist.class).filter("artist", artistIds);
        if (artists.list().isEmpty() || artists == null)
            return null;

        return artists.list();
    }

    public List<RelationshipMap> getUsersByArtistSnapshot(Artist artist, SnapshotDB snapshot) {
        Query<RelationshipMap> artistIds = objectify.query(RelationshipMap.class).filter("artist", artist).filter("snapshot",snapshot);
        return artistIds.list();
    }

    public List<User> getArtistListeners(String name) {
        Artist artist = getArtistByName(name);
        Query<RelationshipMap> userIds = objectify.query(RelationshipMap.class).filter("artist", artist);
        if (userIds.list().isEmpty())
            return null;
        Query<User> users = objectify.query(User.class).filter("user", userIds);
        if (users.list().isEmpty() || users == null)
            return null;

        return users.list();
    }

    public User getUserByKey(Key<User> userKey) {
        Query<User> user = objectify.query(User.class).filter("id", userKey.getId());
        if (user.list().isEmpty())
            return null;
        return user.list().get(0);
    }


    public Artist getArtistByKey(Key<Artist> artistKey) {
        Query<Artist> artist = objectify.query(Artist.class).filter("id", artistKey.getId());
        if (artist.list().isEmpty())
            return null;
        return artist.list().get(0);
    }


    public SnapshotDB getSnapshotByKey(Key<SnapshotDB> snapshotKey) {
        Query<SnapshotDB> snapshot = objectify.query(SnapshotDB.class).filter("id", snapshotKey.getId());
        if (snapshot.list().isEmpty())
            return null;
        return snapshot.list().get(0);
    }


    public List<RelationshipMap> getArtistRelationsWithListeners(Key<Artist> artist, Key<SnapshotDB> snapshot) {
        Query<RelationshipMap> userIds = objectify.query(RelationshipMap.class).filter("artist", artist).filter("snapshot", snapshot);
        if (userIds.list().isEmpty())
            return null;

        List<RelationshipMap> distinctRelMap = new ArrayList<RelationshipMap>();
        for (int i = 0; i < userIds.list().size(); i++) {

            long id = userIds.list().get(i).getUser().getId();
            boolean exist = false;

            for (int j = 0; j < distinctRelMap.size(); j++) {
                if (id == distinctRelMap.get(j).getUser().getId()) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                distinctRelMap.add(userIds.list().get(i));
            }
        }
        return distinctRelMap;
    }


    public List<User> getArtistsListeners(Key<Artist> artist, SnapshotDB snapshot) {
        List<RelationshipMap> distinctRelMap = getArtistRelationsWithListeners(artist, snapshot.getKey());
        if (distinctRelMap.isEmpty())
            return null;

        List<User> users = new ArrayList<User>();
        for (int i = 0; i < distinctRelMap.size(); i++) {
            Query<User> tmpUsers = objectify.query(User.class).filter("id", distinctRelMap.get(i).getUser().getId());
            users.addAll(tmpUsers.list());
        }

        if (users.isEmpty())
            return null;

        return users;
    }

    public List<RelationshipMap> getUsersRelationsWithArtists(Key<User> user, Key<SnapshotDB> snapshot) {
        Query<RelationshipMap> userIds = objectify.query(RelationshipMap.class).filter("user", user).filter("snapshot", snapshot);
        if (userIds.list().isEmpty())
            return null;

        List<RelationshipMap> distinctRelMap = new ArrayList<RelationshipMap>();
        for (int i = 0; i < userIds.list().size(); i++) {
            long id = userIds.list().get(i).getUser().getId();
            boolean exist = false;

            for (int j = 0; j < distinctRelMap.size(); j++) {
                if (id == distinctRelMap.get(j).getUser().getId()) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                distinctRelMap.add(userIds.list().get(i));
            }
        }
        return distinctRelMap;
    }


    public List<Artist> getUsersArtists(Key<User> user, SnapshotDB snapshot) {
        List<RelationshipMap> distinctRelMap = getUsersRelationsWithArtists(user, snapshot.getKey());
        if (distinctRelMap.isEmpty())
            return null;

        List<Artist> artists = new ArrayList<Artist>();
        for (int i = 0; i < distinctRelMap.size(); i++) {
            Query<Artist> tmpArtists = objectify.query(Artist.class).filter("id", distinctRelMap.get(i).getArtist().getId());
            artists.addAll(tmpArtists.list());
        }

        if (artists.isEmpty())
            return null;

        return artists;
    }


    public XMLPath getXmlPath(String name) {
        Query<XMLPath> path = objectify.query(XMLPath.class).filter("name = ", name);
        return (path.count()>0) ? path.list().get(0): null;
    }


    public List<SnapshotDB> getAllSnapshotsSortedByDate() {
        Query<SnapshotDB> res = objectify.query(SnapshotDB.class).filter("finished",true).order("date");

        List<SnapshotDB> snapshots = res.list();
        if (snapshots.isEmpty() || snapshots == null)
            return null;

        return snapshots;
    }

    public SnapshotDB getSnapshotByDate(Date d) {
        Query<SnapshotDB> res = objectify.query(SnapshotDB.class).filter("date",d);

        List<SnapshotDB> snapshots = res.list();

        return (!snapshots.isEmpty()) ? snapshots.get(0) : null;
    }

    public <T> List<T> getAllByClass(Class<T> type) {
        Query<T> result = objectify.query(type);
        return result.list();
    }

    public <T> void saveEntity(T e) {
        objectify.put(e);
    }

    public <T> void saveEntities(T... e) {
        for (T t : e) {
            saveEntity(t);
        }
    }

    public <T> void deleteEntity(T e) {
        objectify.delete(e);
    }

    public <T> void updateEntity(T e) {
        objectify.put(e);
    }

    public <T> void deleteEntity(Class<T> type, Long id) {
        objectify.delete(type, id);
    }

    public <T> void updateEntity(Class<T> type, T e, Long id) {
        User tmpUser = objectify.query(User.class).filter("id", id).get();
        objectify.put(tmpUser);
    }

    public <T> T getEntity(Class<T> type, Long id) {
        return objectify.get(type, id);
    }

    public <T> T getEntity(Key<T> key) {
        return objectify.get(key);
    }

    public <T> Key<T> getKey(Class<T> type, Long id) {
        return new Key<T>(type, id);
    }

    public <T> void deleteAll(Class<T> type) {
        List<T> l = getAllByClass(type);
        for (T e : l) {
            deleteEntity(e);
        }
    }
}
