/*
 * Copyright 2020 Roberto Leibman
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

package service

import japgolly.scalajs.react.{ AsyncCallback, Callback }
import japgolly.scalajs.react.extra.Ajax
import model.Search
import org.scalajs.dom.XMLHttpRequest
import org.scalajs.dom.ext.AjaxException
import ujson.Value.InvalidData
import upickle.default.{ read, _ }

trait RESTOperations {
  def processErrors[A](fn: XMLHttpRequest => A)(xhr: XMLHttpRequest): A =
    try {
      if (xhr.status >= 400) {
        throw AjaxException(xhr)
      } else {
        fn(xhr)
      }
    } catch {
      case e: InvalidData =>
        e.printStackTrace()
        throw e
      case e: AjaxException =>
        e.printStackTrace()
        throw e
    }

  def RESTOperation[Request, Response](
    method: String,
    fullUrl: String,
    request: Option[Request] = None,
    withTimeout: Option[(Double, XMLHttpRequest => Callback)] = None
  )(
    implicit requestW: Writer[Request],
    responseR: Reader[Response]
  ): AsyncCallback[Response] = {
    val step1 = Ajax(method, fullUrl)
      .and(_.withCredentials = true)

    val step2 = request
      .fold(step1.send)(s => step1.setRequestContentTypeJson.send(write(s)))

    withTimeout
      .fold(step2)(a => step2.withTimeout(a._1, a._2))
      .asAsyncCallback
      .map {
        processErrors(xhr => read[Response](xhr.responseText))
      }
  }
}

trait RESTClient[E, PK, SEARCH <: Search[_]] {
  def remoteSystem: RESTClient.Service[E, PK, SEARCH]
}

object RESTClient {

  trait Service[E, PK, SEARCH <: Search[_]] extends RESTOperations {
    def get(id: PK)(implicit typeRW: Reader[E]): AsyncCallback[Option[E]]

    def delete(id: PK): AsyncCallback[Boolean]

    def upsert(obj: E)(implicit typeRW: ReadWriter[E]): AsyncCallback[E]

    def search(
      search: Option[SEARCH] = None
    )(implicit typeRW: Reader[E], searchRW: Writer[SEARCH]): AsyncCallback[Seq[E]]

    def count(
      searchObj: Option[SEARCH]
    )(implicit typeRW: Reader[E], searchRW: Writer[SEARCH]): AsyncCallback[Int]

  }
}

trait LiveRESTClient[E, PK, SEARCH <: Search[_]] extends RESTClient[E, PK, SEARCH] {
  val baseUrl: String

  override def remoteSystem: RESTClient.Service[E, PK, SEARCH] = new LiveClientService

  class LiveClientService extends RESTClient.Service[E, PK, SEARCH] {

    override def get(id: PK)(implicit typeRW: Reader[E]): AsyncCallback[Option[E]] =
      RESTOperation[String, Option[E]]("GET", s"$baseUrl/${id.toString}", None)

    override def delete(id: PK): AsyncCallback[Boolean] =
      RESTOperation[String, Boolean]("DELETE", s"$baseUrl/${id.toString}", None)

    override def upsert(obj: E)(implicit typeRW: ReadWriter[E]): AsyncCallback[E] =
      RESTOperation[E, E]("POST", s"$baseUrl", Option(obj))

    override def search(
      searchObj: Option[SEARCH]
    )(implicit typeRW: Reader[E], searchRW: Writer[SEARCH]): AsyncCallback[Seq[E]] =
      RESTOperation[SEARCH, Seq[E]]("POST", s"$baseUrl/search", searchObj)

    override def count(
      searchObj: Option[SEARCH]
    )(implicit typeRW: Reader[E], searchRW: Writer[SEARCH]): AsyncCallback[Int] =
      RESTOperation[SEARCH, Int]("POST", s"$baseUrl/count", searchObj)
  }
}
