package dk.group2.smap.shinemyroom.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import dk.group2.smap.shinemyroom.R;

public class LampActivity extends AppCompatActivity {

    SeekBar seekBarGreen;
    SeekBar seekBarBlue;
    SeekBar seekBarRed;
    TextView colorField;
    Button ok;
    Button cancel;
    int red = 0;
    int green = 0;
    int blue = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamp);
        colorField = (TextView)findViewById(R.id.color);
        seekBarBlue = (SeekBar) findViewById(R.id.seekBarBlue);
        seekBarGreen = (SeekBar) findViewById(R.id.seekBarGreen);
        seekBarRed = (SeekBar) findViewById(R.id.seekBarRed);

        seekBarBlue.setMax(255);
        seekBarGreen.setMax(255);
        seekBarRed.setMax(255);

        seekBarBlue.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
        seekBarBlue.getThumb().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);
        seekBarGreen.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        seekBarGreen.getThumb().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
        seekBarRed.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        seekBarRed.getThumb().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);

        ok = (Button) findViewById(R.id.buttonOk);
        cancel = (Button) findViewById(R.id.buttonCancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("green_key", green);
                i.putExtra("blue_key", blue);
                i.putExtra("red_key",red);
                setResult(RESULT_OK, i);
                finish();
            }
        });
//
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });


        seekBarRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                red = progress;
                updateBackgroundColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                green = progress;
                updateBackgroundColor();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blue = progress;
                updateBackgroundColor();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void updateBackgroundColor(){
        int newColor = Color.rgb(red, green, blue);
        colorField.setBackgroundColor(newColor);
    }
}
