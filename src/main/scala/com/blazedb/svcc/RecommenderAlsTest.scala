package com.blazedb.svcc

import org.apache.spark.mllib.recommendation.ALS.BlockStats
import org.apache.spark.mllib.recommendation.{ALS, Rating}

/**
 * RecommenderAlsTest
 *
 * Created by javadba@gmail.com on 10/10/14
 */
object RecommenderAlsTest extends SparkSetupAndShutdown {

  def testAls() = {
    import org.apache.spark.mllib.recommendation
    println("analyze one user block and one product block")
    val localRatings = Seq(
      Rating(0, 100, 1.0),
      Rating(0, 101, 2.0),
      Rating(0, 102, 3.0),
      Rating(1, 102, 4.0),
      Rating(2, 103, 5.0))
    val ratings = sc.makeRDD(localRatings, 2)
    val stats = ALS.analyzeBlocks(ratings, 1, 1)
    assert(stats.size == 2)
    assert(stats(0) == BlockStats("user", 0, 3, 5, 4, 3))
    assert(stats(1) == BlockStats("product", 0, 4, 5, 3, 4))
  }

}
