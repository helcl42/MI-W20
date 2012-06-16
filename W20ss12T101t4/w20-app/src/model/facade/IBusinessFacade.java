package model.facade;

import com.googlecode.objectify.Key;
import model.*;
import rest.entities.Snapshot;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: lubos
 * Date: 3/9/12
 * Time: 6:08 PM
 * To change this template use File | Settings | File Templates.
 */

/*
 *  Interface, jehoz metody umoznuji pristup k logice pristupu do DB
 */
public interface IBusinessFacade {
    public void saveUser(User user);

    public void saveArtist(Artist artist);

    public void saveSnapshot(Key<SnapshotDB> snapshot, List<Key<Artist>> artist, List<Key<User>> users);

    public void saveSnapshot(Key<SnapshotDB> snapshot, Key<Artist> artist, Key<User> user);

    public void saveSnapshot(SnapshotDB snapshot);

    public void saveRelationship(Key<SnapshotDB> snapshot, Key<Artist> artist, Key<User> user);

    public void saveGenre(Genre genre);

    public void saveGenre(Key<Genre> genre, Key<Artist> artist);

    public void saveGenre(List<Key<Genre>> genres, Key<Artist> artist);

    public Boolean genreExists(String name);

    public List<Artist> getAllArtists();

    public List<User> getAllUsers();

    public List<SnapshotDB> getAllSnapshots();

    public List<Genre> getAllGenres();

    public Map<Key<SnapshotDB>,SnapshotDB> getSnapshotMap();

    public List<SnapshotDB> getAllSnapshotsSortedByDate();

    public List<Artist> getAllUsersArtistByUsername(String username);

    public List<User> getArtistsListenersByName(String name);

    public List<User> getArtistListeners(Key<Artist> artist ,SnapshotDB snapshot);

    public Integer getArtistCountInSnapshot(Key<SnapshotDB> snapshot);
    
    public List<ArtistSnapshotMap> getArtistsInSnapshot(Key<SnapshotDB> snapshot);

    public Integer getUsersInSnapshot(Key<SnapshotDB> snapshot);

    public Integer getRelationsInSnapshot(Key<SnapshotDB> snapshot);

    public User getUserById(String id);

    public User getUserByName(String username);

    public Artist getArtistByName(String name);

    public Genre getGenreByName(String name);

    public Integer getCountOfUsers();

    public Integer getCountOfArtists();

    public SnapshotDB getLastSnapshot();

    public void updateUser(String id, String image, String name, Long plays);

    public void updateArtist(String id, String image, Long listeners, Long plays);

    public void updateSnapshot(SnapshotDB snapshot);
}
