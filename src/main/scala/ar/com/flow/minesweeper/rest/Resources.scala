package ar.com.flow.minesweeper.rest

import java.time.format.DateTimeFormatter

import ar.com.flow.minesweeper.{Board, CartesianCoordinates, Cell, Cells, Dimensions, Game, Visibility}

case class NewGameRequestBody(rows: Int, columns: Int, bombs: Int)

object GameResource {
  def from(game: Game): GameResource = {
    GameResource(game.id, game.createdAt.format(DateTimeFormatter.ISO_DATE_TIME), BoardResource(game.board), game.state.playStatus, game.state.result)
  }
}

case class GameResource(id: String, createdAt: String, board: BoardResource, state: String, result: String)

object BoardResource {
  def apply(board: Board): BoardResource = {
    BoardResource(board.dimensions, board.totalBombs, CellResources.from(board))
  }
}

case class BoardResource(dimensions: Dimensions, totalBombs: Int, cells: Seq[CellResource])

object CellResources {
  def from(board: Board): Seq[CellResource] = {
    board.cells.toSeq.map(cell => CellResource.from(cell)).sorted
  }
}

object CellResource {
  def from(cell: Cell): CellResource = {
    new CellResource(cell.coordinates, cell.hasBomb, cell.visibility, cell.mark)
  }
}

case class CellResource(coordinates: CartesianCoordinates, hasBomb: Boolean = false, visibility: Visibility, mark: Option[String] = None) extends Ordered[CellResource] {
  // https://stackoverflow.com/a/19348339/545273
  import scala.math.Ordered.orderingToOrdered

  override def compare(that: CellResource): Int = {
    this.coordinates compare that.coordinates
  }
}


