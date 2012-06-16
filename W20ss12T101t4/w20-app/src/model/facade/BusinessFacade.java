package model.facade;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceException;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.googlecode.objectify.Key;
import model.*;
import model.dao.DAO;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: lubos
 * Date: 3/9/12
 * Time: 6:08 PM
 * To change this template use File | Settings |     File Templates.
 */


public class BusinessFacade implements IBusinessFacade {

    @Override
    public void saveUser(User user) {
        DAO.getInstance().saveEntity(user);
    }

    @Override
    public void saveArtist(Artist artist) {
        DAO.getInstance().saveEntity(artist);
    }

    @Override
    public void saveSnapshot(SnapshotDB snapshot) {
        DAO.getInstance().saveEntity(snapshot);
    }

    @Override
    public void saveSnapshot(Key<SnapshotDB> snapshot, List<Key<Artist>> artists, List<Key<User>> users) {
        Iterator<Key<Artist>> it = artists.iterator();
        while (it.hasNext()) {
            ArtistSnapshotMap artMap = new ArtistSnapshotMap(it.next(), snapshot);
            DAO.getInstance().saveEntity(artMap);
        }

        Iterator<Key<User>> ij = users.iterator();
        while (ij.hasNext()) {
            UserSnapshotMap userMap = new UserSnapshotMap(ij.next(), snapshot);
            DAO.getInstance().saveEntity(userMap);
        }
    }

    @Override
    public void saveSnapshot(Key<SnapshotDB> snapshot, Key<Artist> artist, Key<User> user) {
        ArtistSnapshotMap artMap = new ArtistSnapshotMap(artist, snapshot);
        DAO.getInstance().saveEntity(artMap);

        UserSnapshotMap userMap = new UserSnapshotMap(user, snapshot);
        DAO.getInstance().saveEntity(userMap);
    }

    @Override
    public void saveGenre(Genre genre) {
        DAO.getInstance().saveEntity(genre);
    }

    @Override
    public void saveGenre(Key<Genre> genre, Key<Artist> artist) {
        GenreMap genreRel = new GenreMap(artist, genre);
        DAO.getInstance().saveEntity(genreRel);
    }

    @Override
    public List<ArtistSnapshotMap> getArtistsInSnapshot(Key<SnapshotDB> snapshot) {

        List<ArtistSnapshotMap> artistCount = DAO.getInstance().getArtistsInSnapshot(snapshot);

        return artistCount;
    }

    @Override
    public void saveGenre(List<Key<Genre>> genres, Key<Artist> artist) {
        Iterator<Key<Genre>> it = genres.iterator();

        while (it.hasNext()) {
            GenreMap genRel = new GenreMap(artist, it.next());
            DAO.getInstance().saveEntity(genRel);
        }
    }

    @Override
    public SnapshotDB getLastSnapshot() {
        SnapshotDB result = null;
        String key = "ls";

        try {
            MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
            if (memcache.contains(key)) {
                result = (SnapshotDB) memcache.get(key);
                return result;
            }
        } catch (MemcacheServiceException e) {
            e.printStackTrace();
        }

        result = DAO.getInstance().getLastSnapshot();

        try {
            MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
            memcache.put(key, result);
        } catch (MemcacheServiceException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Integer getCountOfUsers() {
        Integer result = null;
        String key = "uc";

        try {
            MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
            if (memcache.contains(key)) {
                result = (Integer) memcache.get(key);
                return result;
            }
        } catch (MemcacheServiceException e) {
            e.printStackTrace();
        }

        result = DAO.getInstance().getCountOfAllByClass(User.class);

        try {
            MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
            memcache.put(key, result);
        } catch (MemcacheServiceException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Integer getCountOfArtists() {
        Integer result = null;
        String key = "ac";

        try {
            MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
            if (memcache.contains(key)) {
                result = (Integer) memcache.get(key);
                return result;
            }
        } catch (MemcacheServiceException e) {
            e.printStackTrace();
        }

        result = DAO.getInstance().getCountOfAllByClass(Artist.class);

        try {
            MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
            memcache.put(key, result);
        } catch (MemcacheServiceException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<SnapshotDB> getAllSnapshots() {
        List<SnapshotDB> result = null;
        String key = "as";

        try {
            MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
            if (memcache.contains(key)) {
                result = (List<SnapshotDB>) memcache.get(key);
                return result;
            }
        } catch (MemcacheServiceException e) {
            e.printStackTrace();
        }

        result = DAO.getInstance().getAllByClass(SnapshotDB.class);

        try {
            MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
            memcache.put(key, result);
        } catch (MemcacheServiceException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Map<Key<SnapshotDB>, SnapshotDB> getSnapshotMap() {
        List<SnapshotDB> snapshots = getAllSnapshots();
        Map<Key<SnapshotDB>, SnapshotDB> map = new HashMap<Key<SnapshotDB>, SnapshotDB>();
        for (SnapshotDB s : snapshots) {
            map.put(s.getKey(), s);
        }
        return map;
    }

    public List<SnapshotDB> getAllSnapshotsSortedByDate() {
        List<SnapshotDB> result = null;
        String key = "sns";

        try {
            MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
            if (memcache.contains(key)) {
                result = (List<SnapshotDB>) memcache.get(key);
                return result;
            }
        } catch (MemcacheServiceException e) {
            e.printStackTrace();
        }

        result = DAO.getInstance().getAllSnapshotsSortedByDate();

        try {
            MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
            memcache.put(key, result);
        } catch (MemcacheServiceException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Integer getArtistCountInSnapshot(Key<SnapshotDB> snapshot) {
        String key = "ais" + String.valueOf(snapshot.getId());
        Integer result = null;

        try {
            MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
            if (memcache.contains(key)) {
                result = (Integer) memcache.get(key);
                return result;
            }
        } catch (MemcacheServiceException e) {
            e.printStackTrace();
        }

        result = DAO.getInstance().getArtistsInSnapshot(snapshot).size();

        try {
            MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
            memcache.put(key, result);
        } catch (MemcacheServiceException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Integer getUsersInSnapshot(Key<SnapshotDB> snapshot) {
        String key = "uis" + String.valueOf(snapshot.getId());
        Integer result = null;

        try {
            MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
            if (memcache.contains(key)) {
                result = (Integer) memcache.get(key);
                return result;
            }
        } catch (MemcacheServiceException e) {
            e.printStackTrace();
        }

        result = DAO.getInstance().getUsersInSnapshot(snapshot);

        try {
            MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
            memcache.put(key, result);
        } catch (MemcacheServiceException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Integer getRelationsInSnapshot(Key<SnapshotDB> snapshot) {
        String key = "ris" + String.valueOf(snapshot.getId());
        Integer result = null;

        try {
            MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
            if (memcache.contains(key)) {
                result = (Integer) memcache.get(key);
                return result;
            }
        } catch (MemcacheServiceException e) {
            e.printStackTrace();
        }

        result = DAO.getInstance().getRelationsInSnapshot(snapshot);

        try {
            MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
            memcache.put(key, result);
        } catch (MemcacheServiceException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void saveRelationship(Key<SnapshotDB> snapshot, Key<Artist> artist, Key<User> user) {
        RelationshipMap map = new RelationshipMap(snapshot, artist, user);
        DAO.getInstance().saveEntity(map);
    }

    @Override
    public Artist getArtistByName(String name) {
        return DAO.getInstance().getArtistByName(name);
    }

    @Override
    public User getUserByName(String username) {
        return DAO.getInstance().getUserByUsername(username);
    }

    @Override
    public User getUserById(String id) {
        return DAO.getInstance().getEntity(User.class, Long.parseLong(id));
    }

    @Override
    public List<Artist> getAllArtists() {
        return DAO.getInstance().getAllByClass(Artist.class);
    }

    @Override
    public List<User> getAllUsers() {
        return DAO.getInstance().getAllByClass(User.class);
    }

    @Override
    public List<Artist> getAllUsersArtistByUsername(String username) {
        return DAO.getInstance().getUsersArtistByUsername(username);
    }

    @Override
    public List<User> getArtistsListenersByName(String name) {
        return DAO.getInstance().getArtistListeners(name);
    }

    @Override
    public List<User> getArtistListeners(Key<Artist> artist, SnapshotDB snapshot) {
        return DAO.getInstance().getArtistsListeners(artist,snapshot);
    }

    @Override
    public List<Genre> getAllGenres() {
        return DAO.getInstance().getAllByClass(Genre.class);
    }

    @Override
    public Genre getGenreByName(String name) {
        return DAO.getInstance().getGenreByName(name);
    }

    @Override
    public Boolean genreExists(String name) {
        return DAO.getInstance().genreExists(name);
    }

    @Override
    public void updateUser(String id, String image, String name, Long plays) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateArtist(String id, String image, Long listeners, Long plays) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updateSnapshot(SnapshotDB snapshot) {
        DAO.getInstance().updateEntity(snapshot);
    }
}