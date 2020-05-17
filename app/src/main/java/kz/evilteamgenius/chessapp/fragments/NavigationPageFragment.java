package kz.evilteamgenius.chessapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kz.evilteamgenius.chessapp.R;
import kz.evilteamgenius.chessapp.adapters.SliderAdapter;
import kz.evilteamgenius.chessapp.viewModels.GameViewModel;

import static kz.evilteamgenius.chessapp.extensions.LifecycleExtensionKt.replaceFragment;

@SuppressWarnings({"FieldCanBeLocal", "ResultOfMethodCallIgnored", "CheckResult"})
public class NavigationPageFragment extends Fragment {
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
    private ArrayList<String> imageLinks;
    private SliderAdapter adapter;
    private GameViewModel viewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_app_main_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        viewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        checkConnection();
        initData();
        initUI();
    }


    @OnClick({R.id.playText, R.id.communityText, R.id.optionText, R.id.rulesText})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.playText:
                //todo in next update I will add other fragment here
                fragment = ChooseGame.newInstance(true);
                break;
            case R.id.communityText:
                fragment = ChooseGame.newInstance(false);
                break;
            case R.id.optionText:
                fragment = new FragmentOptionsFragment();
                break;
            case R.id.rulesText:
                fragment = new FragmentRulesFragment();
                break;
        }
        replaceFragment(this, fragment);
    }

    private void checkConnection() {
        viewModel.getInternetCheck().observe(requireActivity(), aBoolean -> {
            if (!aBoolean) {
                playText.setEnabled(false);
                playText.setTextColor(Color.GRAY);
            } else {
                playText.setEnabled(true);
                playText.setTextColor(Color.WHITE);
            }
        });
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
}
