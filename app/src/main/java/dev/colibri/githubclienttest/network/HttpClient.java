package dev.colibri.githubclienttest.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dev.colibri.githubclienttest.entity.RepositoriesResponse;
import dev.colibri.githubclienttest.entity.Repository;
import retrofit2.Call;
import retrofit2.Response;

@Singleton
public class HttpClient {
    private final GithubService githubService;

    @Inject
    public HttpClient(GithubService githubService) {
        this.githubService = githubService;
    }

    public List<Repository> getRepositories(String query) throws IOException {
        RepositoriesResponse response = getResponse(githubService.searchRepos(query));
        return response.getRepositories();
    }

    private <T> T getResponse(Call<T> call) throws IOException {
        Response<T> response = call.execute();
        if(response.isSuccessful()) {
            return response.body();
        }
        else {
            throw new IOException("Неуспешный статус ответа " + response);
        }
    }
}