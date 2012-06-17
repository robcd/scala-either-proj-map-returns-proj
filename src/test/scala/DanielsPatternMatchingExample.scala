import scala.{Either => _, Left => _, Right => _}

trait DanielsPatternMatchingExample {
  type E[B] = Either[Exception, B]

  type Pair = (Int, String)

  def either: E[Pair]

  for ((n, s) <- either) yield s
  //               ^
  // error: value filter is not a member of DanielsPatternMatchingExample.this.E[(Int, String)]

  // if there's no filter, perhaps the compiler could desugar like so, instead:
  val res1: E[String] = either match {
    case Left(a) => Left(a)
    case Right(b) => val (n, s) = b; Right(s)
  }

  // which is the same as this:
  val res2: E[String] = either map { b => val (n, s) = b; s }
}
