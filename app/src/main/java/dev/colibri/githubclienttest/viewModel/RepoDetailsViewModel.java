package dev.colibri.githubclienttest.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.repository.DataRepository;

public class RepoDetailsViewModel extends ViewModel {

    private DataRepository dataRepository;
    private Executor executor;

    private MutableLiveData<Repository> repositoryMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isNetworkException = new MutableLiveData<>();

    @Inject
    public RepoDetailsViewModel(DataRepository dataRepository, Executor executor) {
        this.dataRepository = dataRepository;
        this.executor = executor;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<Boolean> getIsNetworkException() {
        return isNetworkException;
    }

    public MutableLiveData<Repository> getRepositoryMutableLiveData() {
       return repositoryMutableLiveData;
    }

    public void updateContent(String repoName, String userLogin) {
        executor.execute(() -> {
            Repository repository = null;

            isLoading.postValue(true);

            try {
                repository = dataRepository.getRepository(repoName, userLogin);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(repository != null) {
                repositoryMutableLiveData.postValue(repository);
            } else {
                isNetworkException.postValue(true);
            }

            isLoading.postValue(false);
        });
    }

    public String getFormattedDate(String dateString) {
        SimpleDateFormat githubFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            Date date = githubFormat.parse(dateString);

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy");
            return outputFormat.format(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
