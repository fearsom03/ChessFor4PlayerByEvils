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
import kz.evilteamgenius.chessapp.api.loaders.LastMoveLoader;
import kz.evilteamgenius.chessapp.api.loaders.MakeNewGameLoader;
import kz.evilteamgenius.chessapp.models.Game;

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
            getLastMove();
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
    void communityButton(){

        showToast(getToken());
    }

    //test ping server
    @OnClick(R.id.optionText)
    void optionButton(){
    }

    //test make new game
    @OnClick(R.id.rulesText)
    void rulesButton(){

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
                showToast(errorMessage);
            }
        });
        loader.loadMakeNewGame(getToken());

    }

    private void getLastMove() {
        SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        String token = preferences.getString("token","");
        showToast(token);
        LastMoveLoader lastMoveLoader = new LastMoveLoader(new LastMoveLoader.LastMoveCallback() {
            @Override
            public void onMoveLoaded(Game game) {
                checkIfMatched(game);
                showToast(game.toString());
            }

            @Override
            public void onResponseFailed(String errorMessage) {
                showToast(errorMessage);
            }
        });

        lastMoveLoader.getLastMove(token);
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
