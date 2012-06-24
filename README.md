branch: add_right-bias_2-10
---------------------------

[This](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10/src/main/scala/Either.scala) version of `scala.Either` retains its capability for unbiased usage
(which is now fixed), via the new `lp` and `rp` methods, and gains the
capability for right-biased usage.

(It is therefore, at the same time, both unbiased and right-biased!)

The example code now includes `for` comprehension tests involving
pattern-matching, which only works correctly from Scala 2.10
onwards. (Hence, this branch is using Scala 2.10.0-M4 and SBT 0.11.3.)

Example code
------------

N.B. All 'tests' have been converted to test apps, since a Scala 2.10
ScalaTest is not yet available.

`for` comprehension [tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10/src/test/scala/unbiased_Tests.scala) (unbiased usage)  
`for` comprehension [tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10/src/test/scala/rightbiased_Tests.scala) (right-biased usage)  
[tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10/src/test/scala/unbiased_TestsInvolvingOption.scala) involving `Option` (unbiased usage)  
[tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10/src/test/scala/rightbiased_TestsInvolvingOption.scala) involving `Option` (right-biased usage)  
`for` comprehension [tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10/src/test/scala/unbiased_PatternMatchingTests.scala) involving pattern-matching (unbiased usage)  
`for` comprehension [tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10/src/test/scala/rightbiased_PatternMatchingTests.scala) involving pattern-matching (right-biased usage)  
`for` comprehension [tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10/src/test/scala/RexsTests.scala) demonstrating work-arounds for absence of None (right-biased usage)  
Nightclub [tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10/src/test/scala/nightclub_tests.scala)
