package kz.evilteamgenius.chessapp.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kz.evilteamgenius.chessapp.BuildConfig;
import kz.evilteamgenius.chessapp.Const;
import kz.evilteamgenius.chessapp.R;
import kz.evilteamgenius.chessapp.activity.LoginActivity;
import kz.evilteamgenius.chessapp.activity.MainActivity;
import kz.evilteamgenius.chessapp.service.MusicService;
import kz.evilteamgenius.chessapp.viewModels.GameViewModel;
import timber.log.Timber;

import static android.content.Context.MODE_PRIVATE;
import static kz.evilteamgenius.chessapp.extensions.LifecycleExtensionKt.getBackFragment;
import static kz.evilteamgenius.chessapp.extensions.ViewExtensionsKt.toast;

public class FragmentOptionsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, ItemDesignListDialogFragment.ListenerOfChoose {

    @BindView(R.id.languageSpinner)
    Spinner languageSpinner;
    @BindView(R.id.notificationSwitch)
    Switch notificationSwitch;
    @BindView(R.id.soundSwitch)
    Switch soundSwitch;
    @BindView(R.id.exitTextView)
    TextView exitTextView;
    @BindView(R.id.shareApp)
    TextView shareApp;
    @BindView(R.id.backButtonInOption)
    MaterialButton backButtonInOption;
    @BindView(R.id.versionText)
    TextView versionText;
    @BindView(R.id.pauseMusic)
    ImageView pauseMusic;
    @BindView(R.id.playMusic)
    ImageView playMusic;
    @BindView(R.id.linearOfMainSettings)
    LinearLayout linearOfMainSettings;
    @BindView(R.id.musicLine)
    LinearLayout musicLine;
    @BindView(R.id.selectBackground)
    TextView selectBackground;

    private Locale myLocale;
    private String something, getlanguageStat;
    private SharedPreferences preferences;
    private GameViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_options, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        viewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        viewModel.getMusicValue().observe(requireActivity(), aBoolean -> {
            if (aBoolean) {
                playMusic.setBackgroundColor(Color.GREEN);
                pauseMusic.setBackgroundColor(Color.TRANSPARENT);
            } else {
                pauseMusic.setBackgroundColor(Color.GREEN);
                playMusic.setBackgroundColor(Color.TRANSPARENT);
            }
        });
        preferences
                = requireActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        spinnerInit();
        initToggle();
        initVersionText();
    }

    private void initVersionText() {
        String version = getString(R.string.versionText, BuildConfig.VERSION_NAME);
        versionText.setText(version);
    }

    private void initToggle() {
        boolean forMusic = preferences.getBoolean(Const.PLAY_MUSIC, true);
        boolean forNotification = preferences.getBoolean(Const.NOTIFICATION, false);
        soundSwitch.setChecked(forMusic);
        if (forMusic) {
            musicLine.setVisibility(View.VISIBLE);
        } else {
            musicLine.setVisibility(View.GONE);
        }
        notificationSwitch.setChecked(forNotification);
        soundSwitch.setOnCheckedChangeListener(this);
        notificationSwitch.setOnCheckedChangeListener(this);
    }

    private void spinnerInit() {
        ArrayAdapter mAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.language_option));
        languageSpinner.setAdapter(mAdapter);
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (languageSpinner.getItemAtPosition(0).equals("Select Language")) {
                    getlanguageStat = "def";
                } else if (languageSpinner.getItemAtPosition(0).equals("Тiлдi танданыз")) {
                    getlanguageStat = "kk";
                } else if (languageSpinner.getItemAtPosition(0).equals("Выберите язык")) {
                    getlanguageStat = "ru";
                } else if (languageSpinner.getItemAtPosition(0).equals("选择语言")) {
                    getlanguageStat = "zh";
                } else {
                    Timber.d("DONT CHANGED %s", adapterView.getChildAt(0).toString());
                }

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
                    case 4:
                        setLocale("zh");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setLocale(String localeName) {
        if (!localeName.equals(getlanguageStat)) {
            myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(getContext(), MainActivity.class);
            refresh.putExtra("buttonId", 1);
            refresh.putExtra(something, conf.locale.getLanguage());
            viewModel.setMusicIsPlaying(false);
            startActivity(refresh);
            requireActivity().finish();
        } else {
            toast(requireActivity(), getString(R.string.OOPS_TryAgain));
        }
    }

    @OnClick({R.id.exitTextView, R.id.shareApp, R.id.backButtonInOption, R.id.previousMusic, R.id.pauseMusic, R.id.playMusic, R.id.nextMusic, R.id.selectBackground})
    public void onViewClicked(View view) {
        Intent intent = new Intent(this.getContext(), LoginActivity.class);
        switch (view.getId()) {
            case R.id.exitTextView:
                startActivity(intent);
                requireActivity()
                        .stopService(new Intent(requireActivity()
                                , MusicService.class));
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                editor.commit();
                requireActivity().finish();
                break;
            case R.id.shareApp:
                final String appPackageName = requireActivity().getPackageName();
                String shareString = "https://play.google.com/store/apps/details?id="
                        + appPackageName;
//                https://github.com/fearsom03/AndroidFinalChessApp
                intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, shareString);
                startActivity(Intent.createChooser(intent, "Share"));
                break;
            case R.id.backButtonInOption:
                getBackFragment(this, new NavigationPageFragment());
                break;
            case R.id.previousMusic:
                view.setBackgroundColor(Color.GREEN);
                viewModel.startPrevMusic();
                view.postDelayed(() ->
                        view.setBackgroundColor(Color.TRANSPARENT), TimeUnit.MILLISECONDS.toMillis(500));
                break;
            case R.id.pauseMusic:
                viewModel.setMusicIsPlaying(false);
                break;
            case R.id.playMusic:
                viewModel.setMusicIsPlaying(true);
                break;
            case R.id.nextMusic:
                view.setBackgroundColor(Color.GREEN);
                viewModel.startNextMusic();
                view.postDelayed(() ->
                        view.setBackgroundColor(Color.TRANSPARENT), TimeUnit.MILLISECONDS.toMillis(500));
                break;
            case R.id.selectBackground:
                new ItemDesignListDialogFragment(this).show(getParentFragmentManager(), "Hello");
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor editor = preferences.edit();
        switch (buttonView.getId()) {
            case R.id.notificationSwitch:
                editor.putBoolean(Const.NOTIFICATION, isChecked);
                editor.apply();
                editor.commit();
                break;
            case R.id.soundSwitch:
                editor.putBoolean(Const.PLAY_MUSIC, isChecked);
                editor.apply();
                editor.commit();
                viewModel.setMusicIsPlaying(isChecked);
                if (isChecked) {
                    musicLine.setVisibility(View.VISIBLE);
                } else {
                    musicLine.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void selectStyle(int position) {
        toast(requireContext(), "Selected --> " + position);
        viewModel.setSelectedStyle(position);
    }
}
