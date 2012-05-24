import org.scalatest.{FunSuite, matchers}
import matchers.ShouldMatchers
import scala.{Either => _, Left => _, Right => _}

class Tests extends FunSuite with ShouldMatchers {
  val intVal = 1
  val intVal2 = 1

  test("left foreach") {
    val either: Either[Int, String] = Left(intVal)
    var res = 0
    for {
      a <- either.left
      b = a + intVal2
    } res = b
    res should equal(intVal + intVal2)
  }

  test("right foreach") {
    val either: Either[String, Int] = Right(intVal)
    var res = 0
    for {
      a <- either.right
      b = a + intVal2
    } res = b
    res should equal(intVal + intVal2)
  }

  test("left map") {
    val either: Either[Int, String] = Left(intVal)
    val res = for {
      a <- either.left
      b = a + intVal2
    } yield b
    res should equal(Either.LeftProjection(Left(intVal + intVal2)))
  }

  test("right map") {
    val either: Either[String, Int] = Right(intVal)
    val res = for {
      a <- either.right
      b = a + intVal2
    } yield b
    res should equal(Either.RightProjection(Right(intVal + intVal2)))
  }
}

