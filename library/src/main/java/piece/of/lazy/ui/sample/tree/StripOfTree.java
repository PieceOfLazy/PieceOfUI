package piece.of.lazy.ui.sample.tree;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zpdl
 */

public abstract class StripOfTree<NODE extends IStripOfNode> implements IStripOfNode {
    private class NotifyParam {
        final boolean mTreeChanged;
        final NotifyState mState;

        int mNodePosition;
        int mNodeCount;
        int mLeafCount;

        NotifyParam(NotifyState state) {
            mState = state;
            mTreeChanged = true;

            mNodePosition = -1;
            mNodeCount = -1;
            mLeafCount = -1;
        }

        NotifyParam(NotifyState state, int nodePosition, int nodeCount, int leafCount) {
            mState = state;
            mTreeChanged = false;

            mNodePosition = nodePosition;
            mNodeCount = nodeCount;
            mLeafCount = leafCount;
        }
    }

    @NonNull
    private final List<NODE> mNodes = new ArrayList<>();

    private int mLastNodeId = 0;

    private IStripOfTreeNotify mNotify = null;
    private NotifyParam mNotifyParam = null;
    private NotifyListener mNotifyListener = null;
    private int mNotifyPosition = -1;
    private boolean mNotifyHasViewType = false;
    private boolean mNotifyExpand = true;

    StripOfTree getRoot() {
        if(mParent == null) {
            return this;
        }

        return mParent.getRoot();
    }

    protected int getNextNodeId() {
        if(mParent == null) {
            mLastNodeId++;
            if(mLastNodeId == Integer.MIN_VALUE) {
                mLastNodeId = 1;
            }
            return mLastNodeId;
        }

        return mParent.getNextNodeId();
    }

    public void beginNotifyChanged(@NonNull IStripOfTreeNotify notify) {
        beginNotifyChanged(notify, -1, null);
    }

    @Override
    public void beginNotifyChanged(@NonNull IStripOfTreeNotify notify, int position, NotifyListener l) {
        mNotifyHasViewType = hasViewTypeTree();
        mNotifyExpand = isExpandTree();

        mNotify = notify;
        mNotifyListener = l;
        mNotifyParam = null;
        if(position < 0) {
            mNotifyPosition = mNotify.getNodePosition(this);
        } else {
            mNotifyPosition = position;
        }


        int nodePosition = mNotifyPosition + (mNotifyHasViewType ? 1 : 0);
        for(NODE node : mNodes) {
            if(node != null) {
                node.beginNotifyChanged(notify, nodePosition, new NotifyListener() {
                    @Override
                    public void onNotifyChanged(IStripOfNode node) {
                        if(node == null) {
                            return;
                        }

                        for(int i = 0; i < mNodes.size(); i++) {
                            if(node.equals(mNodes.get(i))) {
                                composeNotifyChanged(NotifyState.CHANGED, i, 1);
                                break;
                            }
                        }
                    }
                });
                if(mNotifyExpand) {
                    nodePosition += node.getCount();
                }
            }
        }
    }

    @Override
    public void setNotifyPosition(int position) {
        mNotifyPosition = position;
        refreshChildPosition();
    }

    @Override
    public void endNotifyChanged() {
        hasViewTypeTree();
        isExpandTree();
        commitNotifyChanged();
        for(NODE node : mNodes) {
            if(node != null) {
                node.endNotifyChanged();
            }
        }
        clearNotifyChanged();
    }

    @Override
    public void clearNotifyChanged() {
        mNotify = null;
        mNotifyListener = null;
        mNotifyParam = null;
    }

    @Override
    public void commitNotifyChanged() {
        if(mNotify == null || mNotifyParam == null) {
            return;
        }

        if(mNotifyPosition < 0) {
            mNotifyPosition = mNotify.getNodePosition(this);
        }

        if(mNotifyParam.mTreeChanged) {
            switch (mNotifyParam.mState) {
                case CHANGED: {
                    mNotify.notifyDataChanged(mNotifyPosition, 1);
                    break;
                }
                case INSERTED: {
                    mNotify.notifyDataInserted(mNotifyPosition, 1);
                    break;
                }
                case REMOVED: {
                    mNotify.notifyDataRemoved(mNotifyPosition, 1);
                    break;
                }
            }
        } else {
            switch(mNotifyParam.mState) {
                case CHANGED: {
                    NODE node = getNode(mNotifyParam.mNodePosition);
                    if(node != null) {
                        node.commitNotifyChanged();
                    }
                    break;
                }
                case INSERTED: {
                    int position = mNotifyPosition + (hasViewTypeTree() ? 1 : 0);
                    if(mNotifyParam.mLeafCount > 0 && mNotifyParam.mNodePosition >= 0 && mNotifyParam.mNodePosition < mNodes.size()) {
                        for(int i = 0; i < mNotifyParam.mNodePosition; i++) {
                            position += mNodes.get(i).getCount();
                        }
                        mNotify.notifyDataInserted(position, mNotifyParam.mLeafCount);
                    }
                    break;
                }
                case REMOVED: {
                    int position = mNotifyPosition + (hasViewTypeTree() ? 1 : 0);
                    if(mNotifyParam.mLeafCount > 0 && mNotifyParam.mNodePosition >= 0 && mNotifyParam.mNodePosition <= mNodes.size()) {
                        for(int i = 0; i < mNotifyParam.mNodePosition; i++) {
                            position += mNodes.get(i).getCount();
                        }
                        mNotify.notifyDataRemoved(position, mNotifyParam.mLeafCount);
                    }
                    break;
                }
            }
        }

        refreshChildPosition();
        mNotifyParam = null;
    }

    private void composeNotifyChangedTree(NotifyState state) {
        if(mNotifyParam == null) {
            mNotifyParam = new NotifyParam(state);
            if(mNotifyListener != null) {
                mNotifyListener.onNotifyChanged(this);
            }
            return;
        }

        if(mNotifyParam.mState != state || !mNotifyParam.mTreeChanged) {
            commitNotifyChanged();
            mNotifyParam = new NotifyParam(state);
            if(mNotifyListener != null) {
                mNotifyListener.onNotifyChanged(this);
            }
        }
    }

    private void composeNotifyChanged(NotifyState state, int nodePosition, int nodeCount) {
        NODE node = getNode(nodePosition);
        if(node == null) {
            return;
        }

        if(mNotifyParam == null) {
            mNotifyParam = new NotifyParam(state, nodePosition, nodeCount, node.getCount());
            if(mNotifyListener != null) {
                mNotifyListener.onNotifyChanged(this);
            }
            return;
        }

        if(mNotifyParam.mState != state || mNotifyParam.mTreeChanged) {
            commitNotifyChanged();
            mNotifyParam = new NotifyParam(state, nodePosition, nodeCount, node.getCount());
            if(mNotifyListener != null) {
                mNotifyListener.onNotifyChanged(this);
            }
            return;
        }

        switch(state) {
            case CHANGED: {
                if(mNotifyParam.mNodePosition != nodePosition) {
                    commitNotifyChanged();
                    mNotifyParam = new NotifyParam(state, nodePosition, nodeCount, -1);
                }
                break;
            }
            case INSERTED: {
                if(mNotifyParam.mNodePosition <= nodePosition && mNotifyParam.mNodePosition + mNotifyParam.mNodeCount >= nodePosition) {
                    mNotifyParam.mNodeCount += nodeCount;
                    mNotifyParam.mLeafCount += node.getCount();
                } else if(nodePosition <= mNotifyParam.mNodePosition && nodePosition + nodeCount >= mNotifyParam.mNodePosition) {
                    mNotifyParam.mNodePosition = nodePosition;
                    mNotifyParam.mNodeCount += nodeCount;
                    mNotifyParam.mLeafCount += node.getCount();
                } else {
                    commitNotifyChanged();
                    mNotifyParam = new NotifyParam(state, nodePosition, nodeCount, node.getCount());
                }
                break;
            }
            case REMOVED: {
                if(mNotifyParam.mNodePosition == nodePosition) {
                    mNotifyParam.mNodeCount += nodeCount;
                    mNotifyParam.mLeafCount += node.getCount();
                } else if(nodePosition + nodeCount == mNotifyParam.mNodePosition) {
                    mNotifyParam.mNodePosition = nodePosition;
                    mNotifyParam.mNodeCount += nodeCount;
                    mNotifyParam.mLeafCount += node.getCount();
                } else {
                    commitNotifyChanged();
                    mNotifyParam = new NotifyParam(state, nodePosition, nodeCount, node.getCount());
                }
                break;
            }
        }

        if(mNotifyListener != null) {
            mNotifyListener.onNotifyChanged(this);
        }
    }

    @Override
    public int getCount() {
        return (hasViewTypeTree() ? 1 : 0) + getLeafCount();
    }

    public int getLeafCount() {
        int count = 0;
        if(isExpandTree()) {
            for(NODE node : mNodes) {
                count += node.getCount();
            }
        }
        return count;
    }

    public int getNodeCount() {
        return mNodes.size();
    }

    public final NODE getNode(int index) {
        if(index >= 0 && index < getNodeCount()) {
            return mNodes.get(index);
        }
        return null;
    }

    public final int getNodeIndex(NODE node) {
        return mNodes.indexOf(node);
    }

    public final void clearNode() {
        if(mNodes.size() > 0) {
            if(mNotify != null) {
                composeNotifyChanged(NotifyState.REMOVED, 0, mNodes.size());
            }
        }
        mNodes.clear();
    }

    public final void addNode(NODE node) {
        if(node == null) {
            return;
        }

        if(mNotify != null && isExpandTree()) {
            composeNotifyChanged(NotifyState.INSERTED, mNodes.size(), 1);
        }

        node.setStripId(getNextNodeId());
        node.setParent(this);
        mNodes.add(node);
    }

    public final void addNode(int index, NODE node) {
        if(mNotify != null && isExpandTree()) {
            NODE tmpNode = getNode(index);
            if(tmpNode == null) {
                addNode(node);
                return;
            } else {
                composeNotifyChanged(NotifyState.CHANGED, index, 1);
            }
        }

        node.setStripId(getNextNodeId());
        node.setParent(this);
        mNodes.add(index, node);
    }

    public final void removeNode(NODE node) {
        int index = getNodeIndex(node);
        if(index < 0) {
            return;
        }

        removeNode(index);
    }

    public final NODE removeNode(int index) {
        NODE node = mNodes.get(index);
        if(node == null) {
            return null;
        }


        if(mNotify != null && isExpandTree()) {
            composeNotifyChanged(NotifyState.REMOVED, index, 1);
        }

        mNodes.remove(index);
        node.setParent(null);
        return node;
    }

    public final void changedTree() {
        if(mNotify == null) {
            return;
        }

        if(hasViewTypeTree()) {
            composeNotifyChangedTree(NotifyState.CHANGED);
        }
    }

    private boolean hasViewTypeTree() {
        boolean hasViewType = hasViewType();
        if(mNotify != null && mNotifyHasViewType != hasViewType) {
            commitNotifyChanged();
            mNotifyHasViewType = hasViewType;
            if(hasViewType) {
                composeNotifyChangedTree(NotifyState.INSERTED);
            } else {
                composeNotifyChangedTree(NotifyState.REMOVED);
            }
        }
        return hasViewType;
    }

    private boolean isExpandTree() {
        boolean isExpand = isExpand();
        if(mNotify != null && mNotifyExpand != isExpand) {
            mNotifyExpand = isExpand;
            if(hasViewType()) {
                composeNotifyChanged(NotifyState.CHANGED, 0, 1);
            }
            if(isExpand) {
                composeNotifyChanged(NotifyState.INSERTED, 0, getNodeCount());
            } else {
                composeNotifyChanged(NotifyState.REMOVED, 0, getNodeCount());
            }
        }
        return isExpand;
    }

    private void refreshChildPosition() {
        int nodePosition = mNotifyPosition + (hasViewTypeTree() ? 1 : 0);
        for(NODE node : mNodes) {
            if(node != null) {
                node.setNotifyPosition(nodePosition);
                if(isExpandTree()) {
                    nodePosition += node.getCount();
                }
            }
        }
    }

    private StripOfTree mParent = null;

    @Override
    public void setParent(IStripOfNode parent) {
        if(parent == null) {
            mParent = null;
        } else if(parent instanceof StripOfTree) {
            mParent = (StripOfTree) parent;
        }
    }

    @Override
    public IStripOfNode getParent() {
        return mParent;
    }

    private int mStripId = -1;

    @Override
    public void setStripId(int id) {
        mStripId = id;
    }

    @Override
    public int getStripId() {
        return mStripId;
    }
}