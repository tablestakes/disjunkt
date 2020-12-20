package disjunct

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

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

    @Test
    fun mapTransformsRightValue() {
        assertEquals(SomeThing(expectedRightValue), right.map { SomeThing(it) }.right())
    }

    @Test
    fun mapPerformsNoActionOnLeftValue() {
        assertEquals(left, left.map { SomeThing(it) })
        assertSame(left, left.map { SomeThing(it) })
    }

    @Test
    fun mapLeftTransformsLeftValue() {
        assertEquals(SomeThing(expectedLeftValue), left.mapLeft {SomeThing(it)}.left() )
    }

    @Test
    fun mapLeftPerformsNoActionOnRightValue() {
        assertEquals(right, right.mapLeft {SomeThing(it)} )
        assertSame(right, right.mapLeft {SomeThing(it)} )
    }
}


private data class SomeThing<T>(val value: T)
