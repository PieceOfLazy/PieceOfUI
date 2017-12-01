package piece.of.lazy.ui.sample.tree;

/**
 * Created by zpdl
 */

public abstract class StripOfLeaf implements IStripOfLeaf {
    private StripOfNode mParent = null;
    private int mStripId = -1;

    public void setParent(IStripOfNode parent) {
        if(parent == null) {
            mParent = null;
        } else if(parent instanceof StripOfNode) {
            mParent = (StripOfNode) parent;
        }
    }

    public IStripOfNode getParent() {
        return mParent;
    }

    @Override
    public void setStripId(int id) {
        mStripId = id;
    }

    @Override
    public int getStripId() {
        return mStripId;
    }
}