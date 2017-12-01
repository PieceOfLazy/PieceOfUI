package piece.of.lazy.ui.sample.wrappertree;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zpdl
 */

public class StripOfRoot implements IStripOfNode<Void> {

    @NonNull
    private List<StripOfNode> mNodes = new ArrayList<>();

    private int mViewCount = 0;

    private long mNextNodeId = 0;

    private IStripOfNotify mNotify = null;
    private StripOfNotifyParam mNotifyParam = null;

    StripOfRoot() {
    }

    public void beginTransition(@NonNull IStripOfNotify notify) {
        mNotifyParam = null;
        mNotify = notify;
    }

    public void applyTo() {
        StripOfNotifyParam composeParam = mNotifyParam;
        mNotifyParam = null;
        applyTo(composeParam);
    }

    IStripOfNotify getNotify() {
        return mNotify;
    }

    long getNextNodeId() {
        mNextNodeId++;
        if(mNextNodeId == Long.MIN_VALUE) {
            mNextNodeId = 1;
        }
        return mNextNodeId;
    }

    void traversals() {
        mViewCount = 0;
        for(StripOfNode node : mNodes) {
            node.traversals(mViewCount);
            mViewCount += node.getViewCount();
        }
    }

    @Override
    public long getNodeId() {
        return 0;
    }

    @Override
    public IStripOfNode root() {
        return this;
    }

    @Override
    public IStripOfNode getParent() {
        return null;
    }

    @Override
    public Void getModel() {
        return null;
    }

    @Override
    public boolean isView() {
        return false;
    }

    @Override
    public boolean isExpand() {
        return true;
    }

    @Override
    public int getViewCount() {
        return mViewCount;
    }

    @Override
    public int getViewPosition() {
        return 0;
    }

    @Override
    public int getChildNodeCount() {
        return mNodes.size();
    }

    @Override
    public IStripOfNode getChildNode(int nPos) {
        if(nPos >= 0 && nPos < getChildNodeCount()) {
            return mNodes.get(nPos);
        }
        return null;
    }

    @Override
    public void changedNode() {
    }

    @Override
    public void clearNode() {
        mNodes.clear();
        if(mNotify != null && mViewCount > 0) {
            onNotifyChanged(new StripOfNotifyParam(StripOfNotifyParam.State.REMOVED, 0, mViewCount));
        }
    }

    @Override
    public void addNode(IStripOfNode... nodes) {
        if(nodes == null || nodes.length <= 0) {
            return;
        }

        int addedCount = 0;
        for(IStripOfNode node : nodes) {
            if(node != null && node instanceof StripOfNode) {
                StripOfNode implNode = (StripOfNode) node;
                implNode.setParent(this);
                implNode.traversals(mViewCount + addedCount);
                addedCount += implNode.getViewCount();

                mNodes.add(implNode);
            }
        }

        if (mNotify != null) {
            onNotifyChanged(new StripOfNotifyParam(StripOfNotifyParam.State.INSERTED, mViewCount, addedCount));
        }

    }

    @Override
    public void removeNode(int nPos, int nCnt) {
        int removedPosition = -1;
        int removedCount = 0;
        for(int i = 0; i < nCnt; i++) {
            StripOfNode removeNode = mNodes.remove(nPos);
            if (removeNode != null) {
                if(removedPosition < 0) {
                    removedPosition = removeNode.getViewPosition();
                }
                removedCount += removeNode.getViewCount();
                removeNode.setParent(null);
            }
        }
        if(removedPosition >= 0 && removedCount > 0) {
            onNotifyChanged(new StripOfNotifyParam(StripOfNotifyParam.State.REMOVED, removedPosition, removedCount));
        }
    }

    @Override
    public void removeNode(IStripOfNode node) {
        if(node instanceof StripOfNode) {
            int index = mNodes.indexOf(node);
            if(index >= 0) {
                removeNode(index, 1);
            }
        }
    }

//    @Override
//    public void addNode(IStripOfNode node) {
//        if(node != null && node instanceof StripOfNode) {
//            StripOfNode implNode = (StripOfNode) node;
//            implNode.setParent(this);
//            mNodes.add(implNode);
//
//            if (mNotify != null) {
//                implNode.setParent(this);
//                implNode.traversals(mViewCount);
//                mViewCount += implNode.getViewCount();
//                onNotifyChanged(new StripOfNotifyParam(StripOfNotifyParam.State.INSERTED, mViewCount, implNode.getViewCount()));
//            }
//        }
//    }

//    @Override
//    public void removeNode(int nPos) {
//        StripOfNode removeNode = mNodes.remove(nPos);
//        if(removeNode != null) {
//            removeNode.setParent(null);
//            onNotifyChanged(new StripOfNotifyParam(StripOfNotifyParam.State.REMOVED, removeNode.getViewPosition(), removeNode.getViewCount()));
//            removeNode.setParent(null);
//        }
//    }

    public void onNotifyChanged(@NonNull StripOfNotifyParam param) {
        if(mNotify == null) {
            return;
        }

        if(mNotifyParam == null) {
            mNotifyParam = param;
        } else {
            StripOfNotifyParam composeParam = mNotifyParam.composeParam(param);
            if (composeParam == null) {
                composeParam = mNotifyParam;
                mNotifyParam = param;
                applyTo(composeParam);
            } else {
                mNotifyParam = composeParam;
            }
        }
    }

    @Override
    public void changedChildNode() {

    }

    private void applyTo(StripOfNotifyParam param) {
        if(mNotify == null || param == null) {
            return;
        }

        switch (param.getState()) {
            case CHANGED: {
//                Trace.i("KKH", "applyTo CHANGED : pos : "+param.getViewPosition()+" cnt : "+param.getViewCount());
                mNotify.notifyNodeChanged(param.getViewPosition(), param.getViewCount());
                break;
            }
            case INSERTED: {
                traversals();
//                Trace.i("KKH", "applyTo INSERTED : pos : "+param.getViewPosition()+" cnt : "+param.getViewCount());
                mNotify.notifyNodeInserted(param.getViewPosition(), param.getViewCount());
                if(mNotifyParam != null) {
                    if(param.getViewPosition() + param.getViewCount() <= mNotifyParam.getViewPosition()) {
                        mNotifyParam.setViewPosition(mNotifyParam.getViewPosition() + param.getViewCount());
                    }
                }
                break;
            }
            case REMOVED: {
                traversals();
//                Trace.i("KKH", "applyTo REMOVED : pos : "+param.getViewPosition()+" cnt : "+param.getViewCount());
                mNotify.notifyNodeRemoved(param.getViewPosition(), param.getViewCount());
                if(mNotifyParam != null) {
                    if (param.getViewPosition() + param.getViewCount() <= mNotifyParam.getViewPosition()) {
                        mNotifyParam.setViewPosition(mNotifyParam.getViewPosition() - param.getViewCount());
                    }
                }
                break;
            }
        }
    }
}