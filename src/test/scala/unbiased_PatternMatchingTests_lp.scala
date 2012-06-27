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
object unbiased_PatternMatchingTests_lp extends App {
  def test(s: String)(b: => Unit) { b }

  type E[A] = Either[A, Exception]
  type Pair = (Int, String)
  type E1 = E[Pair]
  val ex = new Exception("oops")
  val prefix = "wo"; val suffix = "rd"; val word = prefix + suffix
  val pair = (1, prefix)

  test("foreach - Left") {
    val e1: E1 = Left(pair)
    var res = (0, "")
    for {
      (a, s) <- e1.lp
      b = a + 1
      t = s + suffix
    } res = (b, t)

    assert(res == (2, word))
  }

  test("foreach - Right") {
    val e1: E1 = Right(ex)
    var res = (0, "")
    for {
      (a, s) <- e1.lp
      b = a + 1
      t = s + suffix
    } res = (b, t)

    assert(res == (0, ""))
  }

  test("map - Left") {
    val e1: E1 = Left(pair)
    val rp = for {
      (a, s) <- e1.lp
      b = a + 1
      t = s + suffix
    } yield (b, t)

    assert(rp.e == Left(2, word))
  }

  test("map - Right") {
    val e1: E1 = Right(ex)
    val rp = for {
      (a, s) <- e1.lp
      b = a + 1
      t = s + suffix
    } yield (b, t)

    assert(rp.e == Right(ex))
  }

  type String1st = (String, Int)

  type E2 = E[String1st]

  def toE2(n: Int, s: String): E2 = Left((s, n))

  test("foreach, two generators - Left 1") {
    val e1: E1 = Left(pair)
    var res = ("", 0)
    for {
      (a, s) <- e1.lp
      (t, b) <- toE2(a, s).lp
    } res = (t, b)

    assert(res == (prefix, 1))
  }

  test("foreach, two generators - Left 2") {
    val e1: E1 = Left(pair)
    var res = ("", 0)
    for {
      (a, s) <- e1.lp
      b = a + 1
      (t, c) <- toE2(b, s).lp
    } res = (t, c)

    assert(res == (prefix, 2))
  }

  test("foreach, two generators - Left 3") {
    val e1: E1 = Left(pair)
    var res = ("", 0)
    for {
      (a, s) <- e1.lp
      (t, b) <- toE2(a, s).lp
      c = b + 1
    } res = (t, c)

    assert(res == (prefix, 2))
  }

  test("foreach, two generators - Right 1") {
    val e1: E1 = Right(ex)
    var res = ("", 0)
    for {
      (a, s) <- e1.lp
      (t, b) <- toE2(a, s).lp
    } res = (t, b)

    assert(res == ("", 0))
  }

  test("foreach, two generators - Right 2") {
    val e1: E1 = Right(ex)
    var res = ("", 0)
    for {
      (a, s) <- e1.lp
      b = a + 1
      (t, c) <- toE2(b, s).lp
    } res = (t, c)

    assert(res == ("", 0))
  }

  test("foreach, two generators - Right 3") {
    val e1: E1 = Right(ex)
    var res = ("", 0)
    for {
      (a, s) <- e1.lp
      (t, b) <- toE2(a, s).lp
      c = b + 1
    } res = (t, c)

    assert(res == ("", 0))
  }

  test("map, two generators - Left 1") {
    val e1: E1 = Left(pair)
    val rp = for {
      (a, s) <- e1.lp
      (t, b) <- toE2(a, s).lp
    } yield (t, b)

    assert(rp.e == Left((prefix, 1)))
  }

  test("map, two generators - Left 2") {
    val e1: E1 = Left(pair)
    val rp = for {
      (a, s) <- e1.lp
      b = a + 1
      (t, c) <- toE2(b, s).lp
    } yield (t, c)

    assert(rp.e == Left((prefix, 2)))
  }

  test("map, two generators - Left 3") {
    val e1: E1 = Left(pair)
    val rp = for {
      (a, s) <- e1.lp
      (t, b) <- toE2(a, s).lp
      c = b + 1
    } yield (t, c)

    assert(rp.e == Left((prefix, 2)))
  }

  test("map, two generators - Right 1") {
    val e1: E1 = Right(ex)
    val rp = for {
      (a, s) <- e1.lp
      (t, b) <- toE2(a, s).lp
    } yield (t, b)

    assert(rp.e == Right(ex))
  }

  test("map, two generators - Right 2") {
    val e1: E1 = Right(ex)
    val rp = for {
      (a, s) <- e1.lp
      b = a + 1
      (t, c) <- toE2(b, s).lp
    } yield (t, c)

    assert(rp.e == Right(ex))
  }

  test("map, two generators - Right 3") {
    val e1: E1 = Right(ex)
    val rp = for {
      (a, s) <- e1.lp
      (t, b) <- toE2(a, s).lp
      c = b + 1
    } yield (t, c)

    assert(rp.e == Right(ex))
  }

  import language.implicitConversions
  implicit def f(convert: Right.Convert) = convert.any.toString

  type E3 = Either[Option[Int], String]

  test("foreach, Left(Some), no def") {
    val either: E3 = Left(Some(1))
    var res = 0
    for {
      Some(n) <- either.lp
    } res = n

    assert(res == 1)
  }

  test("foreach, Left(None), no def") {
    val either: E3 = Left(None)
    var res = 0
    for {
      Some(n) <- either.lp
    } res = n

    assert(res == 0)
  }

  test("foreach, Right, no def") {
    val either: E3 = Right("er")
    var res = 0
    for {
      Some(n) <- either.lp
    } res = n

    assert(res == 0)
  }

  test("foreach, Left(Some), def") {
    val either: E3 = Left(Some(1))
    var res = 0
    for {
      Some(n) <- either.lp
      m = n + 1
    } res = m

    assert(res == 2)
  }

  test("foreach, Left(None), def") {
    val either: E3 = Left(None)
    var res = 0
    for {
      Some(n) <- either.lp
      m = n + 1
    } res = m

    assert(res == 0)
  }

  test("foreach, Right, def") {
    val either: E3 = Right("er")
    var res = 0
    for {
      Some(n) <- either.lp
      m = n + 1
    } res = m

    assert(res == 0)
  }

  test("map, Left(Some), no def") {
    val either: E3 = Left(Some(1))
    val res = for {
      Some(n) <- either.lp
    } yield n

    assert(res.e == Left(1))
  }

  test("map, Left(None), no def") {
    val either: E3 = Left(None)
    val res = for {
      Some(n) <- either.lp
    } yield n

    assert(res.e == Right("None"))
  }

  test("map, Right, no def") {
    val either: E3 = Right("er")
    val res = for {
      Some(n) <- either.lp
    } yield n

    assert(res.e == Right("er"))
  }

  test("map, Left(Some), def") {
    val either: E3 = Left(Some(1))
    val res = for {
      Some(n) <- either.lp
      m = n + 1
    } yield m

    assert(res.e == Left(2))
  }

  test("map, Left(None), def") {
    val either: E3 = Left(None)
    val res = for {
      Some(n) <- either.lp
      m = n + 1
    } yield m

    assert(res.e == Right("None"))
  }

  test("map, Right, def") {
    val either: E3 = Right("er")
    val res = for {
      Some(n) <- either.lp
      m = n + 1
    } yield m

    assert(res.e == Right("er"))
  }

  type E4 = Either[Either[String, Int], String]

  test("foreach, Left(Right), no def") {
    val either: E4 = Left(Right(1))
    var res = 0
    for {
      Right(n) <- either.lp
    } res = n

    assert(res == 1)
  }

  test("foreach, Left(Left), no def") {
    val either: E4 = Left(Left("er"))
    var res = 0
    for {
      Right(n) <- either.lp
    } res = n

    assert(res == 0)
  }

  test("foreach, Right, no def") {
    val either: E4 = Right("er")
    var res = 0
    for {
      Right(n) <- either.lp
    } res = n

    assert(res == 0)
  }

  test("foreach, Left(Right), def") {
    val either: E4 = Left(Right(1))
    var res = 0
    for {
      Right(n) <- either.lp
      m = n + 1
    } res = m

    assert(res == 2)
  }

  test("foreach, Left(Left), def") {
    val either: E4 = Left(Left("er"))
    var res = 0
    for {
      Right(n) <- either.lp
      m = n + 1
    } res = m

    assert(res == 0)
  }

  test("foreach, Right, def") {
    val either: E4 = Right("er")
    var res = 0
    for {
      Right(n) <- either.lp
      m = n + 1
    } res = m

    assert(res == 0)
  }

  test("map, Left(Right), no def") {
    val either: E4 = Left(Right(1))
    val res = for {
      Right(n) <- either.lp
    } yield n

    assert(res.e == Left(1))
  }

  test("map, Left(Left), no def") {
    val either: E4 = Left(Left("er"))
    val res = for {
      Right(n) <- either.lp
    } yield n

    assert(res.e == Right("Left(er)"))
  }

  test("map, Right, no def") {
    val either: E4 = Right("er")
    val res = for {
      Right(n) <- either.lp
    } yield n

    assert(res.e == Right("er"))
  }

  test("map, Left(Right), def") {
    val either: E4 = Left(Right(1))
    val res = for {
      Right(n) <- either.lp
      m = n + 1
    } yield m

    assert(res.e == Left(2))
  }

  test("map, Left(Left), def") {
    val either: E4 = Left(Left("er"))
    val res = for {
      Right(n) <- either.lp
      m = n + 1
    } yield m

    assert(res.e == Right("Left(er)"))
  }

  test("map, Right, def") {
    val either: E4 = Right("er")
    val res = for {
      Right(n) <- either.lp
      m = n + 1
    } yield m

    assert(res.e == Right("er"))
  }
}
