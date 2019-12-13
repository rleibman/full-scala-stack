[![Build Status](https://travis-ci.com/rleibman/full-scala-stack.svg?branch=master)](https://travis-ci.com/rleibman/full-scala-stack)

# Full Scala Stack
This is an example project that uses a full scala stack, server to client. 
These are some of the technologies I'm using, I briefly describe why and mention some options in case my choices are not yours.
One of the most difficult thing in our field is to be able to choose a set of technologies for a project that fit well together 
(or can be easily made to fit), are well supported, are easy to find developers who know them (or easy to train in them), modern, etc.

## http-akka (https://akka.io/)
I chose http-akka because it's stable, fast, well maintained and actively developed and full featured. I've also been using it since
it was called spray, so there's the historical reason. If I was choosing from scratch today, I would likely not use play 
(it's too heavy, I think) and may use some lighter library instead, I'm curious as to where zio-http goes. 
My main 2 complaints about http-akka are related: it's not easy to reverse engineer documentation from the routes (as in swagger) and 
the routes are not typed (so if you accidentally change the type returned from a complete you can mess up your client)  
Alternatives: https://www.playframework.com/, https://http4s.org/, https://github.com/softwaremill/sttp, https://github.com/zio/zio-http
 
## slick (http://scala-slick.org/)
I chose slick because I like it's almost ORM way to do tables, it's main disadvantage is that it's not very functional (in the academic sense), but 
it is fairly mature and I've been able to write complex CRUD code with it. 
Alternatives: https://github.com/tpolecat/doobie, https://getquill.io/, http://scalikejdbc.org/

## zio (https://zio.dev/)
Zio says: Type-safe, composable asynchronous and concurrent programming for Scala. For many reasons it's an excellent replacement
for native scala futures, it's easy enough to understand. I have had to write a few bridges to be able to use zio with the
rest of my stack, particularly akka-http and slick. 
Alternatives: Futures, scalaz, cats

## courier (https://github.com/dmurvihill/courier)
Courier is not very well maintained, but there really isn't very much I require of a mail sender library. I did write a zio wrapper as 
part of this project to talk to courier.
I would probably look at zio-email
if I was choosing today, there might be other more full-featured mail libraries available.
Alternatives: https://github.com/funcit/zio-email

## scala.js (https://www.scala-js.org/)
I really wouldn't use anything else right now to create web pages. My biggest complaint about scala-js is that because all the 
source documents are scala it puts graphic web designers at a disadvantage. It really forces you to separate the graphic 
design domain (css) from the web content (traditionally html, now scala produced html), this is not necessarily a bad thing, but
it may force your developers to do things that normally the graphic design team takes care of.  
Alternatives: https://www.playframework.com/

## upickle (http://www.lihaoyi.com/upickle/)
There's way too many json libraries for scala, I had used spray for a long time, but it's not heavily maintained anymore, so
I started using upickle. I'd probably give circe a good look, particularly if you're more into pure functional programming
Alternatives: https://circe.github.io/circe/, https://www.playframework.com/documentation/2.7.x/ScalaJson, https://github.com/spray/spray-json 

## scalajs-react (https://github.com/japgolly/scalajs-react)
I've been using scalajs-react for a long time, so historical reasons lead me to choose it. I particularly like it's use 
of it's zio-like Callback (it would be even better if it actually morphed to use zio). If I was choosing today, I'd probably
give slinky a strong consideration, at least at first reading it seems a bit easier to use.
Alternatives: https://github.com/shadaj/slinky

## scalablytyped (https://github.com/oyvindberg/ScalablyTyped)
An amazing project (that I've participated in, so I'm biased) that takes every typescript project from http://definitelytyped.org/ and 
creates scala bindings for it. For React projects you can choose slinky or scalajs-react flavors. Also, coming soon, an sbt 
plugin that let's you chose exactly what javascript libraries you want to wrap. 

## semantic ui (https://react.semantic-ui.com/)
I really like how the set of react components from the semantic library look and feel, it's very themable as well.
Alternatives: https://material-ui.com/, https://react-bootstrap.github.io/, http://nikgraf.github.io/belle/#/?_k=dyoot9

## MySQL 
I use mySQL mostly because I'm more familiar with it, switching the app to use something else should not be too difficult 
Alternatives: mariadb, postgress, oracle, sql server, etc... or if you want to go nosql: cassandra, mongoDB  

## How the app is put together

### Configuring, compiling and running the app
#### Database
You'll need to have mysql running, if you want to try it first without a database, you can always replace the ```LiveModelDAO``` in ```api.LiveEnvironment``` with
```MockModelDAO```
To initialize the database, just run the scripts in the ```server/src/main/sql``` directory in order. 
Once you do that, you should configure the access to the database in ```server/src/main/resources/application.conf```, change the database url, username and password as needed

#### Server
In general a few parameters in ```server/src/main/resources/application.conf``` will control the application, tell it what port to run on, where to find the static web pages (the ```staticContentDir``` variable), etc.
Once all is configured, in sbt, you should be able to run:
```sbt
  ~server/reStart
```
This will start the server, any changes you make will automatically be recompiled and the server restarted.  

#### Web client
You need to compile all of the scala.js code into a nicely packaged js file. There's a ```dist``` command and a ```debugDist``` command, the first one 
does full optimization, but it's resulting file will be both harder to read and it will take longer to generate, use the second one for development.

## Here's how to do some common tasks

### Add a new web page

### Add a new model object
- Add the model object itself in ```shared/src/main/scala/model``` typically these objects are scala case classes
- Add the database creation code for your object (I like to put these as sql scripts in ```server/src/main/sql```), run it against your database
- Use util.CodeGen to re-generate Tables.scala which will contain the stuff that maps your SQL database with our model.
- Add the CRUD (and other) database operations to ```src/main/scala/dao/ModelDAO```, you'll have to create "live" and "mock" versions of all the methods you create
- Add a service that does basic REST crud operations about your object in ```server/src/main/scala/api```, look at ```ModelService``` as an example
- Add the web pages you need to do stuff with your new object (see above)

### Add a new javascript library
Assuming you are using ScalablyTyped, you need to add to ```build.sbt``` the typings for the javascript library (read the [ScalablyTyped documentation](https://github.com/oyvindberg/ScalablyTyped) ), as well as adding the library itself to the bundler (the ```npmDependencies``` section).
If the library is a react library, you should choose a flavor of react bindings (currently either japgolly or Slinky bindings).
Once you do that you should be good to go!
