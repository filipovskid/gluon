#!/usr/bin/env bash

CLASS_TO_RUN=com.filipovski.gluon.executor.environment.remote.ExecutionEnvironmentDriver


# There are several possibilities to solve the environment driver startup
# 1) Use environment variables which will be set by resource manager before container startup
# 2) Use docker COMMAND during container startup to set ENTRYPOINT script parameters

CMD=(
  java -cp
  /jars/gluon-executor-0.0.1-SNAPSHOT-jar-with-dependencies.jar
  ${CLASS_TO_RUN}
  ${@}
)

"${CMD[@]}"
