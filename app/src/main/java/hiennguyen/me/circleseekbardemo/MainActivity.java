package hiennguyen.me.circleseekbardemo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import hiennguyen.me.circleseekbar.CircleSeekBar;


public class MainActivity extends AppCompatActivity implements CircleSeekBar.OnSeekBarChangedListener {

    EditText point;
    Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title);
        toolbar.setTitleTextColor(Color.WHITE);
        final CircleSeekBar circleSeekBar = findViewById(R.id.circular);
        circleSeekBar.setSeekBarChangeListener(this);
        point = findViewById(R.id.txt_point);
        change = findViewById(R.id.change_button);
        circleSeekBar.setProgressDisplayAndInvalidate(13);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(point.getText().length() != 0) {
                    circleSeekBar.setProgressDisplayAndInvalidate(Integer.valueOf(point.getText().toString()));
                }
            }
        });
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
