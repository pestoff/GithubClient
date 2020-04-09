package dev.colibri.githubclienttest;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import dev.colibri.githubclienttest.entity.Owner;
import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.repository.DataRepository;
import dev.colibri.githubclienttest.viewModel.RepoDetailsViewModel;
import dev.colibri.githubclienttest.viewModel.RepoListViewModel;

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
    private DataRepository stubRepository;
    private RepoListViewModel repoListViewModel;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        repoListViewModel = new RepoListViewModel(stubRepository, TEST_EXECUTOR);
    }

    @After
    public void clearState() {
        stubRepository = null;
    }

    @Test
    public void loadRepositories_validResponse_returnRepositoryAnswer() throws IOException {
        // arrange
        String testQuery = "query";
        Mockito.when(stubRepository.getRepositories(testQuery)).thenReturn(REPOSITORIES);

        // act
        repoListViewModel.loadRepositories(testQuery);

        // assert
        LiveData<List<Repository>> liveData = repoListViewModel.getRepositories();
        Assert.assertEquals(REPOSITORIES, liveData.getValue());
    }


    @Test
    public void loadRepositories_invalidResponse_returnNetworkErrorFlag() throws IOException {
        // arrange
        String testQuery = "query";
        Mockito.when(stubRepository.getRepositories(testQuery)).thenReturn(null);

        // act
        repoListViewModel.loadRepositories(testQuery);

        // assert
        LiveData<Boolean> networkError = repoListViewModel.getIsNetworkException();
        Assert.assertEquals(networkError.getValue(), true);
    }

    @Test
    public void loadRepositories_emptyResponse_returnValidationExceptionFlag() {
        // arrange

        // act
        repoListViewModel.loadRepositories(null);

        // assert
        LiveData<Boolean> validationError = repoListViewModel.getIsQueryValidationException();
        Assert.assertEquals(validationError.getValue(), true);
    }
}
