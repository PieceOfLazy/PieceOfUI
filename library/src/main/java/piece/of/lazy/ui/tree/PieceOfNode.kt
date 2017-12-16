package piece.of.lazy.ui.tree

/**
 * Created by zpdl
 */

open class PieceOfNode<out MODEL>(val model: MODEL){
    private val nodeModel: PieceOfModel? by lazy {
        model as? PieceOfModel
    }
    private val nodeList: PieceOfNodeList? by lazy {
        if(nodeModel != null) {
            PieceOfNodeList(this@PieceOfNode)
        } else {
            null
        }
    }
    private var parentNodeList: PieceOfNodeList? = null

    private var nodeId: Long = 0
    internal var vPos = 0

    init {
        nodeModel?.setOnOnModelInterface(object : PieceOfModel.OnModelInterface {
            override fun onGetNodeInstance(): PieceOfNode<*> = this@PieceOfNode

            override fun onChangedView(model: PieceOfModel) {
                if(model.isView) {
                    parentNodeList?.notifyChanged(PieceOfNotifyParam(PieceOfNotifyParam.STATE.INSERTED, vPos, 1))
                } else {
                    parentNodeList?.notifyChanged(PieceOfNotifyParam(PieceOfNotifyParam.STATE.REMOVED, vPos, 1))
                }
            }

            override fun onChangedExpand(model: PieceOfModel) {
                changedNode()

                val vPos = this@PieceOfNode.vPos + if(model.isView) 1 else 0
                if(model.isExpand) {
                    nodeList?.let {
                        if(it.viewCount() > 0) {
                            parentNodeList?.notifyChanged(PieceOfNotifyParam(PieceOfNotifyParam.STATE.INSERTED, vPos, it.viewCount()))
                        }
                    }
                } else {
                    nodeList?.let {
                        if(it.viewCount() > 0) {
                            parentNodeList?.notifyChanged(PieceOfNotifyParam(PieceOfNotifyParam.STATE.REMOVED, vPos, it.viewCount()))
                        }
                    }
                }
            }
        })
    }

    open fun beginTransition() {
        root()?.beginTransition()
    }

    open fun applyTo() {
        root()?.applyTo()
    }

    fun isView(): Boolean = nodeModel?.isView ?: true

    fun isExpand(): Boolean = nodeModel?.isExpand ?: false

    open fun setView(isView: Boolean) {
        nodeModel?.let {
            if(it.isView != isView) {
                it.isView = isView
            } else if(isView) {
                parentNodeList?.notifyChanged(PieceOfNotifyParam(PieceOfNotifyParam.STATE.CHANGED, vPos, 1))
            }
        }.let {
            if(isView())
                parentNodeList?.notifyChanged(PieceOfNotifyParam(PieceOfNotifyParam.STATE.CHANGED, vPos, 1))
        }
    }

    fun setExpand(isExpand: Boolean) {
        nodeModel?.let {
            it.isExpand = isExpand
        }
    }

    fun getViewPosition(): Int {
        return vPos
    }

    fun getViewCount(): Int {
        var viewCount = if(isView()) 1 else 0
        if(isExpand()) {
            nodeList?.let {
                viewCount += it.viewCount()
            }
        }
        return viewCount
    }

    open fun getParent(): PieceOfNode<*>? = parentNodeList?.node

    open fun root(): PieceOfRoot? = getParent()?.root()

    fun changedNode() {
        if(isView()) {
            setView(true)
        }
    }

    fun getChildNodeCount(): Int = nodeList?.nodeCount() ?: 0

    fun getChildNode(index: Int): PieceOfNode<*>? = nodeList?.get(index)

    fun clearChildNode() {
        nodeList?.clear()
    }

    fun addChildNode(vararg nodes: PieceOfNode<*>) {
        nodeList?.add(*nodes)
    }

    fun removeChildNode(nPos: Int, nCnt: Int) {
        nodeList?.remove(nPos, nCnt)
    }

    fun removeChildNode(node: PieceOfNode<*>) {
        nodeList?.remove(node)
    }

    fun getPieceOfPosition(vPos: Int): PieceOfPosition? {
        if(vPos in 0 until getViewCount()) {
            var index = vPos
            if (isView()) {
                if (index == 0) {
                    return null
                }
                index--
            }
            return nodeList?.getPieceOfPosition(index)
        }
        return null
    }

    fun getNode(pos: PieceOfPosition): PieceOfNode<*>? {
        var result: PieceOfNode<*>? = this@PieceOfNode
        for(nPos in pos.position) {
            if(result == null) {
                return null
            }
            result = result.getChildNode(nPos)
        }

        return result
    }

    internal fun isNode(): Boolean = nodeModel != null

    internal open fun setParentNodeList(nodeList: PieceOfNodeList?) {
        parentNodeList = nodeList

        root()?.let {
            setNodeId(it)
        }
    }

    internal open fun setNodeId(root: PieceOfRoot) {
        nodeId = root.nextNodeId()
        nodeList?.setNodeId(root)
    }

    internal fun getNodeId(): Long = nodeId

    internal open fun notifyChanged(param: PieceOfNotifyParam) {
        if(isExpand()) {
            parentNodeList?.let {
                param.vPos += (vPos + if (isView()) 1 else 0)
                it.notifyChanged(param)
            }
        }
    }
}