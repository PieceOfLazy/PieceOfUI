package piece.of.lazy.ui.sample.tree;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zpdl
 */

public abstract class StripOfNode<LEAF extends IStripOfLeaf> implements IStripOfNode {
    private class NotifyParam {
        int mPosition;
        int mCount;
        NotifyState mState;


        NotifyParam(NotifyState state, int position, int count) {
            mState = state;
            mPosition = position;
            mCount = count;
        }
    }

    @NonNull
    private final List<LEAF> mLeaves = new ArrayList<>();

    private int mLastLeafId = 0;

    private IStripOfTreeNotify mNotify = null;
    private NotifyParam mNotifyParam = null;
    private NotifyListener mNotifyListener = null;
    private int mNotifyPosition = -1;
    private boolean mNotifyHasViewType = false;
    private boolean mNotifyExpand = false;

    public void beginNotifyChanged(@NonNull IStripOfTreeNotify notify) {
        beginNotifyChanged(notify, -1, null);
    }

    @Override
    public synchronized final void beginNotifyChanged(@NonNull IStripOfTreeNotify notify, int position, NotifyListener l) {
        mNotifyHasViewType = hasViewType();
        mNotifyExpand = isExpand();

        mNotify = notify;
        mNotifyListener = l;
        mNotifyParam = null;
        if(position < 0) {
            mNotifyPosition = mNotify.getNodePosition(this);
        } else {
            mNotifyPosition = position;
        }
    }

    @Override
    public void setNotifyPosition(int position) {
        mNotifyPosition = position;
    }

    @Override
    public synchronized final void endNotifyChanged() {
        hasViewTypeNode();
        isExpandNode();
        commitNotifyChanged();
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

        int position = mNotifyPosition + mNotifyParam.mPosition;
        if(mNotifyPosition < 0 || position < 0) {
            return;
        }

        switch(mNotifyParam.mState) {
            case CHANGED: {
                mNotify.notifyDataChanged(position, mNotifyParam.mCount);
                break;
            }
            case INSERTED: {
                mNotify.notifyDataInserted(position, mNotifyParam.mCount);
                break;
            }
            case REMOVED: {
                mNotify.notifyDataRemoved(position, mNotifyParam.mCount);
                break;
            }
        }

        mNotifyParam = null;
    }

    private void composeNotifyChanged(NotifyState state, int position, int count) {
        if(mNotifyParam == null) {
            mNotifyParam = new NotifyParam(state, position, count);
            if(mNotifyListener != null) {
                mNotifyListener.onNotifyChanged(this);
            }
            return;
        }

        if(mNotifyParam.mState != state) {
            commitNotifyChanged();
            mNotifyParam = new NotifyParam(state, position, count);
            if(mNotifyListener != null) {
                mNotifyListener.onNotifyChanged(this);
            }
            return;
        }

        switch(state) {
            case CHANGED: {
                if(mNotifyParam.mPosition <= position && mNotifyParam.mPosition + mNotifyParam.mCount >= position) {
                    int oldMaxPosition = mNotifyParam.mPosition + mNotifyParam.mCount;
                    int newMaxPosition = position + count;

                    if(oldMaxPosition < newMaxPosition) {
                        mNotifyParam.mCount = newMaxPosition - mNotifyParam.mPosition;
                    }
                } else if(position <= mNotifyParam.mPosition && position + count >= mNotifyParam.mPosition) {
                    int oldMaxPosition = mNotifyParam.mPosition + mNotifyParam.mCount;
                    int newMaxPosition = position + count;

                    mNotifyParam.mPosition = position;
                    if(oldMaxPosition > newMaxPosition) {
                        mNotifyParam.mCount = oldMaxPosition - mNotifyParam.mPosition;
                    } else {
                        mNotifyParam.mCount = count;
                    }
                } else {
                    commitNotifyChanged();
                    mNotifyParam = new NotifyParam(state, position, count);
                }
                break;
            }
            case INSERTED: {
                if(mNotifyParam.mPosition <= position && mNotifyParam.mPosition + mNotifyParam.mCount >= position) {
                    mNotifyParam.mCount += count;
                } else if(position <= mNotifyParam.mPosition && position + count >= mNotifyParam.mPosition) {
                    mNotifyParam.mPosition = position;
                    mNotifyParam.mCount += count;
                } else {
                    commitNotifyChanged();
                    mNotifyParam = new NotifyParam(state, position, count);
                }
                break;
            }
            case REMOVED: {
                if(mNotifyParam.mPosition == position) {
                    mNotifyParam.mCount += count;
                } else if(position + count == mNotifyParam.mPosition) {
                    mNotifyParam.mPosition = position;
                    mNotifyParam.mCount += count;
                } else {
                    commitNotifyChanged();
                    mNotifyParam = new NotifyParam(state, position, count);
                }
                break;
            }
        }

        if(mNotifyListener != null) {
            mNotifyListener.onNotifyChanged(this);
        }
    }

    public final int getCount() {
        return (isExpandNode() ? getLeafCount() : 0) + (hasViewTypeNode() ? 1 : 0);
    }

    public int getLeafCount() {
        return mLeaves.size();
    }

    public final LEAF getLeaf(int index) {
        if(index >= 0 && index < mLeaves.size()) {
            return mLeaves.get(index);
        }
        return null;
    }

    public final boolean containLeaf(LEAF leaf) {
        return mLeaves.contains(leaf);
    }

    public final int getLeafIndex(LEAF leaf) {
        return mLeaves.indexOf(leaf);
    }

    public final void clearLeaf() {
        if(mLeaves.size() > 0) {
            if (mNotify != null) {
                composeNotifyChanged(NotifyState.REMOVED, hasViewTypeNode() ? 1 : 0, mLeaves.size());
            }
            mLeaves.clear();
        }
    }

    public final void changedNode() {
        if(mNotify == null) {
            return;
        }

        if(hasViewTypeNode()) {
            composeNotifyChanged(NotifyState.CHANGED, 0, 1);
        }
    }

    public final void changedLeaf(LEAF leaf) {
        if(mNotify != null && isExpandNode()) {
            int index = getLeafIndex(leaf);
            if(index < 0) {
                return;
            }

            int position = index + (hasViewTypeNode() ? 1 : 0);
            if(position >= 0 && position < getCount()) {
                composeNotifyChanged(NotifyState.CHANGED, position, 1);
            }
        }
    }

    public final void addLeaf(LEAF leaf) {
        if(leaf == null) {
            return;
        }

        if(mNotify != null && isExpandNode()) {
            composeNotifyChanged(NotifyState.INSERTED, getCount(), 1);
        }

        mLastLeafId++;
        if(mLastLeafId == Integer.MIN_VALUE) {
            mLastLeafId = 1;
        }

        leaf.setStripId(mLastLeafId);
        leaf.setParent(this);
        mLeaves.add(leaf);
    }

    public final void addLeaf(int index, LEAF leaf) {
        if(leaf == null) {
            return;
        }

        if(mNotify != null && isExpandNode()) {
            LEAF tmpLeaf = getLeaf(index);
            if(tmpLeaf == null) {
                addLeaf(leaf);
                return;
            } else {
                composeNotifyChanged(NotifyState.CHANGED, index + (hasViewTypeNode() ? 1 : 0), 1);
            }
        }

        mLastLeafId++;
        if(mLastLeafId == Integer.MIN_VALUE) {
            mLastLeafId = 1;
        }
        leaf.setStripId(mLastLeafId);
        leaf.setParent(this);
        mLeaves.add(index, leaf);
    }

    public final void removeLeaf(LEAF leaf) {
        int index = getLeafIndex(leaf);
        if(index < 0) {
            return;
        }

        removeLeaf(index);
    }

    public final LEAF removeLeaf(int index) {
        LEAF leaf = mLeaves.get(index);
        if(leaf == null) {
            return null;
        }

        if(mNotify != null && isExpandNode()) {
            int position = index + (hasViewTypeNode() ? 1 : 0);
            if(position >= 0 && position < getCount()) {
                composeNotifyChanged(NotifyState.REMOVED, position, 1);
            }
        }

        mLeaves.remove(index);
        leaf.setParent(null);

        return leaf;
    }

    private boolean hasViewTypeNode() {
        boolean hasViewType = hasViewType();
        if(mNotify != null && mNotifyHasViewType != hasViewType) {
            mNotifyHasViewType = hasViewType;
            if(hasViewType) {
                composeNotifyChanged(NotifyState.INSERTED, 0, 1);
            } else {
                composeNotifyChanged(NotifyState.REMOVED, 0, 1);
            }
        }
        return hasViewType;
    }

    private boolean isExpandNode() {
        boolean isExpandNode = isExpand();
        if(mNotify != null && mNotifyExpand != isExpandNode) {
            mNotifyExpand = isExpandNode;
            if(hasViewType()) {
                composeNotifyChanged(NotifyState.CHANGED, 0, 1);
            }
            if(isExpandNode) {
                composeNotifyChanged(NotifyState.INSERTED, hasViewTypeNode() ? 1 : 0, getLeafCount());
            } else {
                composeNotifyChanged(NotifyState.REMOVED, hasViewTypeNode() ? 1 : 0, getLeafCount());
            }
        }
        return isExpandNode;
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