package com.blazedb.svcc

import org.apache.spark.mllib.linalg
import org.apache.spark.mllib.stat.test.ChiSqTestResult
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.{Matrix, Vectors}

/**
 * KMeansTest
 *
 * Created by javadba@gmail.com on 10/10/14
 */
object CustomChiSqTest extends SparkSetupAndShutdown {

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

  /*
   * Pearon's independence test on the input contingency matrix.
   * TODO: optimize for SparseMatrix when it becomes supported.
   */
  def modifiedChiSquaredMatrix(counts: Matrix, methodName:String = PEARSON.name): ChiSqTestResult = {
    val method = methodFromString(methodName)
    val numRows = counts.numRows
    val numCols = counts.numCols

    // get row and column sums
    val colSums = new Array[Double](numCols)
    val rowSums = new Array[Double](numRows)
    val colMajorArr = counts.toArray
    var i = 0
    while (i < colMajorArr.size) {
      val elem = colMajorArr(i)
      if (elem < 0.0) {
        throw new IllegalArgumentException("Contingency table cannot contain negative entries.")
      }
      colSums(i / numRows) += elem
      rowSums(i % numRows) += elem
      i += 1
    }
    val total = colSums.sum

    // second pass to collect statistic
    var statistic = 0.0
    var j = 0
    while (j < colMajorArr.size) {
      val col = j / numRows
      val colSum = colSums(col)
      if (colSum == 0.0) {
        throw new IllegalArgumentException("Chi-squared statistic undefined for input matrix due to"
          + s"0 sum in column [$col].")
      }
      val row = j % numRows
      val rowSum = rowSums(row)
      if (rowSum == 0.0) {
        throw new IllegalArgumentException("Chi-squared statistic undefined for input matrix due to"
          + s"0 sum in row [$row].")
      }
      val expected = colSum * rowSum / total
      statistic += method.chiSqFunc(colMajorArr(j), expected)
      j += 1
    }
    val df = (numCols - 1) * (numRows - 1)
    val pValue = chiSquareComplemented(df, statistic)
    new ChiSqTestResult(pValue, df, statistic, methodName, NullHypothesis.independence.toString)
  }

  def main(args: Array[String]) {
    testKMeansParallel
  }

}
