package piece.of.lazy.ui.sample.wrappertree;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zpdl
 */

public class StripOfNodeContainer {
    @NonNull
    private final List<StripOfNode> mNodes = new ArrayList<>();

    private int mViewCount = 0;

    public int traversals(int viewCount) {
        if(mViewCount != viewCount) {
            mViewCount = 0;
            for (StripOfNode node : mNodes) {
                node.traversals(mViewCount);
                mViewCount += node.getViewCount();
            }
        }
        return mViewCount;
    }

    public void setNodeId(@NonNull StripOfRoot root) {
        for(StripOfNode node : mNodes) {
            node.setNodeId(root);
        }
    }

    public int getViewCount() {
        return mViewCount;
    }

    public int getNodeCount() {
        return mNodes.size();
    }

    public StripOfNode getNode(int position) {
        if(position >= 0 && position < getNodeCount()) {
            return mNodes.get(position);
        }
        return null;
    }

    public void clearNode() {
        mNodes.clear();
    }

    public void addNode(StripOfNode node) {
        mNodes.add(node);
    }

    public StripOfNode removeNode(int nPos) {
        return mNodes.remove(nPos);
    }

    public int indexOfNode(StripOfNode node) {
        return mNodes.indexOf(node);
    }
}