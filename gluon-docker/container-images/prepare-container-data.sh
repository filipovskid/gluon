#!/bin/bash

[ ! -e Dockerfile ] && echo "Must be in Dockerfile's directory"

dockerfile_dir="$( cd -- "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"
gluon_dir=${dockerfile_dir}/../..

# Build and prepare gluon executor driver

executor_dir=${gluon_dir}/gluon-executor
driver_output_dir=${dockerfile_dir}/driver
driver_jar=gluon-environment-driver--0.0.1-SNAPSHOT.jar

cd $executor_dir
mvn package install
cp ${executor_dir}/target/gluon-executor-0.0.1-SNAPSHOT-jar-with-dependencies.jar ${driver_output_dir}/${driver_jar}

# Build and prepare gluon python executor

python_exec_dir=${gluon_dir}/gluon-python
python_output_dir=${dockerfile_dir}/executors/python
python_exec_jar=gluon-python-executor--0.0.1-SNAPSHOT.jar
python_conf=${dockerfile_dir}/conf/python.conf

mkdir -p $python_output_dir
cd $python_exec_dir
mvn package
cp ${python_exec_dir}/target/gluon-python-0.0.1-SNAPSHOT-jar-with-dependencies.jar ${python_output_dir}/${python_exec_jar}
cp $python_conf ${python_output_dir}/executor.conf

# Build and prepare gluon pyspark executor

spark_exec_dir=${gluon_dir}/gluon-spark
spark_output_dir=${dockerfile_dir}/executors/spark
spark_exec_jar=gluon-spark-executor--0.0.1-SNAPSHOT.jar
spark_conf=${dockerfile_dir}/conf/spark.conf

mkdir -p $spark_output_dir
cd $spark_exec_dir
mvn package
cp ${spark_exec_dir}/target/gluon-spark-0.0.1-SNAPSHOT.jar ${spark_output_dir}/${spark_exec_jar}
cp $spark_conf ${spark_output_dir}/executor.conf