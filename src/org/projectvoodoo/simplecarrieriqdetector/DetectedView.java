
package org.projectvoodoo.simplecarrieriqdetector;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    static final int COLOR_ACTIVE = 0xff450A0A;
    static final int COLOR_INACTIVE = 0xff2E2B04;
    static final int COLOR_NOTFOUND = 0xff111111;

    private Detect detect;
    private DetectTest test;
    private LayoutInflater mInflater;

    private TextView title;
    private TextView confidenceLevel;
    private LinearLayout resultsLayout;
    private RelativeLayout root;

    private Resources resources;

    private ArrayList<String> lines = new ArrayList<String>();

    public DetectedView(Context context, Detect detect, DetectTest test) {
        super(context);
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
        // resultsLayout = (LinearLayout) findViewById(R.id.results_layout);
        confidenceLevel = (TextView) confidenceLayout.findViewById(R.id.confidence_level);

        title.setText(test.name);
        for (String line : detect.getFound().get(test)) {
            addResult(line);
            // addResultWithColor(line, Color.YELLOW);
            root.setBackgroundColor(COLOR_INACTIVE);
        }

        if (detect.getFound().get(test).size() == 0) {
            // addResult(resources.getString(R.string.nothing_found));
            root.setBackgroundColor(COLOR_NOTFOUND);
        }

        setConfidenceLevel(test.confidenceLevel);
    }

    public void addResult(String s) {
        lines.add(s);
        // TextView content = new TextView(getContext());
        // content.setText(s);
        // content.setPadding(8, 0, 8, 0);
        //
        // resultsLayout.addView(content);
    }

    public void setConfidenceLevel(int n) {
        confidenceLevel.setText(Integer.toString(n));
    }

}
