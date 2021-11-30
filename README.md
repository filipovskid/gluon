* Editing in progress. Currently there are only diagrams giving an overview of the different platform features.

# Gluon - Data Analytics Platform

About

## Table of Contents

- [Architecture](#architecture)
- [Gluon Server](#gluon-server)
  - [Resource Management](#resource-management)
  - [Task Handling](#task-handling)
- [Execution Environment](#execution-environment)
  - [Environment Driver](#environment-driver)
  - [Plugin Loading](#plugin-loading)
  - [Tasks](#tasks)
- [Executor](#executor)
  - [Python Executor](#python-executor)
  - [PySpark Executor](#pyspark-executor)
- [Notebook Service](#notebook-service)
- [Communication](#communication)

## Architecture

Gluon platform architecture overview.
<p align="center">
  <img alt="Gluon platform architecture overview" src="docs/images/gluon_architecture_v1.0.png" />
</p>

## Gluon Server

### Resource Management

Components which enable resource management.
<p align="center">
  <img alt="Resource management components" src="docs/images/resource-management.png" />
</p>

<!-- ### Task Handling -->

## Execution Environment

Placement of the execution environment with respect to other components.
<p align="center">
  <img alt="Placement of the environment with respect to other components" src="docs/images/gluon_environment_constructs.png" />
</p>

<!-- ### Environment Driver -->

### Plugin Loading

The figure below shows how executor isolation is achieved using multiple instances of Plugin ClassLoader.
<p align="center">
  <img alt="Achieving executor isolation using different instances of plugin class loader" src="docs/images/plugin_isolation.png" />
  <img alt="Plugin ClassLoader" src="docs/images/plugin_classloader.png" />
</p>

<!-- 
### Tasks

## Executor

### Python Executor

### PySpark Executor

## Notebook Service

## Communication
-->

