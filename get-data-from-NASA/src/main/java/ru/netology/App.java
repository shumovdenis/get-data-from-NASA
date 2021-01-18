package ru.netology;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class App{
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main( String[] args ) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=80mxp2renKO7fZCzchAG5xYmnz0iALkrH7g35ypX");
        CloseableHttpResponse response = httpClient.execute(request);
        Data data = mapper.readValue(response.getEntity().getContent(), new TypeReference<Data>() {});
        String fileName = data.getHdurl().substring(data.getHdurl().lastIndexOf('/')+1);
        try (InputStream in = new URL(data.getHdurl()).openStream()) {
            Files.copy(in
                    ,Paths.get(fileName)
                    , StandardCopyOption.REPLACE_EXISTING);
        }

    }
}
