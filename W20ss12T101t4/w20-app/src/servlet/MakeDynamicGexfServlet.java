package servlet;

import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.RetryOptions;
import com.google.appengine.api.taskqueue.TaskOptions;
import service.GexfDynamicService;
import service.GexfService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

/**
 * Created with IntelliJ IDEA.
 * User: lubos
 * Date: 5/5/12
 * Time: 12:46 AM
 * To change this template use File | Settings | File Templates.
 */
@WebServlet(name = "MakeDynamicGexfServlet")
public class MakeDynamicGexfServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        GexfDynamicService.makeDynamicGexf(Integer.parseInt(request.getParameter("size")));
//        response.setStatus(200);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        Queue queue = QueueFactory.getDefaultQueue();
//        queue.add(withUrl("/api/v1/gexffinal").method(TaskOptions.Method.POST).header("Host",
//                BackendServiceFactory.getBackendService().getBackendAddress("gexf"))
//                .retryOptions(RetryOptions.Builder.withTaskRetryLimit(0)).countdownMillis(0));

//        int size = Integer.parseInt(request.getParameter("size"));
//        size = size > 0 ? size : 1;
//        GexfDynamicService.makeDynamicGexf(size);
//        response.getWriter().println("OK");

        Integer size = Integer.parseInt(request.getParameter("size"));
        Integer order = Integer.parseInt(request.getParameter("order"));
        if ((size != null) && (order != null))
            GexfService.makeDynamicGexf(size, order);
    }
}
