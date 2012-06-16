package apiclient.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jaroslav Málek
 * Date: 12.3.12
 * Time: 20:59
 */
@XmlRootElement(name = "recenttracks")
public class RecentTracks {

    private List<TrackXml> tracks;
    private String user;
    private Integer page;
    private Integer perPage;
    private Integer totalPages;

    @XmlElement(name = "track")
    public List<TrackXml> getTracks() {
        return tracks;
    }

    public void setTracks(List<TrackXml> tracks) {
        this.tracks = tracks;
    }

    @XmlAttribute(name = "user")
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @XmlAttribute(name = "page")
    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    @XmlAttribute(name = "perPage")
    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    @XmlAttribute(name = "totalPages")
    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}
