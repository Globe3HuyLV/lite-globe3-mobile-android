package com.globe3.tno.g3_lite_mobile.activities;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.globe3.tno.g3_lite_mobile.R;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();
    public BaseActivity base_activity;
    public RelativeLayout layout_base_loader;
    public ProgressBar progress_loader;
    public RelativeLayout layout_main;

    public BaseActivity() {
        super();
        base_activity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        layout_base_loader = (RelativeLayout) findViewById(R.id.layout_base_loader);
        progress_loader = (ProgressBar) findViewById(R.id.progress_loader);
        layout_main = (RelativeLayout) findViewById(R.id.layout_main);
        LoaderTask loaderTask = new LoaderTask();
        loaderTask.execute();
    }

    /**
     * Loading configurations or permissions if it is necessary.
     */
    public abstract void onActivityLoading();

    /**
     * Loading configurations or permissions completed.
     */
    public abstract void onActivityReady(
    );

    private class LoaderTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            layout_base_loader.setVisibility(View.VISIBLE);
            layout_main.setVisibility(View.GONE);
            Log.d(TAG, "layout_main gone!");
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Log.d(TAG, "doInBackground()");
                onActivityLoading();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Log.d(TAG, "onPostExecute()");
                onActivityPostLoading();
            } else {
                onActivityError();
            }
        }
    }

    private void onActivityPostLoading() {
        layout_base_loader.setVisibility(View.GONE);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                layout_main.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(base_activity, R.anim.animate_fade_in);
                layout_main.setAnimation(animation);
                onActivityReady();
                Log.d(TAG, "onActivityReady()");
            }
        });
    }

    private void onActivityError() {
        AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder(base_activity)
                        .setMessage(getString(R.string.msg_an_error_has_occured))
                        .setNegativeButton(getString(R.string.label_exit), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                finish();
                            }
                        });
        alertDialogBuilder.show();
    }

}
