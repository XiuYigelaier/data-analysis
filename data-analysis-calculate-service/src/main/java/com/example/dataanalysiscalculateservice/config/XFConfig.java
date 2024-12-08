package com.example.dataanalysiscalculateservice.config;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Data
@Component
public class XFConfig {
    @Value("${xf.config.appId}")
    private String appId;
    @Value("${xf.config.apiSecret}")
    private String apiSecret;
    @Value("${xf.config.apiKey}")
    private String apiKey;
    @Value("${xf.config.hostUrl}")
    private String hostUrl;

    @Value("${xf.config.domain}")
    private  String domain;
    @Value("${xf.config.maxResponseTime}")
    private Integer maxResponseTime;

    public String answer(String text,String uid) throws MalformedURLException, NoSuchAlgorithmException, InvalidKeyException, ExecutionException, InterruptedException, TimeoutException, ExecutionException, TimeoutException {
        String authUrl =getAuthUrl().replace("http://", "ws://").replace("https://", "wss://");
        Request request = new Request.Builder().url(authUrl).build();
        OkHttpClient client = new OkHttpClient.Builder().build();
        StringBuilder sb =new StringBuilder();
        CompletableFuture<String> messageReceived = new CompletableFuture<>();
        String body = buildBody(text,uid);
        WebSocket webSocket =client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                webSocket.send(body);
            }
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                JSONObject obj = JSON.parseObject(text);
                String str= obj.getJSONObject("payload").getJSONObject("choices").getJSONArray("text").getJSONObject(0).getString("content");
                sb.append(str);
                if(obj.getJSONObject("header").getLong("status")==2){
                    webSocket.close(1000, "Closing WebSocket connection");
                    messageReceived.complete(text); // 将收到的消息传递给 CompletableFuture
                }
            }

        } );
        String result = messageReceived.get(30, TimeUnit.SECONDS);; // 阻塞等待消息返回
        webSocket.close(1000, "Closing WebSocket connection");
        return sb.toString();
    }

    private    String buildBody(String text,String uid){
        JSONObject body =new JSONObject();

        JSONObject header =new JSONObject();
        header.put("app_id",appId);
        header.put("uid",uid);
        body.put("header",header);

        JSONObject parameter =new JSONObject();
        JSONObject chat =new JSONObject();
        chat.put("domain",domain);
        parameter.put("chat",chat);

        body.put("parameter",parameter);
         text = "{\"content\":\"" + text + "\"}";
        JSONObject history =JSONObject.parseObject(text);
        body.put("payload",history);

        JSONObject back =new JSONObject();
        back.put("role","system");
        back.put("content","请回答我关于一些xxx的内容");
        history.getJSONObject("message").getJSONArray("text").add(0,back);



        return body.toJSONString();
    }


    /**
     * 权限校验
     * @return String
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws MalformedURLException
     */
    private String getAuthUrl() throws NoSuchAlgorithmException, InvalidKeyException, MalformedURLException {
        URL url = new URL(hostUrl);
        // 时间
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        // 拼接
        String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                "GET " + url.getPath() + " HTTP/1.1";
        // System.err.println(preStr);
        // SHA256加密
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);
        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        // Base64加密
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        // System.err.println(sha);
        // 拼接
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        // 拼接地址
        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder().
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8))).
                addQueryParameter("date", date).
                addQueryParameter("host", url.getHost()).
                build();
        return httpUrl.toString();
    }




}
