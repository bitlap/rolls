package bitlap.rolls.compiler.plugin

import dotty.tools.dotc.plugins.*
import dotty.tools.dotc.transform.*
import dotty.tools.dotc.config.*

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/20
 */
class RollsCompilerPlugin extends StandardPlugin:
  self =>

  override val name: String        = RollsCompilerPlugin.name
  override val description: String = RollsCompilerPlugin.description

  def init(options: List[String]): List[PluginPhase] = {
    val setting = new RollsSetting(options)
    new ClassSchemaPhase(setting) :: new PrettyToStringPhase(setting) :: new ValidateIdentPrefixPhase(setting) :: Nil
  }

end RollsCompilerPlugin

object RollsCompilerPlugin:
  val name        = "RollsCompilerPlugin"
  val description = "Rolls Compiler Plugin"
end RollsCompilerPlugin
