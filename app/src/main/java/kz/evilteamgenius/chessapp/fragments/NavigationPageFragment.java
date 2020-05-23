package kz.evilteamgenius.chessapp.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kz.evilteamgenius.chessapp.R;
import kz.evilteamgenius.chessapp.adapters.SliderAdapter;
import kz.evilteamgenius.chessapp.models.customAdds.AdObj;
import kz.evilteamgenius.chessapp.viewModels.GameViewModel;

import static kz.evilteamgenius.chessapp.extensions.LifecycleExtensionKt.replaceFragment;

@SuppressWarnings({"FieldCanBeLocal", "ResultOfMethodCallIgnored", "CheckResult"})
public class NavigationPageFragment extends Fragment implements SliderAdapter.myListener {
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
    @BindView(R.id.adView)
    AdView adView;

    private Fragment fragment;
    private ArrayList<AdObj> imageLinks;
    private SliderAdapter adapter;
    private GameViewModel viewModel;

    private AdRequest adRequest;

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
        initAddView();
        checkConnection();
        initData();
        initUI();
    }

    private void initAddView() {
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
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
        AdObj adObjIITU = new AdObj("https://instagram.fala4-2.fna.fbcdn.net/v/t51.2885-15/sh0.08/e35/s640x640/99149719_596085964661735_3823630780194234657_n.jpg?_nc_ht=instagram.fala4-2.fna.fbcdn.net&_nc_cat=111&_nc_ohc=1Js5EDqo7EwAX-6N7dL&oh=dbab6534ca4e937789424a9e849aa067&oe=5EF3760C", "https://www.iitu.kz");
        AdObj adObjInsta = new AdObj("https://lh3.googleusercontent.com/2sREY-8UpjmaLDCTztldQf6u2RGUtuyf6VT5iyX3z53JS4TdvfQlX-rNChXKgpBYMw", "https://www.instagram.com/altynboy/");
        for (int i = 0; i < 2; i++) {
            imageLinks.add(adObjIITU);
            imageLinks.add(adObjInsta);
        }

    }

    private void initUI() {
        adapter = new SliderAdapter(getContext(), this);
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

    @Override
    public void goToWebPage(String webPage) {
        Uri uri = Uri.parse(webPage);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
