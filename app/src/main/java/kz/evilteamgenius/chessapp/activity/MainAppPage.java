package kz.evilteamgenius.chessapp.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kz.evilteamgenius.chessapp.R;
import kz.evilteamgenius.chessapp.adapters.AdvertismentAdapter;

public class MainAppPage extends AppCompatActivity {


    @BindView(R.id.discretePicker)
    DiscreteScrollView discretePicker;
    @BindView(R.id.playText)
    TextView playText;
    @BindView(R.id.communityText)
    TextView communityText;
    @BindView(R.id.optionText)
    TextView optionText;
    @BindView(R.id.rulesText)
    TextView rulesText;

    private ArrayList<String> imageLinks;
    private AdvertismentAdapter adapter;
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
        imageLinks=new ArrayList<>();
        imageLinks.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRs3w8x_Cglsbfajw6tdrjCks32sG5uKy17KrovdzdGLsbYguGg9A");
        imageLinks.add("https://3.bp.blogspot.com/-TjJ4W4oX4Tw/XNMG9K9jCZI/AAAAAAAABEs/_TYPxEpjjbE6YfHW6YEGsZgNx_KW4DYIwCLcBGAs/s1600/Describe%2Ban%2Badvertisement%2Bthat%2Byou%2Bremember%2BIELTS%2Bcue%2Bcard.png");
        imageLinks.add("https://i.pinimg.com/originals/33/04/8a/33048aa1286f6ea64f687174ba639271.jpg");
        imageLinks.add("https://content.fortune.com/wp-content/uploads/2017/05/gettyimages-84709618.jpg");

    }

    private void initUI() {
        adapter = new AdvertismentAdapter();
        adapter.setImages(imageLinks);
        discretePicker.setAdapter(adapter);
        discretePicker.setOrientation(DSVOrientation.HORIZONTAL);
        discretePicker.setOverScrollEnabled(true);
        discretePicker.setScrollbarFadingEnabled(true);
        discretePicker.setOffscreenItems(imageLinks.size());
        discretePicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.9f)
                .build());
        discretePicker.setItemTransitionTimeMillis(4500);

        goToLastElement();

    }
    private void goToLastElement(){
        discretePicker.post(new Runnable() {
            @Override
            public void run() {
                discretePicker.smoothScrollToPosition(imageLinks.size() - 1);
            }
        });
    }


    @OnClick(R.id.playText)
    public void onPlayTextClicked() {
    }

    @OnClick(R.id.communityText)
    public void onCommunityTextClicked() {
    }

    @OnClick(R.id.optionText)
    public void onOptionTextClicked() {
    }

    @OnClick(R.id.rulesText)
    public void onRulesTextClicked() {
    }
}
