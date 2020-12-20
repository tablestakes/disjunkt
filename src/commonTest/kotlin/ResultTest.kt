package tablestakes.disjunkt

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ResultTest {

    val success: Result<Int> = Success(42)
    val failure: Result<String> = Failure(SomeExceptionWithMessage("reason"))

    @Test
    fun recover() {
        assertEquals(failure.recover { "Recovered from: ${it.message}" }, Success("Recovered from: reason"))
    }

    @Test
    fun onSuccess() {
        var succeeded = false
        assertEquals(success.onSuccess{ succeeded = true }, success)
        assertTrue(succeeded)
    }

    @Test
    fun onFailure() {
        var failed = false
        assertEquals(failure.onFailure{ failed = true }, failure)
        assertTrue(failed)
    }
}

