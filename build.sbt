name := "metrix"

enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning)

makePublicallyAvailableOnBintray := true

majorVersion := 5

scalaVersion := "2.12.13"

libraryDependencies ++= LibDependencies()

resolvers += Resolver.typesafeRepo("releases")

PlayCrossCompilation.playCrossCompilationSettings
