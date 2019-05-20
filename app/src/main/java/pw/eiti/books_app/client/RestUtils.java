package pw.eiti.books_app.client;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class RestUtils {

    public static RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList((ClientHttpRequestInterceptor)new StatefulRestTemplateInterceptor()));
        return restTemplate;
    }

}
