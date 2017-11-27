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

class SplashActivity : AppCompatActivity() {
    private val log = Log("Splash")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window?.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)

        setContentView(R.layout.splash_activity)

        Log.level = Log.LEVEL.VERBOSE
        Log.prefix = "Lazy:"

        log.i("onCreate")

        val animationSplash = AnimationSplash()
        animationSplash.doBindView(splash_activity_piece_animation)
        animationSplash.doBindItem(this, Unit)

    }

    inner class AnimationSplash : PieceOfView<Unit>() {
        override fun onLayout(): Int = R.layout.splash_piece_animation

        override fun onBindView(v: View) {
            with(v) {
                splash_piece_animation_tv.alpha = 0f
            }
        }

        override fun onBindItem(c: Context, item: Unit?) {
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

                                val intent = Intent(this@SplashActivity, MainListActivity::class.java)
                                startActivity(intent)

                                finish()
                            }

                        })
                        .start()
            }
        }
    }
}



