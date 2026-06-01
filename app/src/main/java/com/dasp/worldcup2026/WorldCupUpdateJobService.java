package com.dasp.worldcup2026;

import android.app.job.JobParameters;
import android.app.job.JobService;

public final class WorldCupUpdateJobService extends JobService {
    @Override
    public boolean onStartJob(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean shouldReschedule = false;
                try {
                    new FixtureRepository(WorldCupUpdateJobService.this).refreshIfStale();
                } catch (Exception exception) {
                    shouldReschedule = true;
                    new FixtureRepository(WorldCupUpdateJobService.this).saveError(exception.getMessage());
                }
                jobFinished(params, shouldReschedule);
            }
        }).start();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
