package kz.evilteamgenius.chessapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kz.evilteamgenius.chessapp.R;
import kz.evilteamgenius.chessapp.activity.GameActivity;
import kz.evilteamgenius.chessapp.adapters.SliderAdapter;
import kz.evilteamgenius.chessapp.api.loaders.LastMoveLoader;
import kz.evilteamgenius.chessapp.api.loaders.MakeNewGameLoader;
import kz.evilteamgenius.chessapp.models.Game;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NavigationPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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


    private ArrayList<String> imageLinks;
    private SliderAdapter adapter;
    private final Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        public void run() {
            getLastMove();
        }
    };

    private OnFragmentInteractionListener mListener;

    public NavigationPageFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
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
        View view = inflater.inflate(R.layout.activity_app_main_page, container, false);
        ButterKnife.bind(this,view);
        initData();
        initUI();
        return view;
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
                makeNewGame();
                callAsynchronousTask();
                break;
            case R.id.communityText:
                showToast(getToken());
//                replaceFragment();
                break;
            case R.id.optionText:
                showToast(getToken());
                fragment = new FragmentOptionsFragment();
                replaceFragment(fragment);
                break;
            case R.id.rulesText:
//                replaceFragment();
                break;
        }
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

    private String getToken(){
        SharedPreferences preferences = Objects.requireNonNull(getContext()).getSharedPreferences("myPrefs", MODE_PRIVATE);
        return preferences.getString("token","");
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

    private void showToast(final String Text) {
                Toast.makeText(getContext(),
                        Text, Toast.LENGTH_SHORT).show();
    }

    private void checkIfMatched(Game game){
        if(!game.getBlack().isEmpty() && !game.getWhite().isEmpty()){
            timer.cancel();  // Terminates this timer, discarding any currently scheduled tasks.
            timer.purge();   // Removes all cancelled tasks from this timer's task queue.
            Intent mIntent = new Intent(getContext(), GameActivity.class);
            mIntent.putExtra("game",game);
            startActivity(mIntent);
            getActivity().finish();
        }
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
        SharedPreferences preferences = Objects.requireNonNull(getContext()).getSharedPreferences("myPrefs", MODE_PRIVATE);
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


    public void replaceFragment(Fragment fragment){
        FragmentTransaction tr = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.frame, fragment);
        tr.commit();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
