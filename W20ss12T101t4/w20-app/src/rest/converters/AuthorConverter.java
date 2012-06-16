package rest.converters;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import rest.entities.Author;

/**
 *
 * NICE COMMENT
 */

@XmlType(name="author")
public class AuthorConverter {
    private Author author;

    public AuthorConverter() {
    }

    public AuthorConverter(Author author) {
        this.author = author;
    }

    @XmlAttribute
    public String getName() {
        return author.getName();
    }

    @XmlAttribute
    public String getCity() {
        return author.getCity();
    }

    public void setName(String name) {
        this.author.setName(name);
    }

    public void setCity(String city) {
        this.author.setCity(city);
    }


}
