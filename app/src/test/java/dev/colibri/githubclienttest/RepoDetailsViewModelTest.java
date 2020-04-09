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
import java.util.concurrent.Executor;

import dev.colibri.githubclienttest.entity.Owner;
import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.repository.DataRepository;
import dev.colibri.githubclienttest.viewModel.RepoDetailsViewModel;

public class RepoDetailsViewModelTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Mock
    private DataRepository stubRepository;
    private RepoDetailsViewModel repoDetailsViewModel;
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

    public static Executor TEST_EXECUTOR = command -> command.run();


    Executor executor = command -> command.run();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        repoDetailsViewModel = new RepoDetailsViewModel(stubRepository, TEST_EXECUTOR);
    }

    @After
    public void clearState() {
        stubRepository = null;
    }

    @Test
    public void updateContent_validResponse_returnValidValue() throws IOException {
        // arrange
        String repoName = "repoName";
        String userLogin = "userLogin";
        Mockito.when(stubRepository.getRepository(repoName, userLogin)).thenReturn(REPOSITORY);

        // act
        repoDetailsViewModel.updateContent(repoName, userLogin);

        // assert
        LiveData<Repository> liveData = repoDetailsViewModel.getRepositoryMutableLiveData();
        Assert.assertEquals(liveData.getValue(), REPOSITORY);
    }

    @Test
    public void updateContent_invalidResponse_returnNetworkExceptionFlag() throws IOException {
        // assert
        String repoName = "repoName";
        String userLogin = "userLogin";
        Mockito.when(stubRepository.getRepository(repoName, userLogin)).thenReturn(null);

        // act
        repoDetailsViewModel.updateContent(repoName, userLogin);

        // assert
        LiveData<Boolean> networkError = repoDetailsViewModel.getIsNetworkException();
        Assert.assertEquals(networkError.getValue(), true);
    }
}
