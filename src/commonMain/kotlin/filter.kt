package tablestakes.disjunkt

fun <L, R> Either<L, R>.filter(predicate: (R) -> Boolean): Either<L, R>? = when (this) {
    is Either.Left -> this
    is Either.Right -> if (predicate(right)) this else null
}

fun <L, R> Either<L, R>.filterLeft(predicate: (L) -> Boolean): Either<L, R>? = when (this) {
    is Either.Left -> if (predicate(left)) this else null
    is Either.Right -> this
}
