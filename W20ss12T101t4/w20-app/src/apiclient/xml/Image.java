package apiclient.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * Created by IntelliJ IDEA.
 * User: Jaroslav Málek
 * Date: 10.3.12
 * Time: 17:16
 * To change this template use File | Settings | File Templates.
 */
@XmlType
public class Image {
    private String size;
    private String value;

    @XmlAttribute
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @XmlValue
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
