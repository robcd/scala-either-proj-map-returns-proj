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
object unbiased_PatternMatchingTests extends App {
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
      (a, s) <- e1.rp
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
      (a, s) <- e1.rp
      b = a + 1
      t = s + suffix
    } res = (b, t)

    //res should equal(0)
    assert(res == (0, ""))
  }

  test("map - Right") {
    val e1: E1 = Right(pair)
    val rp = for {
      (a, s) <- e1.rp
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

    assert(rp.e == Right(2, word))
  }

  test("map - Left") {
    val e1: E1 = Left(ex)
    val rp = for {
      (a, s) <- e1.rp
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

    assert(rp.e == Left(ex))
  }

  type String1st = (String, Int)

  type E2 = E[String1st]

  def toE2(n: Int, s: String): E2 = Right((s, n))

  test("foreach, two generators - Right 1") {
    val e1: E1 = Right(pair)
    var res = ("", 0)
    for {
      (a, s) <- e1.rp
      (t, b) <- toE2(a, s).rp
    } res = (t, b)

    //res should equal(2)
    assert(res == (prefix, 1))
  }

  test("foreach, two generators - Right 2") {
    val e1: E1 = Right(pair)
    var res = ("", 0)
    for {
      (a, s) <- e1.rp
      b = a + 1
      (t, c) <- toE2(b, s).rp
    } res = (t, c)

    //res should equal(2)
    assert(res == (prefix, 2))
  }

  test("foreach, two generators - Right 3") {
    val e1: E1 = Right(pair)
    var res = ("", 0)
    for {
      (a, s) <- e1.rp
      (t, b) <- toE2(a, s).rp
      c = b + 1
    } res = (t, c)

    //res should equal(3)
    assert(res == (prefix, 2))
  }

  test("foreach, two generators - Left 1") {
    val e1: E1 = Left(ex)
    var res = ("", 0)
    for {
      (a, s) <- e1.rp
      (t, b) <- toE2(a, s).rp
    } res = (t, b)

    //res should equal(0)
    assert(res == ("", 0))
  }

  test("foreach, two generators - Left 2") {
    val e1: E1 = Left(ex)
    var res = ("", 0)
    for {
      (a, s) <- e1.rp
      b = a + 1
      (t, c) <- toE2(b, s).rp
    } res = (t, c)

    //res should equal(0)
    assert(res == ("", 0))
  }

  test("foreach, two generators - Left 3") {
    val e1: E1 = Left(ex)
    var res = ("", 0)
    for {
      (a, s) <- e1.rp
      (t, b) <- toE2(a, s).rp
      c = b + 1
    } res = (t, c)

    //res should equal(0)
    assert(res == ("", 0))
  }

  test("map, two generators - Right 1") {
    val e1: E1 = Right(pair)
    val rp = for {
      (a, s) <- e1.rp
      (t, b) <- toE2(a, s).rp
    } yield (t, b)

    //rp.e should equal(Right(a))
    assert(rp.e == Right((prefix, 1)))
  }

  test("map, two generators - Right 2") {
    val e1: E1 = Right(pair)
    val rp = for {
      (a, s) <- e1.rp
      b = a + 1
      (t, c) <- toE2(b, s).rp
    } yield (t, c)

    //rp.e should equal(Right(2))
    assert(rp.e == Right((prefix, 2)))
  }

  test("map, two generators - Right 3") {
    val e1: E1 = Right(pair)
    val rp = for {
      (a, s) <- e1.rp
      (t, b) <- toE2(a, s).rp
      c = b + 1
    } yield (t, c)

    //rp.e should equal(Right(3))
    assert(rp.e == Right((prefix, 2)))
  }

  test("map, two generators - Left 1") {
    val e1: E1 = Left(ex)
    val rp = for {
      (a, s) <- e1.rp
      (t, b) <- toE2(a, s).rp
    } yield (t, b)

    //rp.e should equal(Left("n must be > 1: 1"))
    assert(rp.e == Left(ex))
  }

  test("map, two generators - Left 2") {
    val e1: E1 = Left(ex)
    val rp = for {
      (a, s) <- e1.rp
      b = a + 1
      (t, c) <- toE2(b, s).rp
    } yield (t, c)

    //rp.e should equal(Left("n must be > 1: 0"))
    assert(rp.e == Left(ex))
  }

  test("map, two generators - Left 3") {
    val e1: E1 = Left(ex)
    val rp = for {
      (a, s) <- e1.rp
      (t, b) <- toE2(a, s).rp
      c = b + 1
    } yield (t, c)

    //rp.e should equal(Left("n must be > 1: 1"))
    assert(rp.e == Left(ex))
  }
}

