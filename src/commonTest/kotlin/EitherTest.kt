package tablestakes.disjunkt

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class EitherTest {


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
    fun mapTransformsRight() {
        assertEquals(SomeThing(expectedRightValue), right.map { SomeThing(it) }.right())
    }

    @Test
    fun mapPerformsNoActionOnLeft() {
        assertEquals(left, left.map { SomeThing(it) })
        assertSame(left, left.map { SomeThing(it) })
    }

    @Test
    fun mapLeftTransformsLeft() {
        assertEquals(SomeThing(expectedLeftValue), left.mapLeft { SomeThing(it) }.left())
    }

    @Test
    fun mapLeftPerformsNoActionOnRight() {
        assertEquals(right, right.mapLeft { SomeThing(it) })
        assertSame(right, right.mapLeft { SomeThing(it) })
    }

    @Test
    fun flatMapTransformsRight() {
        assertEquals(SomeThing(expectedRightValue), right.flatMap { Either.Right(SomeThing(it)) }.right())
    }

    @Test
    fun flatMapPerformsNoActionOnLeft() {
        assertEquals(left, left.flatMap { Either.Right(SomeThing(it)) })
        assertSame(left, left.flatMap { Either.Right(SomeThing(it)) })
    }

    @Test
    fun flatMapCanReturnLeft() {
        assertEquals(left, right.flatMap { left })
        assertEquals(SomeThing(false), right.flatMap { Either.Left(SomeThing(false)) }.left())
    }

    @Test
    fun flatMapLeftTransformsLeft() {
        assertEquals(SomeThing(expectedLeftValue), left.flatMapLeft { Either.Left(SomeThing(it)) }.left())
    }

    @Test
    fun flatMapLeftPerformsNoActionOnRight() {
        assertSame(right, right.flatMapLeft { Either.Left(SomeThing(it)) })
    }

    @Test
    fun flatMapLeftCanReturnRight() {
        assertEquals(right, left.flatMapLeft { right })
        assertEquals(SomeThing(false), left.flatMapLeft { Either.Right(SomeThing(false)) }.right())
    }

    @Test
    fun isRightReturnsTrueIfRight() {
        assertTrue(right.isRight())
    }

    @Test
    fun isRightReturnsFalseIfLeft() {
        assertFalse(left.isRight())
    }

    @Test
    fun isLeftReturnsTrueIfLeft() {
        assertTrue(left.isLeft())
    }

    @Test
    fun isLeftReturnsFalseIfRight() {
        assertFalse(right.isLeft())
    }

    @Test
    fun onRightPerformsActionAndReturnsSameInstanceWhenRight() {
        var invokedWith: String? = null
        assertSame(right, right.onRight { invokedWith = it })
        assertEquals(expectedRightValue, invokedWith)
    }

    @Test
    fun onLeftPerformsActionAndReturnsSameInstanceWhenLeft() {
        var invokedWith: Int? = null
        assertSame(left, left.onLeft { invokedWith = it })
        assertEquals(expectedLeftValue, invokedWith)
    }

    @Test
    fun onRightReturnsSameInstanceWithoutPerformingActionWhenLeft() {
        var invokedWith: String? = null
        assertSame(left, left.onRight { invokedWith = it })
        assertNull(invokedWith)
    }

    @Test
    fun onLeftReturnsSameInstanceWithoutPerformingActionWhenRight() {
        var invokedWith: Int? = null
        assertSame(right, right.onLeft { invokedWith = it })
        assertNull(invokedWith)
    }

    @Test
    fun leftEquality() {
        assertEquals(Either.Left("something"), Either.Left("something"))
        assertNotEquals(Either.Left("something"), Either.Left("something else"))
        assertNotEquals(Either.Left("something"), Either.Right("something"))
    }

    @Test
    fun rightEquality() {
        assertEquals(Either.Right("something"), Either.Right("something"))
        assertNotEquals(Either.Right("something"), Either.Right("something else"))
        assertNotEquals(Either.Right("something"), Either.Left("something"))
    }

    @Test
    fun destructuringReturnRightWhenRight() {
        val (value) = right
        assertEquals(expectedRightValue, value)
    }

    @Test
    fun destructuringThrowsWhenLeft() {
        assertFailsWith(NoSuchElementException::class) {
            val (value) = left
        }
    }
}
