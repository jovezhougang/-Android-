package com.csii.simpleone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ShareActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String text = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        TextView textView = new TextView(getApplicationContext());
        textView.setText(text);
        setContentView(textView);
    }
}
