package rest.resources;

import model.SnapshotDB;
import model.facade.BusinessFacade;
import rest.converters.SnapshotConverter;
import rest.converters.SnapshotsConverter;
import rest.entities.Snapshot;
import service.metrics.Graph;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jaroslav Málek
 * Date: 10.3.12
 * Time: 14:07
 * To change this template use File | Settings | File Templates.
 */

@Path("/v1/snapshots")
public class SnapshotsResource {

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getXml() {

        List<Snapshot> snapshots = getSnapshots();
        List<SnapshotConverter> snapshotConverters = new ArrayList<SnapshotConverter>();

        for (Snapshot s : snapshots) {
            Graph graph = new Graph();
            snapshotConverters.add(new SnapshotConverter(s));
        }

        //snapshots.add(new SnapshotConverter(new Snapshot(new Date(112, 2, 5, 4, 3, 2), 2, 5)));
        //snapshots.add(new SnapshotConverter(new Snapshot(new Date(), 10, 20)));

        SnapshotsConverter sc = new SnapshotsConverter(snapshotConverters);

        return Response.ok().entity(sc).build();
    }

    @GET
    @Produces("image/png")
    public Response getImg() {

        List<Snapshot> snapshots = getSnapshots();

        //snapshots.add(new Snapshot(new Date(112, 2, 5, 1, 0, 0), 10, 50));
        //snapshots.add(new Snapshot(new Date(112, 2, 5, 3, 0, 0), 16, 45));
        //snapshots.add(new Snapshot(new Date(112, 2, 6, 1, 0, 0), 35, 116));

        StringBuilder queryBuilder = new StringBuilder();

        Snapshot firstSnapshot = (snapshots.size() > 0 ? snapshots.get(0) : new Snapshot(new Date(), 0, 0));
        Snapshot lastSnapshot = (snapshots.size() > 1 ? snapshots.get(snapshots.size() - 1) : firstSnapshot);

        Date first = firstSnapshot.getCreated();
        Date last = lastSnapshot.getCreated();
        long from = first.getTime();
        long to = last.getTime();

        List<String> dates = new ArrayList<String>();
        Calendar calFst = Calendar.getInstance();
        Calendar calLst = Calendar.getInstance();
        calLst.setTime(last);
        calFst.setTime(first);

        long fstLabel = 0;

        if (calFst.get(Calendar.HOUR_OF_DAY) < 12) {
            calFst.set(Calendar.HOUR_OF_DAY, 12);
            fstLabel = calFst.getTime().getTime() - first.getTime();
        } else {
            calFst.set(Calendar.HOUR_OF_DAY, 12);
            calFst.add(Calendar.DAY_OF_MONTH, 1);
            fstLabel = calFst.getTime().getTime() - first.getTime();
        }

        DateFormat dateFormat = DateFormat.getDateInstance();
        long label = fstLabel;
        while (to - from >= label) {
            dates.add(dateFormat.format(new Date(label + from)));
            label += 1000 * 60 * 60 * 24;
        }


        /* Osy
        * 0 - nodes
        * 1 - edges
        * 2 - cas
        */

        // stabilni hodnoty
        queryBuilder.append("cht=lxy"); // typ grafu
        queryBuilder.append("&chxs=0,0000FF,11.5,0,lt,676767|1,FF0000,10.5,0,lt,676767|2,676767,11.5,0,lt,676767"); // axis styles
        queryBuilder.append("&chxt=y,y,x"); // typ os grafu
        queryBuilder.append("&chdl=Nodes|Edges"); // data labels
        queryBuilder.append("&chdlp=b"); // label position
        queryBuilder.append("&chls=1|1"); // line styles
        queryBuilder.append("&chma=5,5,5,25|40"); // margins
        queryBuilder.append("&chco=3072F3,FF0000"); // barvy
        queryBuilder.append("&chg=-1,-1"); // grid

        // generovane hodnoty

        queryBuilder.append("&chs=").append(50 * (snapshots.size() > 0 ? snapshots.size() : 1)).append("x").append(220); // velikost

        queryBuilder.append("&chxl=2:"); // 2 - labels
        for (String d : dates) {
            queryBuilder.append("|").append(d);
        }

        queryBuilder.append("&chxp=2"); // 2 - pozice labelu
        for (int i = 0; i < dates.size(); i++) {
            queryBuilder.append(",").append(fstLabel + i * 1000 * 60 * 60 * 24);
        }

        // data
        queryBuilder.append("&chd=t:");
        StringBuilder nodes = new StringBuilder();
        nodes.append(firstSnapshot.getNodes());
        StringBuilder edges = new StringBuilder();
        edges.append(firstSnapshot.getEdges());
        StringBuilder tStamps = new StringBuilder();
        tStamps.append(0);

        int maxNodes = firstSnapshot.getNodes();
        int minNodes = firstSnapshot.getNodes();
        int maxEdges = firstSnapshot.getEdges();
        int minEdges = firstSnapshot.getEdges();

        for (Snapshot sp : snapshots) {
            if (sp == firstSnapshot) continue;
            nodes.append(",").append(sp.getNodes());
            edges.append(",").append(sp.getEdges());
            tStamps.append(",").append(sp.getCreated().getTime() - from);


            if (maxNodes < sp.getNodes()) maxNodes = sp.getNodes();
            if (minNodes > sp.getNodes()) minNodes = sp.getNodes();
            if (maxEdges < sp.getEdges()) maxEdges = sp.getEdges();
            if (minEdges > sp.getEdges()) minEdges = sp.getEdges();

        }
        queryBuilder.append(tStamps);
        queryBuilder.append("|").append(nodes);
        queryBuilder.append("|").append(tStamps);
        queryBuilder.append("|").append(edges);

        // rozsahy os
        queryBuilder.append("&chxr=");
        int tmp2 = minNodes - minNodes / 10;
        queryBuilder.append("0,").append(tmp2 > 0 ? tmp2 : 0).append(",").append(maxNodes + maxNodes / 10);
        int tmp = minEdges - minEdges / 10;
        queryBuilder.append("|1,").append(tmp > 0 ? tmp : 0).append(",").append(maxEdges + maxEdges / 10);
        queryBuilder.append("|2,").append(0).append(",").append(to - from);

        // data scale
        queryBuilder.append("&chds=");
        queryBuilder.append(0).append(",").append(to - from).append(","); // data time axis scale
        queryBuilder.append(tmp2 > 0 ? tmp2 : 0).append(",").append(maxNodes + maxNodes / 10); // nodes
        queryBuilder.append(",").append(0).append(",").append(to - from); // data time axis scale
        queryBuilder.append(",").append(tmp > 0 ? tmp : 0).append(",").append(maxEdges + maxEdges / 10); // edges

        try {
            URI uri = new URI("https", null, "chart.googleapis.com", 443, "/chart", queryBuilder.toString(), null);
            System.out.println(uri.toString());
            return Response.ok(uri.toURL().openStream()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    private List<Snapshot> getSnapshots() {

        BusinessFacade bf = new BusinessFacade();

        List<Snapshot> ret = new ArrayList<Snapshot>();

        List<SnapshotDB> snapshots = bf.getAllSnapshotsSortedByDate();
        if (snapshots != null) {
            for (SnapshotDB sp : snapshots) {
                ret.add(new Snapshot(sp.getDate(), sp.getNodes(), sp.getEdges()));
            }
        }

        return ret;
    }
}
