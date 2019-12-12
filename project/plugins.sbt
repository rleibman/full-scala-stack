////////////////////////////////////////////////////////////////////////////////////
// Common stuff
addSbtPlugin("com.dwijnand"      % "sbt-travisci"    % "1.2.0")
addSbtPlugin("com.typesafe.sbt"  % "sbt-git"         % "1.0.0")
addSbtPlugin("de.heikoseeberger" % "sbt-header"      % "5.2.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager"  % "1.4.1")
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.9.0")

////////////////////////////////////////////////////////////////////////////////////
// Server
addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")
addSbtPlugin("io.github.davidmweber" % "flyway-sbt" % "6.0.0")

////////////////////////////////////////////////////////////////////////////////////
// Web client
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.31")
addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.15.0-0.6")

libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.7.28" // Needed by sbt-git
libraryDependencies += "org.vafer" % "jdeb" % "1.4" artifacts (Artifact("jdeb", "jar", "jar"))