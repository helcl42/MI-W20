package service;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.googlecode.objectify.Key;
import com.ojn.gexf4j.core.Edge;
import com.ojn.gexf4j.core.Gexf;
import com.ojn.gexf4j.core.Mode;
import com.ojn.gexf4j.core.Node;
import com.ojn.gexf4j.core.data.*;
import com.ojn.gexf4j.core.impl.GexfImpl;
import com.ojn.gexf4j.core.impl.StaxGraphWriter;
import com.ojn.gexf4j.core.impl.data.AttributeListImpl;
import model.*;
import model.dao.DAO;
import model.facade.BusinessFacade;
import model.facade.IBusinessFacade;
import service.metrics.EdgeKey;
import service.metrics.Graph;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: milos
 * Date: 31.3.12
 * Time: 19:19
 * To change this template use File | Settings | File Templates.
 */
public class GexfService {
    public static void makeGexf() throws IOException {
        IBusinessFacade facade = new BusinessFacade();
        Gexf gexf = new GexfImpl();

        List<Artist> artists = facade.getAllArtists();
        List<User> users = facade.getAllUsers();

        Map<Key<User>,Node> userMap = new HashMap<Key<User>, Node>();
        Map<EdgeKey,Edge> edgeMap = new HashMap<EdgeKey, Edge> ();

        for (User a : users) {
            Node n = gexf.getGraph().createNode(a.getId().toString());
            n.setLabel(a.getUsername());
            userMap.put(a.getKey(),n);
        }

        List<SnapshotDB> snapshotList = facade.getAllSnapshotsSortedByDate();

        List<RelationshipMap> rels;

        //TODO - datum hrany pouze od vyskytu

        for (SnapshotDB snapshot : snapshotList) {
            for(Artist a:artists) {
                rels = DAO.getInstance().getUsersByArtistSnapshot(a,snapshot);

                while(!rels.isEmpty()) {
                    RelationshipMap r1 = rels.remove(0);
                    for(RelationshipMap r :rels) {
                        if (r1.isPossible(r.getUser()) && !r.isClosed(r1.getUser())) {
                            Node user1 = userMap.get(r1.getUser());
                            Node user2 = userMap.get(r.getUser());
                            addEdge(user1,user2,edgeMap);
                            r1.putToPossible(r.getUser());
                            r.putToClosed(r1.getUser());
                        }
                    }
                }
            }
        }

        StaxGraphWriter gexfWriter = new StaxGraphWriter();

        saveXml(gexfWriter, gexf,-1);
    }

    public static void makeDynamicGexf(int size,int order) throws IOException {
        IBusinessFacade facade = new BusinessFacade();
        Gexf gexf = new GexfImpl();

        List<Artist> artists = facade.getAllArtists();
        List<User> users = facade.getAllUsers();

        Map<Key<User>,Node> userMap = new HashMap<Key<User>, Node>();
        Map<EdgeKey,Edge> edgeMap = new HashMap<EdgeKey, Edge> ();

        for (User a : users) {
            Node n = gexf.getGraph().createNode(a.getId().toString());
            n.setLabel(a.getUsername());
            userMap.put(a.getKey(),n);
        }

        List<SnapshotDB> snapshotList = facade.getAllSnapshotsSortedByDate();

        List<RelationshipMap> rels;

        //TODO - datum hrany pouze od vyskytu
        while (snapshotList.size()>15) snapshotList.remove(0);
        List<SnapshotDB> snapshotSublist = snapshotList.subList(0,((size/3)*order));

        for (SnapshotDB snapshot : snapshotSublist) {
            for(Artist a:artists) {
                rels = DAO.getInstance().getUsersByArtistSnapshot(a,snapshot);

                while(!rels.isEmpty()) {
                    RelationshipMap r1 = rels.remove(0);
                    for(RelationshipMap r :rels) {
                        if (r1.isPossible(r.getUser()) && !r.isClosed(r1.getUser())) {
                            Node user1 = userMap.get(r1.getUser());
                            Node user2 = userMap.get(r.getUser());
                            addEdge(user1,user2,edgeMap);
                            r1.putToPossible(r.getUser());
                            r.putToClosed(r1.getUser());
                        }
                    }
                }
            }
        }

        AttributeList nodeAttrList = new AttributeListImpl(AttributeClass.NODE);
        nodeAttrList.setMode(Mode.DYNAMIC);
        Attribute erdosAttr = nodeAttrList.createAttribute("erdos", AttributeType.STRING, "erdos");
        Attribute clusteringAttr = nodeAttrList.createAttribute("clustering", AttributeType.STRING, "clustering");

        AttributeList edgeAttrList = new AttributeListImpl(AttributeClass.EDGE);
        edgeAttrList.setMode(Mode.DYNAMIC);
        Attribute overlapAttr = edgeAttrList.createAttribute("overlap", AttributeType.STRING, "overlap");
        Attribute embedAttr = edgeAttrList.createAttribute("embeddness", AttributeType.STRING, "embeddness");

        for (SnapshotDB snapshot : snapshotSublist)  {
            Graph g = new Graph();
            g.construct(snapshot);
            g.calculateNodeMetrics();
            g.calculateEdgeMetrics();
            Map<String,service.metrics.Node> nodeMap = g.getNodes();
            for (Node n : userMap.values()) {
                if (nodeMap.containsKey(n.getLabel())) {
//                    n.setStartDate(snapshot.getDate());
//                    n.setEndDate(snapshot.getDate());
                    int erdos = nodeMap.get(n.getLabel()).getErdos();
                    float clustering = nodeMap.get(n.getLabel()).getClusterCoeff();
                    if (erdos != -1) {
                        AttributeValue valueErdos = erdosAttr.createValue(String.valueOf(erdos));
                        valueErdos.setStartDate(snapshot.getDate());
                        valueErdos.setEndDate(snapshot.getDate());
                        n.getAttributeValues().add(valueErdos);
                    }
                    AttributeValue valueCluster = clusteringAttr.createValue(String.valueOf(clustering));
                    valueCluster.setStartDate(snapshot.getDate());
                    valueCluster.setEndDate(snapshot.getDate());
                    n.getAttributeValues().add(valueCluster);
                    for (Edge e :n.getEdges()) {
                        Map<service.metrics.EdgeKey,service.metrics.Edge> edgesInSnapshotMap = g.getEdges();
                        service.metrics.EdgeKey key = new service.metrics.EdgeKey(e.getSource().getLabel(),e.getTarget().getLabel());
                        service.metrics.Edge edge = edgesInSnapshotMap.get(key);

                        if (edge!=null) {
                            AttributeValue valueEmbed = embedAttr.createValue(String.valueOf(edge.getEmbeddedness()));
                            valueEmbed.setStartDate(snapshot.getDate());
                            valueEmbed.setEndDate(snapshot.getDate());
                            e.getAttributeValues().add(valueEmbed);

                            AttributeValue valueOverlap = overlapAttr.createValue(String.valueOf(edge.getOverlap()));
                            valueOverlap.setStartDate(snapshot.getDate());
                            valueOverlap.setEndDate(snapshot.getDate());
                            e.getAttributeValues().add(valueOverlap);
                        }
                    }
                }
            }
        }

        StaxGraphWriter gexfWriter = new StaxGraphWriter();

        saveXml(gexfWriter, gexf,order);
    }

    private static void saveXml(StaxGraphWriter graph, Gexf g,int order) throws IOException {

        //TODO - soubor je v blobstoru duplikovan
        String fileName = (order != -1) ? "gexfdynamic.xml" + order: "gexf.xml";
        FileService f = FileServiceFactory.getFileService();
        AppEngineFile file = f.createNewBlobFile("application/xml",fileName);
        FileWriteChannel writeChannel = f.openWriteChannel(file, true);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        graph.writeToStream(g,bos);

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
    }

    private static void addEdge(Node user1,Node user2,Map<EdgeKey,Edge> edgeMap) {
        EdgeKey key = new EdgeKey(user1.getLabel(),user2.getLabel());
        if(!edgeMap.containsKey(key)){
            Edge e = user1.connectTo(key.toString(),user2);
            e.setWeight(0.5f);
            edgeMap.put(key,e);
        } else {
            Edge e = edgeMap.get(key);
            e.setWeight(e.getWeight()+0.5f);
        }
    }


    //pred tim nez se to cely zmenilo tak se EdgeKeye lisily
    //ted sou stejny, ale sem moc linej to odebrat
    static class EdgeKey implements Comparable<EdgeKey> {
        private String nodeUsername1;
        private String nodeUsername2;

        EdgeKey(String nodeUsername1, String nodeUsername2) {
            this.nodeUsername1 = nodeUsername1;
            this.nodeUsername2 = nodeUsername2;
        }

        public String getNodeUsername1() {
            return nodeUsername1;
        }

        public String getNodeUsername2() {
            return nodeUsername2;
        }

        @Override
        public String toString() {
            return nodeUsername1 + "_" + nodeUsername2;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !obj.getClass().equals(getClass())) return false;
            EdgeKey other = (EdgeKey) obj;

            if (nodeUsername1.equals(other.getNodeUsername1()) && nodeUsername2.equals(other.getNodeUsername2()))
                return true;
            if (nodeUsername1.equals(other.getNodeUsername2()) && nodeUsername2.equals(other.getNodeUsername1()))
                return true;

            return false;
        }

        @Override
        public int hashCode() {
            return nodeUsername1.hashCode()+nodeUsername2.hashCode();
        }

        @Override
        public int compareTo(EdgeKey o) {
            if (nodeUsername1.equals(o.getNodeUsername1()) && nodeUsername2.equals(o.getNodeUsername2()))
                return 0;
            if (nodeUsername1.equals(o.getNodeUsername2()) && nodeUsername2.equals(o.getNodeUsername1()))
                return 0;
            return this.toString().compareTo(o.toString());  //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
