package dev.colibri.githubclienttest.entity;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ApiItemsResponse<T> {
    @SerializedName("total_count")
    private Long totalCount;

    @SerializedName("incomplete_results")
    private Boolean incompleteResults;

    private ArrayList<T> items;


    public Long getTotalCount() {
        return totalCount;
    }

    public Boolean getIncompleteResults() {
        return incompleteResults;
    }

    public ArrayList<T> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ApiItemsResponse<?> that = (ApiItemsResponse<?>) o;

        if (totalCount != null ? !totalCount.equals(that.totalCount) : that.totalCount != null) return false;
        if (incompleteResults != null ? !incompleteResults.equals(that.incompleteResults) : that.incompleteResults != null) return false;
        return items != null ? items.equals(that.items) : that.items == null;
    }

    @Override
    public int hashCode() {
        int result = totalCount != null ? totalCount.hashCode() : 0;
        result = 31 * result + (incompleteResults != null ? incompleteResults.hashCode() : 0);
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }
}