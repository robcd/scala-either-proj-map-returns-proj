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
import org.scalatest.{FunSuite, matchers}
import matchers.ShouldMatchers
import scala.{Either => _, Left => _, Right => _}

class unbiased_Tests extends FunSuite with ShouldMatchers {
  test("foreach - Right") {
    val either: Either[String, Int] = Right(1)
    var res = 0
    for {
      a <- either.rp
      b = a + 1
    } res = b

    res should equal(2)
  }

  test("foreach - Left") {
    val either: Either[String, Int] = Left("er")
    var res = 0
    for {
      a <- either.rp
      b = a + 1
    } res = b

    res should equal(0)
  }

  test("map - Right") {
    val either: Either[String, Int] = Right(1)
    val rp = for {
      a <- either.rp
      b = a + 1
    } yield b

    rp.e should equal(Right(2))
    rp.get should equal(2)
    rp.getOrElse(0) should equal(2)
    rp.forall(_ == 1) should be(false)
    rp.forall(_ == 2) should be(true)
    rp.exists(_ == 1) should be(false)
    rp.exists(_ == 2) should be(true)
    rp.toSeq should equal(Seq(2))
    rp.toOption should equal(Some(2))
  }

  test("map - Left") {
    val either: Either[String, Int] = Left("er")
    val rp = for {
      a <- either.rp
      b = a + 1
    } yield b

    rp.e should equal(Left("er"))
    rp.getOrElse(0) should equal(0)
    val thrown = intercept[NoSuchElementException] {
      rp.get
    }
    thrown.getMessage should equal("Either.rp.value on Left")
    rp.forall(_ == 1) should be(true)
    rp.forall(_ == 2) should be(true)
    rp.exists(_ == 1) should be(false)
    rp.exists(_ == 2) should be(false)
    rp.toSeq should equal(Seq())
    rp.toOption should equal(None)
  }

  def gt0(n: Int): Either[String, Int] = if (n > 0) Right(n) else Left("n must be > 0: "+ n)
  def gt1(n: Int): Either[String, Int] = if (n > 1) Right(n) else Left("n must be > 1: "+ n)

  test("foreach, two generators - Right 1") {
    var res = 0
    val a = 2
    for {
      b <- gt0(a).rp
      c <- gt1(b).rp
    } res = c

    res should equal(2)
  }

  test("foreach, two generators - Right 2") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).rp
      c = b + 1
      d <- gt1(c).rp
    } res = d

    res should equal(2)
  }

  test("foreach, two generators - Right 3") {
    var res = 0
    val a = 2
    for {
      b <- gt0(a).rp
      c <- gt1(b).rp
      d = c + 1
    } res = d

    res should equal(3)
  }

  test("foreach, two generators - Left 1") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).rp
      c <- gt1(b).rp
    } res = c

    res should equal(0)
  }

  test("foreach, two generators - Left 2") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).rp
      c = b - 1
      d <- gt1(c).rp
    } res = d

    res should equal(0)
  }

  test("foreach, two generators - Left 3") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).rp
      c <- gt1(b).rp
      d = c + 1
    } res = d

    res should equal(0)
  }

  test("map, two generators - Right 1") {
    val a = 2
    val rp = for {
      b <- gt0(a).rp
      c <- gt1(b).rp
    } yield c

    rp.e should equal(Right(a))
  }

  test("map, two generators - Right 2") {
    val a = 1
    val rp = for {
      b <- gt0(a).rp
      c = b + 1
      d <- gt1(c).rp
    } yield d

    rp.e should equal(Right(2))
  }

  test("map, two generators - Right 3") {
    val a = 2
    val rp = for {
      b <- gt0(a).rp
      c <- gt1(b).rp
      d = c + 1
    } yield d

    rp.e should equal(Right(3))
  }

  test("map, two generators - Left 1") {
    val a = 1
    val rp = for {
      b <- gt0(a).rp
      c <- gt1(b).rp
    } yield c

    rp.e should equal(Left("n must be > 1: 1"))
  }

  test("map, two generators - Left 2") {
    val a = 1
    val rp = for {
      b <- gt0(a).rp
      c = b - 1
      d <- gt1(c).rp
    } yield d

    rp.e should equal(Left("n must be > 1: 0"))
  }

  test("map, two generators - Left 3") {
    val a = 1
    val rp = for {
      b <- gt0(a).rp
      c <- gt1(b).rp
      d = c + 1
    } yield d

    rp.e should equal(Left("n must be > 1: 1"))
  }
}

