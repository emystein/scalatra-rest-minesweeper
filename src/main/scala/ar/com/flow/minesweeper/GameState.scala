package ar.com.flow.minesweeper

// TODO: model using something different than Strings
object GamePlayStatus {
  val playing = "playing"
  val paused = "paused"
  val finished = "finished"
}

// TODO: model using something different than Strings
object GameResult {
  val pending = "pending"
  val won = "won"
  val lost = "lost"

  def of(revealedBombCells: Set[_ <: Cell], remainingEmptyCells: Set[_ <: Cell]): String = {
    if (revealedBombCells.nonEmpty)
      lost
    else if (remainingEmptyCells.isEmpty)
      won
    else
      pending
  }
}

object GameState {
  def apply(board: Board): GameState = {
    val result: String = GameResult.of(board.cells.revealed.withBomb(), board.cells.hidden.empty)

    val playStatus: String = result match {
      case GameResult.pending => GamePlayStatus.playing
      case _ => GamePlayStatus.finished
    }

    GameState(playStatus, result)
  }
}

case class GameState(playStatus: String, result: String)
