package kz.evilteamgenius.chessapp.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kz.evilteamgenius.chessapp.R;

import static kz.evilteamgenius.chessapp.extensions.CheckExtensionKt.isValidEmailAddress;
import static kz.evilteamgenius.chessapp.extensions.ViewExtensionsKt.toast;

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

    {
        showToast(getString(R.string.InputEmailText));
    } else

    @OnClick(R.id.signInButtonRegistration)
    public void onRegisterClicked() {
        if (userName.getText() != null && userName.getText().length() > 4) {
            if (email.getText() != null
                    && isValidEmailAddress(email.getText().toString())) {
                if (password.getText() != null
                        && passwordConf.getText() != null
                        && password.getText()
                        .toString()
                        .equals(passwordConf.getText().toString())) {
<<<<<<<HEAD
                            =======

>>>>>>>origin / websocket
                    registrationInServer(userName.getText().toString()
                            , password.getText().toString()
                            , email.getText().toString());

                } else {
<<<<<<<HEAD
                    toast(this, getString(R.string.confirm_passwordText));
                }
            } else {
                toast(this, getString(R.string.InputEmailText));
            }
        } else {
            toast(this, getString(R.string.EmptyNameInputOrLess));
=======
            showToast(getString(R.string.confirm_passwordText));
        }
    }
} else{
        showToast(getString(R.string.EmptyNameInputOrLess));
        >>>>>>>origin/websocket
        }
    }

    private void registrationInServer(String login, String pass, String email) {
        RegisterMyUser user = new RegisterMyUser();
        user.setLogin(login);
        user.setPass(pass);
        user.setPassCheck(pass);

        RegistrationLoader loader = new RegistrationLoader(new RegistrationLoader.GetRegistrationLoaderCallback() {
            @Override
            public void onGetGoodsLoaded(ResponseForRegistration responseForRegistration) {
        toast(RegistrationActivity.this,getString(R.string.RegisterSuccess));
                Intent i = new Intent(RegistrationActivity.this,
                        LoginActivity.class);
                startActivity(i);
            }

            @Override
            public void onResponseFailed(String errorMessage) {
        toast(RegistrationActivity.this,getString(R.string.OOPS_TryAgain));
            }
        });
        loader.loadRegistration(user);

    }
        <<<<<<<HEAD
=======

    public void showToast(final String Text) {
        this.runOnUiThread(() -> Toast.makeText(RegistrationActivity.this,
                Text, Toast.LENGTH_LONG).show());
    }

public boolean isValidEmailAddress(String email){
        String ePattern="^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p=java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m=p.matcher(email);
        return m.matches();
        }
        >>>>>>>origin/websocket
}

