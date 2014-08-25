/**
 * 
 */
package com.asi.service.resource.util;

import java.util.Enumeration;
import java.util.Iterator;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * @author Rahul K
 * 
 */
public final class ExternalApiHeaderHandler {

    private final String CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;

    public HttpHeaders handleHeaderForRadarAPI(HttpHeaders headers, String authToken) {
        return getHeadersInfo(headers);
    }

    private HttpHeaders getHeadersInfo(HttpHeaders requestHeaders) {

        Iterator<String> headerIter = requestHeaders.keySet().iterator();
        HttpHeaders headers = new HttpHeaders();
        while (headerIter.hasNext()) {
            String key = headerIter.next();
            if (key != null && ( !key.equalsIgnoreCase("Content-Type") && !key.equalsIgnoreCase("Accept")
                    && !key.equalsIgnoreCase("accept-encoding"))) {
                headers.add(key, requestHeaders.getFirst(key));
            }
        }
        headers.add("Content-Type", CONTENT_TYPE);
        headers.add("Accept", CONTENT_TYPE);

        return headers;
    }
}
