package piece.of.lazy.ui.sample.tree;

/**
 * Created by zpdl
 */

public interface IStripOfLeaf {
    void setStripId(int id);
    int getStripId();

    void setParent(IStripOfNode node);
    IStripOfNode getParent();

    int viewType();
}