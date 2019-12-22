package kz.evilteamgenius.chessapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kz.evilteamgenius.chessapp.R;
import kz.evilteamgenius.chessapp.adapters.SliderAdapter;
import kz.evilteamgenius.chessapp.api.loaders.MakeNewGameLoader;
import kz.evilteamgenius.chessapp.models.Game;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import static kz.evilteamgenius.chessapp.Constants.URL_GET_LATEST_MOVE;
import static kz.evilteamgenius.chessapp.Constants.URL_NEW_GAME;
import static kz.evilteamgenius.chessapp.Constants.URL_TEST;

public class MainAppPage extends AppCompatActivity implements OnClickListener {

    @BindView(R.id.imageSlider)
    SliderView sliderView;
    @BindView(R.id.playText)
    TextView playText;
    @BindView(R.id.communityText)
    TextView communityText;
    @BindView(R.id.optionText)
    TextView optionText;
    @BindView(R.id.rulesText)
    TextView rulesText;

    private ArrayList<String> imageLinks;
    private SliderAdapter adapter;
    final Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            try {
                GetLatestMove getLatestMove = new GetLatestMove();
                // PerformBackgroundTask this class is the class that extends AsynchTask
                getLatestMove.execute();
            } catch (Exception e) {
                // TODO Auto-generated catch block
            }
        }
    };
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main_page);
        ButterKnife.bind(this);
        initData();
        initUI();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //handler.removeCallbacks(runnable);
    }

    private void initData() {
        //add advertisment images
        imageLinks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            imageLinks.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRs3w8x_Cglsbfajw6tdrjCks32sG5uKy17KrovdzdGLsbYguGg9A");
            imageLinks.add("https://3.bp.blogspot.com/-TjJ4W4oX4Tw/XNMG9K9jCZI/AAAAAAAABEs/_TYPxEpjjbE6YfHW6YEGsZgNx_KW4DYIwCLcBGAs/s1600/Describe%2Ban%2Badvertisement%2Bthat%2Byou%2Bremember%2BIELTS%2Bcue%2Bcard.png");
            imageLinks.add("https://i.pinimg.com/originals/33/04/8a/33048aa1286f6ea64f687174ba639271.jpg");
            imageLinks.add("https://content.fortune.com/wp-content/uploads/2017/05/gettyimages-84709618.jpg");
        }

    }

    private void initUI() {
        adapter = new SliderAdapter(this.getApplicationContext());
        adapter.setImages(imageLinks);
        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3); //set scroll delay in seconds :
        sliderView.startAutoCycle();
    }

    @OnClick(R.id.playText)
    void GoToGame(){
        makeNewGame();
        callAsynchronousTask();
    }

    //test to output token, replace it once finish community page
    @OnClick(R.id.communityText)
    void showToken(){

        showToast(getToken());
    }

    //test ping server
    @OnClick(R.id.optionText)
    void testPingServer(){
        new TestCode().execute();
    }

    //test make new game
    @OnClick(R.id.rulesText)
    void testToMakeGame(){

    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.playText:
                showToast("Play Clicked!");
                intent = new Intent(this, GameActivity.class);
                startActivity(intent);
                break;
            case R.id.communityText:
                showToast("Community Clicked!");
                break;
        }
    }

    public void showToast(final String Text) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainAppPage.this,
                        Text, Toast.LENGTH_LONG).show();
                System.out.println(Text);

            }
        });
    }

    public class TestCode extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            OkHttpClient okHttpClient = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL_TEST)
                    .get()
                    .build();

            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    if (!result.isEmpty()) {

                        showToast(result);
//                        Intent i = new Intent(GameActivity.this,
//                                MainAppPage.class);
//                        startActivity(i);
//                        finish();
                    } else {
                        showToast("Test Ping Failed");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class MakeNewGame extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
            String token = preferences.getString("token","");
            showToast(token);

            OkHttpClient okHttpClient = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL_NEW_GAME)
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Authorization" , "Bearer " + token)
                    .build();

            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    if (!result.isEmpty()) {

                        JSONObject jsonObject = new JSONObject(result);
                        Game game = new Game(jsonObject.getLong("id"),
                                jsonObject.getString("white"),
                                jsonObject.getString("black"),
                                jsonObject.getString("fen"),
                                jsonObject.getString("result"),
                                jsonObject.getInt("from_x"),
                                jsonObject.getInt("from_y"),
                                jsonObject.getInt("to_x"),
                                jsonObject.getInt("to_y"),
                                jsonObject.getString("next_move"));
                        showToast(game.toString());
//                        Intent i = new Intent(GameActivity.this,
//                                MainAppPage.class);
//                        startActivity(i);
//                        finish();
                    } else {
                        showToast("Test Ping Failed");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class GetLatestMove extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
            String token = preferences.getString("token","");
            showToast(token);

            OkHttpClient okHttpClient = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(URL_GET_LATEST_MOVE)
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Authorization" , "Bearer " + token)
                    .build();

            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    if (!result.isEmpty()) {

                        JSONObject jsonObject = new JSONObject(result);
                        Game game = new Game(jsonObject.getLong("id"),
                                jsonObject.getString("white"),
                                jsonObject.getString("black"),
                                jsonObject.getString("fen"),
                                jsonObject.getString("result"),
                                jsonObject.getInt("from_x"),
                                jsonObject.getInt("from_y"),
                                jsonObject.getInt("to_x"),
                                jsonObject.getInt("to_y"),
                                jsonObject.getString("next_move"));

                        checkIfMatched(game);
                        showToast(game.toString());
//                        Intent i = new Intent(GameActivity.this,
//                                MainAppPage.class);
//                        startActivity(i);
//                        finish();
                    } else {
                        showToast("Test Ping Failed");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public boolean checkIfMatched(Game game){
        if(!game.getBlack().isEmpty() && !game.getWhite().isEmpty()){
            timer.cancel();  // Terminates this timer, discarding any currently scheduled tasks.
            timer.purge();   // Removes all cancelled tasks from this timer's task queue.
            Context mContext = MainAppPage.this;
            Intent mIntent = new Intent(mContext, GameActivity.class);
            mIntent.putExtra("game",game);
            startActivity(mIntent);
            finish();
            return true;
        }
        return false;
    }

    private void makeNewGame() {

        MakeNewGameLoader loader = new MakeNewGameLoader(new MakeNewGameLoader.GetMakeNewGameLoaderCallback() {
            @Override
            public void onGetGoodsLoaded(Game game) {
                checkIfMatched(game);
                showToast(game.toString());
            }

            @Override
            public void onResponseFailed(String errorMessage) {

            }
        });
        loader.loadMakeNewGame(getToken());

    }

    public void callAsynchronousTask() {

        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        };
        timer.schedule(doAsynchronousTask, 0, 6000); //execute in every 50000 ms
    }

    public String getToken(){
        SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        return preferences.getString("token","");
    }
}
