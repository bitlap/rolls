package bitlap.rolls.compiler.plugin

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/24
 */
enum RollsPhase(val name: String, val description: String):
  case PrettyToString      extends RollsPhase("PrettyToStringPhase", "Enable more elegant toString method.")
  case ValidateIdentPrefix extends RollsPhase("ValidateIdentPrefixPhase", "Enable prefix verification for identifiers.")
  case ClassSchema extends RollsPhase("ClassSchemaPhase", "Obtain descriptions of all public methods in the class.")
