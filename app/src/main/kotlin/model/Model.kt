package model

class Model {
    var highScore = 0

    fun sendScore(score : Int) {
        if (score > highScore) {
            highScore = score
        }
    }
}