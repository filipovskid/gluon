package com.filipovski.gluon.executor.executor.output;

import com.google.gson.Gson;

import java.time.Instant;

/**
 * Static display data useful for execution output representation on UIs.
 *
 * <p>This class supports data of different {@link Type}s which can be stored as strings.
 * Current implementation supports only textual data, however, this can be extended to
 * account for HTML, Image, LaTeX and other data types.</p>
 */

public class DisplayData implements ExecutionOutputData {

    private Instant timestamp;

    private Type type;

    private String data;

    public DisplayData(String data, Type type) {
        this(data, type, Instant.now());
    }

    public DisplayData(String data, Type type, Instant timestamp) {
        this.data = data;
        this.type = type;
        this.timestamp = timestamp;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Type getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    @Override
    public String serialize() {
        Gson gson = new Gson();

        return gson.toJson(this);
    }

    public enum Type {
        TEXT
    }
}
