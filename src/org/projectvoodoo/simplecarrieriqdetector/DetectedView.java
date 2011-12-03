
package org.projectvoodoo.simplecarrieriqdetector;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.projectvoodoo.simplecarrieriqdetector.Detect.DetectTest;

public class DetectedView extends LinearLayout {

    private static final LinearLayout.LayoutParams LAYOUT_PARAMS = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.FILL_PARENT,
            2.0f // weight = 1
    );

    private Detect detect;
    private DetectTest test;
    private LayoutInflater mInflater;

    private TextView title;
    private TextView confidenceLevel;
    private LinearLayout resultsLayout;

    private Resources resources;

    private boolean showWhenEmpty = true;

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

        title = (TextView) findViewById(R.id.title);
        resultsLayout = (LinearLayout) findViewById(R.id.results_layout);
        confidenceLevel = (TextView) confidenceLayout.findViewById(R.id.confidence_level);

        title.setText(test.name());
        for (String line : detect.getFound().get(test)) {
            // addResult(line);
            addResultWithColor(line, Color.YELLOW);
        }

        if (detect.getFound().get(test).size() == 0 && showWhenEmpty) {
            addResult(resources.getString(R.string.nothing_found));
        }

        setConfidenceLevel(test.confidenceLevel);
    }

    public void addResult(String s) {
        TextView content = new TextView(getContext());
        content.setText(s);
        content.setPadding(8, 0, 8, 0);

        resultsLayout.addView(content);
    }

    public void addResultWithColor(String s, int c) {
        TextView content = new TextView(getContext());
        content.setText(s);
        content.setPadding(8, 0, 8, 0);
        content.setTextColor(c);

        resultsLayout.addView(content);
    }

    public void setConfidenceLevel(int n) {
        confidenceLevel.setText(Integer.toString(n));
    }

}
