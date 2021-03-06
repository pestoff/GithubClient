package dev.colibri.githubclienttest.viewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.Insert;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import dev.colibri.githubclienttest.app.App;
import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.repository.DataRepository;

public class RepoListViewModel extends ViewModel {
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isNetworkException = new MutableLiveData<>();
    private MutableLiveData<Boolean> isQueryValidationException = new MutableLiveData<>();
    private MutableLiveData<List<Repository>> repositories = new MutableLiveData<>();

    private DataRepository dataRepository;
    private Executor executor;

    @Inject
    public RepoListViewModel(DataRepository dataRepository, Executor executor) {
        this.dataRepository = dataRepository;
        this.executor = executor;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<List<Repository>> getRepositories() {
        return repositories;
    }

    public MutableLiveData<Boolean> getIsNetworkException() {
        return isNetworkException;
    }

    public MutableLiveData<Boolean> getIsQueryValidationException() {
        return isQueryValidationException;
    }

    public void loadRepositories(String query) {
        if (query == null) {
            isQueryValidationException.setValue(true);
        } else {
            executor.execute(() -> {
                List<Repository> repositoryList = null;

                try {
                    repositoryList = dataRepository.getRepositories(query);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (repositoryList != null) {
                    repositories.postValue(repositoryList);
                } else {
                    isNetworkException.postValue(true);
                }
            });
        }
    }
}
