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
@XmlType(name = "album")
public class AlbumXml {
    private String mbid;
    private String title;

    @XmlAttribute(name = "mbid")
    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    @XmlValue
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
