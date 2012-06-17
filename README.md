branch: add_right-bias
----------------------

[This](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias/src/main/scala/Either.scala) version of `scala.Either` retains its capability for unbiased usage
(which is now fixed), via the new `lp` and `rp` methods, and gains the
capability for right-biased usage.

(It is therefore, at the same time, both unbiased and right-biased!)

Example code
------------

[`for` comprehension tests (unbiased usage)](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias/src/test/scala/unbiased_Tests.scala)  
[`for` comprehension tests (right-biased usage)](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias-biased/src/test/scala/rightbiased_Tests.scala)  
[tests involving `Option` (unbiased usage)](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias/src/test/scala/TestsInvolvingOption.scala)  
[tests involving `Option` (right-biased usage)](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias/src/test/scala/TestsInvolvingOption.scala)  
[Nightclub tests](/robcd/scala-either-proj-map-returns-proj/blob/add_right-bias/src/test/scala/SaturdayNight.scala)
