package kz.evilteamgenius.chessapp.activity;

import android.content.Intent;
import android.os.Bundle;

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

                    registrationInServer(userName.getText().toString()
                            , password.getText().toString()
                            , email.getText().toString());

                } else {
                    toast(this, getString(R.string.confirm_passwordText));
                }
            } else {
                toast(this, getString(R.string.InputEmailText));
            }
        } else {
            toast(this, getString(R.string.EmptyNameInputOrLess));
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
                toast(RegistrationActivity.this, getString(R.string.RegisterSuccess));
                Intent i = new Intent(RegistrationActivity.this,
                        LoginActivity.class);
                startActivity(i);
            }

            @Override
            public void onResponseFailed(String errorMessage) {
                toast(RegistrationActivity.this, getString(R.string.OOPS_TryAgain));
            }
        });
        loader.loadRegistration(user);

    }
}

