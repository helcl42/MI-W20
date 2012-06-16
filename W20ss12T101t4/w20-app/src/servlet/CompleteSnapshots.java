package servlet;

import model.SnapshotDB;
import model.facade.BusinessFacade;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jaroslav Málek
 * Date: 25.3.12
 * Time: 20:22
 */
public class CompleteSnapshots extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        BusinessFacade bf = new BusinessFacade();

        List<SnapshotDB> snapshots = bf.getAllSnapshots();

        for (SnapshotDB s : snapshots) {
            if (s.getNodes() == null) {
                int users = bf.getUsersInSnapshot(s.getKey());
                int artists = bf.getArtistCountInSnapshot(s.getKey());
                int edges = bf.getRelationsInSnapshot(s.getKey());
                s.setNodes(users + artists);
                s.setEdges(edges);
                bf.updateSnapshot(s);
            }
        }

        response.setStatus(200);
        response.setHeader("Content-type", "text/plain");

        response.getWriter().println("Ok");
    }
}
