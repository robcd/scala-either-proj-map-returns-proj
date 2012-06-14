/*
 * <p>This code is derived from a Github Gist written by Chris Marshall:
 *   https://gist.github.com/970717
 * </p>
 * <p>It is essentially a copy of a fork of the above, made by Rob Dickens:
 *   https://gist.github.com/1241855
 * </p>
 */
import scala.{Either => _, Left => _, Right => _}
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
  //First CHECK
  def checkAge(p: Person): Either[String, Person] =
    if (p.age < 18) Left("Too Young!")
    else if (p.age > 40) Left("Too Old!")
    else Right(p)

  //Second CHECK
  def checkClothes(p: Person): Either[String, Person] =
    if (p.gender == Gender.Male && !p.clothes("Tie"))
      Left("Smarten Up!")
    else if (p.gender == Gender.Female && p.clothes("Trainers"))
      Left("Wear high heels")
    else
      Right(p)

  //Third CHECK
  def checkSobriety(p: Person): Either[String, Person] =
    if (Set(Sobriety.Drunk, Sobriety.Paralytic, Sobriety.Unconscious) contains p.sobriety)
      Left("Sober Up!")
    else
      Right(p)
}
/**
 * Part One : Clubbed to Death
 *
 * Now let's compose some validation checks */
object ClubbedToDeath extends Nightclub {
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

trait Test {
  def run(block: => Any) {
    println(block)
  }
}

// Now let's see these in action
object Test1 extends App with Test {
  import people._
  // Let's go clubbing!
  run(ClubbedToDeath costToEnter Dave) //res0: scalaz.Validation[String,Double] = Failure(Too Old!)

  run(ClubbedToDeath costToEnter Ken) //res1: scalaz.Validation[String,Double] = Success(5.0)

  run(ClubbedToDeath costToEnter Ruby) //res2: scalaz.Validation[String,Double] = Success(0.0)

  run(ClubbedToDeath costToEnter (Ruby.copy(age = 17)))
  //res3: scalaz.Validation[String,Double] = Failure(Too Young!)

  run(ClubbedToDeath costToEnter (Ken.copy(sobriety = Sobriety.Unconscious)))
  //res5: scalaz.Validation[String,Double] = Failure(Sober Up!)
}
/**
 * The thing to note here is how the Validations can be composed together in a
 * for-comprehension.  * Scala's type system is making sure that failures flow through your
 * computation in a safe manner.
 */
