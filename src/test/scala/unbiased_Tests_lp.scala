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

//class unbiased_Tests extends FunSuite with ShouldMatchers {
object unbiased_Tests_lp extends App {
  def test(s: String)(b: => Unit) { b }

  type E = Either[Int, String]

  test("foreach - Left") {
    val either: E = Left(1)
    var res = 0
    for {
      a <- either.lp
      b = a + 1
    } res = b

    //res should equal(2)
    assert(res == 2)
  }

  test("foreach - Right") {
    val either: E = Right("er")
    var res = 0
    for {
      a <- either.lp
      b = a + 1
    } res = b

    //res should equal(0)
    assert(res == 0)
  }

  test("map - Left") {
    val either: E = Left(1)
    val lp = for {
      a <- either.lp
      b = a + 1
    } yield b

    // rp.e should equal(Right(2))
    // rp.get should equal(2)
    // rp.getOrElse(0) should equal(2)
    // rp.forall(_ == 1) should be(false)
    // rp.forall(_ == 2) should be(true)
    // rp.exists(_ == 1) should be(false)
    // rp.exists(_ == 2) should be(true)
    // rp.toSeq should equal(Seq(2))
    // rp.toOption should equal(Some(2))

    assert(lp.e == Left(2))
    assert(lp.get == 2)
    assert(lp.getOrElse(0) == 2)
    assert(lp.forall(_ == 1) == false)
    assert(lp.forall(_ == 2) == true)
    assert(lp.exists(_ == 1) == false)
    assert(lp.exists(_ == 2) == true)
    assert(lp.toSeq == Seq(2))
    assert(lp.toOption == Some(2))
  }

  test("map - Right") {
    val either: E = Right("er")
    val lp = for {
      a <- either.lp
      b = a + 1
    } yield b

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

    assert(lp.e == Right("er"))
    assert(lp.getOrElse(0) == 0)
    // val thrown = intercept[NoSuchElementException] {
    //   rp.get
    // }
    // thrown.getMessage should equal("Either.rp.value on Left")
    assert(lp.forall(_ == 1) == true)
    assert(lp.forall(_ == 2) == true)
    assert(lp.exists(_ == 1) == false)
    assert(lp.exists(_ == 2) == false)
    assert(lp.toSeq == Seq())
    assert(lp.toOption == None)
  }

  def gt0(n: Int): E = if (n > 0) Left(n) else Right("n must be > 0: "+ n)
  def gt1(n: Int): E = if (n > 1) Left(n) else Right("n must be > 1: "+ n)

  test("foreach, two generators - Left 1") {
    var res = 0
    val a = 2
    for {
      b <- gt0(a).lp
      c <- gt1(b).lp
    } res = c

    //res should equal(2)
    assert(res == 2)
  }

  test("foreach, two generators - Left 2") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).lp
      c = b + 1
      d <- gt1(c).lp
    } res = d

    //res should equal(2)
    assert(res == 2)
  }

  test("foreach, two generators - Left 3") {
    var res = 0
    val a = 2
    for {
      b <- gt0(a).lp
      c <- gt1(b).lp
      d = c + 1
    } res = d

    //res should equal(3)
    assert(res == 3)
  }

  test("foreach, two generators - Right 1") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).lp
      c <- gt1(b).lp
    } res = c

    //res should equal(0)
    assert(res == 0)
  }

  test("foreach, two generators - Right 2") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).lp
      c = b - 1
      d <- gt1(c).lp
    } res = d

    //res should equal(0)
    assert(res == 0)
  }

  test("foreach, two generators - Right 3") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).lp
      c <- gt1(b).lp
      d = c + 1
    } res = d

    //res should equal(0)
    assert(res == 0)
  }

  test("map, two generators - Left 1") {
    val a = 2
    val lp = for {
      b <- gt0(a).lp
      c <- gt1(b).lp
    } yield c

    //rp.e should equal(Right(a))
    assert(lp.e == Left(a))
  }

  test("map, two generators - Left 2") {
    val a = 1
    val lp = for {
      b <- gt0(a).lp
      c = b + 1
      d <- gt1(c).lp
    } yield d

    //rp.e should equal(Right(2))
    assert(lp.e == Left(2))
  }

  test("map, two generators - Left 3") {
    val a = 2
    val lp = for {
      b <- gt0(a).lp
      c <- gt1(b).lp
      d = c + 1
    } yield d

    //rp.e should equal(Right(3))
    assert(lp.e == Left(3))
  }

  test("map, two generators - Right 1") {
    val a = 1
    val lp = for {
      b <- gt0(a).lp
      c <- gt1(b).lp
    } yield c

    //rp.e should equal(Left("n must be > 1: 1"))
    assert(lp.e == Right("n must be > 1: 1"))
  }

  test("map, two generators - Right 2") {
    val a = 1
    val lp = for {
      b <- gt0(a).lp
      c = b - 1
      d <- gt1(c).lp
    } yield d

    //rp.e should equal(Left("n must be > 1: 0"))
    assert(lp.e == Right("n must be > 1: 0"))
  }

  test("map, two generators - Right 3") {
    val a = 1
    val lp = for {
      b <- gt0(a).lp
      c <- gt1(b).lp
      d = c + 1
    } yield d

    //rp.e should equal(Left("n must be > 1: 1"))
    assert(lp.e == Right("n must be > 1: 1"))
  }

  def toStringIfGt0(n: Int): E = if (n > 0) Right(n.toString) else Left(n)

  test("foreach, Left, Right") {
    val e: E = Left(1)
    var res = (0, "")
    for {
      a <- e.lp
      s <- toStringIfGt0(a).rp
    } res = (a, s)

    assert(res == (1, "1"))
  }

  test("foreach, Left, Left") {
    val e: E = Left(0)
    var res = (0, "")
    for {
      a <- e.lp
      s <- toStringIfGt0(a).rp
    } res = (a, s)

    assert(res == (0, ""))
  }

  test("foreach, Right, Right") {
    val e: E = Right("er")
    var res = (0, "")
    for {
      a <- e.lp
      s <- toStringIfGt0(a).rp
    } res = (a, s)

    assert(res == (0, ""))
  }
}

