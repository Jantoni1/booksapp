package pw.eiti.books_app.client;

import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;

public class StatefulRestTemplateInterceptor implements ClientHttpRequestInterceptor {
    private static String sessionId;

    public StatefulRestTemplateInterceptor() {
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        if (sessionId != null) {
            request.getHeaders().add("JSESSIONID", sessionId);
        }

        ClientHttpResponse response = execution.execute(request, body);

        if (sessionId == null) {
//            sessionId = response.getHeaders()
//                    .getFirst("Set-Cookie")
//                    .replaceFirst(".*?=", "")
//                    .replaceFirst(";.*", "");
        }
        return response;
    }
}