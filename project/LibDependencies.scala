/*
 * Copyright 2018 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import sbt._

object LibDependencies {

  def apply(): Seq[ModuleID] = compile ++ test

  private val play26Version = "2.6.25"
  private val play27Version = "2.7.9"
  private val play28Version = "2.8.7"

  private val compile: Seq[ModuleID] = PlayCrossCompilation.dependencies(
    play26 = Seq(
      "com.kenshoo"           %% "metrics-play"     % "2.6.19_0.7.0",
      "io.dropwizard.metrics" %  "metrics-graphite" % "4.0.3", // same version as metrics-play's io.dropwizard.metrics dependencies
      "com.typesafe.play"     %% "play"             % play26Version,
      "uk.gov.hmrc"           %% "mongo-lock"       % "7.0.0-play-26"
    ),
    play27 = Seq(
      "com.kenshoo"           %% "metrics-play"     % "2.7.3_0.8.2",
      "io.dropwizard.metrics" %  "metrics-graphite" % "4.0.5", // same version as metrics-play's io.dropwizard.metrics dependencies
      "com.typesafe.play"     %% "play"             % play27Version,
      "uk.gov.hmrc"           %% "mongo-lock"       % "7.0.0-play-27"
    ),
    play28 = Seq(
      "com.kenshoo"           %% "metrics-play"     % "2.7.3_0.8.2", // no Play 2.8 build, but is compatible.
      "io.dropwizard.metrics" %  "metrics-graphite" % "4.0.5", // same version as metrics-play's io.dropwizard.metrics dependencies
      "com.typesafe.play"     %% "play"             % play28Version,
      "uk.gov.hmrc"           %% "mongo-lock"       % "7.0.0-play-28"
    )
  )

  private val test: Seq[ModuleID] = PlayCrossCompilation.dependencies(
    shared = Seq(
      "org.scalatest"        %% "scalatest"                % "3.1.4"       % Test,
      "com.vladsch.flexmark" %  "flexmark-all"             % "0.36.8"      % Test,
      "org.scalatestplus"    %% "scalatestplus-scalacheck" % "3.1.0.0-RC2" % Test,
      "org.mockito"          %% "mockito-scala-scalatest"  % "1.16.23"     % Test
    ),
    play26 = Seq(
      "com.typesafe.play" %% "play-test"                % play26Version    % Test,
      "uk.gov.hmrc"       %% "reactivemongo-test"       % "5.0.0-play-26"  % Test,
      "uk.gov.hmrc"       %% "service-integration-test" % "1.1.0-play-26"  % Test
    ),
    play27 = Seq(
      "com.typesafe.play" %% "play-test"                % play27Version    % Test,
      "uk.gov.hmrc"       %% "reactivemongo-test"       % "5.0.0-play-27"  % Test,
      "uk.gov.hmrc"       %% "service-integration-test" % "1.1.0-play-27"  % Test
    ),
    play28 = Seq(
      "com.typesafe.play" %% "play-test"                % play28Version    % Test,
      "uk.gov.hmrc"       %% "reactivemongo-test"       % "5.0.0-play-28"  % Test,
      "uk.gov.hmrc"       %% "service-integration-test" % "1.1.0-play-28"  % Test
    )
  )
}
