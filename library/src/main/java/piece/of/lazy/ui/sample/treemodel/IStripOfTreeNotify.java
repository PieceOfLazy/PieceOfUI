package piece.of.lazy.ui.sample.treemodel;

/**
 * Created by zpdl
 */

public interface IStripOfTreeNotify {
    int getNodePosition(IStripOfNode node);

    void notifyDataSetChangedAll();
    void notifyDataChanged(int position, int count);
    void notifyDataInserted(int position, int count);
    void notifyDataRemoved(int position, int count);
}