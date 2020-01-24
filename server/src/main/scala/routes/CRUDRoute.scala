/*
 * Copyright 2019 Roberto Leibman
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
package routes

import akka.http.scaladsl.server.{ Directives, Route }
import api.ZIODirectives
import dao.CRUDOperations
import de.heikoseeberger.akkahttpupickle.UpickleSupport
import model.Search
import ujson.Num
import upickle.default.ReadWriter
import zio.Task

import scala.util.matching.Regex

/**
 * A crud route avoids boilerplate by definining a simple route for crud operations of an object
 *
 * @tparam E The model object that is the base of the route
 * @tparam PK The type of the object's primary key (used for gets/deletes)
 * @tparam SEARCH A search object (extends Search)
 * @tparam SESSION Sessions are useful if you want to limit the capabalities that people have.
 */
trait CRUDRoute[E, PK, SEARCH <: Search[_], SESSION] {
  def crudRoute: CRUDRoute.Service[E, PK, SEARCH, SESSION]
}

object CRUDRoute {

  abstract class Service[E, PK, SEARCH <: Search[_], SESSION]
      extends Directives
      with ZIODirectives
      with UpickleSupport {

    import upickle.default.read

    val url: String

    val ops: CRUDOperations[E, PK, SEARCH, SESSION]

    val pkRegex: Regex = "^[0-9]*$".r

    /**
     * Override this to add other authenticated (i.e. with session) routes
     * @param session
     * @return
     */
    def other(session: SESSION): Route = reject

    /**
     * Override this to add routes that don't require a session
     * @return
     */
    def unauthRoute: Route = reject

    /**
     * Override this to support children routes (e.g. /api/student/classroom)
     * @param obj A Task that will contain the "parent" object
     * @param session
     * @return
     */
    def childrenRoutes(obj: Task[Option[E]], session: SESSION): Seq[Route] = Seq.empty

    /**
     * You need to override this method so that the architecture knows how to get a primary key from an object
     * @param obj
     * @return
     */
    def getPK(obj: E): PK

    def getOperation(id: PK, session: SESSION): Task[Option[E]] = ops.get(id)(session)

    def deleteOperation(objTask: Task[Option[E]], session: SESSION): Task[Boolean] =
      for {
        objOpt <- objTask
        deleted <- objOpt.fold(Task.succeed(false): Task[Boolean])(
                    obj => ops.delete(getPK(obj))(session)
                  )
      } yield deleted

    def upsertOperation(obj: E, session: SESSION): Task[E] = ops.upsert(obj)(session)

    def countOperation(search: Option[SEARCH], session: SESSION): Task[Long] =
      ops.count(search)(session)

    def searchOperation(search: Option[SEARCH], session: SESSION): Task[Seq[E]] =
      ops.search(search)(session)

    /**
     * The main route. Note that it takes a pair of upickle ReaderWriter implicits that we need to be able to
     * marshall the objects in-to json.
     * In scala3 we may move these to parameters of the trait instead.
     * @param session
     * @param objRW
     * @param searchRW
     * @param pkRW
     * @return
     */
    def route(
      session: SESSION
    )(implicit objRW: ReadWriter[E], searchRW: ReadWriter[SEARCH], pkRW: ReadWriter[PK]): Route =
      pathPrefix(url) {
        other(session) ~
        (post | put) {
          entity(as[E]) { obj =>
            complete(upsertOperation(obj, session))
          }
        } ~
        path("search") {
          post {
            entity(as[Option[SEARCH]]) { search =>
              complete(searchOperation(search, session))
            }
          }
        } ~
        path("count") {
          post {
            entity(as[Option[SEARCH]]) { search =>
              complete(countOperation(search, session).map(a => Num(a.toDouble)))
            }
          }
        } ~
        pathPrefix(pkRegex) { id =>
          childrenRoutes(getOperation(read[PK](id), session), session)
            .reduceOption(_ ~ _)
            .getOrElse(reject) ~
          get {
            complete(getOperation(read[PK](id), session).map(_.toSeq)) //The #!@#!@# akka optionMarshaller gets in our way and converts an option to null/object before it ships it, so we convert it to seq
          } ~
          delete {
            complete(deleteOperation(getOperation(read[PK](id), session), session))
          }
        }
      }
  }

}
