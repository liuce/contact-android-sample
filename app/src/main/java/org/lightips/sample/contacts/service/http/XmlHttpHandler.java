package org.lightips.sample.contacts.service.http;

import android.content.Context;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuce on 10/22/14.
 */
public class XmlHttpHandler extends AbstractHttpHandler {

    public XmlHttpHandler(Context context) {
        super(context);
    }

    @Override
    public RestTemplate createRestTemplate() {
        HttpHeaders requestHeaders = new HttpHeaders();
        List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
        acceptableMediaTypes.add(MediaType.APPLICATION_XML);
        requestHeaders.setAccept(acceptableMediaTypes);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());
        return  restTemplate;
    }

    @Override
    public String getContext() {
        return null;
    }
}
