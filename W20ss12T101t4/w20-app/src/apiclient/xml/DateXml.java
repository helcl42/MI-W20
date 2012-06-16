package apiclient.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * Created by IntelliJ IDEA.
 * User: Jaroslav Málek
 * Date: 12.3.12
 * Time: 21:08
 */
@XmlType(name = "date")
public class DateXml {

    private Long timestamp;

    private String formatted;

    @XmlAttribute(name = "uts")
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @XmlValue
    public String getFormatted() {
        return formatted;
    }

    public void setFormatted(String formatted) {
        this.formatted = formatted;
    }
}
