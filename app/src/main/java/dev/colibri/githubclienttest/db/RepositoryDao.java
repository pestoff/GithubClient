package dev.colibri.githubclienttest.db;

import java.util.Collection;
import java.util.List;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import dev.colibri.githubclienttest.entity.Repository;

@Dao
public interface RepositoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRepositories(Collection<Repository> repositories);

    @Query("SELECT * FROM Repository WHERE name = :repoName AND owner_login = :userLogin LIMIT 1")
    Repository getRepository(String repoName, String userLogin);

    @Query("SELECT * FROM Repository WHERE name LIKE :query ORDER BY stargazersCount")
    List<Repository> getRepositories(String query);
}