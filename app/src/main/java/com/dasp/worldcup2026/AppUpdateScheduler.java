package com.dasp.worldcup2026;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

final class AppUpdateScheduler {
    private static final int JOB_ID = 20260206;

    private AppUpdateScheduler() {
    }

    static void schedule(Context context) {
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (scheduler == null) {
            return;
        }

        JobInfo.Builder builder = new JobInfo.Builder(
                JOB_ID,
                new ComponentName(context, WorldCupUpdateJobService.class)
        )
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(FixtureRepository.UPDATE_INTERVAL_MS)
                .setPersisted(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setRequiresBatteryNotLow(true);
        }

        scheduler.schedule(builder.build());
    }
}
