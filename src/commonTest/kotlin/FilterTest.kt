package tablestakes.disjunkt

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

class FilterTest {
    @Test
    fun filterReturnsRightWhenMatched() {
        assertEquals(right, right.filter { true })
    }

    @Test
    fun filterReturnsNullWhenNotMatched() {
        assertNull(right.filter { false })
    }

    @Test
    fun filterReturnsLeftWhenLeft() {
        assertSame(left, left.filter { true })
    }

    @Test
    fun filterLeftReturnsLeftWhenMatched() {
        assertEquals(left, left.filterLeft { true })
    }

    @Test
    fun filterLeftReturnsNullWhenNotMatched() {
        assertNull(left.filterLeft { false })
    }

    @Test
    fun filterLeftReturnsRightWhenRight() {
        assertSame(right, right.filterLeft { true })
    }
}


