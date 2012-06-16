package rest.converters;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jaroslav Málek
 * Date: 10.3.12
 * Time: 13:51
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "snapshots")
public class SnapshotsConverter {
    private List<SnapshotConverter> snapshots;

    public SnapshotsConverter() {
    }

    public SnapshotsConverter(List<SnapshotConverter> snapshots) {
        this.snapshots = snapshots;
    }

    @XmlElement(name = "snapshot")
    public List<SnapshotConverter> getSnapshot() {
        return snapshots;
    }

    public void setSnapshots(List<SnapshotConverter> snapshots) {
        this.snapshots = snapshots;
    }
}
