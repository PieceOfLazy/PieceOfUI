package piece.of.lazy.demo.piece

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.piece_add.view.*
import kotlinx.android.synthetic.main.piece_group.view.*
import piece.of.lazy.demo.R
import piece.of.lazy.ui.PieceOfHolder

/**
 * Created by zpdl
 */
open class AddPiece : PieceOfHolder<AddPiece.Holder, AddPiece.Item>(Holder::class, Item::class) {
    interface OnPieceListener {
        fun onClick(holder: Holder, item: Item)
    }
    private var listener: OnPieceListener? = null

    fun setOnListener(l: OnPieceListener?) {
        listener = l
    }

    override fun onLayout(): Int = R.layout.piece_add

    override fun onCreateViewHolderPiece(view: View): Holder = Holder(view)

    override fun onBindViewHolderPiece(context: Context, holder: Holder, item: Item, position: Int) {
        holder.subject.text = item.subject
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var subject: TextView

        init {
            with(itemView) {
                subject = piece_add_subject
            }

            itemView.setOnClickListener {
                getBindItem(this@Holder)?.let {
                    listener?.onClick(this@Holder, it)
                }
            }
        }
    }

    open class Item(val subject: String, val type: Int = 0)
}