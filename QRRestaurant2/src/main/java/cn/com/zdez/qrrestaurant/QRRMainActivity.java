package cn.com.zdez.qrrestaurant;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.zxing.client.android.CaptureActivity;

import cn.com.zdez.qrrestaurant.helper.ActivityContrl;
import cn.com.zdez.qrrestaurant.utils.ConnectivityUtil;
import cn.com.zdez.qrrestaurant.utils.MyLog;
import cn.com.zdez.qrrestaurant.utils.ToastUtil;
import cn.com.zdez.qrrestaurant.websockets.AutobahnTest;

public class QRRMainActivity extends ActionBarActivity {

    private Button btn_to_scanqr;
    private Button btn_to_chooseR;
    private Button btn_to_booked;
    ActionBar actionBar;
    private boolean doubleBackToExitPressedOnce = false;

    private static String TAG = QRRMainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityContrl.add(this);
        actionBar = getSupportActionBar();

        btn_to_scanqr = (Button) findViewById(R.id.btn_to_scanqr);
        btn_to_chooseR = (Button) findViewById(R.id.btn_to_choose);
        btn_to_booked = (Button) findViewById(R.id.btn_to_booked);

        if (!ConnectivityUtil.isOnline(this)) {
            actionBar.setTitle(getResources().getString(R.string.app_name) + "(未连接)");
        }

        MyLog.d(TAG, "Main activity Created");

        btn_to_chooseR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent toChoose = new Intent();
                // Start to scan
                toChoose.setClass(QRRMainActivity.this, RestaurantChooserActivity.class);
                startActivity(toChoose);
            }
        });

        btn_to_scanqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toScan = new Intent();
                toScan.setClass(QRRMainActivity.this, CaptureActivity.class);
                startActivity(toScan);
            }
        });

        btn_to_booked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutobahnTest.start();
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


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        ToastUtil.showShortToast(this, "再按一次退出");
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;

            }
        }, 2000);
    }
}
