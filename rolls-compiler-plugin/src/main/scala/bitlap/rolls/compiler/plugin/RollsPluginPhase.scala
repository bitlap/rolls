package bitlap.rolls.compiler.plugin

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/24
 */
enum RollsPluginPhase(val name: String, val description: String):
  case PrettyToString extends RollsPluginPhase("PrettyToStringPhase", "Enable more elegant toString method.")

  case ValidateIdentPrefix
      extends RollsPluginPhase("ValidateIdentPrefixPhase", "Enable prefix verification for identifiers.")

  case ClassSchema
      extends RollsPluginPhase("ClassSchemaPhase", "Obtain descriptions of all public methods in the class.")
