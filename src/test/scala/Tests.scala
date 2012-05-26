import org.scalatest.{FunSuite, matchers}
import matchers.ShouldMatchers
import scala.{Either => _, Left => _, Right => _}

class Tests extends FunSuite with ShouldMatchers {
  val intVal = 1
  val intVal2 = 1

  test("left foreach true") {
    val either: Either[Int, String] = Left(intVal)
    var res = 0
    for {
      a <- either.left
      b = a + intVal2
      if b > 0
    } res = b

    res should equal(intVal + intVal2)
  }

  test("left foreach false") {
    val either: Either[Int, String] = Left(intVal)
    var res = 0
    for {
      a <- either.left
      b = a + intVal2
      if b < 0
    } res = b

    res should equal(0)
  }

  test("right foreach true") {
    val either: Either[String, Int] = Right(intVal)
    var res = 0
    for {
      a <- either.right
      b = a + intVal2
      if b > 0
    } res = b
    res should equal(intVal + intVal2)
  }

  test("right foreach false") {
    val either: Either[String, Int] = Right(intVal)
    var res = 0
    for {
      a <- either.right
      b = a + intVal2
      if b < 0
    } res = b
    res should equal(0)
  }

  test("left map true") {
    val either: Either[Int, String] = Left(intVal)
    val res = for {
      a <- either.left
      b = a + intVal2
      if b > 0
    } yield b
    res should equal(Either.LeftProjection(Left(intVal + intVal2)))
  }

  test("left map false") {
    val either: Either[Int, String] = Left(intVal)
    val res = for {
      a <- either.left
      b = a + intVal2
      if b < 0
    } yield b
    res should equal(Either.LeftProjection(LeftAsRight(Left(intVal + intVal2))))
  }

  test("right map true") {
    val either: Either[String, Int] = Right(intVal)
    val res = for {
      a <- either.right
      b = a + intVal2
      if b > 0
    } yield b
    res should equal(Either.RightProjection(Right(intVal + intVal2)))
  }

  test("right map false") {
    val either: Either[String, Int] = Right(intVal)
    val res = for {
      a <- either.right
      b = a + intVal2
      if b < 0
    } yield b
    res should equal(Either.RightProjection(RightAsLeft(Right(intVal + intVal2))))
  }

  test("two right generators with foreach true") {
    def gt0(n: Int): Either[String, Int] = if (n > 0) Right(n) else Left("n must be > 0: "+ n)
    var res = 0
    for {
      a <- gt0(intVal).right
      b <- gt0(a).right
      c = b + intVal2
      if c > 0
    } res = c
    res should equal(intVal + intVal2)
  }

  test("two right generators with foreach false") {
    def gt0(n: Int): Either[String, Int] = if (n > 0) Right(n) else Left("n must be > 0: "+ n)
    var res = 0
    for {
      a <- gt0(intVal).right
      b <- gt0(a).right
      c = b + intVal2
      if c < 0
    } res = c
    res should equal(0)
  }

  test("two right generators with map true") {
    def gt0(n: Int): Either[String, Int] = if (n > 0) Right(n) else Left("n must be > 0: "+ n)
    val res = for {
      a <- gt0(intVal).right
      b <- gt0(a).right
      c = b + intVal2
      if c > 0
    } yield c
    res should equal(Either.RightProjection(Right(intVal + intVal2)))
  }

  test("two right generators with map false") {
    def gt0(n: Int): Either[String, Int] = if (n > 0) Right(n) else Left("n must be > 0: "+ n)
    val res = for {
      a <- gt0(intVal).right
      b <- gt0(a).right
      c = b + intVal2
      if c < 0
    } yield c
    res should equal(Either.RightProjection(RightAsLeft(Right(intVal + intVal2))))
  }
}

