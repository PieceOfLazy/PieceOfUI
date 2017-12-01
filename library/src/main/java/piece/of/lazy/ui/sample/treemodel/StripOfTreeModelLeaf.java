package piece.of.lazy.ui.sample.treemodel;

/**
 * Created by zpdl
 */

public class StripOfTreeModelLeaf implements IStripOfTreeModelLeaf {

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
}