package kz.evilteamgenius.chessapp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kz.evilteamgenius.chessapp.R;
import kz.evilteamgenius.chessapp.activity.MainActivity;
import kz.evilteamgenius.chessapp.adapters.SliderAdapter;
import kz.evilteamgenius.chessapp.api.loaders.LastMove2PLoader;
import kz.evilteamgenius.chessapp.api.loaders.MakeNew2PGameLoader;
import kz.evilteamgenius.chessapp.database.asyncTasksDB.AddGameAsyncTask;
import kz.evilteamgenius.chessapp.database.entitys.GameEntity;
import kz.evilteamgenius.chessapp.engine.Game;
import kz.evilteamgenius.chessapp.engine.Match;
import kz.evilteamgenius.chessapp.models.Game2P;
import timber.log.Timber;

import static android.content.Context.MODE_PRIVATE;
import static kz.evilteamgenius.chessapp.extensions.CheckExtensionKt.getUsername;
import static kz.evilteamgenius.chessapp.extensions.LifecycleExtensionKt.replaceFragment;
import static kz.evilteamgenius.chessapp.extensions.ViewExtensionsKt.toast;
@SuppressWarnings({"FieldCanBeLocal", "ResultOfMethodCallIgnored", "CheckResult"})
public class NavigationPageFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.imageSlider)
    SliderView imageSlider;
    @BindView(R.id.playText)
    TextView playText;
    @BindView(R.id.communityText)
    TextView communityText;
    @BindView(R.id.optionText)
    TextView optionText;
    @BindView(R.id.rulesText)
    TextView rulesText;
    private Fragment fragment;
    private String mode = "online";
    static int LAST_SELECTED_MATCH_MODE;
    public int numberOfPlayers;
    static boolean IF_CONNECTED_TO_INTERNET = true;

    private ArrayList<String> imageLinks;
    private SliderAdapter adapter;

    private final Handler handler = new Handler();

    private Timer timer = new Timer();
    Runnable runnable = new Runnable() {
        public void run() {
            getLastMove();
        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_app_main_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initData();
        initUI();
    }


    @OnClick({R.id.playText, R.id.communityText, R.id.optionText, R.id.rulesText})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.playText:
                final Dialog d = new Dialog(getContext());
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.mode);
                final RadioGroup mode = d.findViewById(R.id.game_mode);
                final CheckBox local = d.findViewById(R.id.local);
                mode.check(mode.getChildAt(0).getId());
                d.findViewById(R.id.ok).setOnClickListener(v -> {
                    LAST_SELECTED_MATCH_MODE = Integer.parseInt(
                            (String) d.findViewById(mode.getCheckedRadioButtonId()).getTag());
                    if (local.isChecked()) {
                        Match match = new Match(String.valueOf(System.currentTimeMillis()),
                                LAST_SELECTED_MATCH_MODE, true);
                        Game.newGame(match, null, null, null);
                        startGame(match.id);
                    } else {
                        if (!IF_CONNECTED_TO_INTERNET) {
                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(getActivity());
                            builder.setTitle("Not connected to Google Play");
                            builder.setMessage(
                                    "You need to connect to Google Play Services to be able to find opponent players and start an online game")
                                    .setPositiveButton(android.R.string.ok,
                                            (dialog, id) -> dialog.dismiss());
                            builder.create().show();
                        } else {
                            makeNewGame();
                        }
                    }
                    d.dismiss();
                });
                d.findViewById(R.id.back).setOnClickListener(v -> {
                    d.dismiss();
                });
                d.show();
                break;
            case R.id.communityText:
                break;
            case R.id.optionText:
                fragment = new FragmentOptionsFragment();
                replaceFragment(this, fragment);
                break;
            case R.id.rulesText:
                fragment = new FragmentRulesFragment();
                replaceFragment(this, fragment);
                break;
        }
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
        adapter = new SliderAdapter(getContext());
        adapter.setImages(imageLinks);
        imageSlider.setSliderAdapter(adapter);

        imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM);
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        imageSlider.setIndicatorSelectedColor(Color.WHITE);
        imageSlider.setIndicatorUnselectedColor(Color.GRAY);
        imageSlider.setScrollTimeInSec(3); //set scroll delay in seconds :
        imageSlider.startAutoCycle();
    }

    private void insertGameIntoDatabase(GameEntity game) {
        AddGameAsyncTask addGameAsyncTask = new AddGameAsyncTask(getContext());
        addGameAsyncTask.execute(game);
    }

    private void makeNewGame() {
        MakeNew2PGameLoader loader = new MakeNew2PGameLoader(new MakeNew2PGameLoader.GetMakeNewGameLoaderCallback() {
            @Override
            public void onGetGoodsLoaded(Game2P game2P) {
                toast( getContext(),game2P.toString());
                if(!checkIfMatched(game2P))
                    callAsynchronousTask();

            }

            @Override
            public void onResponseFailed(String errorMessage) {
                toast(getContext(), errorMessage);

            }
        });

        loader.loadMakeNew2PGame(getToken(), LAST_SELECTED_MATCH_MODE);
    }

    private boolean checkIfMatched(Game2P game2P){
        if(!game2P.getPlayer1().isEmpty() && !game2P.getPlayer2().isEmpty()){
            timer.cancel();  // Terminates this timer, discarding any currently scheduled tasks.
            timer.purge();   // Removes all cancelled tasks from this timer's task queue.
            Match match = new Match(String.valueOf(System.currentTimeMillis()),
                    LAST_SELECTED_MATCH_MODE, false);
            String[] players = {game2P.getPlayer1(), game2P.getPlayer2()};
            Game.game2P = game2P;
            //TODO: remove room id
            Game.newGame(match, players, getUsername(getContext()), "matchMakingMessageReceived.getRoom_id()");
            startGame(match.id);
            return true;
        }
        return false;
    }

    private void callAsynchronousTask() {
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        };
        timer.schedule(doAsynchronousTask, 0, 6000); //execute in every 50000 ms
    }

    private void getLastMove() {
        String token = getToken();
//        toast(getContext(),token);
        LastMove2PLoader lastMoveLoader = new LastMove2PLoader(new LastMove2PLoader.LastMoveCallback() {
            @Override
            public void onMoveLoaded(Game2P game2P) {
                checkIfMatched(game2P);
                toast(getContext(),game2P.toString());
            }

            @Override
            public void onResponseFailed(String errorMessage) {
                toast(getContext(), errorMessage);
            }
        });
        lastMoveLoader.getLastMove(token);
    }

    private void startGame(final String matchID) {
        Timber.d("startGame");
        fragment = new GameFragment();
        Bundle b = new Bundle();
        b.putString("matchID", matchID);
        fragment.setArguments(b);
        replaceFragment(this, fragment);
    }

    private String getToken(){
        SharedPreferences preferences = requireContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        return preferences.getString("token","");
    }
}
