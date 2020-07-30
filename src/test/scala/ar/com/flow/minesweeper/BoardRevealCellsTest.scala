package ar.com.flow.minesweeper

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers


class BoardRevealCellsTest extends AnyFunSpec with Matchers {
  describe("Revealed cells") {
    describe("when revealing a cell") {
      describe("not previously revealed") {
        it("should add cell to revealed cell set") {
          val board = Board(Dimensions(3, 3), 2)

          val updatedBoard = board.revealCellAt(CartesianCoordinates(1, 1))

          updatedBoard.revealedCells shouldBe Seq(updatedBoard.cellAt(CartesianCoordinates(1, 1)))
        }
      }
      describe("already revealed") {
        it("should not add cell to revealed cell set") {
          val board = Board(Dimensions(3, 3), 2)

          val updatedBoard = board.revealCellAt(CartesianCoordinates(1, 1))

          updatedBoard.revealedCells shouldBe Seq(updatedBoard.cellAt(CartesianCoordinates(1, 1)))
        }
      }
    }
  }

  describe("Revealed empty cells") {
    describe("when revealed empty cell") {
      it("should contain empty cell") {
        val board = Board(Dimensions(2, 2), 2)

        board.revealedCells.filter(_.isEmpty) shouldBe Seq.empty

        val emptyCell = board.emptyCells.head

        val updatedBoard = board.revealCellAt(emptyCell.coordinates)

        val revealedEmptyCell = emptyCell.copy(visibility = Visibility.Shown, board = Some(updatedBoard))

        updatedBoard.revealedCells.filter(_.isEmpty) shouldBe Seq(revealedEmptyCell)
      }
    }
    describe("when revealed bomb cell") {
      it("should not contain bomb cell") {
        val board = Board(Dimensions(3, 3), 2)

        board.revealedCells.filter(_.isEmpty) shouldBe Seq.empty

        val bombCell = board.cellsWithBomb.head

        val updatedBoard = board.revealCellAt(bombCell.coordinates)

        val revealedBombCell = bombCell.copy(visibility = Visibility.Shown, board = Some(updatedBoard))

        updatedBoard.revealedCells.filter(_.isEmpty) should not contain revealedBombCell
      }
    }
  }

  describe("Remaining empty cells") {
    describe("when revealed empty cell") {
      it("should be removed from remaining empty cells") {
        val board = Board(Dimensions(3, 3), 2)

        val emptyCell = board.emptyCells.head

        val updatedBoard = board.revealCellAt(emptyCell.coordinates)

        updatedBoard.hiddenCells.filter(_.isEmpty) shouldNot contain(emptyCell)
      }
    }
  }
}
