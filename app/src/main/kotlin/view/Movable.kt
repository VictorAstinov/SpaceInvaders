package view

import javafx.scene.image.ImageView

abstract class Movable(x : Double, y : Double) : ImageView() {
    init {
        isCache = true
        translateX = x
        translateY = y
    }
    abstract fun getBullet() : Bullet
}