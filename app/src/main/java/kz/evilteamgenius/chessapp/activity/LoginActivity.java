package kz.evilteamgenius.chessapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.evilteamgenius.chessapp.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.signInButton) Button signInButton;
    @BindView(R.id.createAccTextView)
    TextView createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {
        signInButton.setOnClickListener(this);
        createAccount.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent ;
        switch (view.getId()){
            case R.id.signInButton:
                 intent = new Intent(this, MainAppPage.class);
                 startActivity(intent);
                break;
            case R.id.createAccTextView:
                intent = new Intent(this, RegistrationActivity.class);
                startActivity(intent);
                break;
        }
    }
}
