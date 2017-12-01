package piece.of.lazy.ui.sample.treemodel;

/**
 * Created by zpdl
 */

public class StripOfLeaf<MODEL extends IStripOfTreeModelLeaf> implements IStripOfLeaf<MODEL> {
    private StripOfNode mParent = null;
    private int mStripId = -1;

    private MODEL mModel = null;

    @Override
    public MODEL getModel() {
        return mModel;
    }

    @Override
    public void setModel(MODEL model) {
        if (mModel != null) {
            mModel.setOnGetTreeListener(null);
        }
        mModel = model;
        if (mModel != null) {
            mModel.setOnGetTreeListener(new IStripOfTreeModelListener() {
                @Override
                public IStripOfLeaf onGetTreeInstance() {
                    return StripOfLeaf.this;
                }
            });
        }
    }

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