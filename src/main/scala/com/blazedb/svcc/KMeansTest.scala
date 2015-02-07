package com.blazedb.svcc

import org.apache.spark.mllib.linalg
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors

/**
 * KMeansTest
 *
 * Created by javadba@gmail.com on 10/10/14
 */
object KMeansTest extends SparkSetupAndShutdown {

  import org.apache.spark.mllib.clustering.KMeans.{K_MEANS_PARALLEL, RANDOM}

  def computeSse(vector: linalg.Vector, vector1: linalg.Vector) = {
    vector.toArray.zip(vector1.toArray).foldLeft(0.0) { case (sse, (v1, v2)) =>
      sse += Math.pow(v1 - v2, 2)
      sse
    }
  }

  def withinEpsilon(target: Double, epsilon: Double): Boolean = {
    Math.sqrt(target) <= epsilon
  }

  def testKMeansParallel() = {

    val data = sc.parallelize(Array(
      Vectors.dense(1.0, 2.0, 6.0),
      Vectors.dense(1.0, 3.0, 0.0),
      Vectors.dense(1.0, 4.0, 6.0)
    ))

    val center = Vectors.dense(1.0, 3.0, 4.0)

    val model = KMeans.train(
      data, k = 1, maxIterations = 1, runs = 1, initializationMode = K_MEANS_PARALLEL)
    val sse = computeSse(model.clusterCenters.head,center)
    assert(withinEpsilon(sse,1E-5), s"Error not in range: $sse")
  }

  def main(args: Array[String]) {
    testKMeansParallel
  }

}
