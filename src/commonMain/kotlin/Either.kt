package disjunct

sealed class Either<out L, out R> {
    class Left<out L>(val left: L) : Either<L, Nothing>()
    class Right<out R>(val right: R): Either<Nothing, R>()
}

fun <L, R> Either<L, R>.right(): R = when (this) {
    is Either.Right -> right
    is Either.Left -> throw NoSuchElementException()
}

fun <L, R> Either<L, R>.left(): L = when (this) {
    is Either.Left -> this.left
    is Either.Right -> throw NoSuchElementException()
}
