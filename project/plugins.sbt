////////////////////////////////////////////////////////////////////////////////////
// Common stuff
addSbtPlugin("com.dwijnand"      % "sbt-travisci"    % "1.2.0")
addSbtPlugin("com.typesafe.sbt"  % "sbt-git"         % "1.0.0")
addSbtPlugin("de.heikoseeberger" % "sbt-header"      % "5.2.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager"  % "1.4.1")
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.9.0")
addSbtPlugin("org.portable-scala" % "sbt-crossproject"         % "0.6.0")  // (1)
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "0.6.0")  // (2)

////////////////////////////////////////////////////////////////////////////////////
// Server
addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")
addSbtPlugin("io.github.davidmweber" % "flyway-sbt" % "6.0.0")

////////////////////////////////////////////////////////////////////////////////////
// Web client
resolvers += Resolver.bintrayRepo("oyvindberg", "ScalajsReactTyped")

addSbtPlugin("org.scalablytyped.japgolly" % "sbt-scalajsreacttyped" % "201912140138")
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.32")
addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.15.0-0.6")

libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.7.30" // Needed by sbt-git
libraryDependencies += "org.vafer" % "jdeb" % "1.4" artifacts (Artifact("jdeb", "jar", "jar"))
