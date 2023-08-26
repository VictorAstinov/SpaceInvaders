import javafx.application.Application
import javafx.scene.image.Image
import javafx.stage.Stage
import model.Model
import viewmodel.ViewModel

class SpaceInvaders : Application() {

    override fun start(primaryStage: Stage?) {

        with (primaryStage!!) {
            val model = Model()
            val vm = ViewModel(primaryStage, model)

            primaryStage.isResizable = false
            primaryStage.height = 650.0
            primaryStage.width = 950.0
            title = "Space Invaders"
            icons.add(Image("saucer3a.png"))
            show()
        }
    }
}
