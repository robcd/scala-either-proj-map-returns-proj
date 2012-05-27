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
    val left = for {
      a <- either.left
      b = a + intVal2
      if b > 0
    } yield b
    left.e should equal(Left(intVal + intVal2))
    left.get should equal(intVal + intVal2)
    left.getOrElse(0) should equal(intVal + intVal2)
    left.forall(_ == intVal) should be(false)
    left.forall(_ == intVal + intVal2) should be(true)
    left.exists(_ == intVal) should be(false)
    left.exists(_ == intVal + intVal2) should be(true)
    left.toSeq should equal(Seq(intVal + intVal2))
    left.toOption should equal(Some(intVal + intVal2))
  }

  test("left map false") {
    val either: Either[Int, String] = Left(intVal)
    val left = for {
      a <- either.left
      b = a + intVal2
      if b < 0
    } yield b
    left.e should equal(LeftAsRight(Left(intVal + intVal2)))
    intercept[NoSuchElementException] {
      left.get
    }
    left.getOrElse(0) should equal(0)
    left.forall(_ == intVal) should be(true) // since no elements
    left.forall(_ == intVal + intVal2) should be(true) // "
    left.exists(_ == intVal) should be(false) // "
    left.exists(_ == intVal + intVal2) should be(false) // "
    left.toSeq should equal(Seq())
    left.toOption should equal(None)
  }

  test("right map true") {
    val either: Either[String, Int] = Right(intVal)
    val right = for {
      a <- either.right
      b = a + intVal2
      if b > 0
    } yield b
    right.e should equal(Right(intVal + intVal2))
    right.get should equal(intVal + intVal2)
    right.getOrElse(0) should equal(intVal + intVal2)
    right.forall(_ == intVal) should be(false)
    right.forall(_ == intVal + intVal2) should be(true)
    right.exists(_ == intVal) should be(false)
    right.exists(_ == intVal + intVal2) should be(true)
    right.toSeq should equal(Seq(intVal + intVal2))
    right.toOption should equal(Some(intVal + intVal2))
  }

  test("right map false") {
    val either: Either[String, Int] = Right(intVal)
    val right = for {
      a <- either.right
      b = a + intVal2
      if b < 0
    } yield b
    right.e should equal(RightAsLeft(Right(intVal + intVal2)))
    intercept[NoSuchElementException] {
      right.get
    }
    right.getOrElse(0) should equal(0)
    right.forall(_ == intVal) should be(true) // since no elements
    right.forall(_ == intVal + intVal2) should be(true) // "
    right.exists(_ == intVal) should be(false) // "
    right.exists(_ == intVal + intVal2) should be(false) // "
    right.toSeq should equal(Seq())
    right.toOption should equal(None)
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
    val right = for {
      a <- gt0(intVal).right
      b <- gt0(a).right
      c = b + intVal2
      if c > 0
    } yield c
    right.e should equal(Right(intVal + intVal2))
  }

  test("two right generators with map true 2") {
    def gt0(n: Int): Either[String, Int] = if (n > 0) Right(n) else Left("n must be > 0: "+ n)
    val right = for {
      a <- gt0(intVal).right
      b = a + intVal2
      if b > 0
      c <- gt0(b).right
    } yield c
    right.e should equal(Right(intVal + intVal2))
  }

  test("two right generators with map false") {
    def gt0(n: Int): Either[String, Int] = if (n > 0) Right(n) else Left("n must be > 0: "+ n)
    val right = for {
      a <- gt0(intVal).right
      b <- gt0(a).right
      c = b + intVal2
      if c < 0
    } yield c
    right.e should equal(RightAsLeft(Right(intVal + intVal2)))
  }

  test("two right generators with map false 2") {
    def gt0(n: Int): Either[String, Int] = if (n > 0) Right(n) else Left("n must be > 0: "+ n)
    val right = for {
      a <- gt0(intVal).right
      b = a + intVal2
      if b < 0
      c <- gt0(b).right
    } yield c
    right.e should equal(RightAsLeft(Right(intVal + intVal2)))
  }
}

