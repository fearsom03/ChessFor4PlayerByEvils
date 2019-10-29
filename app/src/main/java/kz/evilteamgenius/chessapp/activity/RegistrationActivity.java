package kz.evilteamgenius.chessapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kz.evilteamgenius.chessapp.R;
import timber.log.Timber;

public class RegistrationActivity extends AppCompatActivity {

    @BindView(R.id.userNameRegistration)
    TextInputEditText userName;
    @BindView(R.id.emailRegistration)
    TextInputEditText email;
    @BindView(R.id.passwordRegistration)
    TextInputEditText password;
    @BindView(R.id.passwordConfRegistration)
    TextInputEditText passwordConf;
    @BindView(R.id.signInButtonRegistration)
    MaterialButton signInButtom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.signInButtonRegistration)
    public void onRegisterClicked() {
        if (userName.getText()!=null && userName.getText().length()>4){
            if (email.getText()!=null && email.getText().length()>1){
                if (password.getText()!=null && passwordConf.getText()!=null
                        && password.getText().toString().equals(passwordConf.getText().toString())){
                    registrationInServer();

                }else {
                    Toast.makeText(this,"Confirm your password",Toast.LENGTH_SHORT).show();
                }

            }else {
                Toast.makeText(this,"Input email",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this,"Name is empty or less that 4 letters",Toast.LENGTH_SHORT).show();
        }
    }

    private void registrationInServer() {
        Timber.d("You've registered and go to another page");
        Intent intent = new Intent(this,MainAppPage.class);
        startActivity(intent);
    }
}

