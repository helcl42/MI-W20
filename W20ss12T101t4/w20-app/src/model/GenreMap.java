package model;

import com.googlecode.objectify.Key;
import model.dao.DAO;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: lubos
 * Date: 3/17/12
 * Time: 1:08 AM
 * To change this template use File | Settings | File Templates.
 */

@Entity
public class GenreMap implements Serializable {
    @Id
    private Long id;
    private Key<Artist> artist;
    private Key<Genre> genre;

    public GenreMap() {
    }

    public GenreMap(Key<Artist> artist, Key<Genre> genre) {
        this.artist = artist;
        this.genre = genre;
    }

    public Key<GenreMap> getKey() {
        return DAO.getInstance().getKey(GenreMap.class, id);
    }

    public Long getId() {
        return id;
    }

    public Key<Artist> getArtist() {
        return artist;
    }

    public void setArtist(Key<Artist> artist) {
        this.artist = artist;
    }

    public Key<Genre> getGenre() {
        return genre;
    }

    public void setGenre(Key<Genre> genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "GenreMap{" +
                "id=" + id +
                ", artist=" + artist +
                ", genre=" + genre +
                '}';
    }
}
