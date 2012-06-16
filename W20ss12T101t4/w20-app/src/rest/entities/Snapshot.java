package rest.entities;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Jaroslav Málek
 * Date: 10.3.12
 * Time: 13:49
 * To change this template use File | Settings | File Templates.
 */

public class Snapshot {
    private Date created;
    private Integer nodes;
    private Integer edges;

    public Snapshot() {
    }

    public Snapshot(Date created, Integer nodes, Integer edges) {
        this.created = created;
        this.nodes = nodes;
        this.edges = edges;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getNodes() {
        return nodes;
    }

    public void setNodes(Integer nodes) {
        this.nodes = nodes;
    }

    public Integer getEdges() {
        return edges;
    }

    public void setEdges(Integer edges) {
        this.edges = edges;
    }
}
