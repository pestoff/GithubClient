package dev.colibri.githubclienttest.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import dev.colibri.githubclienttest.entity.Repository;

@Database(entities = {Repository.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RepositoryDao getRepositoryDao();
}
