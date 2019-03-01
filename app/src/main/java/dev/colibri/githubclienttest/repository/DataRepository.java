package dev.colibri.githubclienttest.repository;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import dev.colibri.githubclienttest.db.AppDatabase;
import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.network.HttpClient;

@Singleton
public class DataRepository {
    private HttpClient httpClient;
    private AppDatabase db;

    @Inject
    public DataRepository(HttpClient httpClient, AppDatabase db) {
        this.httpClient = httpClient;
        this.db = db;
    }

    public Repository getRepository(String repoName, String userLogin) throws IOException {
        Repository repository = db.repositoryDao().getRepository(repoName, userLogin);
        if (repository == null) throw new IOException("Can't find repository entity in db");

        return repository;
    }

    public ArrayList<Repository> getRepositories(String query) throws IOException {
        ArrayList<Repository> repositories = null;
        try {

            repositories = httpClient.getRepositories(query);
            db.repositoryDao().insertRepositories(repositories);
        } catch (IOException e) {
            String dbWildCardQuery = "%" + query + "%";
            repositories = new ArrayList<>(db.repositoryDao().getRepositories(dbWildCardQuery));
        }

        if(repositories == null) throw new IOException("Can't find repositories entities in db or api");

        return repositories;
    }
}