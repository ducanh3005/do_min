package dev.lucasnlm.antimine.common.level.logic

import dev.lucasnlm.antimine.common.level.models.Area
import dev.lucasnlm.antimine.common.level.models.Minefield
import org.junit.Test

import org.junit.Assert.*
import kotlin.random.Random

class BruteForceSolverTest {
    private fun handleMinefield(block: (MinefieldHandler, MutableList<Area>) -> Unit) {
        val creator = MinefieldCreator(Minefield(9, 9, 12), Random(200))
        val minefield = creator.create(40, true).toMutableList()
        val minefieldHandler = MinefieldHandler(minefield, false)
        block(minefieldHandler, minefield)
    }

    @Test
    fun isSolvable() {
        handleMinefield { handler, minefield ->
            handler.openAt(40)
            val bruteForceSolver = BruteForceSolver(minefield.toMutableList())
            assertTrue(bruteForceSolver.isSolvable())
        }

        handleMinefield { handler, minefield ->
            handler.openAt(0)
            val bruteForceSolver = BruteForceSolver(minefield.toMutableList())
            assertFalse(bruteForceSolver.isSolvable())
        }
    }
}