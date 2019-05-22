package pw.eiti.books_app.client;

import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RestUtils {

    public static final String SERVICE_URL = "https://wpam-library-backend.herokuapp.com";


    public static RestTemplate getRestTemplate() {
        HttpMessageConverter<MultiValueMap<String, ?>> formHttpMessageConverter = new FormHttpMessageConverter();

        HttpMessageConverter<String> stringHttpMessageConverter = new StringHttpMessageConverter();

        List<HttpMessageConverter<?>> messageConverters = new LinkedList<>();

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));

        messageConverters.add(formHttpMessageConverter);
        messageConverters.add(stringHttpMessageConverter);
        messageConverters.add(converter);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(messageConverters);
        restTemplate.setInterceptors(Collections.singletonList((ClientHttpRequestInterceptor)new StatefulRestTemplateInterceptor()));

        return restTemplate;
    }

}
