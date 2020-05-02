package kz.evilteamgenius.chessapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kz.evilteamgenius.chessapp.R;

import static kz.evilteamgenius.chessapp.extensions.LifecycleExtensionKt.getBackFragment;

public class FragmentRulesFragment extends Fragment {

    @BindView(R.id.backButtonInOption)
    MaterialButton backButtonInOption;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rules, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.backButtonInOption)
    public void onViewClicked() {
        getBackFragment(this, new NavigationPageFragment());
    }
}
