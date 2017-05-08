package dk.group2.smap.assigment2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startWeatherService();

//        final ArrayList<Demo> demos = new ArrayList<>();
//        demos.add(new Demo(getString(R.string.pickerDemo),NumberPickerActivity.class.getName(),REQUEST_PICK_A_NUMBER));
//        demos.add(new Demo(getString(R.string.editTextDemo),EditTextsActivity.class.getName(),REQUEST_EDIT_TESTS));
//        demos.add(new Demo(getString(R.string.slidersDemo),SliderActivity.class.getName(),REQUEST_COlOR_SLIDER));
//        demos.add(new Demo(getString(R.string.pickerDemo),NumberPickerActivity.class.getName(),REQUEST_PICK_A_NUMBER));
//        demos.add(new Demo(getString(R.string.editTextDemo),EditTextsActivity.class.getName(),REQUEST_EDIT_TESTS));
//        demos.add(new Demo(getString(R.string.slidersDemo),SliderActivity.class.getName(),REQUEST_COlOR_SLIDER));
//        demos.add(new Demo(getString(R.string.pickerDemo),NumberPickerActivity.class.getName(),REQUEST_PICK_A_NUMBER));
//        demos.add(new Demo(getString(R.string.editTextDemo),EditTextsActivity.class.getName(),REQUEST_EDIT_TESTS));
//        demos.add(new Demo(getString(R.string.slidersDemo),SliderActivity.class.getName(),REQUEST_COlOR_SLIDER));
//        demos.add(new Demo(getString(R.string.pickerDemo),NumberPickerActivity.class.getName(),REQUEST_PICK_A_NUMBER));
//        demos.add(new Demo(getString(R.string.editTextDemo),EditTextsActivity.class.getName(),REQUEST_EDIT_TESTS));
//        demos.add(new Demo(getString(R.string.slidersDemo),SliderActivity.class.getName(),REQUEST_COlOR_SLIDER));
//        DemoAdapter demoAdapter = new DemoAdapter(this,demos);
//        ListView lw = (ListView) findViewById(R.id.demoListView);
//        lw.setAdapter(demoAdapter);
//        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Intent startDemoIntent = new Intent();
//                String action = demos.get(position).getIntentAction();
//                int resultCode = demos.get(position).getResultCode();
//                if(action != null && !action.equals("")){
//                    startDemoIntent.setAction(action);
//                    startActivityForResult(startDemoIntent,resultCode);
//                }
//            }
//        });
    }

    private void startWeatherService() {
        Intent backgroundServiceIntent = new Intent(MainActivity.this, WeatherService.class);
        startService(backgroundServiceIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
