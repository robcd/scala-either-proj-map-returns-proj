The project description about says it all.

However, note that support for `if` in `for` comprehensions has now
been removed, on the grounds that an empty result isn't appropriate -
either it's `Left` or it's `Right`!

The modified version is
[here](/robcd/scala-either-proj-map-returns-proj/blob/lp_rp/src/main/scala/Either.scala).

Example code
------------

[`for` comprehension tests](/robcd/scala-either-proj-map-returns-proj/blob/lp_rp/src/test/scala/Tests.scala)  
[tests involving `Option`](/robcd/scala-either-proj-map-returns-proj/blob/lp_rp/src/test/scala/TestsInvolvingOption.scala)  
[`SaturdayNight`](/robcd/scala-either-proj-map-returns-proj/blob/lp_rp/src/test/scala/SaturdayNight.scala)
