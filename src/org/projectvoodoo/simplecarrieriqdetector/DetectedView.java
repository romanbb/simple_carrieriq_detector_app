
package org.projectvoodoo.simplecarrieriqdetector;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.projectvoodoo.simplecarrieriqdetector.Detect.DetectTest;

import java.util.ArrayList;

public class DetectedView extends LinearLayout {

    private static final LinearLayout.LayoutParams LAYOUT_PARAMS = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.FILL_PARENT, // width
            ViewGroup.LayoutParams.FILL_PARENT, // height
            0.0f // weight
    );

    private Context mContext;

    static final int COLOR_ACTIVE = 0xff450A0A;
    static final int COLOR_INACTIVE = 0xff2E2B04;
    static final int COLOR_NOTFOUND = 0xff111111;

    protected static final String TAG = "Voodoo";

    private Detect detect;
    private DetectTest test;
    private LayoutInflater mInflater;

    private TextView title;
    private TextView confidenceLevel;
    private LinearLayout resultsLayout;
    private RelativeLayout root;
    private ImageView icon;

    private Resources resources;

    private ArrayList<String> lines = new ArrayList<String>();

    public DetectedView(Context context, Detect detect, DetectTest test) {
        super(context);
        mContext = context;
        this.test = test;
        this.detect = detect;

        resources = context.getResources();

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        init();
    }

    private void init() {
        View callView = mInflater.inflate(R.layout.detected_view, null, false);
        addView(callView, LAYOUT_PARAMS);

        LinearLayout confidenceLayout = (LinearLayout) findViewById(R.id.confidence_layout);

        root = (RelativeLayout) findViewById(R.id.relative_root);

        title = (TextView) findViewById(R.id.title);
        title.setText(test.name);

        icon = (ImageView) findViewById(R.id.info_image);
        icon.setOnClickListener(buttonOnClickListener);

        // resultsLayout = (LinearLayout) findViewById(R.id.results_layout);
        confidenceLevel = (TextView) confidenceLayout.findViewById(R.id.confidence_level);

        for (String line : detect.getFound().get(test)) {
            addResult(line);
        }

        if (detect.getFound().get(test).size() == 0) { // gray for nothing
            root.setBackgroundColor(COLOR_NOTFOUND);
        } else if (test.toString().equals(DetectTest.RUNNING_PROCESSES.name())
                && detect.getFound().size() > 0) { // process is active, color
                                                   // red
            root.setBackgroundColor(COLOR_ACTIVE);
        } else { // detected, not running, yellow
            root.setBackgroundColor(COLOR_INACTIVE);
        }

        setConfidenceLevel(test.confidenceLevel);
    }

    public void addResult(String s) {
        lines.add(s);
    }

    public void setConfidenceLevel(int n) {
        confidenceLevel.setText(Integer.toString(n));
    }

    private OnClickListener buttonOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.e(TAG, "Lines: " + lines.size());
            Dialog d = new Dialog(mContext);

            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.setContentView(R.layout.test_dialog);
            d.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

            TextView testTitle = (TextView) d.findViewById(R.id.title);
            testTitle.setText(test.name);

            TextView testDescription = (TextView) d.findViewById(R.id.text_description);
            testDescription.setText(test.description);

            LinearLayout layout = (LinearLayout) d.findViewById(R.id.details_list);

            if (lines.isEmpty()) {
                TextView content = new TextView(getContext());
                content.setText("Nothing found");
                content.setPadding(8, 0, 12, 8);
                layout.addView(content);
            } else {
                for (String s : lines) {
                    TextView content = new TextView(getContext());
                    content.setText(s);
                    content.setPadding(8, 0, 12, 8);
                    layout.addView(content);
                }
            }

            d.show();

        }
    };

}
