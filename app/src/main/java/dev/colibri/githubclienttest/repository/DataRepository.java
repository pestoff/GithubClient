package dev.colibri.githubclienttest.repository;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dev.colibri.githubclienttest.db.RepositoryDao;
import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.network.HttpClient;

@Singleton
public class DataRepository {
    private HttpClient httpClient;
    private RepositoryDao repositoryDao;

    @Inject
    public DataRepository(HttpClient httpClient, RepositoryDao repositoryDao) {
        this.httpClient = httpClient;
        this.repositoryDao = repositoryDao;
    }

    public Repository getRepository(String repoName, String userLogin) throws IOException {
        Repository repository = repositoryDao.getRepository(repoName, userLogin);
        if (repository == null) throw new IOException("Can't find repository entity in repositoryDao");

        return repository;
    }

    public List<Repository> getRepositories(String query) throws IOException {
        List<Repository> repositories = null;
        try {

            repositories = httpClient.getRepositories(query);
            repositoryDao.insertRepositories(repositories);
        } catch (IOException e) {
            String dbWildCardQuery = "%" + query + "%";
            repositories = repositoryDao.getRepositories(dbWildCardQuery);
        }

        if(repositories == null) throw new IOException("Can't find repositories entities in repositoryDao or api");

        return repositories;
    }
}