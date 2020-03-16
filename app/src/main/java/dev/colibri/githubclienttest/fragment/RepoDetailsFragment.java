package dev.colibri.githubclienttest.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dev.colibri.githubclienttest.R;
import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.network.HttpClient;

public class RepoDetailsFragment extends Fragment {

    private static String mRepoName;
    private static String mUserLogin;

    private HttpClient httpClient;

    private ImageView ownerImageView;
    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView starsTextView;
    private TextView forksTextView;
    private TextView createdTextView;
    private TextView updatedTextView;
    private TextView languageTextView;

    public RepoDetailsFragment() {
    }

    public static RepoDetailsFragment newInstance(Repository repository) {
        mRepoName = repository.getName();
        mUserLogin = repository.getOwner().getLogin();

        return new RepoDetailsFragment();
    }

    public void updateContent(Repository repository) {
        new GetRepositoryAsyncTask().execute(repository.getName(), repository.getOwner().getLogin());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repo_details, container, false);

        initView(view);

        httpClient = new HttpClient();

        new GetRepositoryAsyncTask().execute(mRepoName, mUserLogin);

        return view;
    }

    private void initView(View view) {
        ownerImageView = view.findViewById(R.id.owner_image_view);
        nameTextView = view.findViewById(R.id.name_text_view);
        descriptionTextView = view.findViewById(R.id.description_text_view);
        starsTextView = view.findViewById(R.id.stars_text_view);
        forksTextView = view.findViewById(R.id.forks_text_view);
        createdTextView = view.findViewById(R.id.created_text_view);
        updatedTextView = view.findViewById(R.id.updated_text_view);
        languageTextView = view.findViewById(R.id.language_text_view);
    }

    void display(Repository repository) {
        String avatarUrl = repository.getOwner().getAvatarUrl();
        Picasso.get().load(avatarUrl).into(ownerImageView);
        nameTextView.setText(repository.getName());
        descriptionTextView.setText(repository.getDescription());

        int starsCount = repository.getStargazersCount();
        starsTextView.setText(String.valueOf(starsCount));

        int forksCount = repository.getForksCount();
        forksTextView.setText(String.valueOf(forksCount));

        languageTextView.setText(repository.getLanguage());
        String createdAt = repository.getCreatedAt();
        createdTextView.setText(getFormattedDate(createdAt));
        String updatedAt = repository.getUpdatedAt();
        updatedTextView.setText(getFormattedDate(updatedAt));
    }

    private String getFormattedDate(String dateString) {
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
        protected Repository doInBackground(String... params) {
            String repoName = params[0];
            String userLogin = params[1];
            try {
                return httpClient.getRepository(repoName, userLogin);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Repository repository) {
            if(repository != null) {
                display(repository);
            } else {
                Toast.makeText(getActivity(), R.string.error_msg, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
