package apiclient.xml;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: Jaroslav Málek
 * Date: 10.3.12
 * Time: 17:04
 * To change this template use File | Settings | File Templates.
 */

@XmlRootElement(name = "lfm")
public class LastFmResponse {
    private String status;
    private Object innerXML;

    @XmlAttribute
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlAnyElement()
    public Object getInnerXML() {
        return innerXML;
    }

    public void setInnerXML(Object innerXML) {
        this.innerXML = innerXML;
    }
}
