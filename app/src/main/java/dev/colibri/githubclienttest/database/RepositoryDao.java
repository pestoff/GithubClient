package dev.colibri.githubclienttest.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import dev.colibri.githubclienttest.entity.Repository;

@Dao
public interface RepositoryDao {

    @Query("SELECT * FROM repository WHERE name LIKE :query ORDER BY stargazersCount")
    public List<Repository> getRepositories(String query);

    @Query("SELECT * FROM repository WHERE name = :repoName AND owner_login = :userLogin LIMIT 1")
    public Repository getRepository(String repoName, String userLogin);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(List<Repository> repositories);
}
