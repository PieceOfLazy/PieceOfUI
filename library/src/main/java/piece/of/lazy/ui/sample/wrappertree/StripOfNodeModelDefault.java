package piece.of.lazy.ui.sample.wrappertree;

import android.support.annotation.NonNull;

/**
 * Created by zpdl
 */

public class StripOfNodeModelDefault implements StripOfNodeModel {

    private OnNodeModelListener mListener = null;

    private boolean mView;
    private boolean mExpand;

    public StripOfNodeModelDefault() {
        this(true);
    }

    public StripOfNodeModelDefault(boolean view) {
        this(view, true);
    }

    public StripOfNodeModelDefault(boolean view, boolean expand) {
        mView = view;
        mExpand = expand;
    }

    @Override
    public void setOnNodeModelListener(OnNodeModelListener l) {
        mListener = l;
    }

    @Override
    public boolean isView() {
        return mView;
    }

    @Override
    public boolean isExpand() {
        return mExpand;
    }

    @Override
    public void setView(boolean view) {
        if(mView != view) {
            mView = view;
            if(mListener != null) {
                mListener.onChangedView();
            }
        }
    }

    @Override
    public void setExpand(boolean expand) {
        if(mExpand != expand) {
            mExpand = expand;
            if(mListener != null) {
                mListener.onChangedExpand();
            }
        }
    }

    public StripOfNode getNode() {
        if (mListener != null) {
            return mListener.onGetNodeInstance();
        }
        return null;
    }

    public <T> StripOfNode<T> getNode(@NonNull Class<T> modelClass) {
        StripOfNode node = getNode();

        if(node != null && modelClass.isInstance(node.getModel())) {
            //noinspection unchecked
            return (StripOfNode<T>) node;
        }
        return null;
    }
}