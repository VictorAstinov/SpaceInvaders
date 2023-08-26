package view

import javafx.scene.image.Image

class GreenAlien(x : Double, y : Double) : Movable(x, y) {
    init {
        image = Image("images\\green_alien.png", 50.0, 50.0, true, true)
    }

    override fun getBullet() : Bullet {
        return Bullet(true).apply {
            image = Image("images\\green_bullet.png", 0.0, 15.0, true, true).apply {
                isCache = true
            }
        }
    }
}