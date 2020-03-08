package dev.colibri.githubclienttest.network;

import dev.colibri.githubclienttest.entity.RepositoriesResponse;
import dev.colibri.githubclienttest.entity.Repository;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GithubService {

    String BASE_URL = "https://api.github.com";

    @GET("search/repositories")
    Call<RepositoriesResponse> getRepositories(@Query("q") String query);


    @GET("repos/{user}/{repo}")
    Call<Repository> getRepository(@Path("user") String user, @Path("repo") String repo);
}
