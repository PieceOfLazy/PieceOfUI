package piece.of.lazy.ui.sample.wrappertree;

import android.support.annotation.NonNull;

/**
 * Created by zpdl
 */

public interface IStripOfNode<MODEL> {

    long getNodeId();

    IStripOfNode root();

    IStripOfNode getParent();

    MODEL getModel();

    boolean isView();

    boolean isExpand();

    int getViewCount();

    int getViewPosition();

    int getChildNodeCount();

    IStripOfNode getChildNode(int nPos);

    void changedNode();

    void clearNode();

    void addNode(IStripOfNode... nodes);

    void removeNode(int nPos, int nCnt);

    void removeNode(IStripOfNode node);

    void onNotifyChanged(@NonNull StripOfNotifyParam param);

    void changedChildNode();
}