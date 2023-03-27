package bitlap.rolls.compiler.plugin

import dotty.tools.dotc.core.Contexts.Context

trait PluginPhaseFilter[T]:

  val annotationFullNames: List[String]

  def existsAnnot(tree: T)(using ctx: Context): Boolean

  def handle(tree: T)(using ctx: Context): T

end PluginPhaseFilter
