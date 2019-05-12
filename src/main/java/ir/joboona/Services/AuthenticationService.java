package ir.joboona.Services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import ir.joboona.Models.User;
import ir.joboona.Repositories.UserRepository;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Optional;

public class AuthenticationService {

    private final String TOKEN_TYPE = "JWT";
    private final String TOKEN_ISSUER = "joboonja.com";
    private final String TOKEN_PREFIX = "Bearer ";
    private final String JWT_SECRET = "n2r5u8x/A%D*G-KaPdSgVkYp3s6v9y$B&E(H+MbQeThWmZq4t7w!z%C*F-J@NcRf";

    private static AuthenticationService instance;

    private UserRepository userRepository = UserRepository.getInstance();

    private AuthenticationService() {
    }

    public static AuthenticationService getInstance() {
        if (instance == null)
            instance = new AuthenticationService();
        return instance;
    }

    public String generateJWT_Token(User user){

        byte[] signingKey = JWT_SECRET.getBytes();

        return TOKEN_PREFIX + Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS256)
                .setHeaderParam("typ", TOKEN_TYPE)
                .setIssuer(TOKEN_ISSUER)
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 864000000))
                .compact();
    }

    public Optional<User> getPrincipal(String token) throws Exception {
        byte[] signingKey = JWT_SECRET.getBytes();

        Jws<Claims> parsedToken = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""));

        String username = parsedToken.getBody().getSubject();

        if (StringUtils.isNotEmpty(username))
            return userRepository.findUserByUsername(username);
        return Optional.empty();
    }
}
