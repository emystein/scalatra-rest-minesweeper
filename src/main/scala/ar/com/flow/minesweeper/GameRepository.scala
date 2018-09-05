package ar.com.flow.minesweeper

import java.text.SimpleDateFormat

import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class GameRepository(val db: Database) {
  protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global
  private val dateFormat = "yyyy-MM-dd HH:mm:ss"

  def save(game: Game): Unit = {
    val simpleDateFormat = new SimpleDateFormat(dateFormat)

    val cells = game.board.cells.values.map(c => (game.id, c.row, c.column, c.hasBomb, c.numberOfAdjacentBombs, c.isRevealed, c.value)).toSeq

    val cellsToBeInsertedOrUpdated = cells.map(cell => Tables.cells.insertOrUpdate(cell))

    db.run(DBIO.sequence(cellsToBeInsertedOrUpdated))

    db.run(DBIO.seq(
      Tables.boards += (game.id, game.board.totalRows, game.board.totalColumns, game.board.totalBombs),
      Tables.games += (game.id, simpleDateFormat.format(game.createdAt))
    ))
  }

  def findById(id: String): Game = {
    // TODO: Use Slick query instead of retrieving all in memory and then filtering
    findAll.filter(_.id == id).head
  }

  def findAll: Seq[Game] = {
    val sdf = new SimpleDateFormat(dateFormat)

    val query = Tables.games
      .join(Tables.boards).on(_.id === _.id)
      .join(Tables.cells).on(_._2.id === _.id)
      .result.map(r => r.groupBy(_._1._1))

    // TODO: remove Await
    Await.result(
      db.run(query).map(results => results.map(result =>
        new Game(result._1._1, sdf.parse(result._1._2),
          BoardFactory(result._2.head._1._2._2, result._2.head._1._2._3, result._2.head._1._2._4, result._2.map(c => Tables.mapToCell(c._2))))).toSeq)
    , Duration.Inf)
  }
}