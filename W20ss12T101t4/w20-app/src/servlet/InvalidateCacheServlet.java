package servlet;

import com.google.appengine.api.memcache.MemcacheServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: lubos
 * Date: 3/26/12
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 */
@WebServlet(name = "InvalidateCacheServlet")
public class InvalidateCacheServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MemcacheServiceFactory.getMemcacheService().clearAll();
        response.sendRedirect("index.jsp");
    }
}
