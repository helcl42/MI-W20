package rest.converters;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: milos
 * Date: 3.5.12
 * Time: 1:21
 * To change this template use File | Settings | File Templates.
 */

@XmlRootElement(name = "results")
public class ResultsConverter {
    private List<ResultConverter> results;

    public ResultsConverter() {
    }

    public ResultsConverter(List<ResultConverter> results) {
        this.results = results;
    }

    @XmlElement(name = "result")
    public List<ResultConverter> getResult() {
        return results;
    }

    public void setResult(List<ResultConverter> results) {
        this.results = results;
    }
}
