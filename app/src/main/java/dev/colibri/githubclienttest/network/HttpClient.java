package dev.colibri.githubclienttest.network;

import java.io.IOException;
import java.util.ArrayList;

import android.net.Uri;
import android.support.annotation.NonNull;

import dev.colibri.githubclienttest.entity.Repository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpClient {
    private static final String REPOSITORY_SEARCH_URL = "https://api.github.com/search/repositories";
    private static final String REPOS_URL = "https://api.github.com/repos";
    private static final String QUERY_PARAM = "q";
    private final JsonParser jsonParser = new JsonParser();

    private OkHttpClient client = new OkHttpClient.Builder()
            .build();


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
        Request request = new Request.Builder()
                .url(requestUrl)
                .build();


        Response responseRaw = client.newCall(request).execute();
        if(responseRaw.isSuccessful()) {
            String response = responseRaw.body().string();
            return response;
        } else {
            throw new IOException("Response is not successful");
        }
    }
}