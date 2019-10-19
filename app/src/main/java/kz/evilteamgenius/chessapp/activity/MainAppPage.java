package kz.evilteamgenius.chessapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import kz.evilteamgenius.chessapp.R;

public class MainAppPage extends AppCompatActivity implements View.OnClickListener {

    private TextView play,option,community,rules;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main_page);
        initUI();
    }

    private void initUI() {
        play = findViewById(R.id.playText);
        option = findViewById(R.id.optionText);
        community=findViewById(R.id.communityText);
        rules=findViewById(R.id.rulesText);

        play.setOnClickListener(this);
        option.setOnClickListener(this);
        community.setOnClickListener(this);
        rules.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.playText:
                break;
            case R.id.optionText:
                break;
            case R.id.communityText:
                break;
            case R.id.rulesText:
                break;
        }
    }
}
