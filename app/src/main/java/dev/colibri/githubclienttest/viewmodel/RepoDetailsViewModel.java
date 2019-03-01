package dev.colibri.githubclienttest.viewmodel;

import java.io.IOException;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.repository.DataRepository;

public class RepoDetailsViewModel extends ViewModel {

    private DataRepository dataRepository;
    private Executor executor;

    private MutableLiveData<Repository> repository = new MutableLiveData<>();
    private MutableLiveData<Boolean> isNetworkException = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    @Inject
    public RepoDetailsViewModel(DataRepository dataRepository, Executor executor) {
        this.dataRepository = dataRepository;
        this.executor = executor;
    }

    public LiveData<Repository> getRepository() {
        return repository;
    }


    public LiveData<Boolean> isException() {
        return isNetworkException;
    }

    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    public void updateContent(String repoName, String userLogin) {

        isLoading.setValue(true);
        executor.execute(() -> {
            try {
                Repository result = dataRepository.getRepository(repoName, userLogin);
                repository.postValue(result);

            } catch (IOException e) {
                e.printStackTrace();
                isNetworkException.postValue(true);
            } finally {
                isLoading.postValue(false);
            }
        });
    }

}
