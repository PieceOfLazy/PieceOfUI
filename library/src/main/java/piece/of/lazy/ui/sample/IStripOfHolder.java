package piece.of.lazy.ui.sample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public interface IStripOfHolder {

    RecyclerView.ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup container);

    void onBindViewHolder(Context context, RecyclerView.ViewHolder holder, Object item, Object position);

    boolean isBindItem(Object item);

    int getViewType();
}