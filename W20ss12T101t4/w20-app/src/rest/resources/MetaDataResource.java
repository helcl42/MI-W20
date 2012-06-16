package rest.resources;

import model.SnapshotDB;
import model.facade.BusinessFacade;
import rest.converters.AuthorConverter;
import rest.converters.AuthorsConverter;
import rest.converters.MetaDataConverter;
import rest.entities.Author;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * NICE COMMENT
 */
@Path("/v1/metadata")
public class MetaDataResource {

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response get() {

        BusinessFacade bf = new BusinessFacade();

        List<AuthorConverter> authorsList = new ArrayList<AuthorConverter>();
        AuthorsConverter authors = new AuthorsConverter(authorsList);
        authorsList.add(new AuthorConverter(new Author("Miloš Pensimus", "Uhelná Příbram")));
        authorsList.add(new AuthorConverter(new Author("Luboš Helcl", "Černý Dub")));
        authorsList.add(new AuthorConverter(new Author("Jaroslav Málek", "Praha")));
        SnapshotDB lastSnapshot = bf.getLastSnapshot();
        MetaDataConverter mdc = new MetaDataConverter(authors, (lastSnapshot == null ? new Date() : lastSnapshot.getDate()));
        return Response.ok().entity(mdc).build();
    }
}
