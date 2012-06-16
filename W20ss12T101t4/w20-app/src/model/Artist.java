package model;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import model.dao.DAO;

import javax.persistence.Id;
import java.io.Serializable;


/**
 * Created by IntelliJ IDEA.
 * User: lubos
 * Date: 3/9/12
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
public class Artist implements Serializable {
    @Id
    private Long id;
    private String name;
    private String urn;
    private String image;
    private Long plays;
    private Long listeners;

    public Artist() {
    }

    public Artist(String image, Long listeners, String name, Long plays, String urn) {
        this.image = image;
        this.listeners = listeners;
        this.name = name;
        this.plays = plays;
        this.urn = urn;
    }

    public Artist(String name) {
        this.name = name;
    }

    public Key<Artist> getKey() {
        return DAO.getInstance().getKey(Artist.class, id);
    }

    public Long getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public Long getListeners() {
        return listeners;
    }

    public String getName() {
        return name;
    }

    public Long getPlays() {
        return plays;
    }

    public String getUrn() {
        return urn;
    }

    @Override
    public String toString() {
        return "Artist{" +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", url='" + urn + '\'' +
                ", image='" + image + '\'' +
                ", plays=" + plays +
                ", listeners=" + listeners +
                '}';
    }
}