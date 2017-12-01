package piece.of.lazy.ui.sample.treemodel;

import android.support.annotation.NonNull;

/**
 * Created by zpdl
 */

public interface IStripOfNode<MODEL extends IStripOfTreeModelNode> extends IStripOfNodeModel, IStripOfLeaf<MODEL> {

    void beginTransition(@NonNull IStripOfTreeNotify notify, int position, NotifyListener l);

    void applyTo();

    void commitNotifyChanged();

    void setNotifyPosition(int position);

    void clearNotifyChanged();

    int getCount();

    enum NotifyState {
        CHANGED,
        INSERTED,
        REMOVED
    }

    interface NotifyListener {
        void onNotifyChanged(IStripOfNode node);
    }
}