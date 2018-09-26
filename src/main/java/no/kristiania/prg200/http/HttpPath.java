package no.kristiania.prg200.http;

public class HttpPath {

    private String path;

    public HttpPath(String path) {
        this.path = path;
    }

    public HttpQuery getQuery() {
        return getQuestionPos() != -1 ? new HttpQuery(path.substring(getQuestionPos() + 1)) : null;
    }

    private int getQuestionPos() {
        return path.indexOf("?");
    }

    public String getPath() {
        if(getQuestionPos() != -1) {
            return path.substring(0, getQuestionPos());
        }
        return path;
    }

    public String toString() {
        HttpQuery query = getQuery();
        return getPath() + (query != null ?  "?" + query : "");
    }


    public String[] getPathParts() {
        int endPos = path.indexOf('?');
        String[] parts = path.substring(1, endPos).split("/");
        return parts;
    }

}
