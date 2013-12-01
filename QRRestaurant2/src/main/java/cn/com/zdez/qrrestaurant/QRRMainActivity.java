package cn.com.zdez.qrrestaurant;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;

public class QRRMainActivity extends ActionBarActivity {

    private Button btn_to_scanqr;
    private Button btn_to_chooseR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_to_scanqr = (Button) findViewById(R.id.btn_to_scanqr);
        btn_to_chooseR = (Button) findViewById(R.id.btn_to_choose);

        btn_to_chooseR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent startScan = new Intent();
                // Start to scan
                startScan.setClass(QRRMainActivity.this, RestaurantChooserActivity.class);
                startActivity(startScan);
            }
        });

        btn_to_scanqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start Restaurant chooser
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.qrrmain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}