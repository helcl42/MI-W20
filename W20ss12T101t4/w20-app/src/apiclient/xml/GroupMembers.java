package apiclient.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jaroslav Málek
 * Date: 10.3.12
 * Time: 17:03
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "members")
public class GroupMembers {

    private Integer page;
    private Integer perPage;
    private Integer totalPages;
    private Integer total;
    private String group;

    private List<UserXml> userList;

    @XmlAttribute
    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    @XmlAttribute
    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    @XmlAttribute
    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    @XmlAttribute
    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @XmlAttribute(name = "for")
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @XmlElement(name = "user")
    public List<UserXml> getUserList() {
        return userList;
    }

    public void setUserList(List<UserXml> userList) {
        this.userList = userList;
    }
}
