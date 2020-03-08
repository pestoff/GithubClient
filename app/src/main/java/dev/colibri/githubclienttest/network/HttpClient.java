package dev.colibri.githubclienttest.network;

import java.io.IOException;
import java.util.List;

import dev.colibri.githubclienttest.entity.RepositoriesResponse;
import dev.colibri.githubclienttest.entity.Repository;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpClient {
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(GithubService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private GithubService githubService = retrofit.create(GithubService.class);

    public Repository getRepository(String repoName, String userLogin) throws IOException {
        return getResponse(githubService.getRepository(userLogin, repoName));
    }

    public List<Repository> getRepositories(String query) throws IOException {
        RepositoriesResponse response = getResponse(githubService.getRepositories(query));
        return response.getItems();
    }

    private <T> T getResponse(Call<T> call) throws IOException {
        Response<T> response = call.execute();

        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw new IOException("Неуспешный статус ответа " + response);
        }
    }
}