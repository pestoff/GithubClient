package dev.colibri.githubclienttest.repository;

import static dev.colibri.githubclienttest.viewmodel.RepoListViewModelTest.REPOSITORIES;
import static dev.colibri.githubclienttest.viewmodel.RepoListViewModelTest.REPOSITORY;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dev.colibri.githubclienttest.db.RepositoryDao;
import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.network.HttpClient;

public class DataRepositoryTest {
    @Mock
    private HttpClient httpClient;

    @Mock
    private RepositoryDao repositoryDao;

    private DataRepository dataRepository;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        dataRepository = new DataRepository(httpClient, repositoryDao);
    }

    @Test
    public void getRepositories_ValidServerResponse_StoreResponseInDbAndReturn() throws IOException {
        // arrange
        String query = "Android";

        Mockito.when(httpClient.getRepositories(query)).thenReturn(REPOSITORIES);

        // act
        List<Repository> result = dataRepository.getRepositories(query);

        // assert
        Assert.assertEquals(REPOSITORIES, result);
        Mockito.verify(repositoryDao).insertRepositories(REPOSITORIES);

    }

    @Test
    public void getRepositories_HttpErrorAndDatabaseIsNotEmpty_ReturnDbResponse() throws IOException {
        // arrange
        String query = "Android";
        String dbQuery = "%Android%";

        Mockito.when(httpClient.getRepositories(query)).thenThrow(new IOException());
        Mockito.when(repositoryDao.getRepositories(dbQuery)).thenReturn(REPOSITORIES);

        // act
        List<Repository> result = dataRepository.getRepositories(query);

        // assert
        Assert.assertEquals(REPOSITORIES, result);
        Mockito.verify(repositoryDao, Mockito.never()).insertRepositories(REPOSITORIES);
    }

    @Test(expected = IOException.class)
    public void getRepositories_ServerErrorAndDatabaseIsEmpty_throwIoException() throws IOException {
        // arrange
        String query = "Android";
        String dbQuery = "%Android%";

        Mockito.when(httpClient.getRepositories(query)).thenThrow(new IOException());
        Mockito.when(repositoryDao.getRepositories(dbQuery)).thenReturn(null);

        // act
        dataRepository.getRepositories(query);

        // assert IoException
    }

    @Test
    public void getRepository_DatabaseIsNotEmpty_ReturnDbResponse() throws IOException {
        // arrange
        String repoName = "repoName";
        String userLogin = "userLogin";

        Mockito.when(repositoryDao.getRepository(repoName, userLogin)).thenReturn(REPOSITORY);

        // act
        Repository result = dataRepository.getRepository(repoName, userLogin);

        // assert
        Assert.assertEquals(REPOSITORY, result);
    }

    @Test(expected = IOException.class)
    public void getRepository_DatabaseIsEmpty_throwIoException() throws IOException {
        // arrange
        String repoName = "repoName";
        String userLogin = "userLogin";

        Mockito.when(repositoryDao.getRepository(repoName, userLogin)).thenReturn(null);

        // act
        dataRepository.getRepository(repoName, userLogin);

        // assert IoException
    }

}