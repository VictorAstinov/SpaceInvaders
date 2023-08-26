package viewmodel

import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.ReadOnlyDoubleWrapper
import javafx.beans.property.ReadOnlyIntegerProperty
import javafx.beans.property.ReadOnlyIntegerWrapper
import javafx.scene.Scene
import javafx.scene.media.AudioClip
import javafx.stage.Stage
import model.Model
import view.*

class ViewModel(private val stage : Stage, private val model : Model) {

    private val initLives = 3
    private val maxBullets = listOf(4, 6, 8)
    private val speeds = listOf(1.0, 1.25, 1.5)
    private val spawns = listOf(150, 100, 75)
    private val bulletSpeeds = listOf(4.0, 5.0, 6.0)
    var level = 0

    private val speed = ReadOnlyDoubleWrapper(speeds[0])
    val speedProperty : ReadOnlyDoubleProperty = speed.readOnlyProperty

    private val lives = ReadOnlyIntegerWrapper(initLives)
    val livesProperty : ReadOnlyIntegerProperty = lives.readOnlyProperty

    private val score = ReadOnlyIntegerWrapper(0)
    val scoreProperty : ReadOnlyIntegerProperty = score.readOnlyProperty

    val widthProperty: ReadOnlyDoubleProperty = stage.widthProperty()
    val heightProperty : ReadOnlyDoubleProperty = stage.heightProperty()

    private val enemyBulletLimit : ReadOnlyIntegerWrapper = ReadOnlyIntegerWrapper(maxBullets[0])
    val enemyBulletLimitProperty : ReadOnlyIntegerProperty = enemyBulletLimit.readOnlyProperty

    private val bulletSpawnRate : ReadOnlyIntegerWrapper = ReadOnlyIntegerWrapper(spawns[0])
    val bulletSpawnRateProperty : ReadOnlyIntegerProperty = bulletSpawnRate.readOnlyProperty

    private val bulletSpeed : ReadOnlyDoubleWrapper = ReadOnlyDoubleWrapper(bulletSpeeds[0])
    val bulletSpeedProperty : ReadOnlyDoubleProperty = bulletSpeed.readOnlyProperty

    // putting sounds here so that they persist and don't have to be reconstructed
    val playerShootSound : AudioClip = AudioClip(Level::class.java.classLoader.getResource("sounds\\shoot.wav").toString())
    val playerDeathSound : AudioClip = AudioClip(Level::class.java.classLoader.getResource("sounds\\explosion.wav").toString())
    val enemyDeathSound : AudioClip = AudioClip(Level::class.java.classLoader.getResource("sounds\\invaderkilled.wav").toString())

    // not making a sound per movement, that is very annoying
    val blueAlienSound : AudioClip = AudioClip(Level::class.java.classLoader.getResource("sounds\\fastinvader1.wav").toString())
    val pinkAlienSound : AudioClip = AudioClip(Level::class.java.classLoader.getResource("sounds\\fastinvader2.wav").toString())
    val greenAlienSound : AudioClip = AudioClip(Level::class.java.classLoader.getResource("sounds\\fastinvader3.wav").toString())
    val collidingBulletSound : AudioClip = AudioClip(Level::class.java.classLoader.getResource("sounds\\fastinvader4.wav").toString())


    init {
        // scenes.add(TitleView(this))
        /*
        val level = Level(this)
        scenes.add(Scene(level).apply {
            onKeyPressed = level.onKeyPressed
        })

         */
        // scenes.add(EndScreen(this))
        val t = TitleView(this)
        stage.scene = Scene(t).apply {
            onKeyPressed = t.onKeyPressed
        }
    }

    fun loadMainScreen() {
        val t = TitleView(this)
        stage.scene.root = t
        stage.scene.onKeyPressed = t.onKeyPressed
    }
    fun startLevel(l: Levels) {
        level = l.ordinal + 1
        updateValues()
        val nextLevel = Level(this)
        stage.scene.root = nextLevel
        stage.scene.onKeyPressed = nextLevel.onKeyPressed
    }

    fun doneLevel() {
        ++level
        if (level > 3) {
            endGame(true)
        }
        else {
            val nextLevel = Level(this)
            stage.scene.root = nextLevel
            stage.scene.onKeyPressed = nextLevel.onKeyPressed
            updateValues()
        }
    }

    private fun updateValues() {
        speed.value = speeds[level - 1]
        bulletSpawnRate.value = spawns[level - 1]
        enemyBulletLimit.value = maxBullets[level - 1]
        bulletSpeed.value = bulletSpeeds[level - 1]
    }

    fun incrementSpeed() {
        speed.value += 0.07 + (level / 100) - 0.01
    }

    fun endGame(didWin : Boolean) {
        model.sendScore(score.value)
        gameOver(didWin, model.highScore)
    }

    fun playerHit() {
        lives.value -= 1
        if (lives.value <= 0) {
            // stage.close()
            endGame(false)
            // stage.show()
        }
    }

    private fun gameOver(didWin : Boolean, highScore : Int) {
        val e = EndScreen(this, didWin, highScore, score.value)
        stage.scene.root = e
        stage.scene.onKeyPressed = e.onKeyPressed
        score.value = 0
        lives.value = initLives
    }

    fun incrementScore(movable: Movable) {
        val multiplier : Double = if (level == 1) {
            1.0
        }
        else if (level == 2) {
            1.5
        }
        else {
            2.0
        }
        if (movable is GreenAlien) {
            score.value += (multiplier * 30).toInt()
        }
        else if (movable is BlueAlien) {
            score.value += (multiplier * 20).toInt()
        }
        // pink alien
        else {
            score.value += (multiplier * 10).toInt()
        }
    }

}