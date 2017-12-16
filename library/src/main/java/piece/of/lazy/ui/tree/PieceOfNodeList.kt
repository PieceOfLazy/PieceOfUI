package piece.of.lazy.ui.tree

/**
 * Created by zpdl
 */

class PieceOfNodeList(val node: PieceOfNode<*> ) {

    private var count: Int = 0
    private val list: MutableList<PieceOfNode<*>> = mutableListOf()

    private var update = false
    private var isNodeList = false

    internal fun viewCount(): Int {
        if(update) {
            traversals()
            update = false
        }

        return if(isNodeList) {
            count
        } else {
            list.size
        }
    }

    internal fun notifyChanged(param: PieceOfNotifyParam) {
        update = true
        node.notifyChanged(param)
    }

    internal fun nodeCount(): Int = list.size

    internal fun get(index: Int): PieceOfNode<*>? {
        return if(index in 0 until list.size) {
            list[index]
        } else
            null
    }

    internal fun setNodeId(root: PieceOfRoot) {
        for(node in list) {
            node.setNodeId(root)
        }
    }

    internal fun clear() {
        val vCnt = viewCount()

        for(node in list) {
            node.setParentNodeList(null)
        }
        list.clear()
        count = 0

        node.notifyChanged(PieceOfNotifyParam(PieceOfNotifyParam.STATE.REMOVED, 0, vCnt))
    }

    internal fun add(vararg nodes: PieceOfNode<*>) {
        var addedVCnt = 0
        val vCnt = viewCount()

        for(node in nodes) {
            node.setParentNodeList(this@PieceOfNodeList)
            node.vPos = vCnt + addedVCnt

            if(!isNodeList) {
                if(node.isNode()) {
                    isNodeList = true
                }
            }
            list.add(node)
            addedVCnt += node.getViewCount()
        }
        count = vCnt + addedVCnt

        if(addedVCnt > 0) {
            node.notifyChanged(PieceOfNotifyParam(PieceOfNotifyParam.STATE.INSERTED, vCnt, addedVCnt))
        }
    }

    internal fun remove(nPos: Int, nCnt: Int) {
        var removedPos = -1
        var removedCnt = 0

        for(i: Int in 0 until nCnt) {
            val node = list.removeAt(nPos)
            node?.let {
                if(removedPos < 0) {
                    removedPos = it.vPos
                }
                removedCnt += it.getViewCount()
                node.setParentNodeList(null)
            }
        }
        traversals()

        if(removedPos >= 0 && removedCnt > 0) {
            node.notifyChanged(PieceOfNotifyParam(PieceOfNotifyParam.STATE.REMOVED, removedPos, removedCnt))
        }
    }

    internal fun remove(node: PieceOfNode<*>) {
        val index = list.indexOf(node)
        if(index >= 0) {
            remove(index, 1)
        }
    }

    internal fun getPieceOfPosition(vPos: Int): PieceOfPosition? {
        if(isNodeList) {
            var low = 0
            var high = nodeCount() - 1
            var mid = (low + high) / 2
            while (low <= high) {
                val node = get(mid)
                if(node == null) {
                    high = (low + high) / 2
                    mid = (low + high) / 2
                    continue
                }

                val cPos = node.getViewPosition()
                val cCnt = node.getViewCount()
                if(cCnt <= 0) {
                    mid++
                    continue
                }

                if(cPos > vPos) {
                    high = if(mid < high) mid - 1 else high -1
                } else if (cPos + cCnt - 1 < vPos) {
                    low = mid + 1
                } else {
                    break
                }
                mid = (low + high) / 2
            }
            val node = get(mid)
            return if(node != null) {
                PieceOfPosition.compose(mid,
                        node.getPieceOfPosition(vPos - node.getViewPosition()))
            } else
                null
        } else {
            return PieceOfPosition(intArrayOf(vPos))
        }
    }

    private fun traversals() {
        if(update && isNodeList) {
            count = 0
            for(childNode in list) {
                childNode.vPos = count
                count += childNode.getViewCount()
            }
        }
    }
}