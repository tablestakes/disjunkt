package disjunct

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class EitherTest {

    @Test
    fun rightGivesValueWhenRight() {

        val expected = "expected"
        val d: Either<Int, String> = Either.Right(expected)

        assertEquals(expected, d.right())
    }

    @Test
    fun rightThrowsWhenLeft() {

        val d: Either<Int, String> = Either.Left(42)

        assertFailsWith(NoSuchElementException::class) { d.right() }
    }

}

