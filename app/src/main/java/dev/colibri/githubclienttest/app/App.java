package dev.colibri.githubclienttest.app;

import android.app.Application;
import android.arch.persistence.room.Room;

import dev.colibri.githubclienttest.database.AppDatabase;
import dev.colibri.githubclienttest.network.HttpClient;
import dev.colibri.githubclienttest.repository.DataRepository;

public class App extends Application {
    private static App instance;
    private static AppDatabase database;
    private static HttpClient httpClient;
    private static DataRepository dataRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static AppDatabase getDatabase() {
        if (database == null) {
            database = Room.databaseBuilder(instance, AppDatabase.class, "app-database").build();
        }
        return database;
    }

    public static HttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = new HttpClient();
        }
        return httpClient;
    }

    public static DataRepository getDataRepository() {
        if (dataRepository == null) {
            dataRepository = new DataRepository(getDatabase().getRepositoryDao(), getHttpClient());
        }
        return dataRepository;
    }
}
