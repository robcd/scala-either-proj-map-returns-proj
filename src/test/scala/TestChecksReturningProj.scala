import org.scalatest.{FunSuite, matchers}
import matchers.ShouldMatchers
import scala.{Either => _, Left => _, Right => _}

class TestChecksReturningProj extends FunSuite with ShouldMatchers {
  type RP[L, R] = Either.RightProjection[L, R]
  def RP = Either.RightProjection

  def gt0(n: Int): RP[String, Int] =
    if (n > 0) RP(Right(n)) else RP(Left("n must be > 0: "+ n))
  def gt1(n: Int): RP[String, Int] =
    if (n > 1) RP(Right(n)) else RP(Left("n must be > 1: "+ n))

  test("two right generators with foreach - succeeds 1") {
    var res = 0
    val a = 2
    for {
      b <- gt0(a)
      c <- gt1(b)
    } res = c
    res should equal(2)
  }

  test("two right generators with foreach - fails") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a)
      c <- gt1(b)
    } res = c
    res should equal(0)
  }

  test("two right generators with map - succeeds 1") {
    val a = 2
    val right = for {
      b <- gt0(a)
      c <- gt1(b)
    } yield c
    right.e should equal(Right(a))
  }

  test("two right generators with map - fails") {
    val a = 1
    val right = for {
      b <- gt0(a)
      c <- gt1(b)
    } yield c
    right.e should equal(Left("n must be > 1: 1"))
    right.getOrElse(0) should equal(0)
  }
}
