package com.dasp.worldcup2026;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.List;

public final class ScoreWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager manager, int[] appWidgetIds) {
        for (int id : appWidgetIds) {
            update(context, manager, id);
        }
    }

    private static void update(Context context, AppWidgetManager manager, int appWidgetId) {
        FixtureRepository repository = new FixtureRepository(context);
        Fixture fixture = selectFixture(repository.cachedFixtures());
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_score);
        if (fixture == null) {
            views.setTextViewText(R.id.score_match, "Open app to refresh");
            views.setTextViewText(R.id.score_status, "Bundled schedule unavailable");
        } else {
            views.setTextViewText(R.id.score_match, fixture.scoreTitle());
            views.setTextViewText(R.id.score_status, fixture.statusLine());
        }

        Intent openIntent = new Intent(context, MainActivity.class);
        PendingIntent openPendingIntent = PendingIntent.getActivity(
                context,
                appWidgetId,
                openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        views.setOnClickPendingIntent(R.id.score_match, openPendingIntent);

        Intent refreshIntent = new Intent(context, ScoreWidgetProvider.class);
        refreshIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
        PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(
                context,
                appWidgetId + 20000,
                refreshIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        views.setOnClickPendingIntent(R.id.score_refresh, refreshPendingIntent);
        manager.updateAppWidget(appWidgetId, views);
    }

    private static Fixture selectFixture(List<Fixture> fixtures) {
        if (fixtures.isEmpty()) {
            return null;
        }
        long nowSeconds = System.currentTimeMillis() / 1000L;
        Fixture latestFinished = null;
        Fixture nextUpcoming = null;
        for (Fixture fixture : fixtures) {
            if (fixture.isLive()) {
                return fixture;
            }
            if (fixture.isFinished() && fixture.timestampSeconds <= nowSeconds) {
                latestFinished = fixture;
            }
            if (nextUpcoming == null && fixture.timestampSeconds >= nowSeconds) {
                nextUpcoming = fixture;
            }
        }
        if (latestFinished != null) {
            return latestFinished;
        }
        return nextUpcoming == null ? fixtures.get(0) : nextUpcoming;
    }

}
