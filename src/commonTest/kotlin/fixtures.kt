package tablestakes.disjunkt

internal val expectedLeftValue = 42
internal val left: Either<Int, String> = Either.Left(expectedLeftValue)

internal val expectedRightValue = "expected"
internal val right: Either<Int, String> = Either.Right(expectedRightValue)


internal class SomeException : Exception()
internal class SomeExceptionWithMessage(message: String) : Exception(message)
internal class SomeExceptionWithMultipleConstructors(code: Int) : Exception("$code") {
    constructor() : this(0)
}

internal class SomeExceptionWithCode(code: Int) : Exception("$code")
internal data class SomeThing<T>(val value: T)
