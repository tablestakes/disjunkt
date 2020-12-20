package tablestakes.disjunkt


fun <L, R> Either<L, R>.orThrow(exception: () -> Exception): R = orThrow { _ -> exception() }
fun <L : Exception, R> Either<L, R>.orThrow(): R = orThrow { x -> x }

fun <L, R> Either<L, R>.orThrow(exception: (L) -> Exception): R = when (this) {
    is Either.Left -> throw exception(left)
    is Either.Right -> right
}
