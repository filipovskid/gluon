import findspark
findspark.init()

from pyspark import SparkContext, SparkConf
from pyspark.sql import SparkSession

spark_conf = SparkConf() \
    .setAppName("GluonNotebookApp") \
    .setMaster("local[*]")

spark = SparkSession.builder.config(conf=spark_conf).getOrCreate()
sc = spark.sparkContext
