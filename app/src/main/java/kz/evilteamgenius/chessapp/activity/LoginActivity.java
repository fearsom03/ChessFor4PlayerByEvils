package kz.evilteamgenius.chessapp.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.evilteamgenius.chessapp.R;
import kz.evilteamgenius.chessapp.api.loaders.LoginLoader;
import kz.evilteamgenius.chessapp.databinding.ActivityLoginBinding;
import kz.evilteamgenius.chessapp.models.BackGroundMusic;
import kz.evilteamgenius.chessapp.models.RegisterMyUser;
import timber.log.Timber;

import static kz.evilteamgenius.chessapp.extensions.ViewExtensionsKt.toast;

public class LoginActivity extends AppCompatActivity implements OnClickListener, LoginLoader.LoginCallback {


    @BindView(R.id.signInButton)
    MaterialButton signInButton;
    @BindView(R.id.createAccTextView)
    TextView createAccount;
    @BindView(R.id.username)
    TextInputEditText usernameEditText;
    @BindView(R.id.password)
    TextInputEditText passwordEditText;
    @BindView(R.id.spLanguage)
    Spinner spinner;

    private Locale myLocale;
    private String currentLanguage = "def", something, getlanguageStat;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        if (getToken() != null && !getToken().isEmpty()) {
            goToMainPage();
        }
        startService(new Intent(LoginActivity.this, BackGroundMusic.class));
        RegisterMyUser user = new RegisterMyUser();
        user.setLogin("testkuka");
        user.setPass("12345qw");
        user.setPassCheck("12345qw");
        binding.setUser(user);
        ButterKnife.bind(this);
        initUI();
        spinnerInit();
    }

    private void spinnerInit() {
        ArrayAdapter mAdapter = new ArrayAdapter<>(LoginActivity.this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.language_option));
        spinner.setAdapter(mAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 1:
                        setLocale("def");
                        break;
                    case 2:
                        setLocale("kk");
                        break;
                    case 3:
                        setLocale("ru");
                        break;
                    case 4:
                        setLocale("zh");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initUI() {
        signInButton.setOnClickListener(this);
        createAccount.setOnClickListener(this);
        getlanguageStat = getIntent().getStringExtra(something);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.signInButton:
                if (usernameEditText.getText() != null
                        && passwordEditText.getText() != null
                        && !usernameEditText.getText().toString().isEmpty()
                        && !passwordEditText.getText().toString().isEmpty()
                ) {
                    String username = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    loginFunction(username, password);
                } else {
                    toast(this, getString(R.string.EmptyInput));
                }
                break;
            case R.id.createAccTextView:
                intent = new Intent(this, RegistrationActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void loginFunction(String username, String password) {
        LoginLoader loginLoader = new LoginLoader(this);
        loginLoader.loginUser(username, password);
    }

    private void goToMainPage() {
        stopService(new Intent(LoginActivity.this, BackGroundMusic.class));
        intent = new Intent(LoginActivity.this,
                MainActivity.class);
        startActivity(intent);
        finish();
    }

    public String getToken() {
        SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        return preferences.getString("token", null);
    }

    private void setLocale(String localeName) {
        if (!localeName.equals(getlanguageStat)) {
            myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            intent = new Intent(this, LoginActivity.class);
            intent.putExtra(something, conf.locale.getLanguage());
            startActivity(intent);
        } else {
            toast(this, getString(R.string.OOPS_TryAgain));
        }
    }

    @Override
    public void onGetGoodsLoaded(String responseForRegistration, String username) {
        if (!responseForRegistration.isEmpty()) {
            SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
            preferences.edit().putString("token", responseForRegistration).apply();
            preferences.edit().putString("username", username).apply();
            goToMainPage();
            finish();
        }
    }

    @Override
    public void onResponseFailed(String errorMessage) {
        Timber.e(errorMessage);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
        builder1.setMessage(R.string.errorLoginOrPass);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                getResources().getText(R.string.okText),
                (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
