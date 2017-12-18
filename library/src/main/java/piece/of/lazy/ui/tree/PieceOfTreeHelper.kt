package piece.of.lazy.ui.tree

import android.support.v7.widget.RecyclerView
import kotlin.reflect.KClass

/**
 * Created by zpdl
 */

class PieceOfTreeHelper {

    companion object {
        fun <T> createNode(model: T): PieceOfNode<T> {
            return PieceOfNode(model)
        }

        inline fun <reified T: PieceOfModel> getNode(model: T): PieceOfNode<T>? {
            @Suppress("UNCHECKED_CAST")
            return model.getNode() as? PieceOfNode<T>
        }

        fun getNode(adapter: PieceOfTreeAdapter, holder: RecyclerView.ViewHolder): PieceOfNode<*>? {
            adapter.getPosition(holder)?.let {
                return adapter.root().getNode(it)
            }
            return null
        }

        fun <T> addNode(parent: PieceOfNode<*>, model: T, update: Boolean = false): PieceOfNode<T> {
            if(update)
                parent.beginTransition()

            val node = createNode(model)
            parent.addChildNode(node)

            if(update)
                parent.applyTo()
            return node
        }

        fun <T> addNode(parent: PieceOfNode<*>, nPos: Int, model: T, update: Boolean = false): PieceOfNode<T> {
            if(update)
                parent.beginTransition()

            val node = createNode(model)
            parent.addChildNode(nPos, node)

            if(update)
                parent.applyTo()
            return node
        }

        fun <T> addNodes(parent: PieceOfNode<*>, vararg models: T, update: Boolean = false) {
            if(update)
                parent.beginTransition()

            val nodes = Array(models.size, { createNode(models[it]) })
            parent.addChildNode(*nodes)

            if(update)
                parent.applyTo()
        }

        fun <T> addNodes(parent: PieceOfNode<*>, nPos: Int, vararg models: T, update: Boolean = false) {
            if(update)
                parent.beginTransition()

            val nodes = Array(models.size, { createNode(models[it]) })
            parent.addChildNode(nPos, *nodes)

            if(update)
                parent.applyTo()
        }

        fun addEmptyNode(parent: PieceOfNode<*>, update: Boolean = false): PieceOfNode<PieceOfModel> {
            if(update)
                parent.beginTransition()

            val node = createNode(DefaultPieceOfModel(false, true))
            parent.addChildNode(node)

            if(update)
                parent.applyTo()
            return node
        }

        fun removeNode(model: PieceOfModel, update: Boolean = false) {
            val node = model.getNode()
            if(node != null) {
                node.getParent()?.let {
                    if(update)
                        it.beginTransition()
                    it.removeChildNode(node)
                    if(update)
                        it.applyTo()
                }
            }
        }

        fun removeNode(adapter: PieceOfTreeAdapter, holder: RecyclerView.ViewHolder, update: Boolean = false) {
            val node = getNode(adapter, holder)
            if(node != null) {
                node.getParent()?.let {
                    if(update)
                        it.beginTransition()
                    it.removeChildNode(node)
                    if(update)
                        it.applyTo()
                }
            }
        }

        fun changedNode(model: PieceOfModel, update: Boolean = false) {
            val node = model.getNode()
            node?.let {
                if(update)
                    it.beginTransition()
                it.changedNode()
                if(update)
                    it.applyTo()
            }
        }

        fun <T : Any> childNodeFromModel(model: PieceOfModel, type: KClass<T>): PieceOfNode<T>? {
            val node = model.getNode()
            node?.let {
                (0 until it.getChildNodeCount())
                        .map { i -> it.getChildNode(i) }
                        .filter { type.isInstance(it?.model) }
                        .forEach {
                            @Suppress("UNCHECKED_CAST")
                            return it as PieceOfNode<T>?
                        }
            }
            return null
        }

        fun <T : Any> childNodeListFromModel(model: PieceOfModel, type: KClass<T>): MutableList<PieceOfNode<T>> {
            val list = mutableListOf<PieceOfNode<T>>()

            val node = model.getNode()
            node?.let {
                (0 until it.getChildNodeCount())
                        .map { i -> it.getChildNode(i) }
                        .filter { type.isInstance(it?.model) }
                        .forEach {
                            @Suppress("UNCHECKED_CAST")
                            list.add(it as PieceOfNode<T>)
                        }
            }
            return list
        }

        fun getParent(adapter: PieceOfTreeAdapter, holder: RecyclerView.ViewHolder): PieceOfNode<*>? {
            adapter.getPosition(holder)?.let {
                it.getParentPosition()?.let {
                    return adapter.root().getNode(it)
                }
            }
            return null
        }
    }
}