package dev.lucasnlm.antimine.gdx

import android.util.SizeF
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector2
import dev.lucasnlm.antimine.gdx.models.RenderSettings

class CameraController(
    private val renderSettings: RenderSettings,
    private val camera: Camera,
) {
    private val velocity: Vector2 = Vector2.Zero.cpy()

    private fun limitSpeed(minefieldSize: SizeF) {
        val screenWidth = Gdx.graphics.width
        val screenHeight = Gdx.graphics.height
        val padding = renderSettings.internalPadding
        val virtualHeight = screenHeight - renderSettings.appBarHeight - renderSettings.navigationBarHeight

        camera.run {
            val newX = (position.x - velocity.x)
            val newY = (position.y + velocity.y)
            val start = 0.5f * screenWidth - padding.start
            val end = minefieldSize.width - 0.5f * screenWidth + padding.end
            val top = minefieldSize.height - 0.25f * screenHeight + padding.top
            val bottom = 0.5f * screenHeight - 0.25f * screenHeight - padding.bottom - renderSettings.navigationBarHeight

            if (screenWidth > minefieldSize.width) {
                velocity.x = 0f
            } else {
                if ((newX < start && velocity.x < 0.0) || (newX > end && velocity.x > 0.0)) {
                    velocity.x = 0.0f
                } else {
                    velocity.x *= RESISTANCE
                }
            }

            if (virtualHeight > minefieldSize.height) {
                velocity.y = 0f
            } else {
                if ((newY > top && velocity.y > 0.0) || newY < bottom && velocity.y < 0.0) {
                    velocity.y = 0.0f
                } else {
                    velocity.y *= RESISTANCE
                }
            }
        }
    }

    fun act(minefieldSize: SizeF) {
        if (!velocity.isZero) {
            limitSpeed(minefieldSize)

            camera.run {
                translate(velocity.x , velocity.y , 0f)
                update(true)
                Gdx.graphics.requestRendering()
            }
        }
    }

    fun addVelocity(dx: Float, dy: Float) {
        velocity.add(dx, dy)
        camera.update(true)
    }

    companion object {
        const val RESISTANCE = 0.6f
    }
}
