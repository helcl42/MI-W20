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
import com.ojn.gexf4j.core.impl.NodeImpl;
import com.ojn.gexf4j.core.impl.StaxGraphWriter;
import com.ojn.gexf4j.core.impl.data.AttributeListImpl;
import model.*;
import model.dao.DAO;
import model.facade.BusinessFacade;
import model.facade.IBusinessFacade;
import service.metrics.Graph;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: lubos
 * Date: 5/5/12
 * Time: 12:43 AM
 * To change this template use File | Settings | File Templates.
 */

public class GexfDynamicService {

    private static IBusinessFacade facade = new BusinessFacade();

    public static void makeDynamicGexf(int size) throws IOException {
        List<User> users = facade.getAllUsers();
        List<SnapshotDB> snapshots = facade.getAllSnapshotsSortedByDate();
        if (snapshots.size() > size) {
            snapshots = snapshots.subList(snapshots.size() - 3, snapshots.size() - 3 + size);
        }

        Logger.getAnonymousLogger().info("SIZE: " + snapshots.size());
        Gexf gexf = new GexfImpl();
        gexf.getGraph().setMode(Mode.DYNAMIC);
        gexf.setVisualization(true);

        AttributeList nodeAttrList = new AttributeListImpl(AttributeClass.NODE);
        nodeAttrList.setMode(Mode.DYNAMIC);

        Attribute degreeAttr = nodeAttrList.createAttribute("degree", AttributeType.STRING, "degree");
        Attribute erdosAttr = nodeAttrList.createAttribute("erdos", AttributeType.STRING, "erdos");
        Attribute clusterAttr = nodeAttrList.createAttribute("cluster", AttributeType.STRING, "cluster");
        gexf.getGraph().getAttributeLists().add(nodeAttrList);

        AttributeList edgeAttrList = new AttributeListImpl(AttributeClass.EDGE);
        edgeAttrList.setMode(Mode.DYNAMIC);

        Attribute embeddednessAttr = edgeAttrList.createAttribute("embeddness", AttributeType.STRING, "embeddness");
        Attribute overlapAttr = edgeAttrList.createAttribute("overlap", AttributeType.STRING, "overlap");
        gexf.getGraph().getAttributeLists().add(edgeAttrList);

        Map<Key<User>, Node> userNodeMap = new HashMap<Key<User>, Node>();
        Map<String, String> embeddnessMap = new HashMap<String, String>();
        Map<String, String> overlapMap = new HashMap<String, String>();
        Date startDate = snapshots.get(0).getDate();

        for (User a : users) {
            int degree = 1;
            Node n = null;
            boolean first = true;
            for (SnapshotDB sn : snapshots) {
                Logger.getAnonymousLogger().info("UserId: " + a.getId() + " Snapshot_1: " + sn.getId());
                List<RelationshipMap> userRelations = DAO.getInstance().getUsersRelationsWithArtists(a.getKey(), sn.getKey());
                Date endDate = sn.getDate();
                if (userRelations != null) {
                    Graph graph = new Graph();
                    graph.construct(sn);
                    graph.calculateNodeMetrics();
                    graph.calculateEdgeMetrics();
                    overlapMap.put(sn.getId().toString(), String.valueOf(graph.getEmbeddedness()));
                    overlapMap.put(sn.getId().toString(), String.valueOf(graph.getOverlap()));
                    if (first) {
                        n = gexf.getGraph().createNode(a.getId().toString());
                        n.setLabel(a.getUsername());
                        userNodeMap.put(a.getKey(), n);
                        first = false;
                    }
                    n.getAttributeValues().add(degreeAttr.createValue(String.valueOf(degree)));
                    n.getAttributeValues().add(clusterAttr.createValue(String.valueOf(graph.getClusteringCoeff())));
                    n.getAttributeValues().add(erdosAttr.createValue(String.valueOf(graph.getErdos())));
                    AttributeValue attr = n.getAttributeValues().get(n.getAttributeValues().size() - 3);
                    attr.setStartDate(startDate);
                    attr.setEndDate(endDate);
                    attr = n.getAttributeValues().get(n.getAttributeValues().size() - 2);
                    attr.setValue(String.valueOf(graph.getClusteringCoeff()));
                    attr.setStartDate(startDate);
                    attr.setEndDate(endDate);
                    attr = n.getAttributeValues().get(n.getAttributeValues().size() - 1);
                    attr.setValue(String.valueOf(graph.getErdos()));
                    attr.setStartDate(startDate);
                    attr.setEndDate(endDate);

                    degree += userRelations.size();
                }
                startDate = endDate;
            }

        }

        List<String> distinctEdges = new ArrayList<String>();
        for (Map.Entry<Key<User>, Node> entry : userNodeMap.entrySet()) {
            for (SnapshotDB sn : snapshots) {
                List<RelationshipMap> userRelations = DAO.getInstance().getUsersRelationsWithArtists(entry.getKey(), sn.getKey());
                User user = DAO.getInstance().getUserByKey(entry.getKey());
                Date endDate = sn.getDate();
                String embeedness = embeddnessMap.get(sn.getId().toString());
                String overpap = overlapMap.get(sn.getId().toString());
                Logger.getAnonymousLogger().info("UserId: " + user.getId() + " Snapshot_1: " + sn.getId());
                if (userRelations != null) {
                    for (RelationshipMap rel : userRelations) {
                        List<User> userList = DAO.getInstance().getArtistsListeners(rel.getArtist(), sn);
                        String artistName = DAO.getInstance().getArtistByKey(rel.getArtist()).getName();
                        for (User u : userList) {
                            if (!distinctEdges.contains(createKey(user.getId(), u.getId()))
                                    && !distinctEdges.contains(createKey(u.getId(), user.getId()))
                                    && !distinctEdges.contains(createKey(u.getId(), user.getId()))
                                    && !user.getId().equals(u.getId())) {
                                Node n2 = userNodeMap.get(u.getKey());
                                n2.setLabel(u.getUsername());
                                String edgeName = user.getUsername() + "-" + u.getUsername();
                                Edge e = entry.getValue().connectTo(edgeName, n2);
                                e.setLabel(artistName);
                                e.setStartDate(startDate);
                                e.setEndDate(endDate);
                                e.getAttributeValues().add(embeddednessAttr.createValue(embeedness));
                                e.getAttributeValues().add(overlapAttr.createValue(overpap));
                                AttributeValue attr = e.getAttributeValues().get(e.getAttributeValues().size() - 2);
                                attr.setValue(embeedness);
                                attr.setStartDate(startDate);
                                attr.setEndDate(endDate);
                                attr = e.getAttributeValues().get(e.getAttributeValues().size() - 1);
                                attr.setValue(overpap);
                                attr.setStartDate(startDate);
                                attr.setEndDate(endDate);

                                distinctEdges.add(createKey(user.getId(), u.getId()));
                            }
                        }
                    }
                }
                startDate = endDate;
            }
        }

        StaxGraphWriter gexfWriter = new StaxGraphWriter();
        saveXml(gexfWriter, gexf);
    }

    private static String createKey(Long a, Long b) {
        StringBuilder sb = new StringBuilder();
        sb.append(a.toString());
        sb.append("-");
        sb.append(b.toString());
        return sb.toString();
    }


    private static void saveXml(StaxGraphWriter graph, Gexf g) throws IOException {
        String fileName = "dynamicgexf.xml";
        FileService f = FileServiceFactory.getFileService();
        AppEngineFile file = f.createNewBlobFile("application/xml", fileName);
        FileWriteChannel writeChannel = f.openWriteChannel(file, true);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        graph.writeToStream(g, bos);

        writeChannel.write(ByteBuffer.wrap(bos.toByteArray()));
        writeChannel.closeFinally();

        String fullPath = file.getFullPath();

        XMLPath path = DAO.getInstance().getXmlPath(fileName);

        if (path != null) {
            path.setPath(fullPath);
            DAO.getInstance().updateEntity(path);
        } else {
            path = new XMLPath(fullPath, fileName);
            DAO.getInstance().saveEntity(path);
        }
    }
}
