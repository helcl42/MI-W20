package apiclient.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jaroslav Málek
 * Date: 13.3.12
 * Time: 12:52
 */
@XmlRootElement(name = "user")
public class UserInfoXml {

    private String id;
    private String name;
    private String realName;
    private String url;
    private List<Image> images;
    private String country;
    private Integer age;
    private String gender;
    private String subscriber;
    private Integer playcount;
    private Integer playlists;
    private String bootstrap;
    private RegisteredXml registered;
    private String type;

    @XmlElement(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "realname")
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @XmlElement(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @XmlElement(name = "image")
    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    @XmlElement(name = "country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @XmlElement(name = "age")
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @XmlElement(name = "gender")
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @XmlElement(name = "subscriber")
    public String getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(String subscriber) {
        this.subscriber = subscriber;
    }

    @XmlElement(name = "playcount")
    public Integer getPlaycount() {
        return playcount;
    }

    public void setPlaycount(Integer playcount) {
        this.playcount = playcount;
    }

    @XmlElement(name = "playlists")
    public Integer getPlaylists() {
        return playlists;
    }

    public void setPlaylists(Integer playlists) {
        this.playlists = playlists;
    }

    @XmlElement(name = "bootstrap")
    public String getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(String bootstrap) {
        this.bootstrap = bootstrap;
    }

    @XmlElement(name = "registered")
    public RegisteredXml getRegistered() {
        return registered;
    }

    public void setRegistered(RegisteredXml registered) {
        this.registered = registered;
    }


    @XmlElement(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "UserInfoXml{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", realName='" + realName + '\'' +
                ", url='" + url + '\'' +
                ", country='" + country + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", subscriber='" + subscriber + '\'' +
                ", playcount=" + playcount +
                ", playlists=" + playlists +
                ", bootstrap='" + bootstrap + '\'' +
                ", registered=" + registered +
                ", type=" + type +
                '}';
    }
}

