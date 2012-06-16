package servlet;

import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.RetryOptions;
import com.google.appengine.api.taskqueue.TaskOptions;
import model.XMLPath;
import model.dao.DAO;
import service.GexfService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

/**
 * Created by IntelliJ IDEA.
 * User: milos
 * Date: 3.5.12
 * Time: 17:42
 * To change this template use File | Settings | File Templates.
 */
public class ResultsServlet extends HttpServlet  {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/xml");
        XMLPath path = DAO.getInstance().getXmlPath("results.xml");
        FileService fileService = FileServiceFactory.getFileService();
        AppEngineFile file = new AppEngineFile(path.getPath());
        BlobKey key = fileService.getBlobKey(file);
        BlobstoreService service = BlobstoreServiceFactory.getBlobstoreService();
        service.serve(key, response);
    }
}
