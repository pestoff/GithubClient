package dev.colibri.githubclienttest.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.net.Uri;
import android.support.annotation.NonNull;

import org.json.JSONException;

import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.network.JsonParser;

public class HttpClient {
    private static final String REPOSITORY_SEARCH_URL = "https://api.github.com/search/repositories";
    private static final String REPOS_URL = "https://api.github.com/repos";
    private static final String QUERY_PARAM = "q";
    private final JsonParser jsonParser = new JsonParser();


    public Repository getRepository(String repoName, String userLogin) throws IOException, JSONException {
        String requestUrl = Uri.parse(REPOS_URL)
                               .buildUpon()
                               .appendPath(userLogin)
                               .appendPath(repoName)
                               .build()
                               .toString();
        String response = getResponse(requestUrl);

        return jsonParser.getRepository(response);
    }

    public ArrayList<Repository> getRepositories(String query) throws IOException, JSONException {
        String requestUrl = Uri.parse(REPOSITORY_SEARCH_URL)
                               .buildUpon()
                               .appendQueryParameter(QUERY_PARAM, query)
                               .build()
                               .toString();

        String response = getResponse(requestUrl);

        return jsonParser.getRepositories(response);
    }

    @NonNull
    private String getResponse(String requestUrl) throws IOException {
        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            connection.connect();

            InputStream in;
            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                in = connection.getInputStream();
            }
            else {
                in = connection.getErrorStream();
            }

            String response = convertStreamToString(in);

            return response;

        } finally {
            connection.disconnect();
        }
    }

    private String convertStreamToString(InputStream stream) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        stream.close();

        return sb.toString();
    }
}