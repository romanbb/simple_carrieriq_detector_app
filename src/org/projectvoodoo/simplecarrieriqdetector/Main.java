
package org.projectvoodoo.simplecarrieriqdetector;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.projectvoodoo.simplecarrieriqdetector.Detect.DetectTest;

public class Main extends Activity {

    private DetectorTask dt = new DetectorTask();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        // run asynchronously the detection stuff
        dt = new DetectorTask();
        dt.execute();
    }

    private class DetectorTask extends AsyncTask<Void, Integer, Integer> {

        private Detect detect;

        @Override
        protected void onPostExecute(Integer detectionScore) {
            super.onPostExecute(detectionScore);

            ProgressBar pb = (ProgressBar) findViewById(R.id.detectionProgress);
            pb.setVisibility(View.GONE);

            TextView resultDisplay = (TextView) findViewById(R.id.result_display);
            if (detectionScore == 0) {
                resultDisplay.setText(R.string.not_found);
                resultDisplay.setTextColor(Color.GREEN);
            } else if (detect.getFound().get(DetectTest.RUNNING_PROCESSES).size() > 0) {
                resultDisplay.setText(R.string.found_active);
                resultDisplay.setTextColor(Color.RED);
            } else {
                resultDisplay.setText(R.string.found_inactive);
                resultDisplay.setTextColor(Color.YELLOW);
            }

            TextView numericScore = (TextView) findViewById(R.id.numeric_score);
            if (detectionScore > 0)
                numericScore.setText("detection score (less is better): " + detectionScore);
            else
                numericScore.setVisibility(View.GONE);

            LinearLayout details = (LinearLayout)
                    findViewById(R.id.details_list);

            for (DetectTest test : detect.getFound().keySet()) {
                DetectedView detectedView = new DetectedView(getApplicationContext(), detect, test);
                details.addView(detectedView);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgressBar pb = (ProgressBar) findViewById(R.id.detectionProgress);
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            ProgressBar pb = (ProgressBar) findViewById(R.id.detectionProgress);
            pb.setProgress(0);
            pb.setVisibility(View.INVISIBLE);
        }

        protected Integer doInBackground(Void... params) {

            detect = new Detect(getApplicationContext());
            detect.findEverything();
            detect.dumpFoundInLogcat();

            return detect.getDetectionScore();
        }

    }
}
