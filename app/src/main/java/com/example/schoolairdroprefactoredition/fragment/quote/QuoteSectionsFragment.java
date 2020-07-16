package com.example.schoolairdroprefactoredition.fragment.quote;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class QuoteSectionsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private QuoteSectionsFragmentViewModel viewModel;

    public static QuoteSectionsFragment newInstance(int index) {
        QuoteSectionsFragment fragment = new QuoteSectionsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(QuoteSectionsFragmentViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }

    }
}
