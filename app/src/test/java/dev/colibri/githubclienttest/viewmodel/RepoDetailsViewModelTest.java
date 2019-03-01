package dev.colibri.githubclienttest.viewmodel;


import java.io.IOException;
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

public class RepoDetailsViewModelTest {
    private static final Repository REPOSITORY = Constants.REPOSITORY;
    private static final Executor TEST_EXECUTOR = Constants.TEST_EXECUTOR;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Mock
    private DataRepository dataRepository;
    private RepoDetailsViewModel viewModel;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        viewModel = new RepoDetailsViewModel(dataRepository, TEST_EXECUTOR);
        viewModel.getRepository().observeForever((repository -> {}));
    }

    @Test
    public void searchRepositories_ValidRepositoryResponse_SameValueInLiveData() throws IOException {
        // arrange
        String repoName = "Android";
        String userLogin = "Androider";
        Mockito.when(dataRepository.getRepository(repoName, userLogin)).thenReturn(REPOSITORY);

        // act
        viewModel.updateContent(repoName, userLogin);

        // assert
        Repository result = viewModel.getRepository().getValue();
        Assert.assertEquals(REPOSITORY, result);
    }

    @Test
    public void updateContent_ErrorResponse_ErrorLiveDataIsTrue() throws IOException {
        // arrange
        String repoName = "Android";
        String userLogin = "Androider";
        Mockito.when(dataRepository.getRepository(repoName, userLogin)).thenThrow(new IOException());

        // act
        viewModel.updateContent(repoName, userLogin);

        // assert
        Boolean result = viewModel.isException().getValue();
        Assert.assertEquals(true, result);
    }

}