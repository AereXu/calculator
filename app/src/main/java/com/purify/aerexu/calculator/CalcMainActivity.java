package com.purify.aerexu.calculator;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.purify.aerexu.calculator.entity.CalcStrings;


public class CalcMainActivity extends Activity
        implements CalcFragButtons.OnButtonClickedListener {
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_main);
        fm = getFragmentManager();
        if (fm.findFragmentById(R.id.buttons_container) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.buttons_container, new CalcFragButtons());
            ft.commit();
        }
        if (fm.findFragmentById(R.id.viewport_container) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.viewport_container, new CalcFragViewport());
            ft.commit();
        }
    }

    @Override
    public void onButtonClicked(CalcStrings calcResult) {
        CalcFragViewport calcFragViewport = (CalcFragViewport)fm.findFragmentById(R.id.viewport_container);
        calcFragViewport.updateCalcResult(calcResult);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calc_main, menu);
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
