package kz.evilteamgenius.chessapp.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import kz.evilteamgenius.chessapp.models.User;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static kz.evilteamgenius.chessapp.Constants.UrlForLogin;

public class LoginActivity extends AppCompatActivity implements OnClickListener {


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
    private String currentLanguage ="def",something, getlanguageStat;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        if (getToken()!=null&&!getToken().isEmpty()){
            gotoMainPage();
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
        ArrayAdapter mAdapter = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.language_option));
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
                    case 4 :
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
        getlanguageStat =getIntent().getStringExtra(something);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.signInButton:
                if (usernameEditText.getText() != null && passwordEditText.getText() != null) {
                    String username = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    new LoginUser().execute(username, password);
//                    loginFunction(username,password);
                } else {
                    showToast(getResources().getString(R.string.EmptyInput));
                }
                break;
            case R.id.createAccTextView:
                intent = new Intent(this, RegistrationActivity.class);
                startActivity(intent);
                break;
        }
    }

    // todo later need to realize
    private void loginFunction(String username, String password) {

        User user = new User(username,password);
        LoginLoader loginLoader = new LoginLoader(new LoginLoader.LoginCallback() {
            @Override
            public void onGetGoodsLoaded(String token) {
                if (!token.isEmpty()) {
                    //showToast(JWTtoken);
                    SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                    showToast(token);
                    preferences.edit().putString("token", token).apply();
                    preferences.edit().putString("username", username).apply();
                    gotoMainPage();
                    finish();
                } else {
                    showToast("Wrong Email or Password!");
                }
            }

            @Override
            public void onResponseFailed(String errorMessage) {

            }
        });
        loginLoader.loginUser(user);

    }

    public class LoginUser extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String username = strings[0];
            String password = strings[1];

            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("username", username)
                    .add("password", password)
                    .build();

            Request request = new Request.Builder()
                    .url(UrlForLogin)
                    .post(formBody)
                    .build();

            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    String JWTtoken = response.body().string();
                    if (!JWTtoken.isEmpty()) {
                        //showToast(JWTtoken);
                        SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                        preferences.edit().putString("token", JWTtoken).apply();
                        preferences.edit().putString("username", username).apply();
                        gotoMainPage();
                        finish();

                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
                                builder1.setMessage(R.string.errorEmailorLogin);
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        getResources().getText(R.string.okText),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void gotoMainPage(){
        stopService(new Intent(LoginActivity.this, BackGroundMusic.class));
        intent = new Intent(LoginActivity.this,
                KukaActivity.class);
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
            Toast.makeText(LoginActivity.this, getString(R.string.OOPS_TryAgain), Toast.LENGTH_SHORT).show();
        }
    }

    public void showToast(final String Text) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this,
                        Text, Toast.LENGTH_LONG).show();
            }
        });
    }
}
