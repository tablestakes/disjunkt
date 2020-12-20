package tablestakes.disjunkt

typealias Result<T> = Either<Exception, T>
typealias Success<T> = Either.Right<T>
typealias Failure = Either.Left<Exception>

fun <R, T : R> Result<T>.recover(transform: (Exception) -> R): Result<R> = flatMapLeft { Success(transform(it)) }

fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> = onRight { action(it) }
fun <T> Result<T>.onFailure(action: (Exception) -> Unit): Result<T> = onLeft { action(it) }


