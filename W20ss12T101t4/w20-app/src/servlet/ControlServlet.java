package servlet;

import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.*;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.RetryOptions;
import com.google.appengine.api.taskqueue.TaskOptions;
import model.*;
import model.dao.DAO;
import rest.converters.ResultConverter;
import rest.converters.ResultsConverter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

/**
 * Created by IntelliJ IDEA.
 * User: milos
 * Date: 26.3.12
 * Time: 15:17
 * To change this template use File | Settings | File Templates.
 */
public class ControlServlet extends HttpServlet {

//    DAO dao = DAO.getInstance();

    //servlet, ktery vyhazuje not finished snapshoty

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String fileNameBase = "results.xml" ;
//        try {
//            JAXBContext ctx = JAXBContext.newInstance(ResultsConverter.class);
//            XMLPath xmlPath = dao.getXmlPath(fileNameBase);
//            FileService fileService = FileServiceFactory.getFileService();
//            AppEngineFile file = new AppEngineFile(xmlPath.getPath());
//
//            FileReadChannel frc = fileService.openReadChannel(file, false);
//
//            Unmarshaller um = ctx.createUnmarshaller();
//
//            ResultsConverter b = (ResultsConverter) um.unmarshal(Channels.newInputStream(frc));
//            frc.close();
//
//            for (int i = 0; i< b.getResult().size();i++) {
//                ResultConverter rc = b.getResult().get(i);
//                SnapshotDB snapshotDB = dao.getSnapshotByDate(rc.getSnapshotCreated());
//                if (snapshotDB!=null) {
//                    if (!snapshotDB.isFinished()) {
//                        b.getResult().remove(i);
//                    }
//                }
//            }
//
//            String fileName = "results.xml";
//            FileService f = FileServiceFactory.getFileService();
//            AppEngineFile file2 = f.createNewBlobFile("application/xml",fileName);
//            FileWriteChannel writeChannel = f.openWriteChannel(file2, true);
//
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            Marshaller m = ctx.createMarshaller();
//            m.marshal(b, bos);
//
//            writeChannel.write(ByteBuffer.wrap(bos.toByteArray()));
//            writeChannel.closeFinally();
//
//            String fullPath = file2.getFullPath();
//
//            XMLPath path = DAO.getInstance().getXmlPath(fileName);
//
//            if (path!=null) {
//                path.setPath(fullPath);
//                DAO.getInstance().updateEntity(path);
//            } else {
//                path = new XMLPath(fullPath,fileName);
//                DAO.getInstance().saveEntity(path);
//            }
//        } catch (Exception e) {
//            dao.saveLog(new Log(e.getMessage()));
//        }

//        List<SnapshotDB> snapshotDBs = dao.getAllSnapshotsSortedByDate();
//        while (snapshotDBs.size()>9) snapshotDBs.remove(0);
//        List<User> users = dao.getAllByClass(User.class);
//        List<User> us = new ArrayList<User>();
//        boolean nextSnap=false;
//        for(User u : users) {
//            for(SnapshotDB s: snapshotDBs) {
//                List<RelationshipMap> usersInSnapshot = dao.getRelationsInSnapshot2(s.getKey());
//                nextSnap=false;
//                for(RelationshipMap usm : usersInSnapshot) {
//                    if (usm.getUser().equals(u.getKey())) {
//                        nextSnap=true;
//                        break;
//                    }
//                }
//                if(!nextSnap) break;
//            }
//            if(nextSnap) dao.saveLog(new Log(u.getUsername()));
//        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        Queue queue = QueueFactory.getDefaultQueue();
//        queue.add(withUrl("/control").method(TaskOptions.Method.POST).header("Host",
//                BackendServiceFactory.getBackendService().getBackendAddress("gexf"))
//                .retryOptions(RetryOptions.Builder.withTaskRetryLimit(0)).countdownMillis(0));
        response.setStatus(404);
    }

}
