package com.blazedb.svcc

import org.apache.spark.examples.mllib.MovieLensALS

/**
 * MovieLensALSTest
 *
 * Created by javadba@gmail.com on 10/10/14
 */
object MovieLensALSTest extends SparkSetupAndShutdown {


  val movieLensPath = System.getProperty("movie.lens.path")
  val args = Array(s" --rank 5 --numIterations 20 --lambda 1.0 --kryo $movieLensPath")

}
