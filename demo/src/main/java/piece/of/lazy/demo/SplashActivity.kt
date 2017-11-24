package piece.of.lazy.demo

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.splash_activity.*
import kotlinx.android.synthetic.main.splash_piece_animation.view.*
import piece.of.lazy.ui.PieceOfView
import piece.of.lazy.ui.util.Log
import kotlin.reflect.KClass

class SplashActivity : AppCompatActivity() {
    val log = Log("Lazy:Splash")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window?.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)

        setContentView(R.layout.splash_activity)

        Log.level = Log.LEVEL.VERBOSE

        log.i("onCreate")

        val animationSplash = AnimationSplash()
        animationSplash.doBindView(splash_activity_piece_animation)
        animationSplash.doBindItem(this, Unit)

        val test = Test(TestItemChild::class)
        val castTest:TestItemChild? = test.castTest(TestItem())
        castTest?.let {
            it.toStringTest()
        }
    }

    inner class AnimationSplash : PieceOfView<Unit>() {
        override fun onLayout(): Int = R.layout.splash_piece_animation

        override fun onBindView(v: View) {
            log.i("onBindView()")
            with(v) {
                splash_piece_animation_tv.alpha = 0f
            }
        }

        override fun onBindItem(c: Context, item: Unit?) {
            log.i("onBindItem() : "+mView?.isAttachedToWindow)
            mView?.let {
                if (it.isAttachedToWindow) doAnimation()
            }
        }

        override fun onAttached() {
            super.onAttached()
            log.i("onAttached() : "+mView?.isAttachedToWindow)
            doAnimation()
        }

        override fun onDetached() {
            super.onDetached()
            log.i("onDetached()")
        }

        private fun doAnimation() {
            mView?.run {
                splash_piece_animation_tv.alpha = 0f
                splash_piece_animation_tv
                        .animate()
                        .alpha(1.0f)
                        .setDuration(1000)
                        .withLayer()
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(p0: Animator?) {
                                log.i("onAnimationEnd")
                                Thread.sleep((1 * 1000).toLong())

                                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                                startActivity(intent)

                                finish()
                            }

                        })
                        .start()
            }
        }
    }
}


class Test<VH: TestItem>(private val type: KClass<VH>) {

    fun testString() {
        val testItem: VH? = castTest(TestItemChild2())
        testItem?.toStringTest()
    }

    @Suppress("UNCHECKED_CAST")
    fun castTest(item: TestItem): VH? {
        if(type.isInstance(item)) {
            return item as VH
        }
        return null
    }
}

open class TestItem {
    open fun toStringTest() {
        Log.i("KKH", "TestItem")
    }
}

class TestItemChild: TestItem() {
    override fun toStringTest() {
        Log.i("KKH", "TestItemChild")
    }
}


class TestItemChild2: TestItem() {
    override fun toStringTest() {
        Log.i("KKH", "TestItemChild2")
    }
}



