import scala.{Either => _, Left => _, Right => _}
/*
 * <p>This code is derived from a Github Gist written by Chris Marshall:
 *   https://gist.github.com/970717
 * </p>
 * <p>It is essentially a copy of a fork of the above, made by Rob Dickens:
 *   https://gist.github.com/1241855
 * </p>
 */
/**
 * Part Zero : 10:15 Saturday Night
 */
object Sobriety extends Enumeration {
  val Sober, Tipsy, Drunk, Paralytic, Unconscious = Value
}

object Gender extends Enumeration {
  val Male, Female = Value
}

case class Person(
  gender: Gender.Value,
  age: Int,
  clothes: Set[String],
  sobriety: Sobriety.Value)

object people {
  val Ken = Person(Gender.Male, 28, Set("Tie", "Shirt"), Sobriety.Tipsy)
  val Dave = Person(Gender.Male, 41, Set("Tie", "Jeans"), Sobriety.Sober)
  val Ruby = Person(Gender.Female, 25, Set("High Heels"), Sobriety.Tipsy)
}
/**
 * Let's define a trait which will contain the checks that *all* nightclubs make! */
trait Nightclub {
  type E = Either[String, Person]
  //First CHECK
  def checkAge(p: Person): E =
    if (p.age < 18) Left("Too Young!")
    else if (p.age > 40) Left("Too Old!")
    else Right(p)

  //Second CHECK
  def checkClothes(p: Person): E =
    if (p.gender == Gender.Male && !p.clothes("Tie"))
      Left("Smarten Up!")
    else if (p.gender == Gender.Female && p.clothes("Trainers"))
      Left("Wear high heels")
    else
      Right(p)

  //Third CHECK
  def checkSobriety(p: Person): E =
    if (Set(Sobriety.Drunk, Sobriety.Paralytic, Sobriety.Unconscious) contains p.sobriety)
      Left("Sober Up!")
    else
      Right(p)
}
/**
 * Part One : Clubbed to Death (right-biased usage of Either)
 *
 * Now let's compose some validation checks */
object rightbiased_ClubbedToDeath extends Nightclub {
  def costToEnter(p: Person): Either[String, Double] = {

    //PERFORM THE CHECKS USING Monadic "for comprehension" SUGAR
    for {
      a <- checkAge(p)
      b <- checkClothes(a)
      c <- checkSobriety(b)
      amount = if (c.gender == Gender.Female) 0D else 5D
    } yield amount
  }
}

//import org.scalatest.{FunSuite, matchers}
//import matchers.ShouldMatchers

// Now let's see these in action
//class rightbiased_ClubbedToDeathTest extends FunSuite with ShouldMatchers {
object rightbiased_ClubbedToDeathTest extends App {
  def test(s: String)(b: => Unit) { b }

  import people._

  test("costToEnter") {
    //rightbiased_ClubbedToDeath.costToEnter(Dave) should equal(Left("Too Old!"))
    assert(rightbiased_ClubbedToDeath.costToEnter(Dave) == Left("Too Old!"))
    //scalaz.Validation[String,Double] = Failure(Too Old!)

    //rightbiased_ClubbedToDeath.costToEnter(Ken) should equal(Right(5.0))
    assert(rightbiased_ClubbedToDeath.costToEnter(Ken) == Right(5.0))
    //scalaz.Validation[String,Double] = Success(5.0)

    //rightbiased_ClubbedToDeath.costToEnter(Ruby) should equal(Right(0.0))
    assert(rightbiased_ClubbedToDeath.costToEnter(Ruby) == Right(0.0))
    // scalaz.Validation[String,Double] = Success(0.0)

    //rightbiased_ClubbedToDeath.costToEnter(Ruby.copy(age = 17)) should equal(Left("Too Young!"))
    assert(rightbiased_ClubbedToDeath.costToEnter(Ruby.copy(age = 17)) == Left("Too Young!"))
    // scalaz.Validation[String,Double] = Failure(Too Young!)

    //rightbiased_ClubbedToDeath.costToEnter(Ken.copy(sobriety = Sobriety.Unconscious)) should
    //  equal(Left("Sober Up!"))
    assert(rightbiased_ClubbedToDeath.costToEnter(Ken.copy(sobriety = Sobriety.Unconscious)) ==
      Left("Sober Up!"))
    // scalaz.Validation[String,Double] = Failure(Sober Up!)
  }
}
/**
 * Part One : Clubbed to Death (unbiased usage of Either)
 *
 * Now let's compose some validation checks */
object unbiased_ClubbedToDeath extends Nightclub {
  def costToEnter(p: Person): Either[String, Double] = {

    //PERFORM THE CHECKS USING Monadic "for comprehension" SUGAR
    val rp = for {
      a <- checkAge(p).rp
      b <- checkClothes(a).rp
      c <- checkSobriety(b).rp
      amount = if (c.gender == Gender.Female) 0D else 5D
    } yield amount
    rp.e
  }
}

// Now let's see these in action
//class unbiased_ClubbedToDeathTest extends FunSuite with ShouldMatchers {
object unbiased_ClubbedToDeathTest extends App {
  def test(s: String)(b: => Unit) { b }

  import people._

  test("costToEnter") {
    //unbiased_ClubbedToDeath.costToEnter(Dave) should equal(Left("Too Old!"))
    assert(unbiased_ClubbedToDeath.costToEnter(Dave) == Left("Too Old!"))
    //scalaz.Validation[String,Double] = Failure(Too Old!)

    //unbiased_ClubbedToDeath.costToEnter(Ken) should equal(Right(5.0))
    assert(unbiased_ClubbedToDeath.costToEnter(Ken) == Right(5.0))
    //scalaz.Validation[String,Double] = Success(5.0)

    //unbiased_ClubbedToDeath.costToEnter(Ruby) should equal(Right(0.0))
    assert(unbiased_ClubbedToDeath.costToEnter(Ruby) == Right(0.0))
    // scalaz.Validation[String,Double] = Success(0.0)

    //unbiased_ClubbedToDeath.costToEnter(Ruby.copy(age = 17)) should equal(Left("Too Young!"))
    assert(unbiased_ClubbedToDeath.costToEnter(Ruby.copy(age = 17)) == Left("Too Young!"))
    // scalaz.Validation[String,Double] = Failure(Too Young!)

    //unbiased_ClubbedToDeath.costToEnter(Ken.copy(sobriety = Sobriety.Unconscious)) should
    //  equal(Left("Sober Up!"))
    assert(unbiased_ClubbedToDeath.costToEnter(Ken.copy(sobriety = Sobriety.Unconscious)) ==
      Left("Sober Up!"))
    // scalaz.Validation[String,Double] = Failure(Sober Up!)
  }
}
