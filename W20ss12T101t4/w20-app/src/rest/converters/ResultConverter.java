package rest.converters;

import model.SnapshotDB;
import service.metrics.Graph;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: milos
 * Date: 3.5.12
 * Time: 1:21
 * To change this template use File | Settings | File Templates.
 */
@XmlType(name = "result")
public class ResultConverter {
    private Date snapshotCreated;
    private List<MetricConverter> metrics;

    public ResultConverter() {
    }

    public ResultConverter(Date snapshot, List<MetricConverter> lmc) {
        this.snapshotCreated = snapshot;
        this.metrics = lmc;
    }

    @XmlAttribute
    public Date getSnapshotCreated() {
        return snapshotCreated;
    }

    @XmlElement
    public List<MetricConverter> getMetric() {
        return metrics;
    }

    public void setMetric(List<MetricConverter> metrics) {
        this.metrics = metrics;
    }

    public void setSnapshotCreated(Date snapshotCreated) {
        this.snapshotCreated = snapshotCreated;
    }

}
