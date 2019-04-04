package com.example.android.ffxivhelper;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CollectibleAdapter extends RecyclerView.Adapter<CollectibleAdapter.CollectibleViewHolder> {
    private CollectibleObject[] mCollectibles;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class CollectibleViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public final TextView mNameTextView;
        public final TextView mIdTextView;
        public CollectibleViewHolder(View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.tv_collectible_name);
            mIdTextView = itemView.findViewById(R.id.tv_collectible_id);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CollectibleAdapter() {

    }

    // Create new views (invoked by the layout manager)
    @Override
    public CollectibleAdapter.CollectibleViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collectible_item, parent, false);
        CollectibleViewHolder vh = new CollectibleViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CollectibleViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mNameTextView.setText(mCollectibles[position].getCollectibleName());
        holder.mIdTextView.setText(mCollectibles[position].getCollectibleID());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mCollectibles == null) return 0;
        return mCollectibles.length;
    }

    public void setResultsData(CollectibleObject[] results) {
        Log.d("COLLECTIBLES", "Setting " + results.length + " results");
        mCollectibles = results;
        notifyDataSetChanged();
    }

}
