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

class TestChecksReturningProj extends FunSuite with ShouldMatchers {
  type RP[L, R] = Either.RightProj[L, R]
  def RP = Either.RightProj

  def gt0(n: Int): RP[String, Int] =
    if (n > 0) RP(Right(n)) else RP(Left("n must be > 0: "+ n))
  def gt1(n: Int): RP[String, Int] =
    if (n > 1) RP(Right(n)) else RP(Left("n must be > 1: "+ n))

  test("two right generators with foreach - succeeds 1") {
    var res = 0
    val a = 2
    for {
      b <- gt0(a)
      c <- gt1(b)
    } res = c
    res should equal(2)
  }

  test("two right generators with foreach - fails") {
    var res = 0
    val a = 1
    for {
      b <- gt0(a)
      c <- gt1(b)
    } res = c
    res should equal(0)
  }

  test("two right generators with map - succeeds 1") {
    val a = 2
    val right = for {
      b <- gt0(a)
      c <- gt1(b)
    } yield c
    right.e should equal(Right(a))
  }

  test("two right generators with map - fails") {
    val a = 1
    val right = for {
      b <- gt0(a)
      c <- gt1(b)
    } yield c
    right.e should equal(Left("n must be > 1: 1"))
    right.getOrElse(0) should equal(0)
  }
}
