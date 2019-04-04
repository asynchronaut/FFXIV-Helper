package com.example.android.ffxivhelper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultViewHolder> {
    private ResultObject[] mResults;
    private final ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ResultViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        // each data item is just a string in this case
        public final TextView mNameTextView;
        public final TextView mIdTextView;
        public ResultViewHolder(View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.tv_result_name);
            mIdTextView = itemView.findViewById(R.id.tv_result_id);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ResultsAdapter(ListItemClickListener listener) {
        mOnClickListener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ResultsAdapter.ResultViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result_item, parent, false);
        ResultViewHolder vh = new ResultViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mNameTextView.setText(mResults[position].getCharacterName());
        holder.mIdTextView.setText(mResults[position].getCharacterID());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mResults == null) return 0;
        return mResults.length;
    }

    public void setResultsData(ResultObject[] results) {
        mResults = results;
        notifyDataSetChanged();
    }

    public String getResultId(int position) {
        return mResults[position].getCharacterID();
    }
}
