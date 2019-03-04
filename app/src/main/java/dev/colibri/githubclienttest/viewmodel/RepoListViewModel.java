package dev.colibri.githubclienttest.viewmodel;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.repository.DataRepository;

public class RepoListViewModel extends ViewModel {
    private DataRepository dataRepository;
    private Executor executor;

    private MutableLiveData<List<Repository>> repositories = new MutableLiveData<>();
    private MutableLiveData<Boolean> isNetworkException = new MutableLiveData<>();
    private MutableLiveData<Boolean> isQueryValidationException = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    @Inject
    public RepoListViewModel(DataRepository dataRepository, Executor executor) {
        this.dataRepository = dataRepository;
        this.executor = executor;
    }

    public LiveData<List<Repository>> getRepositories() {
        return repositories;
    }


    public LiveData<Boolean> isNetworkException() {
        return isNetworkException;
    }

    public LiveData<Boolean> isQueryValidationException() {
        return isQueryValidationException;
    }

    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    public void searchRepositories(String query) {
        if(query.isEmpty()) {
            isQueryValidationException.setValue(true);
        } else {
            requestRepositories(query);
        }
    }

    private void requestRepositories(String query) {
        isLoading.setValue(true);
        executor.execute(() -> {
            try {
                List<Repository> result = dataRepository.getRepositories(query);
                repositories.postValue(result);

            } catch (IOException e) {
                e.printStackTrace();
                isNetworkException.postValue(true);
            } finally {
                isLoading.postValue(false);
            }
        });
    }

}
