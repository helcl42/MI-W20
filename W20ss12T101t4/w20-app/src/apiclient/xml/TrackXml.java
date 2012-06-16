package apiclient.xml;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jaroslav Málek
 * Date: 12.3.12
 * Time: 21:00
 */
@XmlType(name = "track")
public class TrackXml {

    private String nowPlaying;
    private ArtistXml artist;

    private String name;

    private String mbid;

    private AlbumXml album;
    private String url;
    private DateXml date;
    private String streamable;

    private List<Image> images;

    @XmlAttribute(name = "nowplaying")
    public String getNowPlaying() {
        return nowPlaying;
    }

    public void setNowPlaying(String nowPlaying) {
        this.nowPlaying = nowPlaying;
    }

    @XmlElement(name = "artist")
    public ArtistXml getArtist() {
        return artist;
    }

    public void setArtist(ArtistXml artist) {
        this.artist = artist;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "mbid")
    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    @XmlElement(name = "album")
    public AlbumXml getAlbum() {
        return album;
    }

    public void setAlbum(AlbumXml album) {
        this.album = album;
    }

    @XmlElement(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @XmlElement(name = "date")
    public DateXml getDate() {
        return date;
    }

    public void setDate(DateXml date) {
        this.date = date;
    }

    @XmlElement(name = "streamable")
    public String getStreamable() {
        return streamable;
    }

    public void setStreamable(String streamable) {
        this.streamable = streamable;
    }

    @XmlElement(name = "image")
    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}