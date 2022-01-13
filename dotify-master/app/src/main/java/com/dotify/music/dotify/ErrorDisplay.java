package com.dotify.music.dotify;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

public class ErrorDisplay extends Fragment {

    private static String fragmentErrMesg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.error_display, container, false);
        TextView textView = rootView.findViewById(R.id.error_message);
        textView.setText(fragmentErrMesg);
        return rootView;
    }

    public static View displayError(final String errMsg, Fragment fragment, int id) {
        ErrorDisplay display = new ErrorDisplay();
        fragmentErrMesg = errMsg;

        FragmentTransaction transaction = Objects.requireNonNull(fragment.getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.add(id, display);
        transaction.commit();

        return display.getView();
    }
}
