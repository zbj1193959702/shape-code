package com.biji.puppeteer.util;

import com.google.common.collect.Lists;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    public static String get(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        return HttpUtil.executeGet(httpGet);
    }

    public static String getHttps(String url) throws Exception {
        URL console = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) console.openConnection();
        if (conn instanceof HttpsURLConnection) {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new SecureRandom());
            ((HttpsURLConnection) conn).setSSLSocketFactory(sc.getSocketFactory());
            ((HttpsURLConnection) conn).setHostnameVerifier(new TrustAnyHostnameVerifier());
        }
        conn.setRequestMethod("GET");
        conn.connect();
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = conn.getInputStream();
            return convertStream2String(inputStream);
        }
        return null;

    }

    private static String convertStream2String(InputStream input) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(input,"utf-8"));
            StringBuilder resultBuffer = new StringBuilder();
            String tempLine;
            while ((tempLine = br.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
            return resultBuffer.toString();
        } catch (Exception e) {
            return "";
        }
    }


    public static String getWithAuth(String url, String token) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Authorization", token);
        return HttpUtil.executeGet(httpGet);
    }

    private static String executeGet(HttpGet httpGet) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);

            if (httpResponse.getEntity() == null)
                return null;

            return EntityUtils.toString(httpResponse.getEntity());
        } finally {
            if (httpResponse != null)
                httpResponse.close();

            if (httpClient != null)
                httpClient.close();
        }
    }

    public static String buildUrl(String scheme, String host, Map<String, String> params) {
        List<NameValuePair> qparams = Lists.newArrayList();
        params.forEach((k, v) -> qparams.add(new BasicNameValuePair(k, v)));
        URIBuilder builder = new URIBuilder().setScheme(scheme).setHost(host).setParameters(qparams);
        return builder.toString();
    }

    public static String post(String url, Map<String, String> paramsMap) throws IOException {
        return post(url, paramsMap, null);
    }

    public static String post(String url, Map<String, String> paramsMap, Map<String, String> headerMap) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;

        try {
            HttpPost method = new HttpPost(url);
            if (paramsMap != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> param : paramsMap.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
                    paramList.add(pair);
                }
                method.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            }
            if (!CollectionUtils.isEmpty(headerMap)) {
                for (Map.Entry<String, String> param : headerMap.entrySet()) {
                    method.addHeader(param.getKey(), param.getValue());
                }
            }
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity, "UTF-8");
            }
        } finally {
            if (response != null)
                response.close();
        }

        return responseText;
    }

    public static String post(String url, String params) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;

        try {
            HttpPost method = new HttpPost(url);
            method.setEntity(new StringEntity(params, "UTF-8"));
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
        } finally {
            if (response != null)
                response.close();
        }

        return responseText;
    }

    public static byte[] postForImage(String url, String params) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        byte[] responseBytes = null;
        CloseableHttpResponse response = null;

        try {
            HttpPost method = new HttpPost(url);
            method.setEntity(new StringEntity(params, "UTF-8"));
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseBytes = EntityUtils.toByteArray(entity);
            }
        } finally {
            if (response != null)
                response.close();
        }
        return responseBytes;
    }

    private static class TrustAnyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
