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
@XmlType(name = "artist")
public class ArtistXml {
    private String mbid;
    private String name;

    @XmlAttribute(name = "mbid")
    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    @XmlValue
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
