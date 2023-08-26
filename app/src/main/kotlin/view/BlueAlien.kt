package view

import javafx.scene.image.Image

class BlueAlien(x : Double, y : Double) : Movable(x, y) {
    init {
        image = Image("images\\blue_alien.png", 50.0, 50.0, true, true)
    }

    override fun getBullet() : Bullet {
        return Bullet(true).apply {
            image = Image("images\\blue_bullet.png", 0.0, 15.0, true, true).apply {
                isCache = true
            }
        }
    }
}