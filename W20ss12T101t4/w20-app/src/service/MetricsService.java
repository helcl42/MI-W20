package service;


import com.ojn.gexf4j.core.Gexf;
import com.ojn.gexf4j.core.Node;
import com.ojn.gexf4j.core.impl.GexfImpl;
import model.SnapshotDB;
import model.dao.DAO;
import service.metrics.Graph;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jaroslav Málek
 * Date: 23.4.12
 * Time: 21:35
 */
public class MetricsService {

    public static void calculate() {

        // priklad vypocitani metrik pro snapshot
        SnapshotDB snapshot = DAO.getInstance().getLastSnapshot();

        Graph graph = new Graph();
        // vytvorime graf snapshotu
        graph.construct(snapshot);

        // Vypocitame uzlove metriky (erdos, clustering) 
        graph.calculateNodeMetrics();

        // vypocitame hranove metriky (overlap, embeddedness)
        graph.calculateEdgeMetrics();

        // metriky
        graph.getErdos();
        graph.getClusteringCoeff();
        graph.getEmbeddedness();
        graph.getOverlap();
        graph.getDensity();
//
//        // pro jednotlive uzivatele ziskame metriky pres
//        graph.getNodes().get(0).getErdos();
//        graph.getNodes().get(0).getClusterCoeff();
//
//        // pro hrany jsou metriky v
//        graph.getEdges().get(0).getEmbeddedness();
//        graph.getEdges().get(0).getOverlap();
    }

}
