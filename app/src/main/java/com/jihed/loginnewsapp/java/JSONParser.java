package com.jihed.loginnewsapp.java;

import android.content.Entity;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Jihed on 09.01.2016.
 */
public class JSONParser {
    public JSONObject makeHttpRequest(String url , List<NameValuePair> pairs) {

        JSONObject jsonObjectResult = new JSONObject();

        try {
            HttpParams httpParams = new BasicHttpParams();


            int timeOutConnection = 10000;
            HttpConnectionParams.setConnectionTimeout(httpParams, timeOutConnection);
            int timeOutSocket = 10000;
            HttpConnectionParams.setConnectionTimeout(httpParams,timeOutSocket);


            DefaultHttpClient defaultHttpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(url);

            if (pairs != null) {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs));
            }
            HttpResponse httpResponse = defaultHttpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();

            if (httpEntity != null) {
                jsonObjectResult = new JSONObject(EntityUtils.toString(httpEntity));
                return jsonObjectResult;

                /*String resultString = EntityUtils.toString(httpEntity);
                jsonObjectResult = new JSONObject(resultString.substring(resultString.indexOf("("), resultString.lastIndexOf(")") + 1));
                return jsonObjectResult;*/
            }
        } catch (Exception ex) {
            Log.e("lognew", "exception: " + ex.getMessage());
            ex.printStackTrace();
        }

        return jsonObjectResult;
    }
}
