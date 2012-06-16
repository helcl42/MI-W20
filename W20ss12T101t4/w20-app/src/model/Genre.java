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
 * Time: 1:06 AM
 * To change this template use File | Settings | File Templates.
 */

@Entity
public class Genre implements Serializable {
    @Id
    private Long id;
    private String name;

    public Genre() {
    }

    public Genre(String name) {
        this.name = name;
    }

    public Key<Genre> getKey() {
        return DAO.getInstance().getKey(Genre.class, id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
