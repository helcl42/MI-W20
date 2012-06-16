package servlet;

import model.Log;
import model.dao.DAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: milos
 * Date: 17.3.12
 * Time: 16:29
 * To change this template use File | Settings | File Templates.
 */

public class LogServlet extends HttpServlet {
    
    DAO dao = DAO.getInstance();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        List<Log> logs = dao.getAllLogs();
        Collections.sort(logs,new Comparator<Log>() {
            @Override
            public int compare(Log o1, Log o2) {
                return o2.getDate().compareTo(o1.getDate());  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        
        for (Log log : logs) {
            pw.println(log.toString() + "<br>");
        }
    }
}
