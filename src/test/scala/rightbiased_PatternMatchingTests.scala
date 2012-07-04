/*
 * Copyright 2012 Latterfrosken Software Development Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//import org.scalatest.{FunSuite, matchers}
//import matchers.ShouldMatchers
import scala.{Either => _, Left => _, Right => _}

//class unbiased_PatternMatchingTests extends FunSuite with ShouldMatchers {
object rightbiased_PatternMatchingTests extends App {
  def test(s: String)(b: => Unit) { b }

  type E[B] = Either[Exception, B]
  type Pair = (Int, String)
  type E1 = E[Pair]
  val ex = new Exception("oops")
  val prefix = "wo"; val suffix = "rd"; val word = prefix + suffix
  val pair = (1, prefix)

  test("foreach - Right") {
    val e1: E1 = Right(pair)
    var res = (0, "")
    for {
      (a, s) <- e1
      b = a + 1
      t = s + suffix
    } res = (b, t)

    //res should equal(2)
    assert(res == (2, word))
  }

  test("foreach - Left") {
    val e1: E1 = Left(ex)
    var res = (0, "")
    for {
      (a, s) <- e1
      b = a + 1
      t = s + suffix
    } res = (b, t)

    //res should equal(0)
    assert(res == (0, ""))
  }

  test("map - Right") {
    val e1: E1 = Right(pair)
    val res = for {
      (a, s) <- e1
      b = a + 1
      t = s + suffix
    } yield (b, t)

    // rp.e should equal(Right(2))
    // rp.get should equal(2)
    // rp.getOrElse(0) should equal(2)
    // rp.forall(_ == 1) should be(false)
    // rp.forall(_ == 2) should be(true)
    // rp.exists(_ == 1) should be(false)
    // rp.exists(_ == 2) should be(true)
    // rp.toSeq should equal(Seq(2))
    // rp.toOption should equal(Some(2))

    assert(res == Right(2, word))
  }

  test("map - Left") {
    val e1: E1 = Left(ex)
    val res = for {
      (a, s) <- e1
      b = a + 1
      t = s + suffix
    } yield (b, t)

    // rp.e should equal(Left("er"))
    // rp.getOrElse(0) should equal(0)
    // val thrown = intercept[NoSuchElementException] {
    //   rp.get
    // }
    // thrown.getMessage should equal("Either.rp.value on Left")
    // rp.forall(_ == 1) should be(true)
    // rp.forall(_ == 2) should be(true)
    // rp.exists(_ == 1) should be(false)
    // rp.exists(_ == 2) should be(false)
    // rp.toSeq should equal(Seq())
    // rp.toOption should equal(None)

    assert(res == Left(ex))
  }

  type String1st = (String, Int)

  type E2 = E[String1st]

  def toE2(n: Int, s: String): E2 = Right((s, n))

  test("foreach, two generators - Right 1") {
    val e1: E1 = Right(pair)
    var res = ("", 0)
    for {
      (a, s) <- e1
      (t, b) <- toE2(a, s)
    } res = (t, b)

    //res should equal(2)
    assert(res == (prefix, 1))
  }

  test("foreach, two generators - Right 2") {
    val e1: E1 = Right(pair)
    var res = ("", 0)
    for {
      (a, s) <- e1
      b = a + 1
      (t, c) <- toE2(b, s)
    } res = (t, c)

    //res should equal(2)
    assert(res == (prefix, 2))
  }

  test("foreach, two generators - Right 3") {
    val e1: E1 = Right(pair)
    var res = ("", 0)
    for {
      (a, s) <- e1
      (t, b) <- toE2(a, s)
      c = b + 1
    } res = (t, c)

    //res should equal(3)
    assert(res == (prefix, 2))
  }

  test("foreach, two generators - Left 1") {
    val e1: E1 = Left(ex)
    var res = ("", 0)
    for {
      (a, s) <- e1
      (t, b) <- toE2(a, s)
    } res = (t, b)

    //res should equal(0)
    assert(res == ("", 0))
  }

  test("foreach, two generators - Left 2") {
    val e1: E1 = Left(ex)
    var res = ("", 0)
    for {
      (a, s) <- e1
      b = a + 1
      (t, c) <- toE2(b, s)
    } res = (t, c)

    //res should equal(0)
    assert(res == ("", 0))
  }

  test("foreach, two generators - Left 3") {
    val e1: E1 = Left(ex)
    var res = ("", 0)
    for {
      (a, s) <- e1
      (t, b) <- toE2(a, s)
      c = b + 1
    } res = (t, c)

    //res should equal(0)
    assert(res == ("", 0))
  }

  test("map, two generators - Right 1") {
    val e1: E1 = Right(pair)
    val res = for {
      (a, s) <- e1
      (t, b) <- toE2(a, s)
    } yield (t, b)

    //rp.e should equal(Right(a))
    assert(res == Right((prefix, 1)))
  }

  test("map, two generators - Right 2") {
    val e1: E1 = Right(pair)
    val res = for {
      (a, s) <- e1
      b = a + 1
      (t, c) <- toE2(b, s)
    } yield (t, c)

    //rp.e should equal(Right(2))
    assert(res == Right((prefix, 2)))
  }

  test("map, two generators - Right 3") {
    val e1: E1 = Right(pair)
    val res = for {
      (a, s) <- e1
      (t, b) <- toE2(a, s)
      c = b + 1
    } yield (t, c)

    //rp.e should equal(Right(3))
    assert(res == Right((prefix, 2)))
  }

  test("map, two generators - Left 1") {
    val e1: E1 = Left(ex)
    val res = for {
      (a, s) <- e1
      (t, b) <- toE2(a, s)
    } yield (t, b)

    //rp.e should equal(Left("n must be > 1: 1"))
    assert(res == Left(ex))
  }

  test("map, two generators - Left 2") {
    val e1: E1 = Left(ex)
    val res = for {
      (a, s) <- e1
      b = a + 1
      (t, c) <- toE2(b, s)
    } yield (t, c)

    //rp.e should equal(Left("n must be > 1: 0"))
    assert(res == Left(ex))
  }

  test("map, two generators - Left 3") {
    val e1: E1 = Left(ex)
    val res = for {
      (a, s) <- e1
      (t, b) <- toE2(a, s)
      c = b + 1
    } yield (t, c)

    //rp.e should equal(Left("n must be > 1: 1"))
    assert(res == Left(ex))
  }

  type E3 = E[Option[Int]]

  test("foreach, Right(Some), avoiding pattern-matching") {
    val e3: E3 = Right(Some(1))
    var res = 0
    for {
      // Some(a) <- e3 
      // CAN now do the above - however, do the following, instead:
      opt <- e3
      a <- opt
      b = a + 1
    } res = b

    assert(res == 2)
  }

  // B => E[C]
  // Option[Int] => E[Int]
  def toEOfInt(opt: Option[Int]): E[Int] = opt match {
    case None => Left(new Exception("Option was None"))
    case Some(a) => Right(a)
  }

  test("map, Right(Some), avoiding pattern-matching, using toEOfInt") {
    val e3: E3 = Right(Some(1))
    val res = for {
      // Some(a) <- e3 
      // CAN now do the above - however, do the following, instead:
      opt <- e3          // Either[Exception, Option[Int]]
      a <- toEOfInt(opt) // Either[Exception, Int]
      b = a + 1
    } yield b

    assert(res == Right(2))
  }

  def toEOfInt2(opt: Option[Int]): E[Int] =
    opt.fold[E[Int]](Left(new Exception("Option was None")))(Right.apply)

  test("map, Right(Some), avoiding pattern-matching, using toEOfInt2") {
    val e3: E3 = Right(Some(1))
    val res = for {
      // Some(a) <- e3 
      // CAN now do the above - however, do the following, instead:
      opt <- e3           // Either[Exception, Option[Int]]
      a <- toEOfInt2(opt) // Either[Exception, Int]
      b = a + 1
    } yield b

    assert(res == Right(2))
  }

  test("map, Right(Some), avoiding pattern-matching, using in-situ toEOfInt2") {
    val e3: E3 = Right(Some(1))
    val res = for {
      // Some(a) <- e3 
      // CAN now do the above - however, do the following, instead:
      opt <- e3         // Either[Exception, Option[Int]]
      a <- opt.fold[E[Int]](Left(new Exception("Option was None")))(Right.apply)
                        // Either[Exception, Int]
      b = a + 1
    } yield b

    assert(res == Right(2))
  }

  import language.implicitConversions
  //implicit def f(convert: Left.Convert[Option[Int]]) = convert.b.toString
  implicit def f[B](convert: Left.Convert[B]) = convert.b.toString

  test("foreach, Right(Some), no def") {
    val either: Either[String, Option[Int]] = Right(Some(1))
    var res = 0
    for {
      Some(n) <- either
    } res = n

    assert(res == 1)
  }

  test("foreach, Right(None), no def") {
    val either: Either[String, Option[Int]] = Right(None)
    var res = 0
    for {
      Some(n) <- either
    } res = n

    assert(res == 0)
  }

  test("foreach, Left, no def") {
    val either: Either[String, Option[Int]] = Left("er")
    var res = 0
    for {
      Some(n) <- either
    } res = n

    assert(res == 0)
  }

  test("foreach, Right(Some), def") {
    val either: Either[String, Option[Int]] = Right(Some(1))
    var res = 0
    for {
      Some(n) <- either
      m = n + 1
    } res = m

    assert(res == 2)
  }

  test("foreach, Right(None), def") {
    val either: Either[String, Option[Int]] = Right(None)
    var res = 0
    for {
      Some(n) <- either
      m = n + 1
    } res = m

    assert(res == 0)
  }

  test("foreach, Left, def") {
    val either: Either[String, Option[Int]] = Left("er")
    var res = 0
    for {
      Some(n) <- either
      m = n + 1
    } res = m

    assert(res == 0)
  }

  test("map, Right(Some), no def") {
    val either: Either[String, Option[Int]] = Right(Some(1))
    val res = for {
      Some(n) <- either
    } yield n

    assert(res == Right(1))
  }

  test("map, Right(None), no def") {
    val either: Either[String, Option[Int]] = Right(None)
    val res = for {
      Some(n) <- either
    } yield n

    assert(res == Left("None"))
  }

  test("map, Left, no def") {
    val either: Either[String, Option[Int]] = Left("er")
    val res = for {
      Some(n) <- either
    } yield n

    assert(res == Left("er"))
  }

  test("map, Right(Some), def") {
    val either: Either[String, Option[Int]] = Right(Some(1))
    val res = for {
      Some(n) <- either
      m = n + 1
    } yield m

    assert(res == Right(2))
  }

  test("map, Right(None), def") {
    val either: Either[String, Option[Int]] = Right(None)
    val res = for {
      Some(n) <- either
      m = n + 1
    } yield m

    assert(res == Left("None"))
  }

  test("map, Left, def") {
    val either: Either[String, Option[Int]] = Left("er")
    val res = for {
      Some(n) <- either
      m = n + 1
    } yield m

    assert(res == Left("er"))
  }

  type E4 = Either[String, Either[String, Int]]

  //implicit def g(convert: Left.Convert[Either[String, Int]]) = convert.b.toString

  test("foreach, Right(Right), no def") {
    val either: E4 = Right(Right(1))
    var res = 0
    for {
      Right(n) <- either
    } res = n

    assert(res == 1)
  }

  test("foreach, Right(Left), no def") {
    val either: E4 = Right(Left("er"))
    var res = 0
    for {
      Right(n) <- either
    } res = n

    assert(res == 0)
  }

  test("foreach, Left, no def") {
    val either: E4 = Left("er")
    var res = 0
    for {
      Right(n) <- either
    } res = n

    assert(res == 0)
  }

  test("foreach, Right(Right), def") {
    val either: E4 = Right(Right(1))
    var res = 0
    for {
      Right(n) <- either
      m = n + 1
    } res = m

    assert(res == 2)
  }

  test("foreach, Right(Left), def") {
    val either: E4 = Right(Left("er"))
    var res = 0
    for {
      Right(n) <- either
      m = n + 1
    } res = m

    assert(res == 0)
  }

  test("foreach, Left, def") {
    val either: E4 = Left("er")
    var res = 0
    for {
      Right(n) <- either
      m = n + 1
    } res = m

    assert(res == 0)
  }

  test("map, Right(Right), no def") {
    val either: E4 = Right(Right(1))
    val res = for {
      Right(n) <- either
    } yield n

    assert(res == Right(1))
  }

  test("map, Right(Left), no def") {
    val either: E4 = Right(Left("er"))
    val res = for {
      Right(n) <- either
    } yield n

    assert(res == Left("Left(er)"))
  }

  test("map, Left, no def") {
    val either: E4 = Left("er")
    val res = for {
      Right(n) <- either
    } yield n

    assert(res == Left("er"))
  }

  test("map, Right(Right), def") {
    val either: E4 = Right(Right(1))
    val res = for {
      Right(n) <- either
      m = n + 1
    } yield m

    assert(res == Right(2))
  }

  test("map, Right(Left), def") {
    val either: E4 = Right(Left("er"))
    val res = for {
      Right(n) <- either
      m = n + 1
    } yield m

    assert(res == Left("Left(er)"))
  }

  test("map, Left, def") {
    val either: E4 = Left("er")
    val res = for {
      Right(n) <- either
      m = n + 1
    } yield m

    assert(res == Left("er"))
  }
}

