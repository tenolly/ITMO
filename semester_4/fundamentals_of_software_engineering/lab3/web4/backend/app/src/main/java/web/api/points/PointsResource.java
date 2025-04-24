package web.api.points;

import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import web.api.auth.AuthConfig;
import web.api.database.PostgreSQLJDBC;
import web.api.database.models.Point;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

@Path("/points")
public class PointsResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPoints(@CookieParam("access_token") String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("no access token").build();
        }

        try {
            Claims claims = Jwts.parserBuilder()
                                .setSigningKey(AuthConfig.getSecretKeySpec())
                                .build()
                                .parseClaimsJws(accessToken)
                                .getBody();

            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("access token has expired").build();
            }

            String username = claims.getSubject();

            try (Connection connection = PostgreSQLJDBC.connect()) {
                ArrayList<Point> points = PostgreSQLJDBC.getLastPoints(connection, username);
                return Response.ok().entity(points).build();
            } catch (SQLException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                               .entity("an error occurred while retrieving points")
                               .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.UNAUTHORIZED).entity("invalid access token").build();
        }
    }
}
