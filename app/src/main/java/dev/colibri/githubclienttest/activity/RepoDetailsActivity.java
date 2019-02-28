package dev.colibri.githubclienttest.activity;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import dev.colibri.githubclienttest.R;
import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.network.HttpClient;

public class RepoDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_REPO_NAME = "repoName";
    public static final String EXTRA_USER_LOGIN = "userLogin";
    private static final String LOG_TAG = "dc.RepoDetailsActivity";

    private HttpClient httpClient;

    private ImageView ownerImageView;
    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView starsTextView;
    private TextView forksTextView;
    private TextView createdTextView;
    private TextView updatedTextView;
    private TextView languageTextView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_details);

        String repoName = getIntent().getStringExtra(EXTRA_REPO_NAME);
        String userLogin = getIntent().getStringExtra(EXTRA_USER_LOGIN);

        initView();

        httpClient = new HttpClient();

        new GetRepositoryAsyncTask().execute(repoName, userLogin);
    }

    private void initView() {
        ownerImageView = findViewById(R.id.owner_image_view);
        nameTextView = findViewById(R.id.name_text_view);
        descriptionTextView = findViewById(R.id.description_text_view);
        starsTextView = findViewById(R.id.stars_text_view);
        forksTextView = findViewById(R.id.forks_text_view);
        createdTextView = findViewById(R.id.created_text_view);
        updatedTextView = findViewById(R.id.updated_text_view);
        languageTextView = findViewById(R.id.language_text_view);
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
                Toast.makeText(RepoDetailsActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
            }
        }
    }
}