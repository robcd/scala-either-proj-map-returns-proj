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

class TestsInvolvingOption extends FunSuite with ShouldMatchers {
  type E = Either[Exception, Seq[String]]

  test("foreach, Seq non-empty, head non-empty") {
    def read: E = Right(Seq("1st", "2nd"))
    var res = ""
    for {
      ss <- read.right
      h <- ss.headOption // okay that this is now an Option, provided we don't use yield;
      if h != "" // can still use 'if' following an Option (which has a filter/withFilter)
    } res = h

    res should equal("1st")
  }

  // test("foreach, Seq non-empty, head non-empty 2") {
  //   def read: E = Right(Seq("1st", "2nd"))
  //   var res = ""
  //   for {
  //     ss <- read.right
  //     //         ^
  //     // value withFilter is not a member of Either.RightProjection[Exception,Seq[String]]
  //     if !ss.empty
  //     h = ss.head
  //   } res = h

  //   res should equal("1st")
  // }

  test("foreach, Seq non-empty, head empty") {
    def read: E = Right(Seq("", "2nd"))
    var res = ""
    for {
      ss <- read.right
      h <- ss.headOption
      if h != ""
    } res = h

    res should equal("")
  }

  test("foreach, Seq empty") {
    def read: E = Right(Seq())
    var res = ""
    for {
      ss <- read.right
      h <- ss.headOption
      if h != ""
    } res = h

    res should equal("")
  }

  // test("map, Seq non-empty, head non-empty") {

  //   def read: E = Right(Seq("1st", "2nd"))
  //   val res = for {
  //     ss <- read.right
  //     h <- ss.headOption
  //   //  ^
  //   // type mismatch;
  //   //  found   : Option[String]
  //   //  required: Either.RightProjection[?,?]
  //     if h != ""
  //   } yield h

  //   res should equal("1st")
  // }



  test("map, Seq non-empty, head non-empty") {
    def read: E = Right(Seq("1st", "2nd"))
    val res = for {
      ss <- read.right.toOption
      h <- ss.headOption
      if h != ""
    } yield h

    res should equal(Some("1st"))
  }

  test("map, Seq non-empty, head empty") {
    def read: E = Right(Seq("", "2nd"))
    val res = for {
      ss <- read.right.toOption
      h <- ss.headOption
      if h != ""
    } yield h

    res should equal(None)
  }

  test("map, Seq empty") {
    def read: E = Right(Seq())
    val res = for {
      ss <- read.right.toOption
      h <- ss.headOption
      if h != ""
    } yield h

    res should equal(None)
  }

  test("foreach, Left") {
    def read: E = Left(new Exception("er"))
    var res = ""
    for {
      ss <- read.right
      h <- ss.headOption
      if h != ""
    } res = h

    res should equal("")
  }

  test("map, Left") {
    def read: E = Left(new Exception("er"))
    val res = for {
      ss <- read.right.toOption
      h <- ss.headOption
      if h != ""
    } yield h

    res should equal(None)
  }

  type E2 = Either.RightProjection[Exception, Option[String]]

  // to convert an Either.RightProjection[L, R] into an Either.RightProjection[L, S], you can
  // write an R => Either.RightProjection[L, S]
  def read2(ss: Seq[String]): E2 = {
    val res = for {
      h <- ss.headOption
      if h != ""
    } yield h
    Right(res).right
  }
  // this may then be passed to Either.RightProjection's flatMap, and therefore used in
  // for-comprehensions

  test("map, Seq non-empty, head non-empty 2") {
    def read: E = Right(Seq("1st", "2nd"))
    val  res: E2 = for {
      ss <- read.right // Either.RightProjection[L,    Seq[String]]
      h <- read2(ss)   // Either.RightProjection[L, Option[String]]
    } yield h

    res.e should equal(Right(Some("1st")))
  }

  test("map, Seq non-empty, head empty 2") {
    def read: E = Right(Seq("", "2nd"))
    val res = for {
      ss <- read.right
      h <- read2(ss)
    } yield h

    res.e should equal(Right(None))
  }

  test("map, Seq empty 2") {
    def read: E = Right(Seq())
    val res = for {
      ss <- read.right
      h <- read2(ss)
    } yield h

    res.e should equal(Right(None))
  }

  test("map, Left 2") {
    val ex = new Exception("er")
    def read: E = Left(ex)
    val res = for {
      ss <- read.right
      h <- read2(ss)
    } yield h

    res.e should equal(Left(ex))
  }
  // the previous four tests show how to convert from an R to an S without losing your L

  // however, this my be done more concisely using map:

  // Seq[String]) => Option[Sring]
  def toHead(ss: Seq[String]) = for {
    h <- ss.headOption
    if h != ""
  } yield h

  test("map, Seq non-empty, head non-empty 3") {
    def read: E = Right(Seq("1st", "2nd"))
    val  res: E2 = read.right map toHead

    res.e should equal(Right(Some("1st")))
  }

  test("map, Seq non-empty, head empty 3") {
    def read: E = Right(Seq("", "2nd"))
    val res = read.right map toHead

    res.e should equal(Right(None))
  }

  test("map, Seq empty 3") {
    def read: E = Right(Seq())
    val res = read.right map toHead

    res.e should equal(Right(None))
  }

  test("map, Left 3") {
    val ex = new Exception("er")
    def read: E = Left(ex)
    val res = read.right map toHead

    res.e should equal(Left(ex))
  }
}

