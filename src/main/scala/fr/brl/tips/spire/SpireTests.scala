package fr.brl.tips.spire

import spire.compat.ordering
import spire.math.{Above, All, Below, Bounded, Empty, Point}
import spire.math.Interval.{atOrAbove, openUpper}
import spire.math.extras.interval.IntervalSeq
import spire.math.extras.interval.IntervalSeq.{apply, below, point}

object SpireTests extends App {

  val intervals = Map[String, IntervalSeq[Double]](
    "Norisk" -> below(0),
    "Minor" -> openUpper(0.0, 4.5),
    "Moderate" -> openUpper(4.5, 9.0),
    "Major" -> openUpper(9.0, 20.0),
    "Severe" -> atOrAbove(20.0)
  )

  // all intervals
  println(intervals)

  // finds the severity given a point
  println(intervals.find(_._2.intersects(point(-200))).map(_._1)) // norisk
  println(intervals.find(_._2.intersects(point(0))).map(_._1)) // norisk
  println(intervals.find(_._2.intersects(point(2))).map(_._1)) // minor
  println(intervals.find(_._2.intersects(point(4.5))).map(_._1)) // moderate
  println(intervals.find(_._2.intersects(point(4.55))).map(_._1)) // moderate
  println(intervals.find(_._2.intersects(point(20))).map(_._1)) // severe
  println(intervals.find(_._2.intersects(point(Double.PositiveInfinity))).map(_._1)) // severe

  // check if contiguous and disjoint by doing an XOR on all intervals
  println("--- contiguous and disjoint ? ---")
  println(intervals.values.foldLeft(IntervalSeq.empty[Double])(_ ^ _).isContiguous)

  for (elem <- intervals) {
    println(elem._1)
    println(elem._2.edges)
    println(elem._2.edges.max >= elem._2.edges.min)
    println("---")
  }

  println(intervals.values.foldLeft(IntervalSeq.empty[Double])(_ | _))
  println(intervals.values.foldLeft(IntervalSeq.empty[Double])(_ | _).hull match {
    case All() => true
    case _ => false
  })

}
