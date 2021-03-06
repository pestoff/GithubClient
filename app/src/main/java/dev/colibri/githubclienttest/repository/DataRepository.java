package dev.colibri.githubclienttest.repository;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dev.colibri.githubclienttest.app.App;
import dev.colibri.githubclienttest.database.RepositoryDao;
import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.network.HttpClient;

@Singleton
public class DataRepository {

    private RepositoryDao repositoryDao;
    private HttpClient httpClient;

    @Inject
    public DataRepository(RepositoryDao repositoryDao, HttpClient httpClient) {
        this.repositoryDao = repositoryDao;
        this.httpClient = httpClient;
    }

    public List<Repository> getRepositories(String query) throws IOException {
        List<Repository> repositories = null;

        try {
            repositories = httpClient.getRepositories(query);
            repositoryDao.insert(repositories);
            return repositories;
        } catch (IOException e) {
            repositories = repositoryDao.getRepositories("%" + query + "%");
        }

        if (repositories == null) {
            throw new   IOException("Can't find repositories entities in db or api");
        }

        return repositories;
    }

    public Repository getRepository(String repoName, String userLogin) throws IOException {
        Repository repository = repositoryDao.getRepository(repoName, userLogin);

        if (repository == null) {
            throw new IOException("Can't find repository entity in db");
        }

        return repository;
    }

}
