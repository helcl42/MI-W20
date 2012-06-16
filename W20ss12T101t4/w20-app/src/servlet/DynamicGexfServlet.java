package servlet;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import model.XMLPath;
import model.dao.DAO;
import service.GexfService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: lubos
 * Date: 5/5/12
 * Time: 11:25 AM
 * To change this template use File | Settings | File Templates.
 */
@WebServlet(name = "DynamicGexfServlet")
public class DynamicGexfServlet extends HttpServlet {

    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/gexf+xml");
        XMLPath path = DAO.getInstance().getXmlPath("gexfdynamic.xml3");
        FileService fileService = FileServiceFactory.getFileService();
        AppEngineFile file = new AppEngineFile(path.getPath());
        BlobKey key = fileService.getBlobKey(file);
        BlobstoreService service = BlobstoreServiceFactory.getBlobstoreService();
        service.serve(key,response);
    }
}
