package piece.of.lazy.ui.tree

/**
 * Created by zpdl
 */
class PieceOfPosition(val position: IntArray) {
    companion object {
        fun compose(pos: Int, pPos: PieceOfPosition?): PieceOfPosition {
            if(pPos != null) {
                val newPPos = IntArray(pPos.position.size + 1)
                newPPos[0] = pos
                for(i in 1..pPos.position.size) {
                    newPPos[i] = pPos.position[i -1]
                }
                return PieceOfPosition(newPPos)
            }
            return PieceOfPosition(intArrayOf(pos))
        }
    }

    fun getPosition(): Int {
        if(position.isNotEmpty()) {
            return position[position.size - 1]
        }
        return -1
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for(pos in position) {
            sb.append("[$pos]")
        }
        return sb.toString()
    }
}