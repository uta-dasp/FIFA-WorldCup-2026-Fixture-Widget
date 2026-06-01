package com.dasp.worldcup2026;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

final class FixtureRepository {
    static final long UPDATE_INTERVAL_MS = 24L * 60L * 60L * 1000L;

    private static final String DATA_URL = "https://raw.githubusercontent.com/openfootball/worldcup.json/master/2026/worldcup.json";
    private static final String PREFS = "worldcup_data";
    private static final String KEY_JSON = "json";
    private static final String KEY_UPDATED_AT = "updated_at";
    private static final String KEY_LAST_ERROR = "last_error";
    private static final int CONNECT_TIMEOUT_MS = 15000;
    private static final int READ_TIMEOUT_MS = 15000;

    private final Context context;

    FixtureRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    List<Fixture> cachedFixtures() {
        return parseFixtures(loadBestJson());
    }

    List<Fixture> refreshIfStale() throws Exception {
        if (!isStale()) {
            return cachedFixtures();
        }
        return refreshNow();
    }

    List<Fixture> refreshNow() throws Exception {
        String json = fetchOnlineJson();
        List<Fixture> fixtures = parseFixtures(json);
        if (fixtures.isEmpty()) {
            throw new IllegalStateException("Downloaded schedule is empty");
        }
        prefs().edit()
                .putString(KEY_JSON, json)
                .putLong(KEY_UPDATED_AT, System.currentTimeMillis())
                .putString(KEY_LAST_ERROR, "")
                .apply();
        WidgetRefresh.updateAll(context);
        return fixtures;
    }

    boolean isStale() {
        long updatedAt = prefs().getLong(KEY_UPDATED_AT, 0L);
        return updatedAt == 0L || System.currentTimeMillis() - updatedAt >= UPDATE_INTERVAL_MS;
    }

    String dataSourceLine() {
        long updatedAt = prefs().getLong(KEY_UPDATED_AT, 0L);
        String error = prefs().getString(KEY_LAST_ERROR, "");
        if (!error.isEmpty()) {
            return "Using cached schedule. Last update failed.";
        }
        if (updatedAt == 0L) {
            return "Bundled schedule. Online updates run once per day.";
        }
        java.text.DateFormat format = java.text.DateFormat.getDateTimeInstance(
                java.text.DateFormat.MEDIUM,
                java.text.DateFormat.SHORT,
                Locale.getDefault()
        );
        return "Updated " + format.format(new java.util.Date(updatedAt));
    }

    String lastError() {
        return prefs().getString(KEY_LAST_ERROR, "");
    }

    void saveError(String message) {
        prefs().edit().putString(KEY_LAST_ERROR, message == null ? "" : message).apply();
    }

    private List<Fixture> parseFixtures(String rawJson) {
        List<Fixture> fixtures = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(rawJson);
            JSONArray array = root.optJSONArray("matches");
            if (array == null) {
                return fixtures;
            }
            for (int i = 0; i < array.length(); i++) {
                fixtures.add(Fixture.fromOpenFootballJson(array.getJSONObject(i)));
            }
            Collections.sort(fixtures, new Comparator<Fixture>() {
                @Override
                public int compare(Fixture left, Fixture right) {
                    return Long.compare(left.timestampSeconds, right.timestampSeconds);
                }
            });
        } catch (Exception ignored) {
            return new ArrayList<>();
        }
        return fixtures;
    }

    private String loadBestJson() {
        String cached = prefs().getString(KEY_JSON, "");
        if (!cached.isEmpty() && !parseFixtures(cached).isEmpty()) {
            return cached;
        }
        try {
            return readAsset();
        } catch (Exception ignored) {
            return "{\"matches\":[]}";
        }
    }

    private String fetchOnlineJson() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(DATA_URL).openConnection();
        connection.setConnectTimeout(CONNECT_TIMEOUT_MS);
        connection.setReadTimeout(READ_TIMEOUT_MS);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        int code = connection.getResponseCode();
        InputStream stream = code >= 200 && code < 300 ? connection.getInputStream() : connection.getErrorStream();
        String body = readStream(stream);
        connection.disconnect();
        if (code < 200 || code >= 300) {
            throw new IllegalStateException("Schedule download failed: HTTP " + code);
        }
        return body;
    }

    private String readAsset() throws Exception {
        InputStream stream = context.getAssets().open("worldcup_2026.json");
        return readStream(stream);
    }

    private String readStream(InputStream stream) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        reader.close();
        return builder.toString();
    }

    private SharedPreferences prefs() {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }
}
