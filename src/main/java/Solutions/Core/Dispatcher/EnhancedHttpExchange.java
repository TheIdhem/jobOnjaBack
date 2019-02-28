package Solutions.Core.Dispatcher;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class EnhancedHttpExchange extends HttpExchange {

    private HttpExchange decorated;

    public EnhancedHttpExchange(HttpExchange decorated) {
        this.decorated = decorated;
    }

    public Map<String, String> getQueryParams() {

        Map<String, String> result = new HashMap<>();
        if (decorated.getRequestURI().getQuery() == null)
            return result;

        for (String param : decorated.getRequestURI().getQuery().split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1)
                result.put(entry[0], entry[1]);
            else
                result.put(entry[0], null);

        }
        return result;
    }

    public String getQueryParam(String key) {
        return getQueryParams().get(key);
    }

    @Override
    public Headers getRequestHeaders() {
        return decorated.getRequestHeaders();
    }

    @Override
    public Headers getResponseHeaders() {
        return decorated.getResponseHeaders();
    }

    @Override
    public URI getRequestURI() {
        return decorated.getRequestURI();
    }

    @Override
    public String getRequestMethod() {
        return decorated.getRequestMethod();
    }

    @Override
    public HttpContext getHttpContext() {
        return decorated.getHttpContext();
    }

    @Override
    public void close() {
        decorated.close();
    }

    @Override
    public InputStream getRequestBody() {
        return decorated.getRequestBody();
    }

    @Override
    public OutputStream getResponseBody() {
        return decorated.getResponseBody();
    }

    @Override
    public void sendResponseHeaders(int i, long l) throws IOException {
        decorated.sendResponseHeaders(i, l);
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return decorated.getRemoteAddress();
    }

    @Override
    public int getResponseCode() {
        return decorated.getResponseCode();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return decorated.getLocalAddress();
    }

    @Override
    public String getProtocol() {
        return decorated.getProtocol();
    }

    @Override
    public Object getAttribute(String s) {
        return decorated.getAttribute(s);
    }

    @Override
    public void setAttribute(String s, Object o) {
        decorated.setAttribute(s, o);
    }

    @Override
    public void setStreams(InputStream inputStream, OutputStream outputStream) {
        decorated.setStreams(inputStream, outputStream);
    }

    @Override
    public HttpPrincipal getPrincipal() {
        return decorated.getPrincipal();
    }
}
