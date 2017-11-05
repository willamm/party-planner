package ca.bcit.ass3.murphy_lastname2;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public abstract class GenericViewHolder extends RecyclerView.ViewHolder {

    protected TextView mTextView;

    public GenericViewHolder(TextView itemView) {
        super(itemView);
        mTextView = itemView;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
