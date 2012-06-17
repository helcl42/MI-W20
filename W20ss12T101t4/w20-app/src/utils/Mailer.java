package utils;

import model.SnapshotDB;
import model.facade.BusinessFacade;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Properties;


/**
 * Created by IntelliJ IDEA.
 * User: lubos
 * Date: 3/12/12
 * Time: 6:09 PM
 * To change this template use File | Settings | File Templates.
 */


public class Mailer {
    /*
     * Constru
     */
    private Mailer() {
    }

    private static String parseException(Exception msg) {
        StringBuilder message = new StringBuilder();
        message.append(msg.getMessage());
        message.append("\n");
        StringWriter strWriter = new StringWriter();
        msg.printStackTrace(new PrintWriter(strWriter));
        message.append(strWriter.toString());

        return message.toString();
    }


    public static void sendMail(MessageTypes message, Object exp) {

        String mailBody = prepareMessage(message, exp);
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("lubos.helcl@gmail.com", "last.fm analyzer"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress("lubos.helcl@gmail.com", "lubos"));
            msg.setSubject("Last.fm analyzer message: " + message.toString());
            msg.setSentDate(new Date());
            msg.setText(mailBody);
            Transport.send(msg);

        } catch (AddressException e) {
        } catch (MessagingException e) {
        } catch (Exception e) {

        }
    }

    public static String prepareMessage(MessageTypes messageType, Object msg) {
        String mailBody;

        BusinessFacade bf = new BusinessFacade();
        SnapshotDB snapshot = bf.getLastSnapshot();

        if (snapshot == null) {
            return "Unknown error - snapshot finished but no last snaphot found";
        }


        switch (messageType) {
            case SNAPSHOT_ERROR:
                mailBody = "Snaphot started " + snapshot.getDate() + "\n";
                mailBody += "Died at " + new Date() + "\n";
                mailBody += parseException((Exception) msg);
                return mailBody;
            case SNAPSHOT_OK:

                StringBuilder sb = new StringBuilder();
                sb.append("Snapshot finished\n\n");
                sb.append("Started: ").append(snapshot.getDate()).append("\n");
                sb.append("Finished: ").append(new Date()).append("\n");
                sb.append("Total users: ").append(bf.getUsersInSnapshot(snapshot.getKey())).append("\n");
                sb.append("Total artists: ").append(bf.getArtistCountInSnapshot(snapshot.getKey())).append("\n");
                sb.append("Total relations: ").append(bf.getRelationsInSnapshot(snapshot.getKey())).append("\n");
                if (!snapshot.isFinished()) {
                    sb.append("Some errors occured:\n");
                    sb.append((String) msg);
                }
                return sb.toString();
            default:
                return "";
        }
    }
}
