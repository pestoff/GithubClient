package dev.colibri.githubclienttest.network;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import dev.colibri.githubclienttest.entity.Repository;

public class JsonParser {
    private static final Gson GSON = new Gson();

    public ArrayList<Repository> getRepositories(String jsonString) {
        JsonObject jsonObject = GSON.fromJson(jsonString, JsonObject.class);
        JsonElement items = jsonObject.get("items");
        Type repositoriesType = new TypeToken<ArrayList<Repository>>(){}.getType();
        ArrayList<Repository> result = GSON.fromJson(items, repositoriesType);
        return result;
    }

    public Repository getRepository(String jsonString) {
        return GSON.fromJson(jsonString, Repository.class);
    }
}