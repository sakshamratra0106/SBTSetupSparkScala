package com.spark.example

import scala.util.Try
import org.apache.spark.sql.functions._
import org.apache.spark.sql.SparkSession

/**
 * Test IO to wasb
 */

object App extends App {

  System.setProperty("hadoop.home.dir", "C:\\winutils")

  val spark = SparkSession.builder()
    .master("local[1]")
    .appName("SparkByExample")
    .getOrCreate()

  val get_last = udf((xs: Seq[String]) => Try(xs.last.replace("_", " ")
    .replace("/", "")
    .replace(".csv", "")).toOption)


  val df = spark
    .read
    .option("header", "true")
    .option("delimiter", ",")
    .option("inferSchema", "true")
    .csv("D:\\Switch\\Jobs Applied for\\PaloAlto\\data\\england_councils\\*.csv")
    .withColumn("FileName", get_last(split(input_file_name, "england_councils")))

  df.printSchema()


  df.show(1)
  df.write
    .format("csv")
    .mode("overwrite")
    .save("D:\\Switch\\Jobs Applied for\\PaloAlto\\data\\england_councils\\output")


  println("This seems to be working")
}
