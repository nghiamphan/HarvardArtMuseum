package com.nphan.android.harvardartmuseum;

import android.content.Intent;
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

    private String mCultureName;
    private String mCultureId;

    private List<ObjectItem> mObjectItems = new ArrayList<>();
    private int mPage = 1;
    private int mStartPos;
    private static boolean mAsyncTaskRunning = false;

    private RecyclerView mRecyclerView;
    private ObjectItemAdapter mAdapter;

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
        //setRetainInstance(true);
        setHasOptionsMenu(true);

        mCultureName = getArguments().getString(ARG_CULTURE_NAME);
        mCultureId = getArguments().getString(ARG_CULTURE_ID);

        new FetchItemsTask().execute();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
                int lastPosition = layoutManager.findLastVisibleItemPosition();

                if (lastPosition == mObjectItems.size()-1) {
                    if (!mAsyncTaskRunning) {
                        mPage += 1;
                        new FetchItemsTask().execute();
                        mAsyncTaskRunning = true;
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(mCultureName);
    }

    private void setupAdapter() {
        if (isAdded()) {
            if (mAdapter == null) {
                mAdapter = new ObjectItemAdapter(mObjectItems);
                mRecyclerView.setAdapter(mAdapter);
            }
            else {
                mAdapter.setItems(mObjectItems);
                mAdapter.notifyItemRangeInserted(mStartPos, mObjectItems.size() - mStartPos);
            }

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

            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String imageUrl = mObjectItem.getPrimaryImageUrl();
                    Intent intent = PhotoViewActivity.newIntent(getActivity(), imageUrl);
                    startActivity(intent);
                }
            });

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
        private List<ObjectItem> mItems;

        public ObjectItemAdapter(List<ObjectItem> objectItems) {
            this.mItems = objectItems;
        }

        public void setItems(List<ObjectItem> items) {
            this.mItems = items;
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
            ObjectItem objectItem = mItems.get(position);
            holder.bindObjectItem(objectItem);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, List<ObjectItem>> {
        @Override
        protected List<ObjectItem> doInBackground(Void... voids) {
            return new HarvardArtMuseumFetch().fetchObjectItems(mCultureId, mPage);
        }

        @Override
        protected void onPostExecute(List<ObjectItem> objectItems) {
            mStartPos = mObjectItems.size();
            mObjectItems.addAll(objectItems);
            setupAdapter();
            mAsyncTaskRunning = false;
        }
    }
}
