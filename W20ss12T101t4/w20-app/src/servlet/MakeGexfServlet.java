package servlet;

import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.RetryOptions;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.googlecode.objectify.Key;
import com.ojn.gexf4j.core.Edge;
import com.ojn.gexf4j.core.Gexf;
import com.ojn.gexf4j.core.Node;
import com.ojn.gexf4j.core.impl.GexfImpl;
import com.ojn.gexf4j.core.impl.StaxGraphWriter;
import model.*;
import model.dao.DAO;
import model.facade.BusinessFacade;
import model.facade.IBusinessFacade;
import service.GexfService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

/**
 * Created by IntelliJ IDEA.
 * User: milos
 * Date: 31.3.12
 * Time: 2:28
 * To change this template use File | Settings | File Templates.
 */
public class MakeGexfServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GexfService.makeGexf();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Queue queue = QueueFactory.getDefaultQueue();
        queue.add(withUrl("/api/v1/makegexf").method(TaskOptions.Method.POST).header("Host",
                BackendServiceFactory.getBackendService().getBackendAddress("gexf"))
                .retryOptions(RetryOptions.Builder.withTaskRetryLimit(0)).countdownMillis(0));

    }
}
