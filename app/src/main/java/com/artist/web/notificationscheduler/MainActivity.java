package com.artist.web.notificationscheduler;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    RadioGroup mNetworkOptions;
    private JobScheduler mJobScheduler;
    private static final int JOB_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNetworkOptions = findViewById(R.id.networkOptions);
    }

    public void scheduleJob(View view){

        mJobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        int selectedNetworkId = mNetworkOptions.getCheckedRadioButtonId();
        int selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;

        switch(selectedNetworkId){
            case R.id.noNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
                break;
            case R.id.anyNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_ANY;
                break;
            case R.id.wifiNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_UNMETERED;
                break;
        }

        ComponentName serviceName = new ComponentName(getPackageName(),
                NotificationJobService.class.getName());
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceName);
        builder.setRequiredNetworkType(selectedNetworkOption);

        boolean constraintSet = selectedNetworkOption != JobInfo.NETWORK_TYPE_NONE;

        if(constraintSet) {

            //Schedule the job and notify the user
            JobInfo jobInfo = builder.build();
            mJobScheduler.schedule(jobInfo);

            Toast.makeText(this, "Job Scheduled, job will run when " +
                    "the constraints are met.", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Please set at least one constraint",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelJob(View view){

        if (mJobScheduler!=null){
            mJobScheduler.cancelAll();
            mJobScheduler = null;
            Toast.makeText(this, "Jobs cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}
