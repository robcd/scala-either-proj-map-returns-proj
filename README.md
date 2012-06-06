The project description about says it all.

However, note that support for `if` in `for` comprehensions has now
been removed, on the grounds that an empty result isn't appropriate -
either it's `Left` or it's `Right`!

The modified version is
[here](scala-either-proj-map-returns-proj/blob/master/src/main/scala/Either.scala).

Test code is
[here](scala-either-proj-map-returns-proj/blob/master/src/test/scala/Tests.scala),
and there's also a test app demonstrating a definiton in a
`for` comprehension, called
[`SaturdayNight`](scala-either-proj-map-returns-proj/blob/master/src/test/scala/SaturdayNight.scala).
