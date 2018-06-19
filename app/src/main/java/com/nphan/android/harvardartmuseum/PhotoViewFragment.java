package com.nphan.android.harvardartmuseum;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PhotoViewFragment extends Fragment {

    private static final String ARG_IMAGE_URL = "arg_image_url";

    public static PhotoViewFragment newInstance(String imageUrl) {

        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_URL, imageUrl);

        PhotoViewFragment fragment = new PhotoViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_view, container, false);

        ImageView imageView = view.findViewById(R.id.image_view);
        String imageUrl = getArguments().getString(ARG_IMAGE_URL);
        Picasso.get()
                .load(imageUrl)
                .into(imageView);

        return view;
    }
}
