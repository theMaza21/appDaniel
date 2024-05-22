package com.daniel.appdaniel.models;

import java.util.ArrayList;
import java.util.Objects;

public class FCMResponse {
    private Long multicatst_id;
    private int success;
    private int failure;
    private int canonical_ids;
    ArrayList<Object> results = new ArrayList<Object>();

    public FCMResponse(Long multicatst_id, int success, int failure, int canonical_ids, ArrayList<Object> results) {
        this.multicatst_id = multicatst_id;
        this.success = success;
        this.failure = failure;
        this.canonical_ids = canonical_ids;
        this.results = results;
    }

    public Long getMulticatst_id() {
        return multicatst_id;
    }

    public void setMulticatst_id(Long multicatst_id) {
        this.multicatst_id = multicatst_id;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public int getCanonical_ids() {
        return canonical_ids;
    }

    public void setCanonical_ids(int canonical_ids) {
        this.canonical_ids = canonical_ids;
    }

    public ArrayList<Object> getResults() {
        return results;
    }

    public void setResults(ArrayList<Object> results) {
        this.results = results;
    }
}
