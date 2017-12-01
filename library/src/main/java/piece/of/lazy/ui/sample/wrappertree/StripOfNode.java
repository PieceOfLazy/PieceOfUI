package piece.of.lazy.ui.sample.wrappertree;

import android.support.annotation.NonNull;

/**
 * Created by zpdl
 */

public class StripOfNode<MODEL> implements IStripOfNode<MODEL> {

    @NonNull
    private final MODEL mModel;
    private final StripOfNodeModel mNodeModel;
    private final StripOfNodeContainer mNodeContainer;

    private boolean mIsNodeParent = false;

    private IStripOfNode mParent = null;
    private long mNodeId = 0;

    private int mViewPosition = 0;

    public StripOfNode(@NonNull MODEL model) {
        mIsNodeParent = false;

        mModel = model;
        if(mModel instanceof StripOfNodeModel) {
            mNodeModel = (StripOfNodeModel) mModel;
            mNodeModel.setOnNodeModelListener(new StripOfNodeModel.OnNodeModelListener() {
                @Override
                public StripOfNode onGetNodeInstance() {
                    return StripOfNode.this;
                }

                @Override
                public void onChangedView() {
                    if(mParent == null) {
                        return;
                    }

                    if(mNodeModel.isView()) {
                        mParent.onNotifyChanged(new StripOfNotifyParam(StripOfNotifyParam.State.INSERTED, mViewPosition, 1));
                    } else {
                        mParent.onNotifyChanged(new StripOfNotifyParam(StripOfNotifyParam.State.REMOVED, mViewPosition, 1));
                    }
                    mParent.changedChildNode();
                }

                @Override
                public void onChangedExpand() {
                    if(mParent == null) {
                        return;
                    }

                    setView(isView());

                    int vPos = mViewPosition + (isView() ? 1 : 0);
                    if (isExpand()) {
                        if (mNodeContainer != null) {
                            int viewCount = getInternalViewCount();
                            if(viewCount > 0) {
                                mParent.onNotifyChanged(new StripOfNotifyParam(StripOfNotifyParam.State.INSERTED, vPos, viewCount));
                            }
                        }
                    } else {
                        int viewCount = getInternalViewCount();
                        if(viewCount > 0) {
                            mParent.onNotifyChanged(new StripOfNotifyParam(StripOfNotifyParam.State.REMOVED, vPos, viewCount));
                        }
                    }
                }
            });

            mNodeContainer = new StripOfNodeContainer();
        } else {
            mNodeModel = null;
            mNodeContainer = null;
        }
    }

    @Override
    public boolean isView() {
        return mNodeModel == null || mNodeModel.isView();
    }

    @Override
    public boolean isExpand() {
        return mNodeModel != null && mNodeModel.isExpand();
    }

    public void setView(boolean isView) {
        if(mNodeModel != null) {
            if(mNodeModel.isView() != isView){
                mNodeModel.setView(isView);
            } else {
                mParent.onNotifyChanged(new StripOfNotifyParam(StripOfNotifyParam.State.CHANGED, mViewPosition, 1));
            }
        } else {
            mParent.onNotifyChanged(new StripOfNotifyParam(StripOfNotifyParam.State.CHANGED, mViewPosition, 1));
        }
    }

    public void setExpand(boolean isExpand) {
        if(mNodeModel != null) {
            mNodeModel.setExpand(isExpand);
        }
    }

    public boolean isNode() {
        return mNodeContainer != null;
    }

    @Override
    public IStripOfNode root() {
        if(mParent == null) {
            return null;
        } else {
            return mParent.root();
        }
    }

    public void setParent(IStripOfNode node) {
        mParent = node;
        IStripOfNode root = root();
        if(root != null && root instanceof StripOfRoot) {
            setNodeId((StripOfRoot) root);
        }
    }

    void setNodeId(StripOfRoot root) {
        if(root != null) {
            mNodeId = root.getNextNodeId();
            if(mNodeContainer != null) {
                mNodeContainer.setNodeId(root);
            }
        }
    }

    public IStripOfNode getParent() {
        return mParent;
    }

    @Override
    public long getNodeId() {
        return mNodeId;
    }

    @Override
    public MODEL getModel() {
        return mModel;
    }

    @Override
    public int getViewCount() {
        int viewCount = isView() ? 1 : 0;
        if(isExpand()) {
            viewCount += getInternalViewCount();
        }
        return viewCount;
    }

    @Override
    public int getViewPosition() {
        return mViewPosition;
    }

    @Override
    public int getChildNodeCount() {
        return mNodeContainer.getNodeCount();
    }

    @Override
    public IStripOfNode getChildNode(int nPos) {
        return mNodeContainer.getNode(nPos);
    }

    @Override
    public void onNotifyChanged(@NonNull StripOfNotifyParam param) {
        if(mParent != null) {
            switch (param.getState()) {
                case INSERTED: {
                    mViewCount += param.getViewCount();
                    break;
                }
                case REMOVED: {
                    mViewCount -= param.getViewCount();
                    break;
                }
            }
            param.setViewPosition(param.getViewPosition() + mViewPosition + (isView() ? 1 : 0));
            mParent.onNotifyChanged(param);
        }
    }

    @Override
    public void changedChildNode() {
        if(mNodeContainer != null) {
            mViewCount = mNodeContainer.traversals(-1);
        }
    }

    @Override
    public void changedNode() {
        if(isView()) {
            setView(true);
        }
    }

    @Override
    public void clearNode() {
        if(mNodeContainer == null || mNodeContainer.getNodeCount() <= 0) {
            return;
        }

        int vCnt = getInternalViewCount();

        for(int i = 0; i < mNodeContainer.getNodeCount(); i++) {
            mNodeContainer.getNode(i).setParent(null);
        }
        mNodeContainer.clearNode();
        if(isExpand()) {
            onNotifyChanged(new StripOfNotifyParam(StripOfNotifyParam.State.REMOVED, 0, vCnt));
        } else {
            setInternalViewCount(0);
        }
    }

    @Override
    public void addNode(IStripOfNode... nodes) {
        if(mNodeContainer == null || nodes == null || nodes.length <= 0) {
            return;
        }

        int addedCount = 0;
        int viewCount = getInternalViewCount();
        for(IStripOfNode node : nodes) {
            if(node != null && node instanceof StripOfNode) {
                StripOfNode implNode = (StripOfNode) node;
                implNode.setParent(this);
                implNode.traversals(viewCount + addedCount);

                if(!mIsNodeParent) {
                    if(implNode.isNode()) {
                        mIsNodeParent = true;
                    }
                }

                mNodeContainer.addNode(implNode);
                addedCount += implNode.getViewCount();
            }
        }

        if(addedCount > 0) {
            if (isExpand() && getParent() != null) {
                onNotifyChanged(new StripOfNotifyParam(StripOfNotifyParam.State.INSERTED, viewCount, addedCount));
            } else {
                setInternalViewCount(viewCount + addedCount);
            }
        }
    }

    @Override
    public void removeNode(int nPos, int nCnt) {
        int removedPosition = -1;
        int removedCount = 0;

        for(int i = 0; i < nCnt; i++) {
            StripOfNode removeNode = mNodeContainer.removeNode(nPos);
            if (removeNode != null) {
                if(removedPosition < 0) {
                    removedPosition = removeNode.getViewPosition();
                }
                removedCount += removeNode.getViewCount();
                removeNode.setParent(null);
            }
        }
        if(removedPosition >= 0 && removedCount > 0) {
            if(isExpand() && getParent() != null) {
                onNotifyChanged(new StripOfNotifyParam(StripOfNotifyParam.State.REMOVED, removedPosition, removedCount));
            } else {
                setInternalViewCount(getInternalViewCount() - removedCount);
            }
        }
    }

    @Override
    public void removeNode(IStripOfNode node) {
        if(node instanceof StripOfNode) {
            int index = mNodeContainer.indexOfNode((StripOfNode) node);
            if(index >= 0) {
                removeNode(index, 1);
            }
        }
    }

    void traversals(int position) {
        mViewPosition = position;
    }

    private int mViewCount = 0;

    private int getInternalViewCount() {
        if(mNodeContainer != null) {
            mViewCount = mNodeContainer.traversals(mViewCount);
        }
        return mViewCount;
    }

    private void setInternalViewCount(int viewCount) {
        mViewCount = viewCount;
    }
}