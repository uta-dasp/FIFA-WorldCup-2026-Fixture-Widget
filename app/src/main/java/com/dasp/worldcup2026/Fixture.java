package com.dasp.worldcup2026;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

final class Fixture {
    final long id;
    final long timestampSeconds;
    final String round;
    final String venue;
    final String city;
    final String homeName;
    final String awayName;
    final int homeGoals;
    final int awayGoals;
    final String statusShort;
    final String statusLong;
    final int elapsed;

    Fixture(
            long id,
            long timestampSeconds,
            String round,
            String venue,
            String city,
            String homeName,
            String awayName,
            int homeGoals,
            int awayGoals,
            String statusShort,
            String statusLong,
            int elapsed
    ) {
        this.id = id;
        this.timestampSeconds = timestampSeconds;
        this.round = clean(round);
        this.venue = clean(venue);
        this.city = clean(city);
        this.homeName = clean(homeName);
        this.awayName = clean(awayName);
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        this.statusShort = clean(statusShort);
        this.statusLong = clean(statusLong);
        this.elapsed = elapsed;
    }

    static Fixture fromOpenFootballJson(JSONObject item) {
        JSONObject score = item.optJSONObject("score");
        int homeGoals = -1;
        int awayGoals = -1;
        if (score != null) {
            org.json.JSONArray fullTime = score.optJSONArray("ft");
            if (fullTime != null && fullTime.length() >= 2) {
                homeGoals = fullTime.optInt(0, -1);
                awayGoals = fullTime.optInt(1, -1);
            }
        }

        return new Fixture(
                item.optLong("num", stableId(item)),
                parseTimestamp(item.optString("date"), item.optString("time")),
                item.optString("round"),
                item.optString("ground"),
                "",
                item.optString("team1"),
                item.optString("team2"),
                homeGoals,
                awayGoals,
                homeGoals >= 0 && awayGoals >= 0 ? "FT" : "NS",
                homeGoals >= 0 && awayGoals >= 0 ? "Match Finished" : "Not Started",
                0
        );
    }

    String matchTitle() {
        String home = homeName.isEmpty() ? "TBD" : homeName;
        String away = awayName.isEmpty() ? "TBD" : awayName;
        return home + " vs " + away;
    }

    String scoreTitle() {
        if (homeGoals >= 0 && awayGoals >= 0) {
            return homeName + " " + homeGoals + " - " + awayGoals + " " + awayName;
        }
        return matchTitle();
    }

    String dateLine() {
        if (timestampSeconds <= 0) {
            return "Time TBD";
        }
        Date date = new Date(timestampSeconds * 1000L);
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault());
        return format.format(date);
    }

    String locationLine() {
        if (venue.isEmpty() && city.isEmpty()) {
            return round;
        }
        if (venue.isEmpty()) {
            return city;
        }
        if (city.isEmpty()) {
            return venue;
        }
        return venue + ", " + city;
    }

    String statusLine() {
        if (isLive()) {
            String clock = elapsed > 0 ? " - " + elapsed + "'" : "";
            return "Live" + clock;
        }
        if (isFinished()) {
            return statusLong.isEmpty() ? "Finished" : statusLong;
        }
        if (!statusLong.isEmpty() && !"Not Started".equalsIgnoreCase(statusLong)) {
            return statusLong;
        }
        return dateLine();
    }

    boolean isLive() {
        return "1H".equals(statusShort)
                || "HT".equals(statusShort)
                || "2H".equals(statusShort)
                || "ET".equals(statusShort)
                || "BT".equals(statusShort)
                || "P".equals(statusShort)
                || "LIVE".equals(statusShort)
                || "INT".equals(statusShort);
    }

    boolean isFinished() {
        return "FT".equals(statusShort) || "AET".equals(statusShort) || "PEN".equals(statusShort);
    }

    boolean isUpcoming() {
        return !isLive() && !isFinished();
    }

    private static String clean(String value) {
        if (value == null || "null".equalsIgnoreCase(value)) {
            return "";
        }
        return value.trim();
    }

    private static long parseTimestamp(String date, String time) {
        try {
            String[] parts = clean(time).split(" ");
            String clock = parts.length > 0 ? parts[0] : "00:00";
            String zone = parts.length > 1 ? parts[1] : "UTC";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            format.setLenient(false);
            format.setTimeZone(TimeZone.getTimeZone(toGmtZone(zone)));
            Date parsed = format.parse(clean(date) + " " + clock);
            return parsed == null ? 0 : parsed.getTime() / 1000L;
        } catch (Exception ignored) {
            return 0;
        }
    }

    private static String toGmtZone(String zone) {
        String cleanZone = clean(zone).replace("UTC", "GMT");
        if ("GMT".equals(cleanZone)) {
            return cleanZone;
        }
        int signIndex = Math.max(cleanZone.indexOf('+'), cleanZone.indexOf('-'));
        if (signIndex < 0) {
            return "GMT";
        }
        String offset = cleanZone.substring(signIndex);
        if (!offset.contains(":")) {
            offset = offset + ":00";
        }
        return "GMT" + offset;
    }

    private static long stableId(JSONObject item) {
        String seed = item.optString("date")
                + item.optString("time")
                + item.optString("team1")
                + item.optString("team2")
                + item.optString("round");
        return Math.abs(seed.hashCode());
    }
}
