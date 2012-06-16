package servlet;

import model.*;
import model.dao.DAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: lubos
 * Date: 3/15/12
 * Time: 6:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeleteDatastore extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DAO dao = DAO.getInstance();
        if (request.getAttribute("action") == null) {
            dao.deleteAll(UserSnapshotMap.class);
            request.setAttribute("action", "artistMap");
            request.getRequestDispatcher("/deletedatastore").forward(request, response);
        } else if (request.getAttribute("action").equals("artistMap")) {
            dao.deleteAll(ArtistSnapshotMap.class);
            request.setAttribute("action", "userMap");
            request.getRequestDispatcher("/deletedatastore").forward(request, response);
        } else if (request.getAttribute("action").equals("userMap")) {
            dao.deleteAll(RelationshipMap.class);
            request.setAttribute("action", "snapshot");
            request.getRequestDispatcher("/deletedatastore").forward(request, response);
        } else if (request.getAttribute("action").equals("snapshot")) {
            dao.deleteAll(SnapshotDB.class);
            request.setAttribute("action", "user");
            request.getRequestDispatcher("/deletedatastore").forward(request, response);
        } else if (request.getAttribute("action").equals("user")) {
            dao.deleteAll(User.class);
            request.setAttribute("action", "artist");
            request.getRequestDispatcher("/deletedatastore").forward(request, response);
        } else if (request.getAttribute("action").equals("artist")) {
            dao.deleteAll(Artist.class);
        }

        response.sendRedirect("/index.jsp");
    }
}
