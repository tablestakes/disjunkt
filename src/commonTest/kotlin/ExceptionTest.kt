package tablestakes.disjunkt

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ExceptionTest {
    @Test
    fun orThrowThrowsWhenLeft() {
        assertFailsWith(SomeException::class) { left.orThrow(::SomeException) }
    }

    @Test
    fun orThrowPassesValueToExceptionCtorWhenLeft() {
        val x1 = assertFailsWith(SomeExceptionWithMessage::class) {
            Either.Left("some message").orThrow(::SomeExceptionWithMessage)
        }
        assertEquals("some message", x1.message)

        val x2 = assertFailsWith(
            SomeExceptionWithCode::class
        ) { left.orThrow(::SomeExceptionWithCode) }
        assertEquals("$expectedLeftValue", x2.message)

        val x3 = assertFailsWith(
            SomeExceptionWithMultipleConstructors::class
        ) { left.orThrow { _ -> SomeExceptionWithMultipleConstructors() } }
        assertEquals("0", x3.message)

    }

    @Test
    fun orThrowReturnsRightValueWhenRight() {
        assertEquals(expectedRightValue, right.orThrow(::SomeException))
        assertEquals(expectedRightValue, right.orThrow(::SomeExceptionWithCode))
        assertEquals(expectedRightValue, right.mapLeft { "" }.orThrow(::SomeExceptionWithMessage))
    }

    @Test
    fun orThrowThrowsValueWhenLeftHasExceptionType() {
        assertFailsWith(SomeException::class) { Either.Left(SomeException()).orThrow() }
    }
}
