package dev.colibri.githubclienttest.viewmodel;


import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dev.colibri.githubclienttest.entity.Owner;
import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.repository.DataRepository;

public class RepoListViewModelTest {
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    public static final  Repository REPOSITORY = new Repository(
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
    public static Executor TEST_EXECUTOR = command -> command.run();

    @Mock
    private DataRepository dataRepository;
    private RepoListViewModel viewModel;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        viewModel = new RepoListViewModel(dataRepository, TEST_EXECUTOR);
        viewModel.getRepositories().observeForever((repositories -> {}));
    }

    @Test
    public void searchRepositories_ValidRepositoryResponse_SameValueInLiveData() throws IOException {
        // arrange
        String query = "Android";
        Mockito.when(dataRepository.getRepositories(query)).thenReturn(REPOSITORIES);

        // act
        viewModel.searchRepositories(query);

        // assert
        List<Repository> result = viewModel.getRepositories().getValue();
        Assert.assertEquals(REPOSITORIES, result);
    }

    @Test
    public void searchRepositories_ErrorResponse_ErrorLiveDataIsTrue() throws IOException {
        // arrange
        String query = "Android";
        Mockito.when(dataRepository.getRepositories(query)).thenThrow(new IOException());

        // act
        viewModel.searchRepositories(query);

        // assert
        Boolean result = viewModel.isNetworkException().getValue();
        Assert.assertEquals(true, result);
    }

    @Test
    public void searchRepositories_QueryIsEmpty_ValidationError() {
        // arrange
        String query = "";

        // act
        viewModel.searchRepositories(query);

        // assert
        Boolean result = viewModel.isQueryValidationException().getValue();
        Assert.assertEquals(true, result);
    }

}