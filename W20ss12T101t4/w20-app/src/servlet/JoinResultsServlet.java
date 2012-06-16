package servlet;

import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.files.*;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.RetryOptions;
import com.google.appengine.api.taskqueue.TaskOptions;
import model.Log;
import model.XMLPath;
import model.dao.DAO;
import rest.converters.ResultsConverter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;


public class JoinResultsServlet extends HttpServlet {
    
    DAO dao = DAO.getInstance();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer snapshotsCount = dao.getAllSnapshotsSortedByDate().size();
        List<ResultsConverter> resultsSnapshots = new ArrayList<ResultsConverter>();
        String fileNameBase = "results.xml." ;
        try {
            JAXBContext ctx = JAXBContext.newInstance(ResultsConverter.class);
            for (int i=0;i<snapshotsCount;i++) {
                XMLPath xmlPath = dao.getXmlPath(fileNameBase + i);
                if (xmlPath == null) throw new Exception("Xml for snapshot number "+ i + " is not created." );
                FileService fileService = FileServiceFactory.getFileService();
                AppEngineFile file = new AppEngineFile(xmlPath.getPath());

                FileReadChannel frc = fileService.openReadChannel(file, true);

                Unmarshaller um = ctx.createUnmarshaller();

                ResultsConverter b = (ResultsConverter) um.unmarshal(Channels.newInputStream(frc));
                resultsSnapshots.add(b);
            }
            Collections.sort(resultsSnapshots,new Comparator<ResultsConverter>() {
                @Override
                public int compare(ResultsConverter o1, ResultsConverter o2) {
                    return o1.getResult().get(0).getSnapshotCreated().compareTo(o2.getResult().get(0).getSnapshotCreated());
                }
            });

            ResultsConverter first = resultsSnapshots.remove(0);
            for (ResultsConverter resultConverter : resultsSnapshots) {
                first.getResult().add(resultConverter.getResult().get(0));
            }

            String fileName = "results.xml";
            FileService f = FileServiceFactory.getFileService();
            AppEngineFile file = f.createNewBlobFile("application/xml",fileName);
            FileWriteChannel writeChannel = f.openWriteChannel(file, true);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Marshaller m = ctx.createMarshaller();
            m.marshal(first, bos);

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
            dao.saveLog(new Log(e.getMessage()));
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Queue queue = QueueFactory.getDefaultQueue();
        queue.add(withUrl("/api/v1/joinresults").method(TaskOptions.Method.POST).header("Host",
                BackendServiceFactory.getBackendService().getBackendAddress("gexf"))
                .retryOptions(RetryOptions.Builder.withTaskRetryLimit(0)).countdownMillis(0));
    }
}
