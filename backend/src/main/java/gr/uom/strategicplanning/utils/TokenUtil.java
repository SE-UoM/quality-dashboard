package gr.uom.strategicplanning.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.List;

public class TokenUtil {
    public static DecodedJWT getDecodedJWTfromToken(String token){
        String refreshToken = token.substring("Bearer ".length());
        Algorithm algorithm = TokenUtil.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refreshToken);
        return decodedJWT;
    }

    public static Algorithm getAlgorithm(){
        return Algorithm.HMAC256("secret".getBytes());
    }

    public static String generateAccessToken(String email, StringBuffer requestURL, String id, String name, List<String> collect) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() +7*60*60*1000))
                .withIssuer(requestURL.toString())
                .withClaim("id", id)
                .withClaim("name", name)
                .withClaim("roles", collect)
                .sign(TokenUtil.getAlgorithm());
    }

    public static String generateRefreshToken(String username, StringBuffer requestURL) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() +8*60*60*1000))
                .withIssuer(requestURL.toString())
                .sign(TokenUtil.getAlgorithm());
    }

}
