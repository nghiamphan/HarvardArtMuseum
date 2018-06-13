package com.nphan.android.harvardartmuseum;

import android.content.res.Resources;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainMenuFragment extends Fragment {

    private static final String TAG = "MainMenuFragment";
    private List<CultureItem> mCultureItems = new ArrayList<>();

    private RecyclerView mRecyclerView;

    public static MainMenuFragment newInstance() {

        Bundle args = new Bundle();

        MainMenuFragment fragment = new MainMenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemsTask().execute();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_menu, container, false);

        mRecyclerView = v.findViewById(R.id.photo_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        return v;
    }

    private void setupAdapter() {
        if (isAdded()) {
            mRecyclerView.setAdapter(new PhotoAdapter(mCultureItems));
        }
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {
        private TextView mMenuItemTextView;

        public PhotoHolder(View itemView) {
            super(itemView);
            mMenuItemTextView = itemView.findViewById(R.id.menu_item);
        }

        public void bindGalleryItem(CultureItem item) {
            mMenuItemTextView.setText(item.getCulture());
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<CultureItem> mCultureItems;

        public PhotoAdapter(List<CultureItem> cultureItems) {
            mCultureItems = cultureItems;
        }

        @NonNull
        @Override
        public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View itemView = inflater.inflate(R.layout.list_item_main_menu, parent, false);
            return new PhotoHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
            CultureItem cultureItem = mCultureItems.get(position);
            Resources resources = getResources();
            switch (position % 4) {
                case 0:
                    holder.itemView.setBackgroundColor(resources.getColor(R.color.orange));
                    break;
                case 1:
                    holder.itemView.setBackgroundColor(resources.getColor(R.color.green));
                    break;
                case 2:
                    holder.itemView.setBackgroundColor(resources.getColor(R.color.purple));
                    break;
                case 3:
                    holder.itemView.setBackgroundColor(resources.getColor(R.color.blue));
            }
            holder.bindGalleryItem(cultureItem);
        }

        @Override
        public int getItemCount() {
            return mCultureItems.size();
        }
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, List<CultureItem>> {
        @Override
        protected List<CultureItem> doInBackground(Void... voids) {
            return new HarvardArtMuseumFetch().fetchItems();
        }

        @Override
        protected void onPostExecute(List<CultureItem> cultureItems) {
            mCultureItems = cultureItems;
            setupAdapter();
        }
    }
}
