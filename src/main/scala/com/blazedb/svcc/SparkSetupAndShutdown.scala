package com.blazedb.svcc

import org.apache.spark.{SparkConf, SparkContext}

/**
 * SparkSetupAndShutdown
 *
 * Created by javadba@gmail.com on 10/10/14
 */
trait SparkSetupAndShutdown {

  @transient var sc: SparkContext = _

  val conf = new SparkConf().setMaster("local").setAppName("svccKMeans||")
  sc = new SparkContext(conf)

  sys.addShutdownHook {
    if (sc != null) {
      sc.stop()
    }
  }


}
