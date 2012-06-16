package service;

import apiclient.LastFmClient;
import apiclient.LastFmException;
import apiclient.xml.TrackXml;
import apiclient.xml.UserInfoXml;
import apiclient.xml.UserXml;
import com.googlecode.objectify.Key;
import model.Artist;
import model.SnapshotDB;
import model.User;
import model.facade.BusinessFacade;

import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jaroslav Málek
 * Date: 12.3.12
 * Time: 15:11
 */
public class SnapshotService {

    /**
     * Provede snapshot
     */
    public static String makeSnapshot() throws IOException {

        LastFmClient client = new LastFmClient();

        BusinessFacade bf = new BusinessFacade();

        // jmeno skupiny z appengine-web.xml
        String groupName = System.getProperties().getProperty("analyzer.groupName");

        if (groupName == null || groupName.trim().equals("")) {
            throw new RuntimeException("Group name not configured in appengine-web.xml");
        }

        //SnapshotDB lastSnapshot = bf.getLastSnapshot();
        // vytvorime novy snapshot
        Date start = new Date();
        SnapshotDB snapshot = new SnapshotDB(start);
        bf.saveSnapshot(snapshot);

        // ziskame seznam uzivatelu ve skupine
        List<UserXml> users = null;

        List<String> badRequests = new ArrayList<String>();

        try {
            users = client.getUsersForGroup(groupName);
        } catch (LastFmException e) {
            if (Integer.valueOf(6).equals(e.getCode())) {
                throw new RuntimeException("Group \"" + groupName + "\" doesn't exist");
            }
            throw new RuntimeException(e);
        }

        if (users == null || users.size() == 0) {
            System.out.println("Warning: snapshot of an empty group.");
            return null; // prazdna skupina, posleme jen warning do logu
        }

        List<Key<User>> usersInSnapshot = new ArrayList<Key<User>>();
        Set<Key<Artist>> artistsInSnapshot = new HashSet<Key<Artist>>();

        int edges = 0;

        for (UserXml user : users) {
            User u = bf.getUserByName(user.getName());
            if (u == null) { // vlozime noveho usera
                try {
                    UserInfoXml info = client.getUserInfo(user.getName());

                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    cal.setTimeInMillis(info.getRegistered().getTimestamp() * 1000);

                    u = new User(info.getAge(), info.getCountry(), cal.getTime(),
                            info.getGender(), null, info.getRealName(), info.getPlaycount().longValue(), info.getUrl(), info.getName());
                    bf.saveUser(u);
                } catch (LastFmException e) { // potichu ho preskocime
                    continue;
                }
            }

            Date from = null;

            //if (lastSnapshot == null) {
            // pokud neexistuje predchozi snapshot, vememe poslednich 8h
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start);
            calendar.add(Calendar.HOUR_OF_DAY, -8);
            from = calendar.getTime();
            //} else {
            // pokud existuje predchozi snapshot vememe cas od posledniho snapshotu
            //    from = lastSnapshot.getDate();
            //}

            List<TrackXml> tracks = null;
            try {
                tracks = client.getRecentTracksForUser(user.getName(), from, start);
            } catch (LastFmException e) {
                if (Integer.valueOf(4).equals(e.getCode())) { // ma privatni recent tracks, preskocime ho
                    continue;
                }
                if (e.getCode() == null) {
                    badRequests.add(e.toString() + "\n\n");
                } else {
                    badRequests.add("Code: " + e.getCode() + " " + e.getUri());
                }
                // chyba -> nemame co uzivateli prochazet, smula
                continue;
            }

            // uzivatel bude ve snapshotu
            usersInSnapshot.add(u.getKey());

            if (tracks == null || tracks.isEmpty()) continue; // uzivatel nema nic noveho

            for (TrackXml track : tracks) {
                if (track == null || track.getArtist() == null || track.getArtist().getName() == null)
                    continue; // neco neni nastavene, nemame co ukladat
                if (track.getNowPlaying() != null && track.getNowPlaying().equals("true"))
                    continue; // nebereme now playing, protoze ji to cpe do vysledku vzdy
                Artist artist = bf.getArtistByName(track.getArtist().getName());
                if (artist == null) { // novy umelec
                    artist = new Artist(track.getArtist().getName());
                    bf.saveArtist(artist);
                }
                edges++;
                bf.saveRelationship(snapshot.getKey(), artist.getKey(), u.getKey());
                artistsInSnapshot.add(artist.getKey());
            }
        }

        bf.saveSnapshot(snapshot.getKey(), new ArrayList<Key<Artist>>(artistsInSnapshot), usersInSnapshot);

        if (badRequests.isEmpty())
            snapshot.setFinished(true);
        else {
            StringBuilder sb = new StringBuilder("Requests failed: \n\n");
            for (String err : badRequests) {
                sb.append(err).append("\n");
            }
            return sb.toString();
        }

        snapshot.setEdges(edges);
        snapshot.setNodes(artistsInSnapshot.size() + usersInSnapshot.size());

        bf.updateSnapshot(snapshot);
        GexfService.makeGexf();
        return null;
    }
}
