package rest.resources;

import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.RetryOptions;
import com.google.appengine.api.taskqueue.TaskOptions;
import model.Log;
import model.SnapshotDB;
import model.XMLPath;
import model.dao.DAO;
import rest.converters.MetricConverter;
import rest.converters.ResultConverter;
import rest.converters.ResultsConverter;
import service.SnapshotService;
import service.metrics.Graph;
import utils.Mailer;
import utils.MessageTypes;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

/**
 * Created by IntelliJ IDEA.
 * User: milos
 * Date: 3.5.12
 * Time: 1:16
 * To change this template use File | Settings | File Templates.
 */

@Path("/v1/makeresults")
public class ResultsResource {

    DAO dao = DAO.getInstance();
    @POST
    @Path("/{id}")
    public Response getMake(@PathParam("id") String id) throws JAXBException, IOException {
        try {
            Integer idvalue = Integer.valueOf(id);
            List<SnapshotDB> snapshots = dao.getAllSnapshotsSortedByDate();
            SnapshotDB snapshot = snapshots.get(idvalue);
            List<ResultConverter> lrc = new ArrayList<ResultConverter>();
            ResultsConverter results = new ResultsConverter();
            Graph g = new Graph();
            List<MetricConverter> lmc = new ArrayList<MetricConverter>();
            g.construct(snapshot);
            g.calculateEdgeMetrics();
            g.calculateNodeMetrics();
            lmc.add(new MetricConverter("erdos", g.getErdos()));
            lmc.add(new MetricConverter("clustering",g.getClusteringCoeff()));
            lmc.add(new MetricConverter("overlap",g.getOverlap()));
            lmc.add(new MetricConverter("embeddedness",g.getEmbeddedness()));
            lmc.add(new MetricConverter("density",g.getDensity()));
            lrc.add(new ResultConverter(snapshot.getDate(),lmc));
            results.setResult(lrc);

            String fileName = "results.xml."+idvalue.toString();
            FileService f = FileServiceFactory.getFileService();
            AppEngineFile file = f.createNewBlobFile("application/xml",fileName);
            FileWriteChannel writeChannel = f.openWriteChannel(file, true);

            JAXBContext ctx = JAXBContext.newInstance(ResultsConverter.class);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Marshaller m = ctx.createMarshaller();
            m.marshal(results, bos);

            writeChannel.write(ByteBuffer.wrap(bos.toByteArray()));
            writeChannel.closeFinally();

            String fullPath = file.getFullPath();

            XMLPath path = DAO.getInstance().getXmlPath(fileName);

            if (path!=null) {
                path.setPath(fullPath);
                DAO.getInstance().updateEntity(path);
            } else {
                path = new XMLPath(fullPath,fileName);
                DAO.getInstance().saveEntity(path);
            }

        } catch (Exception e) {
            DAO.getInstance().saveLog(new Log(e.getMessage()));
        }
        return Response.ok().build();
    }

    public byte[] toByteArray (Object obj)
    {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            bos.close();
            bytes = bos.toByteArray ();
        }
        catch (IOException ex) {

        }
        return bytes;
    }

    @GET
    public Response postTask() {
        Queue queue = QueueFactory.getDefaultQueue();
        Integer snapshotsCount = dao.getAllSnapshotsSortedByDate().size();
        for(int i=0; i<snapshotsCount;i++)
            queue.add(withUrl("/api/v1/makeresults/"+i).method(TaskOptions.Method.POST).header("Host",
                BackendServiceFactory.getBackendService().getBackendAddress("gexf"))
                .retryOptions(RetryOptions.Builder.withTaskRetryLimit(0)).countdownMillis(0));
        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    public Response postTask(@PathParam("id") String id) {
        Queue queue = QueueFactory.getDefaultQueue();
        Integer idValue = Integer.valueOf(id);
        queue.add(withUrl("/api/v1/makeresults/"+idValue).method(TaskOptions.Method.POST).header("Host",
                    BackendServiceFactory.getBackendService().getBackendAddress("gexf"))
                    .retryOptions(RetryOptions.Builder.withTaskRetryLimit(0)).countdownMillis(0));
        return Response.ok().build();
    }
}