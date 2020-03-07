package dev.colibri.githubclienttest.network;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

    public ArrayList<Repository> getRepositories(String jsonString)  {
        JsonObject json = gson.fromJson(jsonString, JsonObject.class);
        JsonElement items = json.get("items");

        Type arrayListType = new TypeToken<ArrayList<Repository>>(){}.getType();
        return gson.fromJson(items.toString(), arrayListType);
    }

    public Repository getRepository(String jsonString) {
        return gson.fromJson(jsonString, Repository.class);
    }
}