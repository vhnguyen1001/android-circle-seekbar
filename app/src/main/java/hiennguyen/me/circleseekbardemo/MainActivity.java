package hiennguyen.me.circleseekbardemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import hiennguyen.me.circleseekbar.CircleSeekBar;


public class MainActivity extends AppCompatActivity implements CircleSeekBar.OnSeekBarChangedListener {

    TextView point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title);
        toolbar.setTitleTextColor(Color.WHITE);
        CircleSeekBar circleSeekBar = findViewById(R.id.circular);
        circleSeekBar.setSeekBarChangeListener(this);
        point = findViewById(R.id.txt_point);
        circleSeekBar.setPoint(13);
    }

    @Override
    public void onPointsChanged(CircleSeekBar circleSeekBar, int points, boolean fromUser) {
        point.setText(String.valueOf(points));
    }

    @Override
    public void onStartTrackingTouch(CircleSeekBar circleSeekBar) {

    }

    @Override
    public void onStopTrackingTouch(CircleSeekBar circleSeekBar) {

    }
}
