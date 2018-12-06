package dev.colibri.githubclienttest.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import dev.colibri.githubclienttest.R;
import dev.colibri.githubclienttest.entity.Repository;

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder> {
    private List<Repository> repositories = new ArrayList<>();

    @NonNull
    @Override
    public RepositoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.repository_item_view, parent, false);

        return new RepositoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RepositoryViewHolder repositoryViewHolder, int position) {
        Repository repository = repositories.get(position);
        repositoryViewHolder.bind(repository);
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    public void addItems(List<Repository> items) {
        repositories.addAll(items);
        notifyDataSetChanged();
    }

    class RepositoryViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView descriptionTextView;
        private ImageView ownerImageView;

        public RepositoryViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.name_text_view);
            descriptionTextView = itemView.findViewById(R.id.description_text_view);
            ownerImageView = itemView.findViewById(R.id.owner_image_view);
        }

        void bind(Repository repository) {
            nameTextView.setText(repository.getName());
            descriptionTextView.setText(repository.getDescription());

            String avatarUrl = repository.getOwner().getAvatarUrl();
            Picasso.get().load(avatarUrl).into(ownerImageView);
        }
    }

}