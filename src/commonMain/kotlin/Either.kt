package disjunkt

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

fun <L, R, T> Either<L, R>.flatMap(transform: (R) -> Either<L, T>): Either<L, T> = when (this) {
    is Either.Left -> this
    is Either.Right -> transform(right)
}

fun <L, R, T> Either<L, R>.map(transform: (R) -> T): Either<L, T> = flatMap { Either.Right(transform(it)) }

fun <L, R, T> Either<L, R>.flatMapLeft(transform: (L) -> Either<T, R>): Either<T, R> = when (this) {
    is Either.Left -> transform(left)
    is Either.Right -> this
}

fun <L, R, T> Either<L, R>.mapLeft(transform: (L) -> T): Either<T, R> = flatMapLeft { Either.Left(transform(it)) }

fun <L, R> Either<L, R>.isLeft(): Boolean = when (this) {
    is Either.Right -> false
    is Either.Left -> true
}

fun <L, R> Either<L, R>.isRight(): Boolean =when (this) {
    is Either.Right -> true
    is Either.Left -> false
}
