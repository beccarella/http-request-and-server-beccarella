package no.kristiania.prg200.http;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpServerTest {

    private static HttpServer server;

    @BeforeClass
    public static void startServer() throws IOException {
        server = new HttpServer(0);
    }

    @Test
    public void shouldWriteStatusCode() throws IOException {
        HttpRequest request = new HttpRequest("localhost", server.getPort(), "/echo?status=200");
        HttpResponse response = request.execute();

        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test
    public void shouldReadOtherStatusCodes() throws IOException {
        HttpRequest request = new HttpRequest("localhost", server.getPort(), "/echo?status=404");
        HttpResponse response = request.execute();

        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    @Test
    public void shouldReadResponseHeaders() throws IOException {
        HttpRequest request = new HttpRequest("localhost", server.getPort(),
                "/echo?status=307&Location=http%3A%2F%2Fwww.kristiania.no");
        HttpResponse response = request.execute();

        assertThat(response.getStatusCode()).isEqualTo(307);
        assertThat(response.getHeader("Location")).isEqualTo("http://www.kristiania.no");
    }

    @Test
    public void shouldReadResponseBody() throws IOException {
        HttpRequest request = new HttpRequest("localhost", server.getPort(),
                "/echo?body=Hello+world!");
        HttpResponse response = request.execute();

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Hello world!");
    }
}
