import org.scalatest.{FunSuite, matchers}
import matchers.ShouldMatchers
import scala.{Either => _, Left => _, Right => _}

class Tests extends FunSuite with ShouldMatchers {
  val intVal = 1
  val either: Either[String, Int] = Right(intVal)

  test("foreach") {
    val intVal2 = 1
    var res = 0
    for {
      a <- either.right
      b = a + intVal2
    } res = b
    res should equal(intVal + intVal2)
  }

  test("map") {
    val intVal2 = 1
    val res = for {
      a <- either.right
      b = a + intVal2
    } yield b
    res should equal(Either.RightProjection(Right(intVal + intVal2)))
  }
}

