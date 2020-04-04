package dev.colibri.githubclienttest.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import dev.colibri.githubclienttest.R;
import dev.colibri.githubclienttest.entity.Repository;
import dev.colibri.githubclienttest.viewModel.RepoDetailsViewModel;

public class RepoDetailsFragment extends Fragment {

    private static String mRepoName;
    private static String mUserLogin;

    private RepoDetailsViewModel viewModel;

    private ImageView ownerImageView;
    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView starsTextView;
    private TextView forksTextView;
    private TextView createdTextView;
    private TextView updatedTextView;
    private TextView languageTextView;
    private ProgressBar progressBar;

    public RepoDetailsFragment() {
    }

    public static RepoDetailsFragment newInstance(Repository repository) {
        mRepoName = repository.getName();
        mUserLogin = repository.getOwner().getLogin();

        return new RepoDetailsFragment();
    }

    public void updateContent(Repository repository) {
        mRepoName = repository.getName();
        mUserLogin = repository.getOwner().getLogin();

        viewModel.updateContent(mRepoName, mUserLogin);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repo_details, container, false);

        initView(view);
        initViewModel();

        viewModel.updateContent(mRepoName, mUserLogin);

        return view;
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(RepoDetailsViewModel.class);

        viewModel.getRepositoryMutableLiveData().observe(this, repository -> {
            display(repository);
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        viewModel.getIsNetworkException().observe(this, isException -> {
            if (isException) {
                Toast.makeText(getActivity(), R.string.error_msg, Toast.LENGTH_SHORT).show();
            }
        });
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
        progressBar = view.findViewById(R.id.progress_bar);
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
        createdTextView.setText(viewModel.getFormattedDate(createdAt));
        String updatedAt = repository.getUpdatedAt();
        updatedTextView.setText(viewModel.getFormattedDate(updatedAt));
    }
}
