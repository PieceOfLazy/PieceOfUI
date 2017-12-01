package piece.of.lazy.ui.sample.wrappertree;

import android.support.annotation.NonNull;

/**
 * Created by zpdl
 */

public interface IStripOfNotify {
    int getViewPosition(IStripOfNode node);

    void beginTransition();
    void applyTo();

    @NonNull
    IStripOfNode root();

    void notifyNodeSetChanged();
    void notifyNodeChanged(int vPos, int cnt);
    void notifyNodeInserted(int vPos, int cnt);
    void notifyNodeRemoved(int vPos, int cnt);
}