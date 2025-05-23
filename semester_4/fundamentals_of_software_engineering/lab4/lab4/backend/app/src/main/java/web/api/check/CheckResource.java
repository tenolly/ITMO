package web.api.check;

import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import web.api.auth.AuthConfig;
import web.api.check.beans.ConsecutiveMisses;
import web.api.check.beans.HitStatistics;
import web.api.database.PostgreSQLJDBC;
import web.api.database.models.Point;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import javax.management.MBeanServer;
import javax.management.ObjectName;

@Path("/check")
public class CheckResource {
    private static final HitStatistics hitStatistics = new HitStatistics();
    private static final ConsecutiveMisses consecutiveMisses = new ConsecutiveMisses();

    static {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            
            ObjectName hitStatsName = new ObjectName("web.api.check:type=HitStatistics");
            mbs.registerMBean(hitStatistics, hitStatsName);
            
            ObjectName missesName = new ObjectName("web.api.check:type=ConsecutiveMisses");
            mbs.registerMBean(consecutiveMisses, missesName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GET
    public Response check(
        @QueryParam("x") double x, @QueryParam("y") double y, 
        @QueryParam("r") double r, @CookieParam("access_token") String accessToken
    ) {
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

            try (Connection connection = PostgreSQLJDBC.connect()) {
                String username = claims.getSubject();

                if (Area.Validator.validateR(r) && Area.Validator.validateX(x) && Area.Validator.validateY(y)) {
                    boolean isHit = Area.Checker.hit(x, y, r);

                    Point point = new Point(x, y, r, isHit, PostgreSQLJDBC.getUserId(connection, username));
                    PostgreSQLJDBC.insertPoint(connection, point);

                    hitStatistics.increment(isHit);
                    consecutiveMisses.increment(isHit);

                    return Response.ok().entity(point.getResult()).build();
                } else {
                    String errMessage = "invalid params";
                    if (!Area.Validator.validateR(r)) errMessage = "invalid R = " + r;
                    else if (!Area.Validator.validateX(x)) errMessage = "invalid X = " + x;
                    else if (!Area.Validator.validateY(y)) errMessage = "invalid Y = " + y;
                    
                    return Response.status(Response.Status.BAD_REQUEST).entity(errMessage).build();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity("an error occurred during checking")
                            .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.UNAUTHORIZED).entity("invalid access token").build();
        }
    }
}
