package uz.urinov.stadium.util;

import io.jsonwebtoken.*;
import uz.urinov.stadium.Profile.enums.ProfileRole;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

public class JWTUtil {
    private static final int tokenLiveTimeDay = 1000 * 3600 * 24; // 1-day
    private static final long tokenLiveTimeMonth = 1000 * 3600 * 24 * 30; // 1-day
    private static final String secretKey = "very_long_mazgiskjdh2skjdhadasdasg7fgdfgdfd213131321515451121231321231231231312231545412123154545122154512205454";

    public static String encodeDay(String profileId, String username, ProfileRole role) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.issuedAt(new Date());

        SignatureAlgorithm sa = SignatureAlgorithm.HS512;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), sa.getJcaName());

        jwtBuilder.signWith(secretKeySpec);

        jwtBuilder.claim("id", profileId);
        jwtBuilder.claim("username", username);
        jwtBuilder.claim("role", role);
        jwtBuilder.claim("refreshToken", false);

        jwtBuilder.expiration(new Date(System.currentTimeMillis() + (tokenLiveTimeDay)));
        jwtBuilder.issuer("Stadium");
        return jwtBuilder.compact();
    }

    public static String encodeMonth(String profileId, String username, ProfileRole role) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.issuedAt(new Date());

        SignatureAlgorithm sa = SignatureAlgorithm.HS512;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), sa.getJcaName());

        jwtBuilder.signWith(secretKeySpec);

        jwtBuilder.claim("id", profileId);
        jwtBuilder.claim("username", username);
        jwtBuilder.claim("role", role);
        jwtBuilder.claim("refreshToken", true);

        jwtBuilder.expiration(new Date(System.currentTimeMillis() + (tokenLiveTimeMonth)));
        jwtBuilder.issuer("Stadium");
        return jwtBuilder.compact();
    }

    public static JwtDTO decode(String token) {
        SignatureAlgorithm sa = SignatureAlgorithm.HS512;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), sa.getJcaName());
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(secretKeySpec)
                .build();

        Jws<Claims> jws = jwtParser.parseSignedClaims(token);
        Claims claims = jws.getPayload();

        String id = (String) claims.get("id");
        String username = (String) claims.get("username");
        String role = (String) claims.get("role");
        boolean refreshToken = (boolean) claims.get("refreshToken");
        if (role != null) {
            ProfileRole profileRole = ProfileRole.valueOf(role);
            return new JwtDTO(id, username, profileRole,refreshToken);
        }
        return new JwtDTO(id);
    }

}
