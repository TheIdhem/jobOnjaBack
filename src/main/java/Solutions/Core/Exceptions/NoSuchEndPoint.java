package Solutions.Core.Exceptions;

import Solutions.Core.Dispatcher.RequestMethod;

public class NoSuchEndPoint extends RuntimeException {

    private RequestMethod method;
    private String requestUri;

    public NoSuchEndPoint(){}

    public NoSuchEndPoint(RequestMethod method, String requestUri) {
        this.method = method;
        this.requestUri = requestUri;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public String getRequestUri() {
        return requestUri;
    }
}
