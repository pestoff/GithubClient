package dev.colibri.githubclienttest.entity;
import com.google.gson.annotations.SerializedName;

public class Repository {
    private int id;
    private String name;
    private String description;
    private String language;
    private Owner owner;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("stargazers_count")
    private int stargazersCount;

    @SerializedName("forks_count")
    private int forksCount;

    public Repository(int id,
                      String name,
                      String description,
                      String createdAt,
                      String updatedAt,
                      int stargazersCount,
                      String language,
                      int forksCount,
                      Owner owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.stargazersCount = stargazersCount;
        this.language = language;
        this.forksCount = forksCount;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public int getStargazersCount() {
        return stargazersCount;
    }

    public String getLanguage() {
        return language;
    }

    public int getForksCount() {
        return forksCount;
    }

    public Owner getOwner() {
        return owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Repository that = (Repository) o;

        if (id != that.id) return false;
        if (stargazersCount != that.stargazersCount) return false;
        if (forksCount != that.forksCount) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        if (updatedAt != null ? !updatedAt.equals(that.updatedAt) : that.updatedAt != null) return false;
        if (language != null ? !language.equals(that.language) : that.language != null) return false;
        return owner != null ? owner.equals(that.owner) : that.owner == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        result = 31 * result + stargazersCount;
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + forksCount;
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Repository{"
               + "id="
               + id
               + ", name='"
               + name
               + '\''
               + ", description='"
               + description
               + '\''
               + ", createdAt='"
               + createdAt
               + '\''
               + ", updatedAt='"
               + updatedAt
               + '\''
               + ", stargazersCount="
               + stargazersCount
               + ", language='"
               + language
               + '\''
               + ", forksCount="
               + forksCount
               + ", owner="
               + owner
               + '}';
    }
}