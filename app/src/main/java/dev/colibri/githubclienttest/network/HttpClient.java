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

import com.google.gson.Gson;

import org.json.JSONException;

import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.network.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpClient {
    private static final String REPOSITORY_SEARCH_URL = "https://api.github.com/search/repositories";
    private static final String REPOS_URL = "https://api.github.com/repos";
    private static final String QUERY_PARAM = "q";
    private final JsonParser jsonParser = new JsonParser(new Gson());
    OkHttpClient client;

    public HttpClient(OkHttpClient client) {
        this.client = client;
    }

    public Repository getRepository(String repoName, String userLogin) throws IOException {
        String requestUrl = Uri.parse(REPOS_URL)
                               .buildUpon()
                               .appendPath(userLogin)
                               .appendPath(repoName)
                               .build()
                               .toString();
        String response = getResponse(requestUrl);

        return jsonParser.getRepository(response);
    }

    public ArrayList<Repository> getRepositories(String query) throws IOException {
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
        Request request = new Request.Builder().url(requestUrl).build();

        Response responseRaw = client.newCall(request).execute();
        if (responseRaw.isSuccessful()) {
            String response = responseRaw.body().string();
            return response;
        } else {
            throw new IOException ("Response is not successful");
        }
    }
}