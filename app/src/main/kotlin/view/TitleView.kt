package view

import javafx.application.Platform
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import viewmodel.Levels
import viewmodel.ViewModel

class TitleView(private val vm: ViewModel) : VBox() {

    init {
        val f = Font.font("Verdana", FontWeight.NORMAL, 15.0)
        background = Background(BackgroundFill(Color.AZURE, null, null))
        children.addAll(
            ImageView(Image("images\\si_logo.png", 400.0, 0.0, true, true)),
            Region().apply{
                prefHeight = 100.0
                maxHeight = 100.0
                setVgrow(this, Priority.ALWAYS)
            },
            Label("Instructions").apply {
                font = Font.font("Verdana", FontWeight.BOLD, 45.0)
            },
            Region().apply{
                prefHeight = 25.0
                maxHeight = 25.0
                setVgrow(this, Priority.ALWAYS)
            },
            Label("ENTER - Start Game").apply {
                font = f
            },
            Label("A and ${'\u2190'}, D and ${'\u2192'} - Move ship").apply {
                font = f
            },
            Label("SPACE - Fire").apply {
                font = f
            },
            Label("Q - Quit").apply {
                font = f
            }, // change the quit key later
            Label("1, 2, 3 - Start game or switch to specified level").apply {
                font = f
            },
            Region().apply{
                prefHeight = 75.0
                maxHeight = 75.0
                setVgrow(this, Priority.ALWAYS)
            },
            Label("Designed by Victor Astinov (20851407)").apply {
                font = Font.font("Verdana", FontWeight.NORMAL, 12.0)
            }
        )
        alignment = Pos.TOP_CENTER
        // shift down a bit
        children.forEach {
            it.translateY = 50.0
        }
        //setOnMouseClicked { println("clicked") }
        onKeyPressed = EventHandler {
            // println("pressed")
            val code = it.code
            if (code == KeyCode.Q) {
                println("stop")
                Platform.exit()
            }
            else if (code == KeyCode.DIGIT1 || code == KeyCode.ENTER) {
                vm.startLevel(Levels.ONE)
            }
            else if (code == KeyCode.DIGIT2) {
                vm.startLevel(Levels.TWO)
            }
            else if (code == KeyCode.DIGIT3) {
                vm.startLevel(Levels.THREE)
            }
        }

    }
}