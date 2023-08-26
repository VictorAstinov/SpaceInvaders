package view

import javafx.animation.AnimationTimer
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.input.KeyCode
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import viewmodel.Levels
import viewmodel.ViewModel
import java.util.Timer
import java.util.TimerTask
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random
import kotlin.random.nextInt

class Level(private val vm : ViewModel) : BorderPane() {


    private var canFire = true
    private var enemyDirection = 1
    private var enemyCount = 50
    private var bulletCount = 0
    private var playerDirection = 0

    private val rof  = 500L // time between shots in milliseconds
    private val moveDistance = 3.5 // Move distance of spacecraft in pixels
    private val enemyMove = 2.0 // base enemy move distance per frame in pixels
    private val background = Background(BackgroundFill(Color.BLACK, null, null))
    private val playerDown = 70.0

    private val speedProperty = SimpleDoubleProperty()
    private val livesProperty = SimpleIntegerProperty()
    private val scoreProperty = SimpleIntegerProperty()
    private val enemyBulletLim = SimpleIntegerProperty()
    private val bulletSpawnRate = SimpleIntegerProperty()
    private val bulletSpeedProperty = SimpleDoubleProperty()

    private val timer = Timer(true)
    private var animation: AnimationTimer

    private var maxLateral = 0.0

    init {
        speedProperty.bind(vm.speedProperty)
        livesProperty.bind(vm.livesProperty)
        scoreProperty.bind(vm.scoreProperty)
        enemyBulletLim.bind(vm.enemyBulletLimitProperty)
        bulletSpawnRate.bind(vm.bulletSpawnRateProperty)
        bulletSpeedProperty.bind(vm.bulletSpeedProperty)

        maxWidthProperty().bind(vm.widthProperty)
        maxHeightProperty().bind(vm.heightProperty)

        top = HBox().apply {

            background = this@Level.background
            val f = Font.font("Verdana", FontWeight.NORMAL, 15.0)
            children.addAll(
                Region().apply {
                    prefWidth = 50.0
                    maxWidth = 50.0
                    HBox.setHgrow(this, Priority.ALWAYS)
                },
                Label().apply {
                    textProperty().bind(Bindings.concat("Score: ", scoreProperty))
                    font = f
                    textFill = Color.WHITE
                },
                Region().apply {
                    prefWidth = 600.0
                    maxWidth = 600.0
                    HBox.setHgrow(this, Priority.ALWAYS)
                },
                Label().apply {
                    textProperty().bind(Bindings.concat("Lives: ", livesProperty))
                    font = f
                    textFill = Color.WHITE
                },
                Region().apply {
                    maxWidth = 20.0
                    prefWidth = 20.0
                    HBox.setHgrow(this, Priority.ALWAYS)
                },
                Label().apply {
                    text = "Level: ${vm.level}"
                    font = f
                    textFill = Color.WHITE
                },
                Region().apply {
                    prefWidth = 50.0
                    maxWidth = 50.0
                    HBox.setHgrow(this, Priority.ALWAYS)
                }
            )
            minHeight = 30.0
        }
        center = Pane().apply {
            background = this@Level.background
            children.add(Player(0.0,0.0).apply {
                translateY = vm.heightProperty.value - this.image.height - playerDown // might have to modify this
                translateX = (vm.widthProperty.value - this.image.width) / 2
            })

            val offset = (vm.widthProperty.value - 500.0) / 2
            var height = 0.0
            for(i in 0 until 10) {
                val e = GreenAlien(i * 50.0, 0.0).apply {
                    translateX += offset
                }
                children.add(e)
                if (i == 0){
                    height += e.image.height
                }
            }

            for (row in 1 .. 2) {
                val h = height
                for (i in 0 until 10) {
                    val e = BlueAlien(i * 50.0, h).apply {
                        translateX += offset
                    }
                    children.add(e)
                    if (i == 0) {
                        height += e.image.height
                    }
                }
            }

            for (row in 3 .. 4) {
                val h = height
                for (i in 0 until 10) {
                    val e = PinkAlien(i * 50.0, h).apply {
                        translateX += offset
                    }
                    children.add(e)
                    if (i == 0) {
                        height += e.image.height
                    }
                }
            }

        }

        // this works because both player and enemies are 50 pixels long
        maxLateral = vm.widthProperty.value - ((center as Pane).children[0] as Player).image.width - 15.0

        animation = object : AnimationTimer() {
            override fun handle(now: Long) {
                if (enemyCount == 0) {
                    this.stop()
                    println("Cleared level")
                    vm.doneLevel()
                }

                // check for collisions
                val screen = center as Pane
                var outside = false

                if (enemyDirection == 1) {
                    for (i in 1 until enemyCount + 1) {
                        val e = screen.children[i] as Movable
                        if (e.translateX >= maxLateral) {
                            addEnemyBullet(false)
                            outside = true
                            break
                        }
                    }
                }
                else {
                    for (i in 1 until enemyCount + 1) {
                        val e = screen.children[i] as Movable
                        if (e.translateX <= 0) {
                            outside = true
                            addEnemyBullet(false)
                            break
                        }
                    }
                }

                // switch enemy direction
                if (outside) {
                    enemyDirection *= -1
                    for (i in 1 until enemyCount + 1) {
                        screen.children[i].translateY += 15.0
                    }
                }

                // random chance for bullet spawn
                if (Random.nextInt(bulletSpawnRate.value) == 0) {
                    addEnemyBullet()
                }

                // move player
                val move = playerDirection * moveDistance
                screen.children[0].translateX = min(max(0.0, move + screen.children[0].translateX), maxLateral)

                // move enemies, check if any enemy has collided with the player. If so end the game
                val player = screen.children[0] as Player
                for (i in 1 until enemyCount + 1) {
                    if (player.boundsInParent.intersects(screen.children[i].boundsInParent)) {
                        this.stop()
                        println("Game over")
                        vm.endGame(false)
                    }
                    screen.children[i].translateX += enemyMove * enemyDirection * speedProperty.value
                }

                // delete bullets that go out of bounds, only bullets will move out of bounds, but this will remove anything
                // this means that if an alien manages to get to the bottom the game will eventually crash, but this will never happen
                // an alien will always hit the player before despawning
                val iterator = screen.children.iterator()
                while (iterator.hasNext()) {
                    val node : Node = iterator.next()
                    if (node.translateY >= vm.heightProperty.value || node.translateY <= -17.5) {
                        iterator.remove()
                        --bulletCount
                    }
                }
                // move bullets
                for (i in enemyCount + 1 until enemyCount + bulletCount + 1) {
                    val bullet = screen.children[i] as Bullet
                    if (bullet.isEnemy) {
                        bullet.translateY += bulletSpeedProperty.value
                    }
                    else {
                        bullet.translateY -= bulletSpeedProperty.value
                    }
                }

                // check for bullet collisions
                var i = enemyCount + 1
                while (i < enemyCount + bulletCount + 1) {
                    val bullet = screen.children[i] as Bullet
                    for (node in screen.children) {
                        if (node != bullet && node.boundsInParent.intersects(bullet.boundsInParent) && (node is Player && bullet.isEnemy || !bullet.isEnemy)) {
                            screen.children.remove(bullet)
                            --bulletCount
                            if (node is Bullet) {
                                --i
                                vm.collidingBulletSound.play()
                                screen.children.remove(node)
                                --bulletCount
                            }
                            else if (node is Player) {
                                playerHit()
                                --i
                            }
                            // alien
                            else if (node is Movable) {
                                i -= 2
                                vm.incrementScore(node)
                                vm.incrementSpeed()
                                screen.children.remove(node)
                                vm.enemyDeathSound.play()
                                --enemyCount
                            }
                            break
                        }
                    }
                    ++i
                }


            }
        }
        onKeyPressed = EventHandler {
            when(it.code) {
                // use state for movement
                KeyCode.A -> move(-1)
                KeyCode.D -> move(1)
                KeyCode.LEFT -> move(-1)
                KeyCode.RIGHT -> move(1)
                KeyCode.SPACE -> fire()
                KeyCode.DIGIT1 -> {animation.stop(); vm.startLevel(Levels.ONE)}
                KeyCode.DIGIT2 -> {animation.stop(); vm.startLevel(Levels.TWO)}
                KeyCode.DIGIT3 -> {animation.stop(); vm.startLevel(Levels.THREE)}
                else -> {}
            }
        }
        animation.start()
    }

    private fun addEnemyBullet(wasRandom : Boolean = true) {
        // spawn enemy bullets
        // this always assumes there is at least 1 player bullet on screen for rng which is fine
        // because I'm offsetting it with the fact that a bullet always fires when the aliens change direction
        if (bulletCount >= enemyBulletLim.value + 1 && wasRandom) {
            return // at bullet limit
        }
        val screen = center as Pane
        val e = screen.children[Random.nextInt(1..enemyCount)] as Movable
        addBullet(e)
    }

    private fun playerHit() {
        vm.playerDeathSound.play()
        // stop animation when player runs out of lives
        if (livesProperty.value == 1) {
            animation.stop()
        }
        vm.playerHit()
        val screen = center as Pane

        (screen.children[0] as Player).apply {
            translateY = vm.heightProperty.value - this.image.height - playerDown // might have to modify this
            translateX = Random.nextInt(0..maxWidth.toInt()).toDouble()
        }
        playerDirection = 0
    }

    private fun fire() {
        if (!canFire) {
            return
        }
        vm.playerShootSound.play()
        addBullet((center as Pane).children[0] as Player)
        // println("Fired")
        canFire = false
        timer.schedule(object : TimerTask() {
            override fun run() {
                // println("Fire reset")
                canFire = true
            }
        }, rof)
    }
    private fun addBullet(ship : Movable) {
        val screen = center as Pane
        screen.children.add(ship.getBullet().apply {
            translateX = ship.translateX + (ship.image.width / 2)
            translateY = ship.translateY + if (isEnemy) image.height else -image.height
        })
        if (ship is PinkAlien) {
            vm.pinkAlienSound.play()
        }
        else if (ship is BlueAlien) {
           vm.blueAlienSound.play()
        }
        else if (ship is GreenAlien) {
            vm.greenAlienSound.play()
        }
        // player
        else {
            vm.playerShootSound.play()
        }
        ++bulletCount
    }
    private fun move(direction : Int) {
        /*
        playerDirection = if (direction == playerDirection || playerDirection == 0) {
            direction
        } else {
            0
        }

         */
        playerDirection = direction
    }
}