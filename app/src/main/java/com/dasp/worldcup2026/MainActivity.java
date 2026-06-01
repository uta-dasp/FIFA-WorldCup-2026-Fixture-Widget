package com.dasp.worldcup2026;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class MainActivity extends Activity {
    private FixtureRepository repository;
    private LinearLayout list;
    private TextView status;
    private Button allButton;
    private Button todayButton;
    private Button scoresButton;
    private String filter = "all";
    private List<Fixture> fixtures = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new FixtureRepository(this);
        setContentView(createContent());
        fixtures = repository.cachedFixtures();
        render();
    }

    private View createContent() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(color(R.color.surface));
        root.setPadding(dp(18), dp(18), dp(18), dp(12));

        TextView title = text("FIFA World Cup 2026", 25, R.color.ink, true);
        root.addView(title, matchWrap());

        TextView subtitle = text("Offline fixtures and home-screen widgets", 14, R.color.muted, false);
        subtitle.setPadding(0, dp(4), 0, dp(14));
        root.addView(subtitle, matchWrap());

        LinearLayout actionRow = new LinearLayout(this);
        actionRow.setGravity(Gravity.CENTER_VERTICAL);
        actionRow.setOrientation(LinearLayout.HORIZONTAL);

        status = text(repository.dataSourceLine(), 12, R.color.muted, false);
        status.setSingleLine(false);
        actionRow.addView(status, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        root.addView(actionRow, matchWrap());

        LinearLayout filterRow = new LinearLayout(this);
        filterRow.setOrientation(LinearLayout.HORIZONTAL);
        filterRow.setPadding(0, dp(14), 0, dp(12));
        allButton = filterButton("All", "all");
        todayButton = filterButton("Today", "today");
        scoresButton = filterButton("Results", "scores");
        filterRow.addView(allButton, buttonWeight());
        filterRow.addView(todayButton, buttonWeight());
        filterRow.addView(scoresButton, buttonWeight());
        root.addView(filterRow, matchWrap());

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(false);
        list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(list, matchWrap());
        root.addView(scroll, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
        return root;
    }

    private Button filterButton(String label, final String value) {
        Button button = new Button(this);
        button.setText(label);
        button.setTextSize(12);
        button.setAllCaps(false);
        button.setSingleLine(true);
        button.setEllipsize(TextUtils.TruncateAt.END);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter = value;
                render();
            }
        });
        return button;
    }

    private void render() {
        list.removeAllViews();
        updateFilterButtons();

        List<Fixture> visible = filtered();
        if (visible.isEmpty()) {
            addEmptyState();
        } else {
            for (Fixture fixture : visible) {
                list.addView(card(fixture), matchWrap());
            }
        }

        status.setText(repository.dataSourceLine());
    }

    private List<Fixture> filtered() {
        List<Fixture> visible = new ArrayList<>();
        long now = System.currentTimeMillis();
        Date today = new Date(now);
        DateFormat dayFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        String todayText = dayFormat.format(today);
        for (Fixture fixture : fixtures) {
            if ("scores".equals(filter) && fixture.isUpcoming()) {
                continue;
            }
            if ("today".equals(filter)) {
                Date fixtureDate = new Date(fixture.timestampSeconds * 1000L);
                if (!todayText.equals(dayFormat.format(fixtureDate))) {
                    continue;
                }
            }
            visible.add(fixture);
        }
        return visible;
    }

    private View card(Fixture fixture) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundResource(R.drawable.panel_background);
        card.setPadding(dp(14), dp(12), dp(14), dp(12));

        TextView date = text(fixture.statusLine(), 13, fixture.isLive() ? R.color.accent_dark : R.color.muted, false);
        card.addView(date, matchWrap());

        TextView match = text(fixture.scoreTitle(), 18, R.color.ink, true);
        match.setPadding(0, dp(4), 0, dp(4));
        card.addView(match, matchWrap());

        TextView location = text(fixture.locationLine(), 13, R.color.muted, false);
        card.addView(location, matchWrap());

        if (!fixture.round.isEmpty()) {
            TextView round = text(fixture.round, 12, R.color.gold, true);
            round.setPadding(0, dp(6), 0, 0);
            card.addView(round, matchWrap());
        }

        LinearLayout.LayoutParams params = matchWrap();
        params.setMargins(0, 0, 0, dp(10));
        card.setLayoutParams(params);
        return card;
    }

    private void addEmptyState() {
        TextView empty = text("No fixtures match this view yet.", 16, R.color.muted, false);
        empty.setGravity(Gravity.CENTER);
        empty.setPadding(dp(20), dp(40), dp(20), dp(40));
        list.addView(empty, matchWrap());
    }

    private void updateFilterButtons() {
        styleFilter(allButton, "all".equals(filter));
        styleFilter(todayButton, "today".equals(filter));
        styleFilter(scoresButton, "scores".equals(filter));
    }

    private void styleFilter(Button button, boolean selected) {
        button.setTextColor(selected ? Color.WHITE : color(R.color.ink));
        button.setBackgroundColor(selected ? color(R.color.accent) : color(R.color.panel));
    }

    private TextView text(String value, int sp, int colorRes, boolean bold) {
        TextView text = new TextView(this);
        text.setText(value);
        text.setTextSize(sp);
        text.setTextColor(color(colorRes));
        text.setIncludeFontPadding(true);
        if (bold) {
            text.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
        }
        return text;
    }

    private LinearLayout.LayoutParams matchWrap() {
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private LinearLayout.LayoutParams buttonWeight() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, dp(38), 1);
        params.setMargins(dp(3), 0, dp(3), 0);
        return params;
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private int color(int resId) {
        return getResources().getColor(resId);
    }
}
