package servlet;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.*;
import com.googlecode.objectify.Key;
import com.ojn.gexf4j.core.Edge;
import com.ojn.gexf4j.core.EdgeType;
import com.ojn.gexf4j.core.Gexf;
import com.ojn.gexf4j.core.Node;
import com.ojn.gexf4j.core.impl.GexfImpl;
import com.ojn.gexf4j.core.impl.StaxGraphWriter;
import model.*;
import model.dao.DAO;
import model.facade.BusinessFacade;
import model.facade.IBusinessFacade;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: milos
 * Date: 26.3.12
 * Time: 15:17
 * To change this template use File | Settings | File Templates.
 */
public class GexfServlet extends HttpServlet {

    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/xml");
        XMLPath path = DAO.getInstance().getXmlPath("gexf.xml");
        FileService fileService = FileServiceFactory.getFileService();
        AppEngineFile file = new AppEngineFile(path.getPath());

        BlobKey key = fileService.getBlobKey(file);
        BlobstoreService service = BlobstoreServiceFactory.getBlobstoreService();

        service.serve(key,response);
    }

}
