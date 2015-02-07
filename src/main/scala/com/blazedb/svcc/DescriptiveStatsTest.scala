package com.blazedb.svcc

import org.apache.spark.mllib.linalg
import org.apache.spark.mllib.stat.{MultivariateStatisticalSummary, MultivariateOnlineSummarizer}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.linalg.distributed.RowMatrix
import org.apache.spark.SparkContext
import org.apache.spark.mllib.random.RandomRDDs._


/**
 * DesriptiveStatsTest
 *
 * Created by javadba@gmail.com on 10/10/14
 */
object DescriptiveStatsTest {

  import org.apache.spark.mllib.clustering.KMeans.{K_MEANS_PARALLEL, RANDOM}

  @transient var sc: SparkContext = _

  val conf = new SparkConf().setMaster("local").setAppName("svccKMeans||")
  sc = new SparkContext(conf)

  sys.addShutdownHook {
    if (sc != null) {
      sc.stop()
    }
  }

  def computeSse(vector: linalg.Vector, vector1: linalg.Vector) = {
    vector.toArray.zip(vector1.toArray).foldLeft(0.0) { case (sse, (v1, v2)) =>
      sse += Math.pow(v1 - v2, 2)
      sse
    }
  }

  def withinEpsilon(target: Double, epsilon: Double): Boolean = {
    Math.sqrt(target) <= epsilon
  }

  // Create a RowMatrix from an RDD[Vector].
  val mat: RowMatrix = new RowMatrix(rows)

  // Get its size.
  val m = mat.numRows()
  val n = mat.numCols()


  import java.util.Random

  val xrand = new Random(37)
  val yrand = new Random(41)
  val zrand = new Random(43)
  val SignalScale = 5.0
  val NoiseScale = 8.0
  val npoints = 50000
  val XScale = SignalScale / npoints
  val YScale = 2.0 * SignalScale / npoints
  val ZScale = 3.0 * SignalScale / npoints
  val randRdd = sc.parallelize({
    for (p <- Range(0, npoints))
    yield
      (NoiseScale * xrand.nextGaussian + XScale * p,
        NoiseScale * yrand.nextGaussian + YScale * p,
        NoiseScale * zrand.nextGaussian + ZScale * p)
  }.toSeq, 2)

  def computeViaMllib() = {
    private def computeSummary(data: RDD[Vector]): MultivariateStatisticalSummary =
    {
      data.treeAggregate(new MultivariateOnlineSummarizer)(
        (aggregator, data) => aggregator.add(data),
        (aggregator1, aggregator2) => aggregator1.merge(aggregator2))
    }

  }

  def main(args: Array[String]) {
  }

}

- createGaussianAffinityMatrix
createNormalizedAffinityMatrix
- createConcentricCirclesData
- gaussianDist
-createSparseEdgesRdd
