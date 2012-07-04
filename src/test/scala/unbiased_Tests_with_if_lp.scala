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
object unbiased_Tests_with_if_lp extends App {
  def test(s: String)(b: => Unit) { b }

  //implicit def f(convert: Right.Convert[Int]) = convert.a.toString
  implicit def f[A](convert: Right.Convert[A]) = convert.a.toString

  type E = Either[Int, String]

  test("foreach - Left, no def, true") {
    val either: E = Left(1)
    var res = 0
    for {
      a <- either.lp
      if a > 0
    } res = a

    assert(res == 1)
  }

  test("foreach - Left, no def, false") {
    val either: E = Left(1)
    var res = 0
    for {
      a <- either.lp
      if a < 0
    } res = a

    assert(res == 0)
  }

  test("foreach - Right, no def, true") {
    val either: E = Right("er")
    var res = 0
    for {
      a <- either.lp
      if a > 0
    } res = a

    assert(res == 0)
  }

  test("foreach - Right, no def, false") {
    val either: E = Right("er")
    var res = 0
    for {
      a <- either.lp
      if a < 0
    } res = a

    assert(res == 0)
  }

  //implicit def g(convert: Right.Convert[(Int, Int)]) = convert.a.toString

  test("foreach - Left, def, true") {
    val either: E = Left(1)
    var res = 0
    for {
      a <- either.lp
      b = a + 1
      if b > 0
    } res = b

    assert(res == 2)
  }

  test("foreach - Left, def, false") {
    val either: E = Left(1)
    var res = 0
    for {
      a <- either.lp
      b = a + 1
      if b < 0
    } res = b

    assert(res == 0)
  }

  test("foreach - Right, def, true") {
    val either: E = Right("er")
    var res = 0
    for {
      a <- either.lp
      b = a + 1
      if b > 0
    } res = b

    assert(res == 0)
  }

  test("foreach - Right, def, false") {
    val either: E = Right("er")
    var res = 0
    for {
      a <- either.lp
      b = a + 1
      if b < 0
    } res = b

    assert(res == 0)
  }

  test("map - Left, no def, true") {
    val either: E = Left(1)
    val res = for {
      a <- either.lp
      if a > 0
    } yield a

    assert(res.e == Left(1))
  }

  test("map - Left, no def, false") {
    val either: E = Left(1)
    val res = for {
      a <- either.lp
      if a < 0
    } yield a

    assert(res.e == Right("1"))
  }

  test("map - Left, def, true") {
    val either: E = Left(1)
    val res = for {
      a <- either.lp
      b = a + 1
      if b > 0
    } yield b

    assert(res.e == Left(2))
  }

  test("map - Left, def, false") {
    val either: E = Left(1)
    val res = for {
      a <- either.lp
      b = a + 1
      if b < 0
    } yield b

    assert(res.e == Right("(1,2)"))
  }

  test("map - Right, no def, true") {
    val either: E = Right("er")
    val res = for {
      a <- either.lp
      if a > 0
    } yield a

    assert(res.e == Right("er"))
  }

  test("map - Right, no def, false") {
    val either: E = Right("er")
    val res = for {
      a <- either.lp
      if a < 0
    } yield a

    assert(res.e == Right("er"))
  }

  test("map - Right, def, true") {
    val either: E = Right("er")
    val res = for {
      a <- either.lp
      b = a + 1
      if b > 0
    } yield b

    assert(res.e == Right("er"))
  }

  test("map - Right, def, false") {
    val either: E = Right("er")
    val res = for {
      a <- either.lp
      b = a + 1
      if b < 0
    } yield b

    assert(res.e == Right("er"))
  }

  def gt0(n: Int): E = if (n > 0) Left(n) else Right("n must be > 0: "+ n)
  def gt1(n: Int): E = if (n > 1) Left(n) else Right("n must be > 1: "+ n)

  test("foreach, two generators - Left 1, true") {
    var res = 0
    val a = 2
    for {
      b <- gt0(a).lp
      c <- gt1(b).lp
      if c > 0
    } res = c

    assert(res == 2)
  }

  test("foreach, two generators - Left 1, false") {
    var res = 0
    val a = 2
    for {
      b <- gt0(a).lp
      c <- gt1(b).lp
      if c < 0
    } res = c

    assert(res == 0)
  }

  test("foreach, two generators - Left 2, true") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).lp
      c = b + 1
      d <- gt1(c).lp
      if d > 0
    } res = d

    assert(res == 2)
  }

  test("foreach, two generators - Left 2, false") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).lp
      c = b + 1
      d <- gt1(c).lp
      if d < 0
    } res = d

    assert(res == 0)
  }

  test("foreach, two generators - Left 3, true") {
    var res = 0
    val a = 2
    for {
      b <- gt0(a).lp
      c <- gt1(b).lp
      d = c + 1
      if d > 0
    } res = d

    assert(res == 3)
  }

  test("foreach, two generators - Left 3, false") {
    var res = 0
    val a = 2
    for {
      b <- gt0(a).lp
      c <- gt1(b).lp
      d = c + 1
      if d < 0
    } res = d

    assert(res == 0)
  }

  test("foreach, two generators - Right 1, true") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).lp
      c <- gt1(b).lp
      if c > 0
    } res = c

    assert(res == 0)
  }

  test("foreach, two generators - Right 1, false") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).lp
      c <- gt1(b).lp
      if c < 0
    } res = c

    assert(res == 0)
  }

  test("foreach, two generators - Right 2, true") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).lp
      c = b - 1
      d <- gt1(c).lp
      if d > 0
    } res = d

    assert(res == 0)
  }

  test("foreach, two generators - Right 2, false") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).lp
      c = b - 1
      d <- gt1(c).lp
      if d < 0
    } res = d

    assert(res == 0)
  }

  test("foreach, two generators - Right 3, true") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).lp
      c <- gt1(b).lp
      d = c + 1
      if d > 0
    } res = d

    assert(res == 0)
  }

  test("foreach, two generators - Right 3, false") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a).lp
      c <- gt1(b).lp
      d = c + 1
      if d < 0
    } res = d

    assert(res == 0)
  }

  test("map, two generators - Left 1, true") {
    val a = 2
    val res = for {
      b <- gt0(a).lp
      c <- gt1(b).lp
      if c > 0
    } yield c

    assert(res.e == Left(a))
  }

  test("map, two generators - Left 1, false") {
    val a = 2
    val res = for {
      b <- gt0(a).lp
      c <- gt1(b).lp
      if c < 0
    } yield c

    assert(res.e == Right("2"))
  }

  test("map, two generators - Left 2, true") {
    val a = 1
    val res = for {
      b <- gt0(a).lp
      c = b + 1
      d <- gt1(c).lp
      if d > 0
    } yield d

    assert(res.e == Left(2))
  }

  test("map, two generators - Left 2, false") {
    val a = 1
    val res = for {
      b <- gt0(a).lp
      c = b + 1
      d <- gt1(c).lp
      if d < 0
    } yield d

    assert(res.e == Right("2"))
  }

  test("map, two generators - Left 3, true") {
    val a = 2
    val res = for {
      b <- gt0(a).lp
      c <- gt1(b).lp
      d = c + 1
      if d > 0
    } yield d

    assert(res.e == Left(3))
  }

  test("map, two generators - Left 3, false") {
    val a = 2
    val res = for {
      b <- gt0(a).lp
      c <- gt1(b).lp
      d = c + 1
      if d < 0
    } yield d

    assert(res.e == Right("(2,3)"))
  }

  test("map, two generators - Right 1, true") {
    val a = 1
    val res = for {
      b <- gt0(a).lp
      c <- gt1(b).lp
      if c > 0
    } yield c

    assert(res.e == Right("n must be > 1: 1"))
  }

  test("map, two generators - Right 1, false") {
    val a = 1
    val res = for {
      b <- gt0(a).lp
      c <- gt1(b).lp
      if c < 0
    } yield c

    assert(res.e == Right("n must be > 1: 1"))
  }

  test("map, two generators - Right 2, true") {
    val a = 1
    val res = for {
      b <- gt0(a).lp
      c = b - 1
      d <- gt1(c).lp
      if d > 0
    } yield d

    assert(res.e == Right("n must be > 1: 0"))
  }

  test("map, two generators - Right 2, false") {
    val a = 1
    val res = for {
      b <- gt0(a).lp
      c = b - 1
      d <- gt1(c).lp
      if d < 0
    } yield d

    assert(res.e == Right("n must be > 1: 0"))
  }

  test("map, two generators - Right 3, true") {
    val a = 1
    val res = for {
      b <- gt0(a).lp
      c <- gt1(b).lp
      d = c + 1
      if d > 0
    } yield d

    assert(res.e == Right("n must be > 1: 1"))
  }

  test("map, two generators - Right 3, false") {
    val a = 1
    val res = for {
      b <- gt0(a).lp
      c <- gt1(b).lp
      d = c + 1
      if d < 0
    } yield d

    assert(res.e == Right("n must be > 1: 1"))
  }
}

