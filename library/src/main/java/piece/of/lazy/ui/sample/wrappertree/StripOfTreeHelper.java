package piece.of.lazy.ui.sample.wrappertree;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zpdl
 */

public class StripOfTreeHelper {
    @NonNull
    public static <T> StripOfNode<T> createNode(@NonNull T model) {
        return new StripOfNode<>(model);
    }

    public static <T> StripOfNode<T> addNode(@NonNull IStripOfNode parent, @NonNull T model) {
        StripOfNode<T> node = createNode(model);
        parent.addNode(node);

        return node;
    }

    public static StripOfNode addNodeEmpty(@NonNull IStripOfNode parent) {
        StripOfNode node = new StripOfNode<>(new StripOfNodeModelDefault(false, true));
        parent.addNode(node);

        return node;
    }

    public static <T> void addNodes(@NonNull IStripOfNode parent, @NonNull List<T> models) {
        if(models.size() <= 0) {
            return;
        }

        StripOfNode[] nodes = new StripOfNode[models.size()];
        for(int i = 0; i < nodes.length; i++) {
            nodes[i] = createNode(models.get(i));
        }

        parent.addNode(nodes);
    }

    public static void removeNode(IStripOfNode node) {
        if(node == null || node.getParent() == null) {
            return;
        }

        node.getParent().removeNode(node);
    }

    public static <T> T getChildModel(IStripOfNode node, Class<T> type) {
        if(node == null || type == null) {
            return null;
        }

        for(int i = 0; i < node.getChildNodeCount(); i++) {
            IStripOfNode child = node.getChildNode(i);
            if(child != null && type.isInstance(child.getModel())) {
                //noinspection unchecked
                return (T) child.getModel();
            }
        }

        return null;
    }

    @NonNull
    public static <T> List<T> getChildModelList(IStripOfNode node, Class<T> type) {
        List<T> list = new ArrayList<>();
        if(node == null || type == null) {
            return list;
        }

        for(int i = 0; i < node.getChildNodeCount(); i++) {
            IStripOfNode child = node.getChildNode(i);
            if(child != null && type.isInstance(child.getModel())) {
                //noinspection unchecked
                list.add((T) child.getModel());
            }
        }

        return list;
    }
}