import org.scalatest.{FunSuite, matchers}
import matchers.ShouldMatchers
import scala.{Either => _, Left => _, Right => _}

class Tests extends FunSuite with ShouldMatchers {
  test("left foreach true") {
    val either: Either[Int, String] = Left(1)
    var res = 0
    for {
      a <- either.left
      b = a + 1
      if b > 0
    } res = b

    res should equal(2)
  }

  test("left foreach false") {
    val either: Either[Int, String] = Left(1)
    var res = 0
    for {
      a <- either.left
      b = a + 1
      if b < 0
    } res = b

    res should equal(0)
  }

  test("right foreach true") {
    val either: Either[String, Int] = Right(1)
    var res = 0
    for {
      a <- either.right
      b = a + 1
      if b > 0
    } res = b
    res should equal(2)
  }

  test("right foreach false") {
    val either: Either[String, Int] = Right(1)
    var res = 0
    for {
      a <- either.right
      b = a + 1
      if b < 0
    } res = b
    res should equal(0)
  }

  test("left map true") {
    val either: Either[Int, String] = Left(1)
    val left = for {
      a <- either.left
      b = a + 1
      if b > 0
    } yield b
    left.e should equal(Left(2))
    left.get should equal(2)
    left.getOrElse(0) should equal(2)
    left.forall(_ == 1) should be(false)
    left.forall(_ == 2) should be(true)
    left.exists(_ == 1) should be(false)
    left.exists(_ == 2) should be(true)
    left.toSeq should equal(Seq(2))
    left.toOption should equal(Some(2))
  }

  test("left map false") {
    val either: Either[Int, String] = Left(1)
    val left = for {
      a <- either.left
      b = a + 1
      if b < 0
    } yield b
    left.e should equal(LeftAsRight(Left(2)))
    val thrown = intercept[NoSuchElementException] {
      left.get
    }
    thrown.getMessage should equal("Either.left.value on Right")
    left.getOrElse(0) should equal(0)
    left.forall(_ == 1) should be(true) // since no elements
    left.forall(_ == 2) should be(true) // "
    left.exists(_ == 1) should be(false) // "
    left.exists(_ == 2) should be(false) // "
    left.toSeq should equal(Seq())
    left.toOption should equal(None)
  }

  test("right map true") {
    val either: Either[String, Int] = Right(1)
    val right = for {
      a <- either.right
      b = a + 1
      if b > 0
    } yield b
    right.e should equal(Right(2))
    right.get should equal(2)
    right.getOrElse(0) should equal(2)
    right.forall(_ == 1) should be(false)
    right.forall(_ == 2) should be(true)
    right.exists(_ == 1) should be(false)
    right.exists(_ == 2) should be(true)
    right.toSeq should equal(Seq(2))
    right.toOption should equal(Some(2))
  }

  test("right map false") {
    val either: Either[String, Int] = Right(1)
    val right = for {
      a <- either.right
      b = a + 1
      if b < 0
    } yield b
    right.e should equal(RightAsLeft(Right(2)))
    val thrown = intercept[NoSuchElementException] {
      right.get
    }
    thrown.getMessage should equal("Either.right.value on Left")
    right.getOrElse(0) should equal(0)
    right.forall(_ == 1) should be(true) // since no elements
    right.forall(_ == 2) should be(true) // "
    right.exists(_ == 1) should be(false) // "
    right.exists(_ == 2) should be(false) // "
    right.toSeq should equal(Seq())
    right.toOption should equal(None)
  }

  def gt0(n: Int): Either[String, Int] = if (n > 0) Right(n) else Left("n must be > 0: "+ n)
  def gt1(n: Int): Either[String, Int] = if (n > 1) Right(n) else Left("n must be > 1: "+ n)

  test("two right generators with foreach - succeeds 1") {
    var res = 0
    val a = 2
    for {
      b <- gt0(a).right
      c <- gt1(b).right
    } res = c
    res should equal(2)
  }

  test("two right generators with foreach - succeeds 2") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).right
      c = b + 1
      d <- gt1(c).right
    } res = d
    res should equal(2)
  }

  test("two right generators with foreach - fails") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).right
      c <- gt1(b).right
    } res = c
    res should equal(0)
  }

  test("two right generators with foreach true") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).right
      c = b + 1
      d <- gt1(c).right
      if d > 0
    } res = d
    res should equal(2)
  }

  test("two right generators with foreach false") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).right
      c = b + 1
      d <- gt1(c).right
      if d < 0
    } res = d
    res should equal(0)
  }

  test("two right generators with map - succeeds 1") {
    val a = 2
    val right = for {
      b <- gt0(a).right
      c <- gt1(b).right
    } yield c
    right.e should equal(Right(a))
  }

  test("two right generators with map - succeeds 2") {
    val a = 1
    val right = for {
      b <- gt0(a).right
      c = b + 1
      d <- gt1(c).right
    } yield d
    right.e should equal(Right(2))
  }

  test("two right generators with map - fails") {
    val a = 1
    val right = for {
      b <- gt0(a).right
      c <- gt1(b).right
    } yield c
    right.e should equal(Left("n must be > 1: 1"))
    right.getOrElse(0) should equal(0)
  }

  test("two right generators with map true") {
    val a = 1
    val right = for {
      b <- gt0(a).right
      c = b + 1
      d <- gt1(c).right
      if d > 0
    } yield d
    right.e should equal(Right(2))
  }

  test("two right generators with map true 2") {
    val a = 1
    val right = for {
      b <- gt0(a).right
      c = b + 1
      if c > 0
      d <- gt1(c).right
    } yield d
    right.e should equal(Right(2))
  }

  test("two right generators with map false") {
    val a = 1
    val right = for {
      b <- gt0(a).right
      c = b + 1
      d <- gt1(c).right
      if d < 0
    } yield d
    right.e should equal(RightAsLeft(Right(2)))
  }

  test("two right generators with map false 2") {
    val a = 1
    val right = for {
      b <- gt0(a).right
      c = b + 1
      if c < 0
      d <- gt1(c).right
    } yield d
    right.e should equal(RightAsLeft(Right(2)))
  }
}

