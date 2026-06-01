package com.dasp.worldcup2026;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

public final class ScheduleRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FixtureViewsFactory(getApplicationContext());
    }

    private static final class FixtureViewsFactory implements RemoteViewsFactory {
        private final Context context;
        private List<Fixture> fixtures = new ArrayList<>();

        FixtureViewsFactory(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate() {
            load();
        }

        @Override
        public void onDataSetChanged() {
            load();
        }

        @Override
        public void onDestroy() {
            fixtures.clear();
        }

        @Override
        public int getCount() {
            return Math.min(fixtures.size(), 8);
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.widget_schedule_row);
            Fixture fixture = fixtures.get(position);
            row.setTextViewText(R.id.widget_row_time, fixture.statusLine());
            row.setTextViewText(R.id.widget_row_match, fixture.scoreTitle());
            return row;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return fixtures.get(position).id;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        private void load() {
            List<Fixture> all = new FixtureRepository(context).cachedFixtures();
            fixtures = new ArrayList<>();
            long nowSeconds = System.currentTimeMillis() / 1000L;
            for (Fixture fixture : all) {
                if (fixture.isLive() || fixture.timestampSeconds >= nowSeconds) {
                    fixtures.add(fixture);
                }
            }
            if (fixtures.isEmpty()) {
                fixtures = all;
            }
        }
    }
}
