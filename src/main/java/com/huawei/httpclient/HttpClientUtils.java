package com.huawei.httpclient;



import com.alibaba.excel.EasyExcel;
import org.apache.http.Consts;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hssf.record.DVALRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;


public class HttpClientUtils {

   private static   Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);


   private static final String IP_PORT = "http://localhost:8001";

    //  客户端从服务端读取数据的超时时间
    private static final int HTTP_TIMEOUT = 600;
    //  客户端与服务器建立连接的超时时间
    private static final int HTTP_CON_TIMEOUT = 300;
    //  客户端从连接池中获取连接的超时时间
    private static final int HTTP_CON_REQ_TIMEOUT = 300;
    //  路由的默认最大连接
    private static final int HTTP_MAX_PERROUTE = 500;
    //  整个连接池连接的最大值
    private static final int HTTP_MAX_TOTAL = 1000;
    private static RequestConfig defaultRequestConfig = null;
    private static CloseableHttpClient httpClient = null;

    static {
        //  创建连接池管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        //  设置socket配置
        SocketConfig socketConfig = SocketConfig.custom()
                .setTcpNoDelay(true)
                .build();
        connManager.setDefaultSocketConfig(socketConfig);
        connManager.setMaxTotal(HTTP_MAX_TOTAL);
        connManager.setDefaultMaxPerRoute(HTTP_MAX_PERROUTE);
        //  设置获取连接超时时间、建立连接超时时间、从服务端读取数据的超时时间
        defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(HTTP_TIMEOUT)
                .setConnectTimeout(HTTP_CON_TIMEOUT)
                .setConnectionRequestTimeout(HTTP_CON_REQ_TIMEOUT)
                .build();
        //  创建httpclient实例
        httpClient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();
    }


    public static String sendJsonPost(String url, String jsonBody, Map<String, String> headers){

        HttpPost httpPost = new HttpPost(IP_PORT + url);
        setHeader(headers,httpPost);
        CloseableHttpResponse response = null;
        StringEntity jsonEntity = new StringEntity(jsonBody, Consts.UTF_8);
        jsonEntity.setContentEncoding(Consts.UTF_8.name());
        jsonEntity.setContentType("application/json;charset=utf-8");
        httpPost.setEntity(jsonEntity);
        String resultString = null;
        try {
            response = httpClient.execute(httpPost);
            if (response !=null && response.getStatusLine().getStatusCode() == 200){
                resultString = EntityUtils.toString(response.getEntity(),"UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpClient != null){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultString;

    }

    public static void setHeader(Map<String, String> headers, HttpRequestBase httpMethod){
        if (headers != null && headers.size() > 0){
            for (Map.Entry<String, String> entry:headers.entrySet()
                 ) {
                httpMethod.setHeader(entry.getKey(),entry.getValue());
            }
        }
    }
}
