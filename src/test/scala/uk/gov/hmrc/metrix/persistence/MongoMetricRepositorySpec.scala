/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.metrix.persistence

import org.scalatest.{BeforeAndAfterEach, LoneElement}
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.hmrc.metrix.domain.PersistedMetric
import uk.gov.hmrc.mongo.MongoSpecSupport

import scala.concurrent.ExecutionContext.Implicits.global

class MongoMetricRepositorySpec
  extends AnyWordSpec
     with Matchers
     with MongoSpecSupport
     with ScalaFutures
     with IntegrationPatience
     with LoneElement
     with BeforeAndAfterEach {

  lazy val metricsRepo = new MongoMetricRepository(databaseName)

  override def beforeEach(): Unit = {
    super.beforeEach()
    metricsRepo.removeAll().futureValue
  }

  override def afterEach(): Unit = {
    super.afterEach()
    metricsRepo.removeAll().futureValue
  }

  "update" should {
    "store the provided MetricsStorage instance with the 'name' key" in {
      val storedMetric = PersistedMetric("test-metric", 5)

      metricsRepo.persist(storedMetric).futureValue

      metricsRepo.findAll().futureValue.loneElement shouldBe storedMetric
    }
  }

}
