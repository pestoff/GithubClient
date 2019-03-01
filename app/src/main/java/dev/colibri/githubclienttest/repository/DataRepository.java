package dev.colibri.githubclienttest.repository;

import java.io.IOException;
import java.util.List;

import dev.colibri.githubclienttest.App;
import dev.colibri.githubclienttest.db.AppDatabase;
import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.network.HttpClient;

public class DataRepository {
    private HttpClient httpClient = App.getHttpClient();
    private AppDatabase db = App.getAppDatabase();

    public Repository getRepository(String repoName, String userLogin) throws IOException {
        Repository repository = db.repositoryDao().getRepository(repoName, userLogin);
        if (repository == null) throw new IOException("Can't find repository entity in db");

        return repository;
    }

    public List<Repository> getRepositories(String query) throws IOException {
        List<Repository> repositories = null;
        try {

            repositories = httpClient.getRepositories(query);
            db.repositoryDao().insertRepositories(repositories);
        } catch (IOException e) {
            String dbWildCardQuery = "%" + query + "%";
            repositories = db.repositoryDao().getRepositories(dbWildCardQuery);
        }

        if(repositories == null) throw new IOException("Can't find repositories entities in db or api");

        return repositories;
    }
}