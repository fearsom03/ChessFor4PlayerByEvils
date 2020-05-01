package kz.evilteamgenius.chessapp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.fragment.app.FragmentTransaction;

import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kz.evilteamgenius.chessapp.R;
import kz.evilteamgenius.chessapp.activity.MainActivity;
import kz.evilteamgenius.chessapp.adapters.SliderAdapter;
import kz.evilteamgenius.chessapp.database.asyncTasksDB.AddGameAsyncTask;
import kz.evilteamgenius.chessapp.database.entitys.GameEntity;
import kz.evilteamgenius.chessapp.engine.Game;
import kz.evilteamgenius.chessapp.engine.Match;
import timber.log.Timber;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NavigationPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
    private Timer timer = new Timer();
    private Fragment fragment;
    private String mode = "online";
    static int LAST_SELECTED_MATCH_MODE;
    public int numberOfPlayers;
    static boolean IF_CONNECTED_TO_INTERNET = true;

    private ArrayList<String> imageLinks;
    private SliderAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public NavigationPageFragment() {
        // Required empty public constructor
    }

    public static NavigationPageFragment newInstance(String param1, String param2) {
        NavigationPageFragment fragment = new NavigationPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
                            // CONNECT TO WEBSCOKET AND START MATCHING
                            ((MainActivity) getActivity()).connectAndMakeMatch(LAST_SELECTED_MATCH_MODE);
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
                replaceFragment(fragment);
                break;
            case R.id.rulesText:
                fragment = new FragmentRulesFragment();
                replaceFragment(fragment);
                break;
        }
    }

    private String getToken() {
        SharedPreferences preferences = Objects.requireNonNull(getContext()).getSharedPreferences("myPrefs", MODE_PRIVATE);
        return preferences.getString("token", "");
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

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction tr = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.frame, fragment);
        tr.commit();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void startGame(final String matchID) {
        Timber.d("startGame");
        fragment = new GameFragment();
        Bundle b = new Bundle();
        b.putString("matchID", matchID);
        fragment.setArguments(b);
        replaceFragment(fragment);
    }

    public String getUsername() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        return preferences.getString("username", null);
    }
}
