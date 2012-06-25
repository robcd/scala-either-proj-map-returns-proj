branch: add_right-bias_2-10_withFilter
--------------------------------------

[This](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10_withFilter/src/main/scala/Either.scala)
version of `scala.Either` takes the one in the
[add_right-bias_2-10](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10)
branch and adds a `withFilter` method to it, in order to support `if` and
pattern-matching in right-biased usage `for` comprehensions.

The `withFilter` method is peculiar in that it takes a second,
implicit, parameter list, containing a conversion, `bToA`, that is used
to obtain a `Left` result whenever the predicate is false.

It works, at least for the right-biased `for` comprehension tests
involving [if]() and [pattern-matching]() so far carried out, provided
the appropriate implicit conversions are supplied (as indicated by the compiler).

Example code
------------

N.B. All 'tests' have been converted to test apps, since a Scala 2.10
ScalaTest is not yet available.

`for` comprehension [tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10_withFilter/src/test/scala/unbiased_Tests.scala) (unbiased usage)  
`for` comprehension [tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10_withFilter/src/test/scala/rightbiased_Tests.scala) (right-biased usage)  
`for` comprehension [tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10_withFilter/src/test/scala/rightbiased_Tests_with_if.scala) involving `if` (right-biased usage)  
[tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10_withFilter/src/test/scala/unbiased_TestsInvolvingOption.scala) involving `Option` (unbiased usage)  
[tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10_withFilter/src/test/scala/rightbiased_TestsInvolvingOption.scala) involving `Option` (right-biased usage)  
`for` comprehension [tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10_withFilter/src/test/scala/unbiased_PatternMatchingTests.scala) involving pattern-matching (unbiased usage)  
`for` comprehension [tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10_withFilter/src/test/scala/rightbiased_PatternMatchingTests.scala) involving pattern-matching (right-biased usage)  
`for` comprehension [tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10_withFilter/src/test/scala/RexsTests.scala) demonstrating work-arounds for absence of None (right-biased usage)  
Nightclub [tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10_withFilter/src/test/scala/nightclub_tests.scala)
