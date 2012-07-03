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
import language.implicitConversions

//class rightbiased_Tests extends FunSuite with ShouldMatchers {
object unbiased_Tests_with_if_rp extends App {
  def test(s: String)(b: => Unit) { b }

  implicit def f(convert: Left.Convert[Int]) = convert.b.toString

  type E = Either[String, Int]

  test("foreach - Right, no def, true") {
    val either: E = Right(1)
    var res = 0
    for {
      a <- either.rp
      if a > 0
    } res = a

    assert(res == 1)
  }

  test("foreach - Right, no def, false") {
    val either: E = Right(1)
    var res = 0
    for {
      a <- either.rp
      if a < 0
    } res = a

    assert(res == 0)
  }

  test("foreach - Left, no def, true") {
    val either: E = Left("er")
    var res = 0
    for {
      a <- either.rp
      if a > 0
    } res = a

    assert(res == 0)
  }

  test("foreach - Left, no def, false") {
    val either: E = Left("er")
    var res = 0
    for {
      a <- either.rp
      if a < 0
    } res = a

    assert(res == 0)
  }

  implicit def g(convert: Left.Convert[(Int, Int)]) = convert.b.toString

  test("foreach - Right, def, true") {
    val either: E = Right(1)
    var res = 0
    for {
      a <- either.rp
      b = a + 1
      if b > 0
    } res = b

    assert(res == 2)
  }

  test("foreach - Right, def, false") {
    val either: E = Right(1)
    var res = 0
    for {
      a <- either.rp
      b = a + 1
      if b < 0
    } res = b

    assert(res == 0)
  }

  test("foreach - Left, def, true") {
    val either: E = Left("er")
    var res = 0
    for {
      a <- either.rp
      b = a + 1
      if b > 0
    } res = b

    assert(res == 0)
  }

  test("foreach - Left, def, false") {
    val either: E = Left("er")
    var res = 0
    for {
      a <- either.rp
      b = a + 1
      if b < 0
    } res = b

    assert(res == 0)
  }

  test("map - Right, no def, true") {
    val either: E = Right(1)
    val res = for {
      a <- either.rp
      if a > 0
    } yield a

    assert(res.e == Right(1))
  }

  test("map - Right, no def, false") {
    val either: E = Right(1)
    val res = for {
      a <- either.rp
      if a < 0
    } yield a

    assert(res.e == Left("1"))
  }

  test("map - Right, def, true") {
    val either: E = Right(1)
    val res = for {
      a <- either.rp
      b = a + 1
      if b > 0
    } yield b

    assert(res.e == Right(2))
  }

  test("map - Right, def, false") {
    val either: E = Right(1)
    val res = for {
      a <- either.rp
      b = a + 1
      if b < 0
    } yield b

    assert(res.e == Left("(1,2)"))
  }

  test("map - Left, no def, true") {
    val either: E = Left("er")
    val res = for {
      a <- either.rp
      if a > 0
    } yield a

    assert(res.e == Left("er"))
  }

  test("map - Left, no def, false") {
    val either: E = Left("er")
    val res = for {
      a <- either.rp
      if a < 0
    } yield a

    assert(res.e == Left("er"))
  }

  test("map - Left, def, true") {
    val either: E = Left("er")
    val res = for {
      a <- either.rp
      b = a + 1
      if b > 0
    } yield b

    assert(res.e == Left("er"))
  }

  test("map - Left, def, false") {
    val either: E = Left("er")
    val res = for {
      a <- either.rp
      b = a + 1
      if b < 0
    } yield b

    assert(res.e == Left("er"))
  }

  def gt0(n: Int): E = if (n > 0) Right(n) else Left("n must be > 0: "+ n)
  def gt1(n: Int): E = if (n > 1) Right(n) else Left("n must be > 1: "+ n)

  test("foreach, two generators - Right 1, true") {
    var res = 0
    val a = 2
    for {
      b <- gt0(a).rp
      c <- gt1(b).rp
      if c > 0
    } res = c

    assert(res == 2)
  }

  test("foreach, two generators - Right 1, false") {
    var res = 0
    val a = 2
    for {
      b <- gt0(a).rp
      c <- gt1(b).rp
      if c < 0
    } res = c

    assert(res == 0)
  }

  test("foreach, two generators - Right 2, true") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).rp
      c = b + 1
      d <- gt1(c).rp
      if d > 0
    } res = d

    assert(res == 2)
  }

  test("foreach, two generators - Right 2, false") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).rp
      c = b + 1
      d <- gt1(c).rp
      if d < 0
    } res = d

    assert(res == 0)
  }

  test("foreach, two generators - Right 3, true") {
    var res = 0
    val a = 2
    for {
      b <- gt0(a).rp
      c <- gt1(b).rp
      d = c + 1
      if d > 0
    } res = d

    assert(res == 3)
  }

  test("foreach, two generators - Right 3, false") {
    var res = 0
    val a = 2
    for {
      b <- gt0(a).rp
      c <- gt1(b).rp
      d = c + 1
      if d < 0
    } res = d

    assert(res == 0)
  }

  test("foreach, two generators - Left 1, true") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).rp
      c <- gt1(b).rp
      if c > 0
    } res = c

    assert(res == 0)
  }

  test("foreach, two generators - Left 1, false") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).rp
      c <- gt1(b).rp
      if c < 0
    } res = c

    assert(res == 0)
  }

  test("foreach, two generators - Left 2, true") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).rp
      c = b - 1
      d <- gt1(c).rp
      if d > 0
    } res = d

    assert(res == 0)
  }

  test("foreach, two generators - Left 2, false") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).rp
      c = b - 1
      d <- gt1(c).rp
      if d < 0
    } res = d

    assert(res == 0)
  }

  test("foreach, two generators - Left 3, true") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).rp
      c <- gt1(b).rp
      d = c + 1
      if d > 0
    } res = d

    assert(res == 0)
  }

  test("foreach, two generators - Left 3, false") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).rp
      c <- gt1(b).rp
      d = c + 1
      if d < 0
    } res = d

    assert(res == 0)
  }

  test("map, two generators - Right 1, true") {
    val a = 2
    val res = for {
      b <- gt0(a).rp
      c <- gt1(b).rp
      if c > 0
    } yield c

    assert(res.e == Right(a))
  }

  test("map, two generators - Right 1, false") {
    val a = 2
    val res = for {
      b <- gt0(a).rp
      c <- gt1(b).rp
      if c < 0
    } yield c

    assert(res.e == Left("2"))
  }

  test("map, two generators - Right 2, true") {
    val a = 1
    val res = for {
      b <- gt0(a).rp
      c = b + 1
      d <- gt1(c).rp
      if d > 0
    } yield d

    assert(res.e == Right(2))
  }

  test("map, two generators - Right 2, false") {
    val a = 1
    val res = for {
      b <- gt0(a).rp
      c = b + 1
      d <- gt1(c).rp
      if d < 0
    } yield d

    assert(res.e == Left("2"))
  }

  test("map, two generators - Right 3, true") {
    val a = 2
    val res = for {
      b <- gt0(a).rp
      c <- gt1(b).rp
      d = c + 1
      if d > 0
    } yield d

    assert(res.e == Right(3))
  }

  test("map, two generators - Right 3, false") {
    val a = 2
    val res = for {
      b <- gt0(a).rp
      c <- gt1(b).rp
      d = c + 1
      if d < 0
    } yield d

    assert(res.e == Left("(2,3)"))
  }

  test("map, two generators - Left 1, true") {
    val a = 1
    val res = for {
      b <- gt0(a).rp
      c <- gt1(b).rp
      if c > 0
    } yield c

    assert(res.e == Left("n must be > 1: 1"))
  }

  test("map, two generators - Left 1, false") {
    val a = 1
    val res = for {
      b <- gt0(a).rp
      c <- gt1(b).rp
      if c < 0
    } yield c

    assert(res.e == Left("n must be > 1: 1"))
  }

  test("map, two generators - Left 2, true") {
    val a = 1
    val res = for {
      b <- gt0(a).rp
      c = b - 1
      d <- gt1(c).rp
      if d > 0
    } yield d

    assert(res.e == Left("n must be > 1: 0"))
  }

  test("map, two generators - Left 2, false") {
    val a = 1
    val res = for {
      b <- gt0(a).rp
      c = b - 1
      d <- gt1(c).rp
      if d < 0
    } yield d

    assert(res.e == Left("n must be > 1: 0"))
  }

  test("map, two generators - Left 3, true") {
    val a = 1
    val res = for {
      b <- gt0(a).rp
      c <- gt1(b).rp
      d = c + 1
      if d > 0
    } yield d

    assert(res.e == Left("n must be > 1: 1"))
  }

  test("map, two generators - Left 3, false") {
    val a = 1
    val res = for {
      b <- gt0(a).rp
      c <- gt1(b).rp
      d = c + 1
      if d < 0
    } yield d

    assert(res.e == Left("n must be > 1: 1"))
  }
}

