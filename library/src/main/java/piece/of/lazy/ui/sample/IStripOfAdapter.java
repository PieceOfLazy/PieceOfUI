package piece.of.lazy.ui.sample;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by zpdl.kim.
 */

public abstract class IStripOfAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    protected final Context mContext;
    @NonNull
    private final LayoutInflater mInflater;
    @NonNull
    protected final SparseArray<IStripOfHolder> mRecyclerHolderInterfaceList;

    public IStripOfAdapter(@NonNull Context context) {
        mContext = context;
        //noinspection ConstantConditions
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRecyclerHolderInterfaceList = new SparseArray<>();

        setHasStableIds(true);
        onBindHolderInterface(mRecyclerHolderInterfaceList);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        IStripOfHolder recyclerHolderInterface = mRecyclerHolderInterfaceList.get(viewType);
        if(recyclerHolderInterface != null) {
            return recyclerHolderInterface.onCreateViewHolder(mInflater, parent);
        } else {
            TextView tv = new TextView(mContext);
            tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setPadding(20, 20, 20, 20);
            tv.setBackgroundColor(Color.DKGRAY);
            tv.setText(String.format("ERROR : viewType : %x", viewType));
            return new RecyclerView.ViewHolder(tv) {
            };
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        IStripOfHolder recyclerHolderInterface = mRecyclerHolderInterfaceList.get(holder.getItemViewType());
        if(recyclerHolderInterface != null) {
            recyclerHolderInterface.onBindViewHolder(mContext, holder, getBindItem(position), position);
        }
    }

    protected abstract void onBindHolderInterface(SparseArray<IStripOfHolder> list);

    public abstract Object getBindItem(int position);
}
