package model;

import com.googlecode.objectify.Key;
import model.dao.DAO;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: milos
 * Date: 31.3.12
 * Time: 2:24
 * To change this template use File | Settings | File Templates.
 */
public class XMLPath implements Serializable {

    @Id
    private Long id;
    private String path;
    private String name;

    public XMLPath() {
    }

    public XMLPath(String path,String name) {
        this.path = path;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
