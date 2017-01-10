package main;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Created by annaig on 08/01/17.
 */
public class HttpRequest {

    public HttpResponse sendGet(String url) throws Exception {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();;
        HttpGet request = new HttpGet(url);
        HttpResponse response = httpClient.execute(request);
        return response;
    }
}
