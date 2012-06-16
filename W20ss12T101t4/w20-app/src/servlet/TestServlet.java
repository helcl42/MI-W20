package servlet;

import apiclient.xml.UserXml;
import com.googlecode.objectify.Key;
import model.*;
import model.dao.DAO;
import model.facade.BusinessFacade;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lubos
 * Date: 4/28/12
 * Time: 4:02 PM
 * To change this template use File | Settings | File Templates.
 */

@WebServlet(name = "TestServlet")
public class TestServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        StringBuilder sb = new StringBuilder();
        SnapshotDB snapshot = DAO.getInstance().getLastSnapshot();

        List<RelationshipMap> relMapList = DAO.getInstance().getRelationsInSnapshot2(snapshot.getKey());
        //92
        sb.append("Users for Artist: ").append(DAO.getInstance().getArtistByKey(relMapList.get(136).getArtist()).getName()).append("\n");
        List<User> users = DAO.getInstance().getArtistsListeners(relMapList.get(136).getArtist(), snapshot);

        sb.append("Size: " + users.size() + "\n");
        for(User u : users) {
            sb.append(u.getUsername()).append("\n");
        }

        response.setStatus(200);
        response.setHeader("Content-type", "text/plain");

        int size = Integer.parseInt(request.getParameter("size"));
        size = size > 0 ? size : 1;
        response.getWriter().println(sb.toString());
        response.getWriter().println("VALUE: " + size);
        response.getWriter().println("OK");
    }
}
