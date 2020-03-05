package dev.colibri.githubclienttest.network;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dev.colibri.githubclienttest.entity.Repository;

public class JsonParser {

    private Gson gson;

    public JsonParser(Gson gson) {
        this.gson = gson;
    }

    public ArrayList<Repository> getRepositories(String jsonString) throws JSONException {
        JSONObject json = new JSONObject(jsonString);
        JSONArray items = json.getJSONArray("items");

        Type arrayListType = new TypeToken<ArrayList<Repository>>(){}.getType();
        return gson.fromJson(items.toString(), arrayListType);
    }

    public Repository getRepository(String jsonString) {
        return gson.fromJson(jsonString, Repository.class);
    }
}