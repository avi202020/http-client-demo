package astro;

import astro.json.AstroResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.ExecutionException;

public class AstroClient {
    private HttpClient client = HttpClient.newBuilder()
                                          .version(HttpClient.Version.HTTP_2)
                                          .connectTimeout(Duration.ofSeconds(2))
                                          .build();

    private Gson gson = new Gson();

    private HttpRequest buildRequest() {
        String astroUrl = "http://api.open-notify.org/astros.json";
        return HttpRequest.newBuilder()
                          .uri(URI.create(astroUrl))
                          .GET()
                          .build();
    }

    public AstroResponse getSync() throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(
                buildRequest(),
                HttpResponse.BodyHandlers.ofString());
        return getResponse(response.body());
    }


    public AstroResponse getAsync() throws ExecutionException, InterruptedException {
        String json = client.sendAsync(buildRequest(),
                                HttpResponse.BodyHandlers.ofString())
                     .thenApply(HttpResponse::body)
                     .get();
        return getResponse(json);
    }


    private AstroResponse getResponse(String json) {
        return gson.fromJson(json, AstroResponse.class);
    }
}
