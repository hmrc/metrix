[DEPRECATED]
=
*This repository has been replaced by [hmrc-mongo-metrix-play-28](https://github.com/hmrc/hmrc-mongo/tree/main/hmrc-mongo-metrix-play-28)*

# metrix

[ ![Download](https://api.bintray.com/packages/hmrc/releases/metrix/images/download.svg) ](https://bintray.com/hmrc/releases/metrix/_latestVersion)

## Version Info

| Version | Scala Version | Play Version |
|---------|---------------|--------------|
| 5.0.0   | 2.12 only     | 2.6, 2.7, 2.8|
| 4.x.x   | 2.11, 2.12    | 2.6, 2.7     |
| 3.x.x   | 2.11 only     | 2.5, 2.6     |

As of version 4.0.0 Metrix is no longer built with Play 2.5 support. If you require Play 2.5 support continue to use the 3.x.x branch or consider upgrading.

## Name origins
We needed to create a new library to contain the classes and mechanisms needed to produce and report on slow-running metrics.
We had a heated debate about the name for this library and we chose to take the blue pill and named it 'metrix'.

This library can help with slow metric reporting so that only one node in the whole sytem is doing the slow-running data gathering. Please see details below.

## How data gathering works
<img src="https://github.com/hmrc/metrix/blob/master/diagrams/metrixDataGathering.png" width="500" alt="Metric Gathering">

All nodes periodically try to acquire the lock. As seen on the above picture after one of the nodes manages to acquire the lock, it will gather the data through predefined classes that implement MetricSource trait, and write it to the datastore. The lock is needed as the metric gathering might be resource and time heavy operation.

All the nodes will also periodically get all the metrics and update local MetricCache. Lock is not needed for this operation. They also control the creation and registration of individual CachedGaguges which are tied with the standard metric reporting mechanism. They would typically read values from MetricCache whenever asked for it. See below for details.

## How reporting works

<img src="https://github.com/hmrc/metrix/blob/master/diagrams/metricReportingMechanism.png" width="250" alt="Metric Reporting">

There is a CacheGauge registered in the MetricRegistry for each metric gathered from metric sources.

Graphite reporter is on a regular (configured) basis getting all registered Gaguges and calling getValue() method on them.
The CacheGauges will then read the value for a given metric from MetricCache and return it. GraphiteReporter will pass this information to graphite.

## Example construction
``` scala
    val exclusiveTimePeriodLock = new ExclusiveTimePeriodLock {
      override val repo: LockRepository = LockRepository
      override val lockId: String = "write-your-lock-id"
      override val holdLockFor =  new JodaDuration(lockExpiryTime.toMillis)
    }

    val sources: List[MetricSource] = AddYourMetricSourcesHere
    val metricOrchestrator = new MetricOrchestrator(
      sources,
      exclusiveTimePeriodLock,
      repositories.MongoMetricRepository,
      MetricsRegistry.defaultRegistry
    )
```
## Example usage
``` scala
    metricOrchestrator
        .attemptToUpdateAndRefreshMetrics(
          skipReportingOn = optionalFilterMethodForPersistedMetrics()
        ).map(_.andLogTheResult())
        .recover { case e: RuntimeException => Logger.error(s"An error occurred processing metrics: ${e.getMessage}", e) }
```

``` scala
    metricOrchestrator
        .attemptToUpdateRefreshAndResetMetrics(
          resetMetricOn = optionalFilterMethodToResetMetrics()
        ).map(_.andLogTheResult())
        .recover { case e: RuntimeException => Logger.error(s"An error occurred processing metrics: ${e.getMessage}", e) }
```

## Installing

Include the following dependency in your SBT build

``` scala
resolvers += Resolver.bintrayRepo("hmrc", "releases")

libraryDependencies += "uk.gov.hmrc" %% "metrix" % "[INSERT-VERSION]"
```
## Compatibility
Metrix since version 3.0.0 uses the latest ReactiveMongo (https://github.com/ReactiveMongo/ReactiveMongo) instead of HMRC fork of it (https://github.com/hmrc/ReactiveMongo). Please review your dependencies if you upgrade. In particular you should no longer use https://github.com/hmrc/Play-ReactiveMongo/ in your microservice.

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
