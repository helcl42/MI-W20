package model;


import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import model.dao.DAO;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: lubos
 * Date: 3/9/12
 * Time: 1:47 PM
 */


@Entity
public class User implements Serializable {

    @Id
    protected Long id;
    protected String username;
    protected String name;
    protected String url;
    protected String image;
    protected String country;
    protected Integer age;
    protected String gender;
    protected Date created;
    protected Long plays;

    public User() {
    }

    public User(Integer age, String country, Date created, String gender,
                String image, String name, Long plays, String url, String username) {
        this.age = age;
        this.country = country;
        this.created = created;
        this.gender = gender;
        this.image = image;
        this.name = name;
        this.plays = plays;
        this.url = url;
        this.username = username;
    }

    public Key<User> getKey() {
        return DAO.getInstance().getKey(User.class, id);
    }

    public Integer getAge() {
        return age;
    }

    public String getCountry() {
        return country;
    }

    public Date getCreated() {
        return created;
    }

    public String getGender() {
        return gender;
    }

    public Long getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public Long getPlays() {
        return plays;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "User{" +
                "age='" + age + '\'' +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", image='" + image + '\'' +
                ", country='" + country + '\'' +
                ", gender='" + gender + '\'' +
                ", created=" + created +
                ", plays=" + plays +
                '}';
    }
}
