package com.nphan.android.harvardartmuseum;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class PhotoGalleryFragment extends Fragment {

    private static final String TAG = "PhotoGalleryFragment";
    private static final String ARG_CULTURE_NAME = "culture_name";
    private static final String ARG_CULTURE_ID = "culture_id";

    private String mCulture;
    private List<ObjectItem> mObjectItems = new ArrayList<>();

    private RecyclerView mRecyclerView;

    public static PhotoGalleryFragment newInstance(String culture, String cultureId) {

        Bundle args = new Bundle();
        args.putString(ARG_CULTURE_NAME, culture);
        args.putString(ARG_CULTURE_ID, cultureId);

        PhotoGalleryFragment fragment = new PhotoGalleryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mCulture = getArguments().getString(ARG_CULTURE_NAME);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        return view;
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
