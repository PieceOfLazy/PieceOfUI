package piece.of.lazy.ui.sample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public abstract class StripOfHolder<VH extends RecyclerView.ViewHolder, VI> extends IStripOfView<VI> implements IStripOfHolder {

    protected VH mHolder = null;

    private Class<VH> mHolderType = null;
    private Class<VI> mItemType = null;

    protected StripOfHolder(Class<VH> holderType, Class<VI> itemType) {
        mHolderType = holderType;
        mItemType = itemType;
    }

    @Override
    protected final void onBindStripOfView(@NonNull View view) {
        mHolder = onCreateStripOfHolder(view);
    }

    @Override
    protected final void onBindStripOfItem(@NonNull Context context, VI item) {
        if(mHolder != null) {
            onBindStripOfViewHolder(context, mHolder, item, null);
        }
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup container) {
        if(inflater == null) {
            return null;
        }

        View view = onCreateStripOfView(inflater, container);
        if(view == null) {
            return null;
        }

        return onCreateStripOfHolder(view);
    }

    @Override
    public final void onBindViewHolder(Context context, RecyclerView.ViewHolder holder, Object item, Object position) {
        if(context != null && holder != null) {
            VH castHolder = onCastHolder(holder);
            VI castItem = onCastItem(item);

            if(castHolder != null) {
                onBindStripOfViewHolder(context, castHolder, castItem, position);
            }
        }
    }

    @Override
    public boolean isBindItem(Object item) {
        return mItemType != null && mItemType.isInstance(item);
    }

    @Override
    public int getViewType() {
        return stripOfLayout();
    }

    protected final VI getBindItem(VH holder) {
        if(mHolder == holder) {
            return mItem;
        }

        ViewParent parent = holder.itemView.getParent();
        if (parent != null && parent instanceof RecyclerView) {
            RecyclerView.Adapter adapter = ((RecyclerView) parent).getAdapter();
            if (adapter != null && adapter instanceof IStripOfAdapter) {
                return onCastItem(((IStripOfAdapter) adapter).getBindItem(holder.getAdapterPosition()));
            }
        }
        return null;
    }

    private VH onCastHolder(@NonNull RecyclerView.ViewHolder holder) {
        if(mHolderType != null) {
            try {
                return mHolderType.cast(holder);
            } catch (ClassCastException e) {
                // null
            }
        }
        return null;
    }

    private VI onCastItem(Object item) {
        if(mItemType != null && item != null) {
            try {
                return mItemType.cast(item);
            } catch (ClassCastException e) {
                // null
            }
        }
        return null;
    }

    public abstract VH onCreateStripOfHolder(@NonNull View view);

    public abstract void onBindStripOfViewHolder(@NonNull Context context, @NonNull VH holder, VI item, Object position);
}
