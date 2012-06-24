//import org.scalatest.{FunSuite, matchers}
//import matchers.ShouldMatchers
import scala.{Either => _, Left => _, Right => _}
/**
 * Tests based on some none pattern-matching examples calling for None as well as Left and
 * Right, as posted by Rex Kerr to the 'fixing either' scala-debate.
 *
 * Since Either must be either Left or Right, we must employ a B => Either[A, Option[C]] in
 * right-biased usage for-comprehensions, or else (as Rex shows) return a Left.
 */
//class rightbiased_Tests extends FunSuite with ShouldMatchers {
object RexsTests extends App {
  def test(s: String)(b: => Unit) { println(s); b }

  val s = "42"
  val m = Map(42 -> "Meaning of life")
  val e = try { Right(s.toInt) } catch { case _: Exception => Left(s) }

  test("Using a B => Either[A, C] e.g. Int => Either[A, Option[String]]") {
    // def cantLiftToOption = {
    //   This is actually trying to for-comprehend something
    //   for (n <- e; what <- m.get(n)) yield what
    // }
    val res = for {
      n <- e                  // Either[String, Int]
      //what <- m.get(n)         Option[String]
      what <- Right(m.get(n)) // Either[String, Option[String]]
    } yield what

    assert(res == Right(Some("Meaning of life")))
    //            Right(None)
    //            Left("something other than an Int")

    for {
      opt <- res
      what <- opt
    } println(s +" -> "+ what)

    res match {
      case Right(opt) => opt match {
        case Some(what) => println(s +" -> "+ what)
        case None => println(s +" didn't map to anything")
      }
      case Left(s) => println(s +" could not be parsed as an Int")
    }
  }

  test("Alternatively, could return a Left instead of a Right(None)") {
    val res = for {
      n <- e
      what <- m.get(n).map(x => Right(x)).getOrElse(Left(s))
    } yield what

    assert(res == Right("Meaning of life"))
    //            Left("something other than an Int")
  }

  test("Prev test, unbiased usage") {
    val res = e.fold(l => Left(l), r => m.get(r).map(x => Right(x)).getOrElse(Left(s)))

    assert(res == Right("Meaning of life"))
    //            Left("something other than an Int")
  }

  test("Putting your 'if' in a B => Either[A, C], where C is an Option[_]") {
    // def cantIf = {
    //   for (n <- e if m.contains(n)) yield n
    // }

    val res = for {
      n <- e                                                  // Either[A, Int]
      opt_n <- Right { if (m.contains(n)) Some(n) else None } // Either[A, Option[Int]]
    } yield opt_n

    assert(res == Right(Some(42)))
    //            Right(None)
    //            Left("something other than an Int")
  }

  test("Alternatively, could return a Left instead of a Right(None)") {
    val res = e.flatMap(n => if (m contains n) Right(n) else Left(s))

    assert(res == Right(42))
    //            Left("something other than an Int contained in m")
  }

  test("Prev test, unbiased usage") {
    val res = e.fold(l => Left(l), r => if (m contains r) Right(r) else Left(s))

    assert(res == Right(42))
    //            Left("something other than an Int contained in m")
  }
}
