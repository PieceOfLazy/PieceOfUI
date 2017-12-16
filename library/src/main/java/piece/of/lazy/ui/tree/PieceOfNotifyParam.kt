package piece.of.lazy.ui.tree

/**
 * Created by zpdl
 */
class PieceOfNotifyParam(var state: STATE, var vPos: Int, var vCnt: Int) {

    enum class STATE {
        CHANGED, INSERTED, REMOVED
    }

    internal fun composeParam(param: PieceOfNotifyParam): PieceOfNotifyParam? {
        if(state != param.state) {
            return null
        }

        return when(state) {
            STATE.CHANGED -> composeParamChanged(param)
            STATE.INSERTED -> composeParamInserted(param)
            STATE.REMOVED -> composeParamRemoved(param)
        }
    }

    private fun composeParamChanged(param: PieceOfNotifyParam): PieceOfNotifyParam? {
        if(param.vPos in vPos..(vPos + vCnt)) {
            if(vPos + vCnt < param.vPos + param.vCnt) {
                return PieceOfNotifyParam(STATE.CHANGED, vPos, param.vPos + param.vCnt - vPos)
            }
        } else if(vPos in param.vPos..(param.vPos + param.vCnt)) {
            if(vPos + vCnt > param.vPos + param.vCnt) {
                return PieceOfNotifyParam(STATE.CHANGED, param.vPos, vPos + vCnt - param.vPos)
            }
        }
        return null
    }

    private fun composeParamInserted(param: PieceOfNotifyParam): PieceOfNotifyParam? {
        if(param.vPos in vPos..(vPos + vCnt)) {
            return PieceOfNotifyParam(STATE.INSERTED, vPos, vCnt + param.vCnt)
        } else if(vPos in param.vPos..(param.vPos + param.vCnt)) {
            return PieceOfNotifyParam(STATE.INSERTED, param.vPos, vCnt + param.vCnt)
        }
        return null
    }

    private fun composeParamRemoved(param: PieceOfNotifyParam): PieceOfNotifyParam? {
        if(param.vPos == (vPos + vCnt)) {
            return PieceOfNotifyParam(STATE.REMOVED, vPos, vCnt + param.vCnt)
        } else if(vPos == param.vPos + param.vCnt) {
            return PieceOfNotifyParam(STATE.REMOVED, param.vPos, vCnt + param.vCnt)
        }
        return null
    }
}