package kz.evilteamgenius.chessapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kz.evilteamgenius.chessapp.R;

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
        FragmentTransaction tr = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.frame, new NavigationPageFragment());
        tr.commit();
    }
}
