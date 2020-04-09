package dev.colibri.githubclienttest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.colibri.githubclienttest.database.RepositoryDao;
import dev.colibri.githubclienttest.entity.Owner;
import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.network.HttpClient;
import dev.colibri.githubclienttest.repository.DataRepository;

public class DataRepositoryTest {

    public static final Repository REPOSITORY = new Repository(
            1,
            "name",
            "description",
            "2019-01-01T04:02:57Z",
            "2018-11-05T04:02:57Z",
            100,
            "en",
            100,
            new Owner("login", 1, "https://sample/sample.png")
    );

    public static final List<Repository> REPOSITORIES = Collections.singletonList(REPOSITORY);

    @Mock
    private RepositoryDao stubDao;

    @Mock
    private HttpClient stubHttpClient;

    private DataRepository dataRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        dataRepository = new DataRepository(stubDao, stubHttpClient);
    }

    @After
    public void clearState() {
        stubDao = null;
        stubHttpClient = null;
    }

    @Test
    public void getRepositories_validResponse_returnRepositories() throws IOException {
        // arrange
        String query = "query";

        Mockito.when(stubHttpClient.getRepositories(query)).thenReturn(REPOSITORIES);

        // act
        List<Repository> repositories = dataRepository.getRepositories(query);

        // assert
        Assert.assertEquals(repositories, REPOSITORIES);
        Mockito.verify(stubDao, Mockito.times(1)).insert(REPOSITORIES);
    }

    @Test
    public void getRepositories_invalidResponse_returnRepositoriesFromDB() throws IOException {
        // arrange
        String query = "query";

        Mockito.when(stubHttpClient.getRepositories(query)).thenThrow(new IOException());
        Mockito.when(stubDao.getRepositories("%" + query + "%")).thenReturn(REPOSITORIES);


        // act
        List<Repository> repositories = dataRepository.getRepositories(query);

        // assert
        Assert.assertEquals(repositories, REPOSITORIES);
    }

    @Test (expected = IOException.class)
    public void getRepositories_invalidResponse_throwIOException() throws IOException {
        // arrange
        String query = "query";

        Mockito.when(stubHttpClient.getRepositories(query)).thenThrow(new IOException());
        Mockito.when(stubDao.getRepositories("%" + query + "%")).thenReturn(null);

        // act
        dataRepository.getRepositories(query);
    }

    @Test
    public void getRepository_validResponse_returnValueFromDB() throws IOException {
        // arrange
        String repoName = "repoName";
        String userLogin = "userLogin";

        Mockito.when(stubDao.getRepository(repoName, userLogin)).thenReturn(REPOSITORY);

        // act
        Repository repository = dataRepository.getRepository(repoName, userLogin);

        // assert
        Assert.assertEquals(repository, REPOSITORY);
    }

    @Test (expected = IOException.class)
    public void getRepository_invalidResponse_throwIOException() throws IOException {
        // arrange
        String repoName = "repoName";
        String userLogin = "userLogin";

        Mockito.when(stubDao.getRepository(repoName, userLogin)).thenReturn(null);

        // act
        dataRepository.getRepository(repoName, userLogin);
    }
}
