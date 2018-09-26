package no.kristiania.prg200.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpQuery {

    private Map<String, String> parameters = new LinkedHashMap<>();

    public HttpQuery(String query) {
        for(String parameter : query.split("&")){
            String key = urlDecode(parameter.split("=", 2)[0]);
            String value = urlDecode(parameter.split("=", 2)[1]);

            parameters.put(key,value);
        }
    }

    public String getParameter(String paramName) {
        return parameters.get(paramName);
    }

    public void addParameter(String key, String value) {
        parameters.put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, String> param : parameters.entrySet()) {
            if(sb.length() > 0) sb.append("&");
            sb.append(urlEncode(param.getKey())).append("=").append(urlEncode(param.getValue()));
        }
        return sb.toString();
    }

    private String urlDecode(String substring) {
        try {
            return URLDecoder.decode(substring, "ISO-8859-1");
        } catch(UnsupportedEncodingException e) {
            throw new RuntimeException(" ", e);
        }
    }

    private String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "ISO-8859-1");
        } catch(UnsupportedEncodingException e) {
            throw new RuntimeException("", e);
        }
    }
}
