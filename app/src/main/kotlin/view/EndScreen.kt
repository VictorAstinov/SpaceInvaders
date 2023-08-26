package view

import javafx.beans.binding.Bindings
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.input.KeyCode
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import viewmodel.ViewModel

class EndScreen(private val vm : ViewModel, didWin : Boolean, hs : Int, score : Int) : VBox() {

    init {
        onKeyPressed = EventHandler {
            when(it.code) {
                KeyCode.ENTER -> vm.loadMainScreen()
                else -> {}
            }
        }
        background = Background(BackgroundFill(Color.AZURE, null, null))
        alignment = Pos.CENTER
        children.addAll(
            Label().apply {
                text = "You ${if (didWin) "Win" else "Lose"}!"
                font = Font.font("Verdana", FontWeight.BOLD, 36.0)
            },
            Region().apply{
                prefHeight = 50.0
                maxHeight = 50.0
                setVgrow(this, Priority.ALWAYS)
            },
            Label().apply{
                textProperty().bind(Bindings.concat("Your Score: ", score))
                font = Font.font("Verdana", FontWeight.BOLD, 20.0)
            },
            Label().apply{
                text = "High Score: $hs"
                font = Font.font("Verdana", FontWeight.BOLD, 20.0)
            },
            Region().apply{
                prefHeight = 50.0
                maxHeight = 50.0
                setVgrow(this, Priority.ALWAYS)
            },
            Label().apply{
                text = "Press Enter to return to the Title Screen"
                font = Font.font("Verdana", FontWeight.BOLD, 12.0)
            }

        )
    }
}