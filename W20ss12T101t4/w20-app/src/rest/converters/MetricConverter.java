package rest.converters;

import model.SnapshotDB;
import service.metrics.Graph;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: milos
 * Date: 3.5.12
 * Time: 1:21
 * To change this template use File | Settings | File Templates.
 */
@XmlType(name = "metric")
public class MetricConverter {
    private String name;
    private float value;

    public MetricConverter() {
    }

    public MetricConverter(String name,float value) {
        this.name = name;
        this.value = value;
    }

    @XmlAttribute
    public float getValue() {
        return value;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
