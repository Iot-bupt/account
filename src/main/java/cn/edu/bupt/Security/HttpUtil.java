package cn.edu.bupt.Security;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/12/23.
 * 在启动的时候不能使用
 */
@Component
public class HttpUtil {


    @Value("${account.check_url}")
    private void getCheck(String checkUrl) {
        checkurl = checkUrl ;
    }

    @Value("${account.client_id}")
    private void getClientId(String client_id) {
        Client_id = client_id ;
    }

    @Value("${account.client_secret}")
    private void getClientSecret(String client_secret) {
        Client_secret = client_secret ;
    }

    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final OkHttpClient httpClient = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType FORM = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    private static String checkurl;
    private static String Client_id;
    private static String Client_secret;


    public static String checkToken(String token){
        String content = "token="+token;
        RequestBody body = RequestBody.create(FORM, content);
        Request.Builder builder = new Request.Builder()
                .url(checkurl)
                .post(body);

        byte[] textByte = (Client_id+":"+Client_secret).getBytes();
        String auth = encoder.encodeToString(textByte);
        builder.header("Authorization","Basic "+auth);

        Request request = builder.build();
        try{
        Response response = execute(request);
        if(response.isSuccessful()){
            String result = response.body().string();
            response.close();
            return result;
        }else{
            throw new Exception("fail to check token!") ;
          }
        }catch (Exception e){
            return "{\"error\":\"Invalid token!\"}";
        }
    }

    /**
     * 同步方法
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException {
        return mOkHttpClient.newCall(request).execute() ;
    }

    private static final OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build();
}
