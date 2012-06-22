// All the examples I gave before do not work, as it is logically impossible for them to work.
// If you want test cases, here they are as methods (unbiasedAlternative versions use the
// existing Scala Either):

// Rules: if it's a number, map it to a string
// If it's not a number, the string should be Left
// If it is a number and not in the map, don't care as long as it's not Right
def cantLiftToOption = {
  // These three lines are always the same
  val s = "42"
  val m = Map(42 -> "Meaning of life")
  val e = try { Right(s.toInt) } catch { case _: Exception => Left(s) }

  // This is actually trying to for-comprehend something
  for (n <- e; what <- m.get(n)) yield what
}

def workingAlternativeOp = {
  val s = "42"
  val m = Map(42 -> "Meaning of life")
  val e = try { Right(s.toInt) } catch { case _: Exception => Left(s) }

  for (n <- e; what <- m.get(n).map(x => Right(x)).getOrElse(Left(s))) yield what
}

def unbiasedAlternativeOp = {
  val s = "42"
  val m = Map(42 -> "Meaning of life")
  val e = try { Right(s.toInt) } catch { case _: Exception => Left(s) }

  e.fold(l => Left(l), r => m.get(r).map(x => Right(x)).getOrElse(Left(s)))
}

def cantIf = {
  val s = "42"
  val m = Map(42 -> "Meaning of life")
  val e = try { Right(s.toInt) catch { case _: Exception => Left(s) }

  for (n <- e if m.contains(n)) yield n
}

def workingAlternativeIf = {
  val s = "42"
  val m = Map(42 -> "Meaning of life")
  val e = try { Right(s.toInt) catch { case _: Exception => Left(s) }
  
  e.flatMap(n => if (m contains n) Right(n) else Left(s))
}

def unbiasedAlternativeIf = {
  val s = "42"
  val m = Map(42 -> "Meaning of life")
  val e = try { Right(s.toInt) catch { case _: Exception => Left(s) }

  e.fold(l => Left(l), r => if (m contains r) Right(n) else Left(s))
}

// There simply isn't any way to handle these since there is no empty Either.  And note that the
// unbiased alternatives _already_ handle this approximately as well as the right-biased version
// can.  So in these cases, there is no benefit of biasing.
//
// In contrast, with a third (empty) option, you can (using my Has class):

def canLiftToOption = {
  val s = "42"
  val m = Map(42 -> "Meaning of life")
  val e = try { Yes(s.toInt) } catch { case _: Exception => Plea(s) }

  for (n <- e; what <- m.get(n)) yield what
}

def canIf = {
  val s = "42"
  val m = Map(42 -> "Meaning of life")
  val e = try { Yes(s.toInt) } catch { case _: Exception => Plea(s) }

  for (n <- e if (m contains n)) yield n
}

// which will return No if s is not in the map, Plea(s) on a non-integer string, and Yes(...) if
// it succeeds.

// Anyway, I'm tired of arguing this point.  Enough people want a right-biased either that it
// makes sense to do it.  If I want to write an unbiased union container, I am always free to do
// so (I can call it Toggle with Up/Down), and I have already written something that does what
// is useful and maximally for-comprehension-feature-compatible in the biased case.
