package rest.converters;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

/**
 * NICE COMMENT
 */

@XmlRootElement(name = "metadata")
@XmlType(propOrder = {"NAME", "DESCRIPTION", "lastUpdate", "UPDATEINTERVAL", "authors"})
public class MetaDataConverter {

    private final String NAME = "Last.fm analyzer";
    private final String DESCRIPTION = "Projekt se zabývá sběrem a porovnáním dat z webu Last.fm. Konkrétně půjde o " +
            "sledování určité skupiny uživatelů, kteří se hlásí skrze skupinu k určitému hudebnímu stylu a poslouchají" +
            " stejné kapely. Poslouchané kapely jsou základem pro zjišťování spojitostí mezi jednotlivými uživatel";
    private final int UPDATEINTERVAL = 480;
    private AuthorsConverter authors;
    private Date lastUpdate;

    public MetaDataConverter(AuthorsConverter authors, Date lastUpdate) {
        this.authors = authors;
        this.lastUpdate = lastUpdate;
    }

    public MetaDataConverter() {
    }

    @XmlElement(name = "description")
    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    @XmlElement(name = "lastUpdate")
    public Date getLastUpdate() {
        return lastUpdate;
    }

    @XmlElement(name = "name")
    public String getNAME() {
        return NAME;
    }

    @XmlElement(name = "updateInterval")
    public int getUPDATEINTERVAL() {
        return UPDATEINTERVAL;
    }

    @XmlElement
    public AuthorsConverter getAuthors() {
        return authors;
    }
}
