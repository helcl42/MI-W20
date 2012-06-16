/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rest.converters;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author milos
 */
@XmlType(name="authors")
public class AuthorsConverter {
    private List<AuthorConverter> authors;

    public AuthorsConverter() {
    }

    public AuthorsConverter(List<AuthorConverter> authors) {
        this.authors = authors;
    }

    @XmlElement(name="author")
    public List<AuthorConverter> getAuthor() {
        return authors;
    }

    public void setAuthor(List<AuthorConverter> authors) {
        this.authors = authors;
    }


}
