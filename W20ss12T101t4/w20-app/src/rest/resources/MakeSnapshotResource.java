package rest.resources;

import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.RetryOptions;
import com.google.appengine.api.taskqueue.TaskOptions;
import service.GexfService;
import service.SnapshotService;
import utils.Mailer;
import utils.MessageTypes;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import java.io.IOException;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;


/**
 * Created by IntelliJ IDEA.
 * User: Jaroslav Málek
 * Date: 12.3.12
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */
@Path("/v1/makesnapshot")
public class MakeSnapshotResource {

    @POST
    public Response getMake() throws IOException
    {
        Response resp = null;
        try {
            String errs = SnapshotService.makeSnapshot();
            Mailer.sendMail(MessageTypes.SNAPSHOT_OK, errs);
            resp =  Response.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            Mailer.sendMail(MessageTypes.SNAPSHOT_ERROR, e);
            resp = Response.serverError().build();
        }
        finally {
            MemcacheServiceFactory.getMemcacheService().clearAll();
            return  resp;
        }
    }

    @GET
    public Response postTask() {
        Queue queue = QueueFactory.getDefaultQueue();
        queue.add(withUrl("/api/v1/makesnapshot").method(TaskOptions.Method.POST).header("Host",
                BackendServiceFactory.getBackendService().getBackendAddress("snapshot"))
                .retryOptions(RetryOptions.Builder.withTaskRetryLimit(0)).countdownMillis(0));
        return Response.ok().build();
    }
}
