package dev.colibri.githubclienttest.viewmodel;


import java.io.IOException;
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

import dev.colibri.githubclienttest.Constants;
import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.repository.DataRepository;

public class RepoListViewModelTest {
    private static final List<Repository> REPOSITORIES = Constants.REPOSITORIES;
    private static final Executor TEST_EXECUTOR = Constants.TEST_EXECUTOR;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

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