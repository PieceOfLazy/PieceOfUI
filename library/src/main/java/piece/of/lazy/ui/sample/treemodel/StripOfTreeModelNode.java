package piece.of.lazy.ui.sample.treemodel;

/**
 * Created by zpdl
 */

public class StripOfTreeModelNode implements IStripOfTreeModelNode {

    private IStripOfTreeModelListener mListener = null;

    @Override
    public void setOnGetTreeListener(IStripOfTreeModelListener l) {
        mListener = l;
    }

    @Override
    public IStripOfLeaf getTreeInstance() {
        if (mListener != null) {
            return mListener.onGetTreeInstance();
        }
        return null;
    }

    private boolean mHasViewType = false;
    private boolean mIsExpand = false;

    @Override
    public boolean hasViewType() {
        return mHasViewType;
    }

    @Override
    public boolean isExpand() {
        return mIsExpand;
    }

    public void setHasViewType(boolean hasViewType) {
        mHasViewType = hasViewType;
    }

    public void setIsExpand(boolean expand) {
        mIsExpand = expand;
    }
}