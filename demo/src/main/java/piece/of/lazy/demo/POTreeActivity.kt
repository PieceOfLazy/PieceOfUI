package piece.of.lazy.demo

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SimpleItemAnimator
import android.widget.Toast
import kotlinx.android.synthetic.main.poa_activity.*
import piece.of.lazy.demo.piece.*
import piece.of.lazy.ui.PieceOfAdapter
import piece.of.lazy.ui.PieceOfHolder
import piece.of.lazy.ui.tree.PieceOfTreeAdapter
import piece.of.lazy.ui.util.Log

class POTreeActivity : AppCompatActivity() {

    private val log = Log("POTree")

    private lateinit var group1: MutableList<Any>
    private lateinit var group2: MutableList<Any>

    private val adapter: Adapter by lazy {
        Adapter(this@POTreeActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.poa_activity)

        poa_activity_recycler.apply {
            val layoutManager = LinearLayoutManager(this@POTreeActivity)
            layoutManager.orientation = LinearLayoutManager.VERTICAL

            setHasFixedSize(true)
            setLayoutManager(layoutManager)

            if(itemAnimator is SimpleItemAnimator) {
                (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            }

            adapter = this@POTreeActivity.adapter
        }

        val list = adapter.list
        list.add(NormalPiece.Item("NORMAL ONE"))
        list.add(NormalPiece.Item("NORMAL TWO"))
        list.add(NormalPiece.Item("NORMAL THREE"))

        group1 = mutableListOf(
                NormalPiece.Item("NORMAL FORE"),
                NormalPiece.Item("NORMAL FIVE"),
                NormalPiece.Item("NORMAL SIX")
                )
        group2 = mutableListOf(
                NormalPiece.Item("NORMAL SEVEN"),
                NormalPiece.Item("NORMAL EIGHT")
        )

        list.add(GroupPiece.Item("GROUP ONE", true))
        list.addAll(group1)

        list.add(GroupPiece.Item("GROUP TWO", true))
        list.addAll(group2)

        list.add(AddPiece.Item("ADD"))
    }

    inner class Adapter(val context: Context): PieceOfTreeAdapter(context) {
        val list = mutableListOf<Any>()

        init {
            this.setHasStableIds(false)
        }

        override fun onBindAdapterInterface(list: MutableList<PieceOfHolder<*, *>>) {
            list.add(AddPiece().apply {
                setOnListener(object : AddPiece.OnPieceListener {
                    override fun onClick(holder: AddPiece.Holder, item: AddPiece.Item) {
                        Toast.makeText(this@POTreeActivity, item.subject, Toast.LENGTH_SHORT).show()

                        val position = holder.adapterPosition
                        this@Adapter.list.add(position, DeletablePiece.Item("DELETABLE COUNT $position"))
                        this@Adapter.notifyItemInserted(position)
                    }
                })
            })
            list.add(DeletablePiece().apply {
                setOnListener(object : DeletablePiece.OnPieceListener {
                    override fun onClick(holder: DeletablePiece.Holder, item: DeletablePiece.Item) {
                        Toast.makeText(this@POTreeActivity, item.subject, Toast.LENGTH_SHORT).show()
                    }

                    override fun onDelete(holder: DeletablePiece.Holder, item: DeletablePiece.Item) {
                        this@Adapter.list.removeAt(holder.adapterPosition)
                        this@Adapter.notifyItemRemoved(holder.adapterPosition)
                    }

                })
            })
            list.add(GroupTreePiece().apply {
                setOnListener(object : GroupTreePiece.OnPieceListener{
                    override fun onClick(holder: GroupTreePiece.Holder, item: GroupTreePiece.Item) {
                        Toast.makeText(this@POTreeActivity, item.subject, Toast.LENGTH_SHORT).show()
                    }
                })
            })
            list.add(NormalPiece().apply {
                setOnListener(object : NormalPiece.OnPieceListener {
                    override fun onClick(holder: NormalPiece.Holder, item: NormalPiece.Item) {
                        Toast.makeText(this@POTreeActivity, item.subject, Toast.LENGTH_SHORT).show()
                    }
                })
            })
        }
    }
}
