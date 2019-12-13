package kz.evilteamgenius.chessapp.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.evilteamgenius.chessapp.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static kz.evilteamgenius.chessapp.Constants.UrlForLogin;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.signInButton)
    MaterialButton signInButton;
    @BindView(R.id.createAccTextView)
    TextView createAccount;
    @BindView(R.id.username)
    TextInputEditText usernameEditText;
    @BindView(R.id.password)
    TextInputEditText passwordEditText;

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
        Intent intent;
        switch (view.getId()) {
            case R.id.signInButton:
                if (usernameEditText.getText()!=null && passwordEditText.getText()!=null){
                    String username = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    new LoginUser().execute(username, password);
//                    loginFunction(username,password);
                }else {
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
            try{
                response = okHttpClient.newCall(request).execute();
                if(response.isSuccessful()){
                    String JWTtoken = response.body().string();
                    if(!JWTtoken.isEmpty()){
                        showToast(JWTtoken);
                        Intent i = new Intent(LoginActivity.this,
                                MainAppPage.class);
                        startActivity(i);
                        finish();
                    }else{
                        showToast("Wrong Email or Password!");
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    public void showToast(final String Text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this,
                        Text, Toast.LENGTH_LONG).show();
            }
        });
    }
}
