package apiclient;

import apiclient.xml.*;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jaroslav Málek
 * Date: 10.3.12
 * Time: 17:02
 */
public class LastFmClient {


    /**
     * Last.fm API klic
     */
    private static final String API_KEY = "d9a04dd513bfe567dcd1e8411d9ed69a";

    /**
     * Adresa Last.fm API
     */
    private static final String BASE_URL = "http://ws.audioscrobbler.com/2.0/";

    /**
     * Timestamp posledniho requestu
     */
    private Date lastRequest = null;

    /**
     * minimalni pocet ms mezi requesty na api
     * aby nas neodrizli
     */
    private static final long WAIT_TIME = 1 * 1000;

    /**
     * Vrati seznam uzivatelu v dane skupine. Vraci prazdny seznam pokud je skupina prazdna..
     * Vyhahzuje LastFmException pokud dojde k chybe, napr. error code 6 - neexistuje skupina
     *
     * @param groupName jmeno skupiny
     * @return seznam uzivatelu, null pokud nejsou uzivatele
     * @throws LastFmException pokud dojde k chybe,
     */
    public List<UserXml> getUsersForGroup(String groupName) throws LastFmException {
        String[] params = {"group=" + groupName, "limit=200", "page=1"};

        try {
            List<UserXml> lst = new ArrayList<UserXml>();
            GroupMembers members = null;
            int page = 1;
            int totalPages = 1;
            do {
                params[2] = "page=" + page;
                members = callApiFunc("group.getmembers", params, GroupMembers.class);
                if (members == null || members.getTotal() == 0)
                    return null;
                page++;
                totalPages = members.getTotalPages();
                lst.addAll(members.getUserList());

            } while (page <= totalPages);

            return lst;
        } catch (JAXBException e) {
            throw new LastFmException(e);
        }
    }

    /**
     * Vraci seznam prvnich 200 sklateb, ktere si zadany uzivatel pustil v rozmezi from - to
     * Pokud nic neposlouchal vraci null, pokud dojde k chybe vyhodi LastFmException
     * napr. error code 6 - neexistujici user, 8 - error fetching (spatne datum?) , 4 - privatni
     *
     * @param user jmeno uzivatele
     * @param from datum od
     * @param to   datum do
     * @return seznam sklateb, null pri neuspechu
     * @throws LastFmException pokud dojde k chybe
     */
    public List<TrackXml> getRecentTracksForUser(String user, Date from, Date to) throws LastFmException {

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));//TimeZone.getTimeZone("UTC"));
        cal.setTime(from);
        long fromUtcUnx = cal.getTimeInMillis();// - cal.getTimeZone().getRawOffset();
        fromUtcUnx /= 1000;

        cal.setTime(to);
        long toUtcUnx = cal.getTimeInMillis();// - cal.getTimeZone().getRawOffset();
        toUtcUnx /= 1000;

        String[] params = {"user=" + user, "limit=200", "page=1", "to=" + toUtcUnx, "from=" + fromUtcUnx};

        try {
            RecentTracks rt = callApiFunc("user.getrecenttracks", params, RecentTracks.class);
            if (rt == null) return null;
            return rt.getTracks();

        } catch (JAXBException e) {
            throw new LastFmException(e);
        }
    }

    /**
     * Ziska info k zadanemu uzivateli, pokud neexistuje vyhodi vyjimku s code 6
     *
     * @param userName uzivatelske jmeno
     * @return info o uzivateli
     * @throws LastFmException
     */
    public UserInfoXml getUserInfo(String userName) throws LastFmException {
        String[] params = {"user=" + userName};

        try {
            UserInfoXml user = callApiFunc("user.getinfo", params, UserInfoXml.class);
            return user;
        } catch (JAXBException e) {
            throw new LastFmException(e);
        }
    }

    /**
     * Fce zavola metodu Last.fm API a z odpovedi unmarshallne zadanou tridu
     *
     * @param method      nazev API metody
     * @param params      pole parametru metody, jedna polozka je param=value
     * @param resultClass trida kterou mame z odpovedi unmarshallnout
     * @return null pri neuspechu, jinak Node obsahu odpovedi
     * @throws JAXBException   pokud dojde k chybe pri unmarshallu
     * @throws LastFmException pokud dojde k chybe api
     */
    @SuppressWarnings("unchecked")
    private <TYPE> TYPE callApiFunc(String method, String[] params, Class<TYPE> resultClass) throws JAXBException, LastFmException {
        // sestavime URL
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append("?api_key=").append(API_KEY);
        urlBuilder.append("&method=").append(method);
        if (params != null)
            for (String param : params) {
                urlBuilder.append("&").append(param);
            }

        URL u = null;
        try {
            u = new URL(urlBuilder.toString());
        } catch (MalformedURLException e) {
            throw new LastFmException("Chyba pri pokladani dotazu", e);
        }

        waitForReq();

        // Ziskame odpoved z lastfm
        JAXBContext jc = JAXBContext.newInstance(LastFmResponse.class);
        Unmarshaller um = jc.createUnmarshaller();
        LastFmResponse lfmr = (LastFmResponse) um.unmarshal(u);

        lastRequest = new Date();

        if (lfmr.getStatus().equals("ok")) {
            // zkusime unmarshallnout podle vysledku
            jc = JAXBContext.newInstance(resultClass);
            um = jc.createUnmarshaller();
            TYPE result = (TYPE) um.unmarshal((Node) lfmr.getInnerXML());
            return result;
        } else {
            jc = JAXBContext.newInstance(LastFmError.class);
            um = jc.createUnmarshaller();
            LastFmError result = (LastFmError) um.unmarshal((Node) lfmr.getInnerXML());
            throw new LastFmException(result.getMessage(), result.getCode(), u.toString());
        }
    }

    /**
     * Pocka dokud neubehne WAIT_TIME ms od posledniho requestu na api
     */
    private void waitForReq() {
        if (lastRequest != null) {
            long diff = new Date().getTime() - lastRequest.getTime();
            if (diff < WAIT_TIME) {
                try {
                    Thread.sleep(WAIT_TIME - diff);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
