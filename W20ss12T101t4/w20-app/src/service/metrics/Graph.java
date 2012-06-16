package service.metrics;

import model.ArtistSnapshotMap;
import model.SnapshotDB;
import model.User;
import model.dao.DAO;
import model.facade.BusinessFacade;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jaroslav Málek
 * Date: 26.4.12
 * Time: 22:15
 */
public class Graph {

    private Map<String,Node> nodes = new HashMap<String,Node>();
    private Map<EdgeKey,Edge> edges = new HashMap<EdgeKey, Edge>();

    private BusinessFacade bf = new BusinessFacade();

    private DAO dao = DAO.getInstance();

    public void construct(SnapshotDB snapshot) {
//        nodes.clear();
//        edges.clear();

        List<ArtistSnapshotMap> artists = dao.getArtistsInSnapshot(snapshot.getKey());

        for (ArtistSnapshotMap a : artists) {
            // vezmeme posluchace kazdeho komedianta
            List<User> listeners = dao.getArtistsListeners(a.getArtist(), snapshot);

            // pro kazdou dvojici vytvorime hranu (pokud jeste neexistuje)
            for (User u1 : listeners) {
                // pokud jeste nejakeho uzivatele nezname, pridame
                Node n1 = nodes.get(u1.getUsername());
                if (n1 == null) {
                    n1 = new Node(u1.getUsername());
                    nodes.put(u1.getUsername(), n1);
                }

                for (User u2 : listeners) {
                    if (u1.getUsername().equals(u2.getUsername())) continue;
                    Node n2 = nodes.get(u2.getUsername());
                    if (n2 == null) {
                        n2 = new Node(u2.getUsername());
                        nodes.put(u2.getUsername(), n2);
                    }
                    // udelame hranu jestli neni
                    Edge e = new Edge(n1, n2);
                    if (edges.get(new EdgeKey(n1.getName(),n2.getName())) == null) {
                        edges.put(new EdgeKey(n1.getName(),n2.getName()), e);
                        n1.getNeighbours().add(n2);
                        n2.getNeighbours().add(n1);
                    }
                }
            }
        }
    }

    private static <TYPE> TYPE find(List<TYPE> set, TYPE t) {
        for (TYPE obj : set) {
            if (obj.equals(t)) return obj;
        }
        return null;
    }

    private float erdos = 0;
    private float clusteringCoeff = 0;

    public void calculateNodeMetrics() {

        float sumErdos = 0;
        float sumClusteringCoeff = 0;

        // spocitame erdosovo cislo pomoci dfs
        String erdosName = System.getProperties().getProperty("analyzer.erdos");
        Node erdosNode = nodes.get(erdosName);
        if (erdosNode == null)
            throw new RuntimeException("User " + erdosName + " not found in snapshot. Erdos number calculation failed.");
        erdosDFS(erdosNode);

        for (Node node : nodes.values()) {

            // spocitame pocet dvojic sousedu uzlu, ktere jsou navzajem sousedni
            int connectionCount = 0;
            for (Node neighbour : node.getNeighbours()) {
                for (Node neighbour2 : node.getNeighbours()) {
                    if (neighbour.getNeighbours().contains(neighbour2))
                        connectionCount++;
                }
            }
            if (node.getNeighbours().size() > 1)
                node.setClusterCoeff(connectionCount / (float) (node.getNeighbours().size() * (node.getNeighbours().size() - 1)));
            else
                node.setClusterCoeff(0);

            sumErdos += (node.getErdos() < 0 ? 0 : node.getErdos()); // do prumeru nebereme v uvahu uzly, ktere nejsou s Erdosem vubec propojene
            sumClusteringCoeff += node.getClusterCoeff();
        }

        erdos = sumErdos / (nodes.size() - 1);
        clusteringCoeff = sumClusteringCoeff / nodes.size();
    }

    private void erdosDFS(Node start) {

        Queue<Node> q = new LinkedList<Node>();

        start.setErdos(0);

        q.add(start);

        while (q.peek() != null) {
            Node curr = q.remove();
            for (Node n : curr.getNeighbours()) {
                if (n.getErdos() < 0) {
                    n.setErdos(curr.getErdos() + 1);
                    q.add(n);
                }
            }
        }
    }


    private float embeddedness = 0;
    private float overlap = 0;

    public void calculateEdgeMetrics() {

        float sumEmbedd = 0;
        float sumOverlap = 0;

        for (Edge edge : edges.values()) {

            // neighbourhood overlap
            Set<Node> all = new TreeSet<Node>();
            all.addAll(edge.getA().getNeighbours());
            all.addAll(edge.getB().getNeighbours());
            all.remove(edge.getA());
            all.remove(edge.getB());

            int commonNeighbours = 0;

            for (Node neighbourA : edge.getA().getNeighbours()) {
                for (Node neighbourB : edge.getB().getNeighbours()) {
                    if (neighbourA.equals(neighbourB))
                        commonNeighbours++;
                }
            }

            //System.out.println("Edge " + edge.getA().getName() + " - " + edge.getB().getName() + " common neighbours: " + commonNeighbours + " all neighbours: " + all.size());

            edge.setEmbeddedness(commonNeighbours);

            if (all.size() > 0) {
                edge.setOverlap(((float) commonNeighbours) / ((float) all.size()));
            } else edge.setOverlap(0);

            sumEmbedd += edge.getEmbeddedness();
            sumOverlap += edge.getOverlap();
        }

        embeddedness = sumEmbedd / edges.size();
        overlap = sumOverlap / edges.size();
    }

    public float getErdos() {
        return erdos;
    }

    public float getClusteringCoeff() {
        return clusteringCoeff;
    }

    public float getEmbeddedness() {
        return embeddedness;
    }

    public float getOverlap() {
        return overlap;
    }

    public float getDensity() {
        int edgeCnt = edges.size();
        int nodeCnt = nodes.size();

        float citatel = edgeCnt * 2;
        float jmenovatel = nodeCnt * (nodeCnt - 1);

        return citatel / jmenovatel;
    }

    public Map<EdgeKey,Edge> getEdges() {
        return edges;
    }

    public Map<String,Node> getNodes() {
        return nodes;
    }
}
