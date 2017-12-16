package piece.of.lazy.ui.tree

/**
 * Created by zpdl
 */

class PieceOfTreeHelper {

    companion object {
        fun <T> createNode(model: T): PieceOfNode<T> {
            return PieceOfNode(model)
        }

        fun <T> addNode(parent: PieceOfNode<*>, model: T): PieceOfNode<T> {
            val node = createNode(model)
            parent.addChildNode(node)
            return node
        }

        fun <T> addNodes(parent: PieceOfNode<*>, vararg models: T) {
            val nodes = Array(models.size, { createNode(models[it]) })
            parent.addChildNode(*nodes)
        }

        fun addEmptyNode(parent: PieceOfNode<*>): PieceOfNode<PieceOfModel> {
            val node = createNode(DefaultPieceOfModel(false, true))
            parent.addChildNode(node)
            return node
        }
    }
}