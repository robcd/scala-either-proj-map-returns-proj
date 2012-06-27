branch: add_right-bias_2-10_withFilter
--------------------------------------

[This](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10_withFilter/src/main/scala/Either.scala)
version of `scala.Either` takes the one in the
[add_right-bias_2-10](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10)
branch and adds a `withFilter` method to it (as well as to `LeftProj`
and `RightProj`), in order to support `if` and pattern-matching in
`for` comprehensions.

The `withFilter` method is peculiar in that it takes a second,
implicit, parameter list, containing a conversion, `bToA` (or `aToB`), that is used
to obtain a `Left` (or `Right`) result whenever the predicate is false.

So all that is required in order to use `if` or pattern-matching in
`for` comprehensions involving `Either` (or one of its projections) is
an implicit conversion such this:

    implicit def f(convert: Left.Convert) = convert.any.toString

You would be prompted by the compiler to supply the above if using
`if` or pattern-matching in a `for` comprehension involving an
`Either[String, ...]` or `Either.RightProj[String, ...]`, in order
that a `Left` instance may be created, were the `Boolean` expression
in question to be `false` or the pattern not match.

Here, `convert` (as in the noun) will contain the contents of the
`Right` instance that was encountered, which will be converted to a `String` in order to
create an instance of a `Left[String, ...]`.

In the case of an `Either.LeftProj`, an implicit conversion from a
`Right.Convert` would be required.

Please see the various test suites that mention `if` and pattern-matching for examples.

Example code
------------

N.B. All 'tests' have been converted to test apps, since a Scala 2.10
ScalaTest is not yet available.

`for` comprehension [tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10_withFilter/src/test/scala/unbiased_Tests.scala) (unbiased usage)  
`for` comprehension [tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10_withFilter/src/test/scala/rightbiased_Tests.scala) (right-biased usage)  
`for` comprehension [tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10_withFilter/src/test/scala/unbiased_Tests_with_if_lp.scala) involving `if` (unbiased usage, `lp`)  
`for` comprehension [tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10_withFilter/src/test/scala/unbiased_Tests_with_if_rp.scala) involving `if` (unbiased usage, `rp`)  
`for` comprehension [tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10_withFilter/src/test/scala/rightbiased_Tests_with_if.scala) involving `if` (right-biased usage)  
[tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10_withFilter/src/test/scala/unbiased_TestsInvolvingOption.scala) involving `Option` (unbiased usage)  
[tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10_withFilter/src/test/scala/rightbiased_TestsInvolvingOption.scala) involving `Option` (right-biased usage)  
`for` comprehension [tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10_withFilter/src/test/scala/unbiased_PatternMatchingTests_lp.scala) involving pattern-matching (unbiased usage, `lp`)  
`for` comprehension [tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10_withFilter/src/test/scala/unbiased_PatternMatchingTests_rp.scala) involving pattern-matching (unbiased usage, `rp`)  
`for` comprehension [tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10_withFilter/src/test/scala/rightbiased_PatternMatchingTests.scala) involving pattern-matching (right-biased usage)  
`for` comprehension [tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10_withFilter/src/test/scala/RexsTests.scala) demonstrating work-arounds for absence of None (right-biased usage)  
Nightclub [tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10_withFilter/src/test/scala/nightclub_tests.scala)
