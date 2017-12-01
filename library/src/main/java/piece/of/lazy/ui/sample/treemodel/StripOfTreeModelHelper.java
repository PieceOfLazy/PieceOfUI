package piece.of.lazy.ui.sample.treemodel;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zpdl
 */

public class StripOfTreeModelHelper {
    public static void beginTransition(IStripOfTreeModelLeaf model, IStripOfTreeNotify notify) {
        if(model == null) {
            return;
        }

        IStripOfLeaf tree = model.getTreeInstance();
        if(tree != null && tree instanceof IStripOfNode) {
            ((IStripOfNode) tree).beginTransition(notify, -1, null);
        }
    }

    public static void applyTo(IStripOfTreeModelLeaf model) {
        if(model != null) {
            IStripOfLeaf tree = model.getTreeInstance();
            if(tree != null && tree instanceof IStripOfNode) {
                ((IStripOfNode) tree).applyTo();
            }
        }
    }

    public static <T extends IStripOfTreeModelNode> StripOfTree<T> baseTree(T model) {
        StripOfTree<T> tree = new StripOfTree<>();
        tree.setModel(model);
        return tree;
    }

    public static <T extends IStripOfTreeModelNode> StripOfNode<T> baseNode(T model) {
        StripOfNode<T> tree = new StripOfNode<>();
        tree.setModel(model);
        return tree;
    }

    public static void changedModel(IStripOfTreeModelLeaf model) {
        if(model == null) {
            return;
        }

        IStripOfLeaf modelNode = model.getTreeInstance();
        if(modelNode == null) {
            return;
        }

        if(modelNode instanceof StripOfLeaf) {
            IStripOfNode node = modelNode.getParent();
            if(node != null && node instanceof StripOfNode) {
                ((StripOfNode) node).changedLeaf((StripOfLeaf) modelNode);
            }
        } else if(modelNode instanceof StripOfNode) {
            ((StripOfNode) modelNode).changedNode();
        } else if(modelNode instanceof StripOfTree) {
            ((StripOfTree) modelNode).changedTree();
        }
    }

    public static IStripOfTreeModelLeaf getParentModel(IStripOfTreeModelLeaf model) {
        if(model != null) {
            IStripOfLeaf tree = model.getTreeInstance();
            if(tree != null && tree.getParent() != null) {
                return tree.getParent().getModel();
            }
        }
        return null;
    }

    public static boolean isTree(IStripOfTreeModelLeaf model) {
        if(model != null) {
            IStripOfLeaf tree = model.getTreeInstance();
            if(tree != null && tree instanceof StripOfTree) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNode(IStripOfTreeModelLeaf model) {
        if(model != null) {
            IStripOfLeaf tree = model.getTreeInstance();
            if(tree != null && tree instanceof StripOfNode) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLeaf(IStripOfTreeModelLeaf model) {
        if(model != null) {
            IStripOfLeaf tree = model.getTreeInstance();
            if(tree != null && tree instanceof StripOfLeaf) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getChildNodeModel(IStripOfTreeModelLeaf model, Class<T> type) {
        if(model == null || type == null) {
            return null;
        }

        IStripOfLeaf leaf = model.getTreeInstance();
        if(leaf != null) {
            if(leaf instanceof StripOfTree) {
                StripOfTree tree = (StripOfTree) leaf;

                for(int i = 0; i < tree.getNodeCount(); i++) {
                    IStripOfNode node = tree.getNode(i);
                    if (node != null && type.isInstance(node.getModel())) {
                        return (T) node.getModel();
                    }
                }
            }
        }

        return null;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public static <T> List<T> getChildNodeModelList(IStripOfTreeModelLeaf model, Class<T> type) {
        List<T> list = new ArrayList<>();
        if(model == null || type == null) {
            return list;
        }

        IStripOfLeaf leaf = model.getTreeInstance();
        if(leaf != null) {
            if(leaf instanceof StripOfTree) {
                StripOfTree tree = (StripOfTree) leaf;

                for(int i = 0; i < tree.getNodeCount(); i++) {
                    IStripOfNode node = tree.getNode(i);
                    if (node != null && type.isInstance(node.getModel())) {
                        list.add((T) node.getModel());
                    }
                }
            }
        }

        return list;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public static <T> List<T> getChildLeafModelList(IStripOfTreeModelLeaf model, Class<T> type) {
        List<T> list = new ArrayList<>();
        if(model == null || type == null) {
            return list;
        }

        IStripOfLeaf leaf = model.getTreeInstance();
        if(leaf != null && leaf instanceof StripOfNode) {
            StripOfNode node = (StripOfNode) leaf;
            for(int i = 0; i < node.getLeafCount(); i++) {
                StripOfLeaf nodeLeaf = node.getLeaf(i);
                if(nodeLeaf != null && nodeLeaf.getModel() != null && type.isInstance(nodeLeaf.getModel())) {
                    list.add((T) nodeLeaf.getModel());
                }
            }
        }

        return list;
    }

    public static void clearNode(IStripOfTreeModelNode model) {
        if(model == null) {
            return;
        }

        IStripOfLeaf container = model.getTreeInstance();
        if(container != null) {
            if(container instanceof StripOfTree) {
                ((StripOfTree) container).clearNode();
            } else if(container instanceof StripOfNode) {
                ((StripOfNode) container).clearLeaf();
            }
        }
    }

    public static int getCount(IStripOfTreeModelNode model) {
        if(model == null) {
            return 0;
        }

        IStripOfLeaf container = model.getTreeInstance();
        if(container != null) {
            if(container instanceof IStripOfNode) {
                return ((IStripOfNode) container).getCount();
            }
        }
        return 0;
    }

    public static <T extends IStripOfTreeModelNode> void addTree(IStripOfTreeModelLeaf treeModel, T model) {
        if(treeModel == null) {
            return;
        }

        IStripOfLeaf container = treeModel.getTreeInstance();
        if(container != null) {
            if(container instanceof StripOfTree) {
                StripOfTree<T> tree = new StripOfTree<>();
                tree.setModel(model);
                ((StripOfTree) container).addNode(tree);
            }
        }
    }

    public static <T extends IStripOfTreeModelNode> void addTree(StripOfTree parent, T model) {
        if(parent == null) {
            return;
        }

        StripOfTree<T> tree = new StripOfTree<>();
        tree.setModel(model);
        parent.addNode(tree);
    }

    public static <T extends IStripOfTreeModelNode> void addNode(IStripOfTreeModelLeaf treeModel, T model) {
        if(treeModel == null) {
            return;
        }

        IStripOfLeaf container = treeModel.getTreeInstance();
        if(container != null) {
            if(container instanceof StripOfTree) {
                StripOfNode<T> node = new StripOfNode<>();
                node.setModel(model);
                ((StripOfTree) container).addNode(node);
            }
        }
    }

    public static <T extends IStripOfTreeModelNode> void addNode(StripOfTree parent, T model) {
        if(parent == null) {
            return;
        }

        StripOfNode<T> node = new StripOfNode<>();
        node.setModel(model);
        parent.addNode(node);
    }

    public static <T extends IStripOfTreeModelLeaf> void addLeaf(IStripOfTreeModelLeaf nodeModel, T model) {
        if(nodeModel == null) {
            return;
        }

        IStripOfLeaf container = nodeModel.getTreeInstance();
        if(container != null) {
            if(container instanceof StripOfNode) {
                StripOfLeaf<T> leaf = new StripOfLeaf<>();
                leaf.setModel(model);
                ((StripOfNode) container).addLeaf(leaf);
            }
        }
    }

    public static <T extends IStripOfTreeModelLeaf> void addLeaf(StripOfNode parent, T model) {
        if(parent == null) {
            return;
        }

        StripOfLeaf<T> leaf = new StripOfLeaf<>();
        leaf.setModel(model);
        parent.addLeaf(leaf);
    }
}