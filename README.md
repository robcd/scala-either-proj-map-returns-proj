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

[`for` comprehension tests (unbiased usage)](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10/src/test/scala/unbiased_Tests.scala)  
[`for` comprehension tests (right-biased usage)](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10-biased/src/test/scala/rightbiased_Tests.scala)  
[tests involving `Option` (unbiased usage)](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10/src/test/scala/TestsInvolvingOption.scala)  
[tests involving `Option` (right-biased usage)](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10/src/test/scala/TestsInvolvingOption.scala)  
[`for` comprehension tests involving pattern-matching (unbiased usage)](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10/src/test/scala/unbiased_PatternMatchingTests.scala)  
[`for` comprehension tests involving pattern-matching (right-biased usage)](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10/src/test/scala/unbiased_PatternMatchingTests.scala)  
[Nightclub tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias_2-10/src/test/scala/SaturdayNight.scala)
