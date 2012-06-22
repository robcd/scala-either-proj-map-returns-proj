//import org.scalatest.{FunSuite, matchers}
//import matchers.ShouldMatchers
import scala.{Either => _, Left => _, Right => _}

//class rightbiased_Tests extends FunSuite with ShouldMatchers {
object RexsTests extends App {
  def test(s: String)(b: => Unit) { println(s); b }

  val s = "42"
  val m = Map(42 -> "Meaning of life")
  val e = try { Right(s.toInt) } catch { case _: Exception => Left(s) }

  test("using a B => C e.g. Int => Option[String]") {
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
}
