package piece.of.lazy.ui.tree

import piece.of.lazy.ui.util.Log

/**
 * Created by zpdl
 */

class PieceOfRoot(private val notify: PieceOfNotify) : PieceOfNode<DefaultPieceOfModel>(DefaultPieceOfModel(false)) {

    private var nextNodeId = 0L

    private var isTransition = false
    private var param: PieceOfNotifyParam? = null

    internal fun nextNodeId(): Long {
        nextNodeId++
        if(nextNodeId == Long.MAX_VALUE) {
            nextNodeId = 1L
        }
        return nextNodeId
    }

    override fun setView(isView: Boolean) {
    }

    override fun getParent(): PieceOfNode<*>? = null

    override fun root(): PieceOfRoot? = this@PieceOfRoot

    override fun setParentNodeList(nodeList: PieceOfNodeList?) {
    }

    override fun setNodeId(root: PieceOfRoot) {
    }

    override fun notifyChanged(param: PieceOfNotifyParam) {
        if(!isTransition) {
            return
        }

        if(this@PieceOfRoot.param == null) {
            this@PieceOfRoot.param = param
        } else {
            val composeParam = this@PieceOfRoot.param?.composeParam(param)
            if(composeParam == null) {
                val changedParam = this@PieceOfRoot.param
                this@PieceOfRoot.param = param
                applyTo(changedParam)
            } else {
                this@PieceOfRoot.param = composeParam
            }
        }
    }

    override fun beginTransition() {
        isTransition = true
        this.param = null
    }

    override fun applyTo() {
        if(!isTransition) {
            notify.notifyNodeSetChanged()
        } else {
            val param = this.param
            this.param = null
            param?.let {
                applyTo(it)
            }
            isTransition = false
        }
    }

    private fun applyTo(param: PieceOfNotifyParam?) {
        if(param == null) {
            return
        }

        if (isTransition) {
            when (param.state) {
                PieceOfNotifyParam.STATE.CHANGED -> {
                    Log.d("PieceOfRoot", "applyTo CHANGED : POS = ${param.vPos}, CNT = ${param.vCnt}")
                    notify.notifyNodeChanged(param.vPos, param.vCnt)
                }
                PieceOfNotifyParam.STATE.INSERTED -> {
                    Log.d("PieceOfRoot", "applyTo INSERTED : POS = ${param.vPos}, CNT = ${param.vCnt}")
                    notify.notifyNodeInserted(param.vPos, param.vCnt)
                    this@PieceOfRoot.param?.let {
                        if (param.vPos + param.vCnt <= it.vPos) {
                            it.vPos += param.vCnt
                        }
                    }
                }
                PieceOfNotifyParam.STATE.REMOVED -> {
                    Log.d("PieceOfRoot", "applyTo REMOVED : POS = ${param.vPos}, CNT = ${param.vCnt}")
                    notify.notifyNodeRemoved(param.vPos, param.vCnt)
                    this@PieceOfRoot.param?.let {
                        if (param.vPos + param.vCnt <= it.vPos) {
                            it.vPos -= param.vCnt
                        }
                    }
                }
            }
        }
    }
}