package kz.evilteamgenius.chessapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import kz.evilteamgenius.chessapp.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signInButtom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        signInButtom = findViewById(R.id.signInButtom);
        signInButtom.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signInButtom:
                Intent intent = new Intent(this, MainAppPage.class);
                startActivity(intent);
                break;
        }
    }
}
