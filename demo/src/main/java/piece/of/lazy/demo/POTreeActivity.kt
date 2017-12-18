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
import piece.of.lazy.ui.tree.PieceOfNode
import piece.of.lazy.ui.tree.PieceOfTreeAdapter
import piece.of.lazy.ui.tree.PieceOfTreeHelper
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

        val root = adapter.root()
        PieceOfTreeHelper.addNode(root, NormalPiece.Item("NORMAL ONE"))
        PieceOfTreeHelper.addNode(root, NormalPiece.Item("NORMAL TWO"))
        PieceOfTreeHelper.addNode(root, NormalPiece.Item("NORMAL THREE"))

        val group1 = PieceOfTreeHelper.createNode(GroupTreePiece.Item("GROUP ONE"))
        PieceOfTreeHelper.addNode(group1, NormalPiece.Item("NORMAL FORE"))
        PieceOfTreeHelper.addNode(group1, NormalPiece.Item("NORMAL FIVE"))
        PieceOfTreeHelper.addNode(group1, NormalPiece.Item("NORMAL SIX"))
        PieceOfTreeHelper.addNode(group1, AddPiece.Item("ADD", 1))

        root.addChildNode(group1)

        val group2 = PieceOfTreeHelper.createNode(GroupTreePiece.Item("GROUP TWO"))
        PieceOfTreeHelper.addNodes(group2, NormalPiece.Item("NORMAL SEVEN"), NormalPiece.Item("NORMAL EIGHT"))
        PieceOfTreeHelper.addNode(group2, AddPiece.Item("ADD", 2))

        root.addChildNode(group2)
        root.applyTo()
    }

    inner class Adapter(val context: Context): PieceOfTreeAdapter(context) {
        init {
            this.setHasStableIds(true)
        }

        override fun onBindAdapterInterface(list: MutableList<PieceOfHolder<*, *>>) {
            list.add(AddPiece().apply {
                setOnListener(object : AddPiece.OnPieceListener {
                    override fun onClick(holder: AddPiece.Holder, item: AddPiece.Item) {
                        Toast.makeText(this@POTreeActivity, item.subject, Toast.LENGTH_SHORT).show()
                        val parent = PieceOfTreeHelper.getParent(adapter, holder)
                        parent?.let {
                            it.beginTransition()
                            if(item.type == 1) {
                                PieceOfTreeHelper.addNode(it, DeletablePiece.Item("DELETABLE COUNT ${holder.adapterPosition}"))
                            } else {
                                PieceOfTreeHelper.addNode(it, parent.getChildNodeCount() - 1, DeletablePiece.Item("DELETABLE COUNT ${holder.adapterPosition}"))
                            }
                            it.applyTo()
                        }
                    }
                })
            })
            list.add(DeletablePiece().apply {
                setOnListener(object : DeletablePiece.OnPieceListener {
                    override fun onClick(holder: DeletablePiece.Holder, item: DeletablePiece.Item) {
                        Toast.makeText(this@POTreeActivity, item.subject, Toast.LENGTH_SHORT).show()
                    }

                    override fun onDelete(holder: DeletablePiece.Holder, item: DeletablePiece.Item) {
                        PieceOfTreeHelper.removeNode(adapter, holder, true)
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
