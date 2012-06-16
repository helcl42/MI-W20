package model;

import com.googlecode.objectify.Key;
import model.dao.DAO;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: milos
 * Date: 3.5.12
 * Time: 0:39
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Log implements Serializable {
    
    @Id
    private Long id;
    private String message;
    private Date date;
    public Log() {
    }
    
    public Log(String message) {
        this.date = new Date();
        this.message=message;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }



    @Override
    public String toString() {
        return "Log:" +
                " date:" + date +
                " - message: " + message;
    }
}
