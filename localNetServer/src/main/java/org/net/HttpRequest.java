package org.net;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public abstract class HttpRequest {

    public HttpResponse methodGetRequest(String url, Map<String, String> headers) throws IOException, InterruptedException {
        HttpResponse httpResponse;
        HttpClient httpClient;

        httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(20*1000); // Timeout 20s

        GetMethod getMethod = new GetMethod(url);
        //getMethod.getParams().setContentCharset("UTF-8");
        JSONObject jsonObject = new JSONObject(headers);
        for(String key: jsonObject.keySet()) {
            getMethod.setRequestHeader(key, jsonObject.getString(key));
        }

        // Set 60s timeout for the GET request.
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60*1000);
        // Retry after 3 seconds
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, true));
        httpClient.executeMethod(getMethod);
        int stateCode = getMethod.getStatusCode();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream()))) {
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line=br.readLine()) != null) {
                stringBuffer.append(line);
            }
            String resp = stringBuffer.toString();
            System.out.println(String.format("[%d] GET: %s", stateCode, url));
            httpResponse = new HttpResponse(stateCode, resp);
        }
        Thread.sleep(100);
        return httpResponse;
    }


    public HttpResponse methodPostRequest(String url, Map<String, String> headers, String body) throws IOException, InterruptedException {
        HttpResponse httpResponse;
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(20*1000); // Timeout 20s
        PostMethod postMethod = new PostMethod(url);
        postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60*1000);
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, true));

        JSONObject jsonObject = new JSONObject(headers);
        for(String key: jsonObject.keySet()) {
            postMethod.setRequestHeader(key, jsonObject.getString(key));
        }

        RequestEntity re = new StringRequestEntity(body, "application/json", "UTF-8");
        postMethod.setRequestEntity(re);

        httpClient.executeMethod(postMethod);
        int stateCode = postMethod.getStatusCode();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()))) {
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line=br.readLine()) != null) {
                stringBuffer.append(line);
            }
            String resp = stringBuffer.toString();
            System.out.println(String.format("[%d] POST: %s", stateCode, url));
            httpResponse =  new HttpResponse(stateCode, resp);
        }

        Thread.sleep(100);
        return httpResponse;
    }

    public int delay(long millis) {
        try {
            Thread.sleep(millis);
            return 0;
        } catch (InterruptedException e) {
            System.err.println(e);
            return 1;
        }

    }


}
