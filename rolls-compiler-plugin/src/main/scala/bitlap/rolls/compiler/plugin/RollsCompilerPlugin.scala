package bitlap.rolls.compiler.plugin

import dotty.tools.dotc.ast.tpd
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.DenotTransformers.IdentityDenotTransformer
import dotty.tools.dotc.plugins.{ PluginPhase, StandardPlugin }
import dotty.tools.dotc.transform.{ PickleQuotes, Staging }

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/20
 */
class RollsCompilerPlugin extends StandardPlugin:
  self =>

  override val name: String        = "RollsCompilerPlugin"
  override val description: String = "Rolls Compiler Plugin"

  def init(options: List[String]): List[PluginPhase] = {
    val setting = new RollsSetting(options.headOption)
    new ClassSchemaPhase(setting) :: new RhsMappingPhase(setting) :: new PrettyToStringPhase(
      setting
    ) :: new ValidateIdentPrefixPhase(setting) :: Nil
  }

end RollsCompilerPlugin
