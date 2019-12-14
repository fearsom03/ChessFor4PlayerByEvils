package kz.evilteamgenius.chessapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kz.evilteamgenius.chessapp.R;
import kz.evilteamgenius.chessapp.api.loaders.RegistrationLoader;
import kz.evilteamgenius.chessapp.api.responses.ResponseForRegistration;
import kz.evilteamgenius.chessapp.models.RegisterMyUser;
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
                    registrationInServer(userName.getText().toString(),
                            password.getText().toString(),email.getText().toString());

                }else {
                    Toast.makeText(this,getString(R.string.confirm_passwordText),Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this,getString(R.string.InputEmailText),Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this,getString(R.string.EmptyNameInputOrLess),Toast.LENGTH_SHORT).show();
        }
    }

    private void registrationInServer(String login,String pass, String email) {
        RegisterMyUser user = new RegisterMyUser();
        user.setLogin(login);
        user.setPass(pass);
        user.setPassCheck(pass);

        RegistrationLoader loader = new RegistrationLoader(new RegistrationLoader.GetRegistrationLoaderCallback() {
            @Override
            public void onGetGoodsLoaded(ResponseForRegistration responseForRegistration) {
                        showToast(getString(R.string.RegisterSuccess));
                        Intent i = new Intent(RegistrationActivity.this,
                                LoginActivity.class);
                        startActivity(i);
            }

            @Override
            public void onResponseFailed(String errorMessage) {
                        showToast(getString(R.string.OOPS_TryAgain));
            }
        });
        loader.loadRegistration(user);

    }

    public void showToast(final String Text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegistrationActivity.this,
                        Text, Toast.LENGTH_LONG).show();
            }
        });
    }
    /*

    {
    "status": "error",
    "content": [
        [
            "username.is.taken"
        ]
    ]
}
{
    "status": "ok",
    "content": []
}
     */
}

