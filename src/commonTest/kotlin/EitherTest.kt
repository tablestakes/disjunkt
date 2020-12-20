package disjunct

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class EitherTest {

    private val expectedLeftValue = 42
    private val left: Either<Int, String> = Either.Left(expectedLeftValue)

    private val expectedRightValue = "expected"
    private val right: Either<Int, String> = Either.Right(expectedRightValue)

    @Test
    fun rightGivesValueWhenRight() {
        assertEquals(expectedRightValue, right.right())
    }

    @Test
    fun rightThrowsWhenLeft() {
        assertFailsWith(NoSuchElementException::class) { left.right() }
    }

    @Test
    fun leftGivesValueWhenLeft() {
        assertEquals(expectedLeftValue, left.left())
    }

    @Test
    fun leftThrowsWhenRight() {
        assertFailsWith(NoSuchElementException::class) { right.left() }
    }

}


