package com.datayes.magnifyinglayout;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.datayes.magnifying.MagnifyingLayout;
import com.datayes.magnifying.MagnifyingLayoutConfiguration;
import com.datayes.magnifying.TextSelectionView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextSelectionView text = (TextSelectionView)findViewById(R.id.text);
        MagnifyingLayout container = (MagnifyingLayout)findViewById(R.id.container);
        container.initConfiguration(MagnifyingLayoutConfiguration.createDefault());
        text.setText(TextContent.content);
        text.setTextSelectListener(new TextSelectionView.TextSelectListener() {
            @Override
            public void onTextSelected(String selected) {
                Toast.makeText(MainActivity.this, "Select word: " + selected, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
