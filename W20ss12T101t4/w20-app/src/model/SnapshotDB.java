package model;

import com.googlecode.objectify.Key;
import model.dao.DAO;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: lubos
 * Date: 3/12/12
 * Time: 12:46 AM
 * To change this template use File | Settings | File Templates.
 */

public class SnapshotDB implements Serializable {
    @Id
    private Long id;
    private Date date;
    private boolean finished = false;
    private Integer edges;
    private Integer nodes;

    public SnapshotDB() {
    }

    public SnapshotDB(Date date) {
        this.date = date;
    }

    public Key<SnapshotDB> getKey() {
        return DAO.getInstance().getKey(SnapshotDB.class, id);
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Integer getEdges() {
        return edges;
    }

    public void setEdges(Integer edges) {
        this.edges = edges;
    }

    public Integer getNodes() {
        return nodes;
    }

    public void setNodes(Integer nodes) {
        this.nodes = nodes;
    }

    @Override
    public String toString() {
        return "SnapshotDB{" +
                "id=" + id +
                ", date=" + date +
                ", finished=" + finished +
                ", edges=" + edges +
                ", nodes=" + nodes +
                '}';
    }
}
