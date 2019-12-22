package kz.evilteamgenius.chessapp.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;

import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kz.evilteamgenius.chessapp.R;
import kz.evilteamgenius.chessapp.activity.KukaActivity;
import timber.log.Timber;

public class FragmentOptionsFragment extends Fragment {

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

    private Locale myLocale;
    private String something, getlanguageStat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_options, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        spinnerInit();
    }

    private void spinnerInit() {
        ArrayAdapter mAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.language_option));
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
                    Timber.d("DONT CHANGED " + adapterView.getChildAt(0).toString());
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
            Intent refresh = new Intent(getContext(), KukaActivity.class);
            refresh.putExtra("buttonId", 1);
            refresh.putExtra(something, conf.locale.getLanguage());
            startActivity(refresh);
            Objects.requireNonNull(getActivity()).finish();
        } else {
            Toast.makeText(getContext(), getString(R.string.OOPS_TryAgain), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.exitTextView, R.id.shareApp, R.id.backButtonInOption})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.exitTextView:
                break;
            case R.id.shareApp:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Kuanysh is best");
                startActivity(Intent.createChooser(intent, "Share"));
                break;
            case R.id.backButtonInOption:
                FragmentTransaction tr = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.frame, new NavigationPageFragment());
                tr.commit();
                break;
        }
    }
}
