import scala.{Either => _, Left => _, Right => _}

object test extends App {
  val either: Either[String, Int] = Right(1)

  for {
    a <- either.right
    b = a + 1
  } println(b)
}
