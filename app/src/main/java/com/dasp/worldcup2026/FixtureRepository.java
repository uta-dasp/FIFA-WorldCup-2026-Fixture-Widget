package com.dasp.worldcup2026;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

final class FixtureRepository {
    private final Context context;

    FixtureRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    List<Fixture> cachedFixtures() {
        List<Fixture> fixtures = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(readAsset());
            JSONArray array = root.optJSONArray("matches");
            if (array == null) {
                return fixtures;
            }
            for (int i = 0; i < array.length(); i++) {
                fixtures.add(Fixture.fromOpenFootballJson(array.getJSONObject(i)));
            }
        } catch (Exception ignored) {
            return new ArrayList<>();
        }
        return fixtures;
    }

    String dataSourceLine() {
        return "Bundled openfootball/worldcup.json schedule";
    }

    private String readAsset() throws Exception {
        InputStream stream = context.getAssets().open("worldcup_2026.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        reader.close();
        return builder.toString();
    }
}
