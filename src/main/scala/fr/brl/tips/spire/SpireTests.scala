package fr.brl.tips.spire

import spire.math.Interval.openLower
import spire.math.extras.interval.IntervalSeq
import spire.math.extras.interval.IntervalSeq.{above, point}

object SpireTests extends App {

  val intervals = Map[String, IntervalSeq[Double]](
    "Minor" -> openLower(0.0, 4.5),
    "Moderate" -> openLower(4.5, 9.0),
    "Major" -> openLower(9.0, 20.0),
    "Severe" -> above(20.0)
  )

  // all intervals
  println(intervals)

  // finds the severity given a point
  println(intervals.find(_._2.intersects(point(-200))).map(_._1)) // None
  println(intervals.find(_._2.intersects(point(2))).map(_._1)) // minor
  println(intervals.find(_._2.intersects(point(4.5))).map(_._1)) // minor
  println(intervals.find(_._2.intersects(point(4.55))).map(_._1)) // moderate
  println(intervals.find(_._2.intersects(point(Double.PositiveInfinity))).map(_._1)) // severe

  // check if contiguous and disjoint by doing an XOR on all intervals
  println("--- contiguous and disjoint ? ---")
  println(intervals.values.foldLeft(IntervalSeq.empty[Double])(_ ^ _).isContiguous)
}
