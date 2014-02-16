package com.example.codedaykcrunningapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

public class LoadingListener extends AsyncTask<String, Integer, Integer> {
	
	public interface LoadingTaskListener {
		void onTaskFinished(); // If you want to pass something back to the listener add a param to this method
	}
	
	// This is the progress bar you want to update while the task is in progress
    private final ProgressBar progressBar;
    // This is the listener that will be told when this task is finished
    private final LoadingTaskListener finishedListener;
 
    /**
     * A Loading task that will load some resources that are necessary for the app to start
     * @param progressBar - the progress bar you want to update while the task is in progress
     * @param finishedListener - the listener that will be told when this task is finished
     */
    public LoadingListener(ProgressBar progressBar, LoadingTaskListener finishedListener) {
        this.progressBar = progressBar;
        this.finishedListener = finishedListener;
    }
 
    @Override
    protected Integer doInBackground(String... params) {
        Log.i("Tutorial", "Starting task with url: "+params[0]);

        downloadResources();
        return 1;
    } 
 
    private void downloadResources() {
       
    	
    	
    }
 
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]); // This is ran on the UI thread so it is ok to update our progress bar ( a UI view ) here
    }
 
    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        finishedListener.onTaskFinished(); // Tell whoever was listening we have finished
    }
}
