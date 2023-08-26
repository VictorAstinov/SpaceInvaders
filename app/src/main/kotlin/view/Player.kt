package view

import javafx.scene.image.Image
class Player(x : Double, y : Double) : Movable(x,y) {
    init {
        image = Image("images\\player_ship.png", 50.0, 50.0, true, true).apply {
            isCache = true
        }
    }

    override fun getBullet() : Bullet {
        return Bullet(false).apply {
            image = Image("images\\player_bullet.png", 0.0, 15.0, true, true).apply {
                isCache = true
            }
        }
    }

}