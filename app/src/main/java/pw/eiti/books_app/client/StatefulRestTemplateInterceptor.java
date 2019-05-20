package pw.eiti.books_app.client;

import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.*;

import java.io.IOException;

public class StatefulRestTemplateInterceptor implements ClientHttpRequestInterceptor {
    private static String sessionId;

    public StatefulRestTemplateInterceptor() {
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        if (sessionId != null) {
            request.getHeaders().add("cookie", sessionId);
        }

        ClientHttpResponse response = execution.execute(request, body);

        if (sessionId == null && response.getStatusCode() == HttpStatus.OK) {
            sessionId = response.getHeaders().getFirst("Set-Cookie");
        }
        return response;
    }
}