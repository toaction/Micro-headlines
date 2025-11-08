package com.action.headline.util;



import com.alibaba.druid.util.StringUtils;
import io.jsonwebtoken.*;

import java.util.Date;

public class JwtUtil {
    private static long tokenExpiration = 1000 * 60 * 60;
    private static String tokenSignKey = "micro-headlines";

    public static String createToken(Integer userId) {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .claim("userId", userId)
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
    }

    public static Integer getUserId(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(tokenSignKey)
                    .parseClaimsJws(token)
                    .getBody();
            return (Integer) claims.get("userId");
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean validateToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        try {
            Date expiration = Jwts.parser()
                    .setSigningKey(tokenSignKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            return expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
