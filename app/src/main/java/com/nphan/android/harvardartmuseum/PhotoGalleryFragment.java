package com.nphan.android.harvardartmuseum;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PhotoGalleryFragment extends Fragment {

    private static final String TAG = "PhotoGalleryFragment";
    private static final String ARG_CULTURE_NAME = "culture_name";
    private static final String ARG_CULTURE_ID = "culture_id";

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
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        new FetchItemsTask().execute();

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(getArguments().getString(ARG_CULTURE_NAME));
    }

    private void setupAdapter() {
        if (isAdded()) {
            mRecyclerView.setAdapter(new ObjectItemAdapter(mObjectItems));
        }
    }

    private class ObjectItemHolder extends RecyclerView.ViewHolder {

        private ObjectItem mObjectItem;
        private ImageView mImageView;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mPeriodTextView;
        private TextView mMediumTextView;

        public ObjectItemHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.object_image);
            mTitleTextView = itemView.findViewById(R.id.object_title);
            mDateTextView = itemView.findViewById(R.id.object_date);
            mPeriodTextView = itemView.findViewById(R.id.object_period);
            mMediumTextView = itemView.findViewById(R.id.object_medium);
        }

        public void bindObjectItem(ObjectItem objectItem) {
            mObjectItem = objectItem;

            Picasso.get()
                    .load(objectItem.getPrimaryImageUrl())
                    .placeholder(R.drawable.loading_placeholder)
                    .into(mImageView);

            Resources rs = getResources();

            mTitleTextView.setText(Html.fromHtml(rs.getString(R.string.object_title)));
            mTitleTextView.append(mObjectItem.getTitle());

            mDateTextView.setText(Html.fromHtml(rs.getString(R.string.object_date)));
            mDateTextView.append(mObjectItem.getDated());

            mPeriodTextView.setText(Html.fromHtml(rs.getString(R.string.object_period)));
            mPeriodTextView.append(mObjectItem.getPeriod());

            mMediumTextView.setText(Html.fromHtml(rs.getString(R.string.object_medium)));
            mMediumTextView.append(mObjectItem.getMedium());
        }
    }

    private class ObjectItemAdapter extends RecyclerView.Adapter<ObjectItemHolder> {
        private List<ObjectItem> mObjectItems;

        public ObjectItemAdapter(List<ObjectItem> objectItems) {
            mObjectItems = objectItems;
        }

        @NonNull
        @Override
        public ObjectItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View itemView = inflater.inflate(R.layout.list_item_photo_gallery, parent, false);
            return new ObjectItemHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ObjectItemHolder holder, int position) {
            ObjectItem objectItem = mObjectItems.get(position);
            holder.bindObjectItem(objectItem);
        }

        @Override
        public int getItemCount() {
            return mObjectItems.size();
        }
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, List<ObjectItem>> {
        @Override
        protected List<ObjectItem> doInBackground(Void... voids) {
            String cultureId = getArguments().getString(ARG_CULTURE_ID);
            return new HarvardArtMuseumFetch().fetchObjectItems(cultureId);
        }

        @Override
        protected void onPostExecute(List<ObjectItem> objectItems) {
            mObjectItems = objectItems;
            setupAdapter();
        }
    }
}