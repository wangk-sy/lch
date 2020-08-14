package utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wangk.config.ShiroProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @ClassName :JwtUtils
 * @Description :TODO
 * @Author :16388
 * @Date :2020/6/9 10:59
 * @Version :1.0
 **/
@RequiredArgsConstructor
@Component
public class JwtUtils {

    @Autowired
    private static  ShiroProperties shiroProperties;

    /*
    * @MethodName:verify
    * @Description:确认token是否正确
    * @Param:[token]
    * @Return:void
    * @Date:2020/6/10
    * @Author:wangk
     */
    public static boolean verify(String token){
        try {
            String secret = getClaim(token,"wangk")+ Base64Util.encodeThrowsException(shiroProperties.getBase64Secret());
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
    * @MethodName:getClaim
    * @Description:获得Token中的信息无需secret解密也能获得
    * @Param:[token, claim]
    * @Return:java.lang.String
    * @Date:2020/6/10
    * @Author:wangk
     */
    public static String getClaim(String token,String claim){
        try {
            DecodedJWT decode = JWT.decode(token);
            return decode.getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String createToekn(String account,String currentTime){
        //加密
        try {
            String secret = account + Base64Util.encodeThrowsException(shiroProperties.getBase64Secret());
            Date date = new Date(System.currentTimeMillis() + shiroProperties.getTokenValidityInSeconds() * 1000);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create().withClaim("account", account).withClaim("currentTimeMillis", currentTime)
                    .withExpiresAt(date).sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
