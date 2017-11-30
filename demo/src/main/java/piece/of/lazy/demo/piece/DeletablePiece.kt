package piece.of.lazy.demo.piece

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.piece_deletable.view.*
import kotlinx.android.synthetic.main.piece_normal.view.*
import piece.of.lazy.demo.R
import piece.of.lazy.ui.PieceOfHolder

/**
 * Created by zpdl
 */
open class DeletablePiece : PieceOfHolder<DeletablePiece.Holder, DeletablePiece.Item>(Holder::class, Item::class) {
    interface OnPieceListener {
        fun onClick(holder: Holder, item: Item)
        fun onDelete(holder: Holder, item: Item)
    }
    private var listener: OnPieceListener? = null

    fun setOnListener(l: OnPieceListener?) {
        listener = l
    }

    override fun onLayout(): Int = R.layout.piece_deletable

    override fun onCreateViewHolderPiece(view: View): Holder = Holder(view)

    override fun onBindViewHolderPiece(context: Context, holder: Holder, item: Item, position: Int) {
        val countStr = "${position + 1} Count"

        holder.subject.text = item.subject
        holder.count.text = countStr
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var subject: TextView
        lateinit var count: TextView
        lateinit var delete: TextView

        init {
            with(itemView) {
                subject = piece_deletable_subject
                count = piece_deletable_count
                delete = piece_deletable_delete
            }

            itemView.setOnClickListener {
                getBindItem(this@Holder)?.let {
                    listener?.onClick(this@Holder, it)
                }
            }

            delete.setOnClickListener {
                getBindItem(this@Holder)?.let {
                    listener?.onDelete(this@Holder, it)
                }
            }
        }
    }

    open class Item(val subject: String)
}