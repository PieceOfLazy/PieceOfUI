package piece.of.lazy.ui.sample.treemodel;

/**
 * Created by zpdl
 */

public interface IStripOfLeaf<MODEL extends IStripOfTreeModelLeaf> {
    void setStripId(int id);
    int getStripId();

    void setParent(IStripOfNode node);
    IStripOfNode getParent();

    MODEL getModel();
    void setModel(MODEL model);
}