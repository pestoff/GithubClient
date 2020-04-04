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

import dev.colibri.githubclienttest.R;
import dev.colibri.githubclienttest.app.App;
import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.repository.DataRepository;

public class RepoDetailsViewModel extends ViewModel {

    private DataRepository dataRepository = App.getDataRepository();

    private MutableLiveData<Repository> repositoryMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isNetworkException = new MutableLiveData<>();

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
        new GetRepositoryAsyncTask().execute(repoName, userLogin);
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

    private class GetRepositoryAsyncTask extends AsyncTask<String, Void, Repository> {

        @Override
        protected void onPreExecute() {
            isLoading.setValue(true);
        }

        @Override
        protected Repository doInBackground(String... params) {
            String repoName = params[0];
            String userLogin = params[1];

            try {
                return dataRepository.getRepository(repoName, userLogin);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Repository repository) {

            isLoading.setValue(false);

            if(repository != null) {
                repositoryMutableLiveData.setValue(repository);
            } else {
                isNetworkException.setValue(true);
            }
        }
    }
}
