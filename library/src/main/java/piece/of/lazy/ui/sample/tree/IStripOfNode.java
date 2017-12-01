package piece.of.lazy.ui.sample.tree;

import android.support.annotation.NonNull;

/**
 * Created by zpdl
 */

public interface IStripOfNode extends IStripOfLeaf {

    void beginNotifyChanged(@NonNull IStripOfTreeNotify notify, int position, NotifyListener l);

    void endNotifyChanged();

    void commitNotifyChanged();

    void setNotifyPosition(int position);

    void clearNotifyChanged();

    int getCount();

    boolean hasViewType();

    boolean isExpand();

    enum NotifyState {
        CHANGED,
        INSERTED,
        REMOVED
    }

    interface NotifyListener {
        void onNotifyChanged(IStripOfNode node);
    }
}