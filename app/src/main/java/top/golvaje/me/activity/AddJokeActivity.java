package top.golvaje.me.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import top.golvaje.me.R;

/**
 * Created by Administrator on 7/14/2017.
 */

public class AddJokeActivity extends Activity implements View.OnClickListener{

    Button submitJoke;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addjoke);

        submitJoke = (Button) this.findViewById(R.id.submit_jokes);
        submitJoke.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.submit_jokes:
                finish();
                break;
        }
    }
}
