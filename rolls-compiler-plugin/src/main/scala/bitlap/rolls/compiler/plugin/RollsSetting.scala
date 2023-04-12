package bitlap.rolls.compiler.plugin

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/31
 */
final case class RollsConfig(
  classSchema: String = "bitlap.rolls.core.annotations.classSchema",
  prettyToString: String = "bitlap.rolls.core.annotations.prettyToString",
  rhsMapping: String = "bitlap.rolls.core.annotations.rhsMapping",
  customRhsMapping: String = "bitlap.rolls.core.annotations.customRhsMapping",
  classSchemaFolder: String = "/tmp/.compiler",
  classSchemaFileName: String = "classSchema_%s.txt",
  rhsMappingUri: String = "http://localhost:18000/rolls-mapping",
  classSchemaPostUri: String = "http://localhost:18000/rolls-doc",
  classSchemaQueryUri: String = "http://localhost:18000/rolls-schema",
  postClassSchemaToServer: Boolean = false,
  stringMask: String = "bitlap.rolls.core.annotations.stringMask",
  rollsRuntimeClass: String = "bitlap.rolls.core.RollsRuntime",
  rollsRuntimeToStringMethod: String = "toString_",
  validateIdentPrefix: List[String] = List.empty,
  validateShouldStartWith: String = ""
)
object RollsConfig:
  lazy val default: RollsConfig = RollsConfig()
end RollsConfig

final class RollsSetting(configString: Option[String]) {

  def config: RollsConfig = readConfig()

  private def readConfig(): RollsConfig = {
    val default = RollsConfig.default
    val lines   = configString.map(_.split('\n')).getOrElse(Array.empty[String])
    lines
      .foldLeft(default) { (config, line) =>
        if line.startsWith("#") then config
        else {
          val parts = line.split('=')
          assert(parts.length == 2, "incorrect config line = " + line)
          val name = parts(0).trim
          name match
            case "classSchema"                => config.copy(classSchema = parts(1).trim)
            case "prettyToString"             => config.copy(prettyToString = parts(1).trim)
            case "rhsMapping"                 => config.copy(rhsMapping = parts(1).trim)
            case "customRhsMapping"           => config.copy(customRhsMapping = parts(1).trim)
            case "classSchemaFolder"          => config.copy(classSchemaFolder = parts(1).trim)
            case "classSchemaFileName"        => config.copy(classSchemaFileName = parts(1).trim)
            case "rhsMappingUri"              => config.copy(rhsMappingUri = parts(1).trim)
            case "classSchemaPostUri"         => config.copy(classSchemaPostUri = parts(1).trim)
            case "classSchemaQueryUri"        => config.copy(classSchemaQueryUri = parts(1).trim)
            case "postClassSchemaToServer"    => config.copy(postClassSchemaToServer = parts(1).trim.toBoolean)
            case "stringMask"                 => config.copy(stringMask = parts(1).trim)
            case "rollsRuntimeClass"          => config.copy(rollsRuntimeClass = parts(1).trim)
            case "rollsRuntimeToStringMethod" => config.copy(rollsRuntimeToStringMethod = parts(1).trim)
            case "validateIdentPrefix"        => config.copy(validateIdentPrefix = parts(1).trim.split(",").toList)
            case "validateShouldStartWith"    => config.copy(validateShouldStartWith = parts(1).trim)
            case _                            => config
        }
      }
  }
}
