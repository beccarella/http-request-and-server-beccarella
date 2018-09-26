package no.kristiania.prg200.http;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpPathTest {

    @Test
    public void shouldFindParameters() {
        HttpPath path = new HttpPath("/echo?status=200");
        HttpQuery query = path.getQuery();
        assertThat(query.toString()).isEqualTo("status=200");
        assertThat(query.getParameter("status"))
            .isEqualTo("200");
    }

    @Test
    public void shouldFindMoreParameters() {
        HttpPath path = new HttpPath("/echo?status=404&body=Hello");
        HttpQuery query = path.getQuery();
        assertThat(query.toString()).isEqualTo("status=404&body=Hello");
        assertThat(query.getParameter("status"))
            .isEqualTo("404");
        assertThat(query.getParameter("body"))
            .isEqualTo("Hello");
    }

    @Test
    public void shouldUrlDecodeParameters() {
        String query = "status=307&Location=http%3A%2F%2Fwww.kristiania.no";
        HttpPath path = new HttpPath("/echo?" + query);
        assertThat(path.getQuery().getParameter("Location"))
            .isEqualTo("http://www.kristiania.no");
        assertThat(path.getQuery().toString())
            .isEqualTo(query);
    }

    @Test
    public void shouldSeparatePathAndQuery() {
        HttpPath path = new HttpPath("/urlecho?status=200");
        assertThat(path.getPath()).isEqualTo("/urlecho");
        assertThat(path.getQuery().toString()).isEqualTo("status=200");
        assertThat(path.toString()).isEqualTo("/urlecho?status=200");
        assertThat(path.getQuery().getParameter("status")).isEqualTo("200");
    }

    @Test
    public void shouldHandleUriWithoutQuery() {
        HttpPath path = new HttpPath("/myapp/echo");
        assertThat(path.getPath()).isEqualTo("/myapp/echo");
        assertThat(path.getQuery()).isNull();
        assertThat(path.toString()).isEqualTo("/myapp/echo");
    }

    @Test
    public void shouldReadParameters() {
        HttpPath path = new HttpPath("?status=403&body=mer+bl%E5b%E6r+%26+jordb%E6r");
        assertThat(path.getQuery().getParameter("status")).isEqualTo("403");
        assertThat(path.getQuery().getParameter("body")).isEqualTo("mer blåbær & jordbær");
    }

    @Test
    public void shouldCreateQuery() {
        HttpQuery query = new HttpQuery("status=200");
        query.addParameter("body", "mer blåbær");
        assertThat(query.toString()).isEqualTo("status=200&body=mer+bl%E5b%E6r");
    }


    @Test
    public void shouldParseUrl() {
        HttpPath path = new HttpPath("/myapp/echo?status=402&body=vi%20plukker%20bl%C3%A5b%C3%A6r");
                assertThat(path.getPath()).isEqualTo("/myapp/echo");
        assertThat(path.getPathParts()).containsExactly("myapp", "echo");
        assertThat(path.getQuery().getParameter("status")).isEqualTo("402");
        assertThat(path.getQuery().getParameter("body")).isEqualTo("vi plukker blåbær");
    }


}
