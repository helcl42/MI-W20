package rest.converters;

import model.SnapshotDB;
import rest.entities.Snapshot;
import service.metrics.Graph;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Jaroslav Málek
 * Date: 10.3.12
 * Time: 13:51
 * To change this template use File | Settings | File Templates.
 */
@XmlType(name = "snapshot") //, propOrder = "created")
public class SnapshotConverter {

    private Snapshot snapshot;

    public SnapshotConverter() {
    }

    public SnapshotConverter(Snapshot snapshot) {
        this.snapshot = snapshot;
    }

    @XmlAttribute
    public Date getCreated() {
        return snapshot.getCreated();
    }

    @XmlAttribute
    public Integer getNode() {
        return snapshot.getNodes();
    }

    @XmlAttribute
    public Integer getEdge() {
        return snapshot.getEdges();
    }

}
