
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.linalg.distributed.RowMatrix

val rows: RDD[Vector] = ... // an RDD of local vectors
// Create a RowMatrix from an RDD[Vector].
val mat: RowMatrix = new RowMatrix(rows)

// Get its size.
val m = mat.numRows()
val n = mat.numCols()

import org.apache.spark.SparkContext
import org.apache.spark.mllib.random.RandomRDDs._


import java.util.Random
val xrand = new Random(37)
val yrand = new Random(41)
val zrand = new Random(43)
val SignalScale = 5.0
val NoiseScale = 8.0
val npoints = 50000
val XScale = SignalScale/npoints
val YScale = 2.0*SignalScale/npoints
val ZScale = 3.0*SignalScale/npoints
val randRdd = sc.parallelize ({
  for (p <- Range(0,npoints))
  yield
 (NoiseScale*xrand.nextGaussian + XScale * p,
  NoiseScale*yrand.nextGaussian + YScale * p,
  NoiseScale*zrand.nextGaussian + ZScale * p)
}.toSeq,2)

SAMPLE CODE


import org.apache.spark.mllib.linalg.{Vector, Vectors}
val rowVec = rows.map(x => Vectors.dense(x)) // RDD[org.apache.spark.mllib.linalg.Vector]

import org.apache.spark.mllib.linalg.distributed._
val countMatrix = new RowMatrix(rowVec)
val stats = countMatrix.computeColumnSummaryStatistics()
val meanvec = stats.mean



val rmat = new RowMatrix(rows)

And then make a local DenseMatrix.

val localMat = Matrices.dense(m, n, mat)

Then multiply them.

rmat.multiply(localMat)

10/19 examples code

 import scala.collection.mutable

import com.esotericsoftware.kryo.Kryo
import org.apache.log4j.{Level, Logger}
import scopt.OptionParser

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.SparkContext._
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.serializer.{KryoSerializer, KryoRegistrator}

  class ALSRegistrator extends KryoRegistrator {
    override def registerClasses(kryo: Kryo) {
      kryo.register(classOf[Rating])
      kryo.register(classOf[mutable.BitSet])
    }
  }

  case class Params(
      input: String = null,
      kryo: Boolean = false,
      numIterations: Int = 20,
      lambda: Double = 1.0,
      rank: Int = 10,
      numUserBlocks: Int = -1,
      numProductBlocks: Int = -1,
      implicitPrefs: Boolean = false)

case class MovieFiles(ufile: String, mfile: String, rfile: String)

    val conf = new SparkConf().setAppName(s"MovieLensALS with $params")
    if (params.kryo) {
      conf.set("spark.serializer", classOf[KryoSerializer].getName)
        .set("spark.kryo.registrator", classOf[ALSRegistrator].getName)
        .set("spark.kryoserializer.buffer.mb", "8")
    }
    val sc = new SparkContext(conf)

    Logger.getRootLogger.setLevel(Level.WARN)

  val mFiles = {
    val mdir = "/shared/svcc/ml-1m";
    MovieFiles(s"$mdir/users.dat", s"$mdir/movies.dat", s"$mdir/ratings.dat")
  }


    val ratings = sc.textFile(params.input).map { line =>
      val fields = line.split("::")
      if (params.implicitPrefs) {
        Rating(fields(0).toInt, fields(1).toInt, fields(2).toDouble - 2.5)
      } else {
        Rating(fields(0).toInt, fields(1).toInt, fields(2).toDouble)
      }
    }.cache()
