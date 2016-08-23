package zhuoxin.lineng.mymusicplays.Activity.Activity.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import zhuoxin.lineng.mymusicplays.R;

public class MainActivity extends AppCompatActivity {
    TextView text_play2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text_play2= (TextView) findViewById(R.id.text_play2);
        text_play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,titleactivity.class);
                startActivity(intent);
            }
        });
    }


}
