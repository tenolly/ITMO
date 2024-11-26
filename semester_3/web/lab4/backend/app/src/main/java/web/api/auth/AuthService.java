package web.api.auth;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import web.api.database.PostgreSQLJDBC;
import web.api.database.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import javax.crypto.SecretKey;

@Path("/auth")
public class AuthService {
    private static final SecretKey SECRET_KEY_SPEC = AuthConfig.getSecretKeySpec();
    private static final int ACCESS_TOKEN_EXPIRATION = 2 * 60 * 1000; // 2 минуты
    private static final int REFRESH_TOKEN_EXPIRATION = 24 * 60 * 60 * 1000; // 1 день

    @POST
    @Path("/register")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response registerUser(User user) {
        if (user.getUsername().length() < 6 || user.getUsername().length() > 18) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("username must be between 6 and 18 characters")
                           .build();
        }
    
        if (user.getPassword().length() < 6 || user.getPassword().length() > 18) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("password must be between 6 and 18 characters")
                           .build();
        }

        try (Connection connection = PostgreSQLJDBC.connect()) {
            if (PostgreSQLJDBC.usernameExists(connection, user.getUsername())) {
                return Response.status(Response.Status.BAD_REQUEST).entity("user already exists").build();
            }

            if (PostgreSQLJDBC.registerUser(connection, user.getUsername(), user.getPassword())) {
                return Response.ok().entity("success").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("username is taken").build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("an error occurred during registration")
                           .build();
        }
    }

    @POST
    @Path("/login")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response login(User user) {
        try (Connection connection = PostgreSQLJDBC.connect()) {
            if (PostgreSQLJDBC.checkPassword(connection, user)) {
                String accessToken = generateToken(user.getUsername(), ACCESS_TOKEN_EXPIRATION);
                String refreshToken = generateToken(user.getUsername(), REFRESH_TOKEN_EXPIRATION);

                NewCookie accessCookie = new NewCookie.Builder("access_token")
                    .value(accessToken).path("/").maxAge(ACCESS_TOKEN_EXPIRATION).httpOnly(true)
                    .sameSite(NewCookie.SameSite.STRICT).build();

                NewCookie refreshCookie = new NewCookie.Builder("refresh_token")
                    .value(refreshToken).path("/").maxAge(REFRESH_TOKEN_EXPIRATION).httpOnly(true)
                    .sameSite(NewCookie.SameSite.STRICT).build();

                return Response.ok().cookie(accessCookie, refreshCookie).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity("invalid credentials").build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("an error occurred during registration")
                           .build();
        }
    }

    @POST
    @Path("/logout")
    public Response logout() {
        NewCookie accessCookie = new NewCookie.Builder("access_token")
            .value("").path("/").maxAge(0).httpOnly(true)
            .sameSite(NewCookie.SameSite.STRICT).build();

        NewCookie refreshCookie = new NewCookie.Builder("refresh_token")
            .value("").path("/").maxAge(0).httpOnly(true)
            .sameSite(NewCookie.SameSite.STRICT).build();
    
        return Response.ok().cookie(accessCookie, refreshCookie).build();
    }

    @POST
    @Path("/access")
    public Response getUsername(@CookieParam("access_token") String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("no access token").build();
        }

        try {
            Claims claims = validateToken(accessToken);
            String username = claims.getSubject();

            return Response.ok().entity(username).build();
        } catch (ExpiredJwtException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("access token expired").build();
        } catch (JwtException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("invalid access token").build();
        }
    }

    @POST
    @Path("/refresh")
    public Response refreshToken(@CookieParam("refresh_token") String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("no refresh token").build();
        }

        try {
            Claims claims = validateToken(refreshToken);
            String username = claims.getSubject();

            String newAccessToken = generateToken(username, ACCESS_TOKEN_EXPIRATION);
            String newRefreshToken = generateToken(username, REFRESH_TOKEN_EXPIRATION);

            NewCookie newAccessCookie = new NewCookie.Builder("access_token")
                .value(newAccessToken).path("/").maxAge(ACCESS_TOKEN_EXPIRATION).httpOnly(true)
                .sameSite(NewCookie.SameSite.STRICT).build();

            NewCookie newRefreshCookie = new NewCookie.Builder("refresh_token")
                .value(newRefreshToken).path("/").maxAge(REFRESH_TOKEN_EXPIRATION).httpOnly(true)
                .sameSite(NewCookie.SameSite.STRICT).build();

            return Response.ok().cookie(newAccessCookie, newRefreshCookie).build();
        } catch (ExpiredJwtException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("refresh token expired").build();
        } catch (JwtException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("invalid refresh token").build();
        }
    }

    private String generateToken(String username, long expirationTime) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SECRET_KEY_SPEC)
                .compact();
    }

    private Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY_SPEC)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
