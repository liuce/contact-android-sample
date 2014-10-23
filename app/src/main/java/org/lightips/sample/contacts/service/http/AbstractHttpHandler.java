package org.lightips.sample.contacts.service.http;

import android.content.Context;
import android.util.Log;

import org.lightips.sample.contacts.R;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by liuce on 10/22/14.
 */
public abstract class AbstractHttpHandler {

    protected static final String TAG = AbstractHttpHandler.class.getSimpleName();

    private String serverUrl;
    private String partUrl;



    public AbstractHttpHandler(Context context) {
        serverUrl = context.getString(R.string.base_uri);
    }

    public HttpEntity<?> createRequestEntity(HttpHeaders requestHeaders){
        return new HttpEntity<Object>(requestHeaders);
    }

    public abstract RestTemplate createRestTemplate();
    public abstract String getContext();

    public String getUrl() {
        return serverUrl + this.getContext() + this.getPartUrl();
    }

    public String getPartUrl() {
        return partUrl;
    }

    public void setPartUrl(String partUrl) {
        this.partUrl = partUrl;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }



    public <T> T getForObject(Class<T> responseType, Object... urlVarialbes) {
        String url = this.getUrl();
        Log.d(TAG, url);
        RestTemplate rest = this.createRestTemplate();
        T result = null;
        try {
            result = rest.getForObject(url, responseType, urlVarialbes);
        } catch (RestClientException e) {
            throw new RuntimeException("rest request exception",e);
        }
        return result;
    }


    public <T> T postForObject(Object request, Class<T> responseType, Object... uriVariables){
        String url = this.getUrl();
        RestTemplate rest = this.createRestTemplate();
        T result;
        try {
            result = rest.postForObject(url, request, responseType, uriVariables);
        } catch (RestClientException e) {
            throw new RuntimeException("request exception",e);
        }
        return result;
    }
}
