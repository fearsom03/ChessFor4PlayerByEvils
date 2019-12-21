package kz.evilteamgenius.chessapp.activity;

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
import kz.evilteamgenius.chessapp.databinding.ActivityLoginBinding;
import kz.evilteamgenius.chessapp.models.RegisterMyUser;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
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
        Intent intent;
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

    // todo leter need to realize
    private void loginFunction(String username, String password) {

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
                        Intent i = new Intent(LoginActivity.this,
                                MainAppPage.class);
                        startActivity(i);
                        finish();
                    } else {
                        showToast("Wrong Email or Password!");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void setLocale(String localeName) {
        if (!localeName.equals(getlanguageStat)) {
            myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(this, LoginActivity.class);
            refresh.putExtra(something, conf.locale.getLanguage());
            startActivity(refresh);
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
