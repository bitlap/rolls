package bitlap.rolls.compiler.plugin

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/31
 */
final case class RollsConfig(
    classSchema: Option[String] = Some("bitlap.rolls.core.annotations.classSchema"),
    prettyToString: Option[String] = Some("bitlap.rolls.core.annotations.prettyToString"),
    stringMask: String = "bitlap.rolls.core.annotations.stringMask",
    validateIdentPrefix: List[String] = List.empty,
    rollsRuntimeClass: String = "bitlap.rolls.core.RollsRuntime",
    rollsRuntimeToStringMethod: String = "toString_",
    classSchemaFolder: String = "/tmp/.compiler",
    classSchemaFileName: String = "classSchema_%s.txt",
    classSchemaPostUri: Option[String] = None,
    validateShouldStartsWith: String = ""
)

object RollsConfig:
  lazy val default: RollsConfig = RollsConfig()
end RollsConfig

final class RollsSetting(configString: List[String]) {

  private enum RollsConfigKey:

    case classSchema, prettyToString, classSchemaFolder, classSchemaFileName,
      classSchemaPostUri, stringMask, rollsRuntimeClass,
      rollsRuntimeToStringMethod, validateIdentPrefix, validateShouldStartsWith

  def config: RollsConfig = readConfig()

  private def readConfig(): RollsConfig = {
    val default = RollsConfig.default
    configString
      .foldLeft(default) { (config, line) =>
        val parts = line.split('=')
        assert(parts.length == 2, "incorrect config line = " + line)
        val configValue = if (parts(1).trim.isEmpty) None else Some(parts(1).trim)
        RollsConfigKey.valueOf(parts(0).trim) match
          case RollsConfigKey.classSchema                => config.copy(classSchema = configValue)
          case RollsConfigKey.prettyToString             => config.copy(prettyToString = configValue)
          case RollsConfigKey.stringMask                 => config.copy(stringMask = parts(1).trim)
          case RollsConfigKey.classSchemaFolder          => config.copy(classSchemaFolder = parts(1).trim)
          case RollsConfigKey.classSchemaFileName        => config.copy(classSchemaFileName = parts(1).trim)
          case RollsConfigKey.classSchemaPostUri         => config.copy(classSchemaPostUri = configValue)
          case RollsConfigKey.rollsRuntimeClass          => config.copy(rollsRuntimeClass = parts(1).trim)
          case RollsConfigKey.rollsRuntimeToStringMethod => config.copy(rollsRuntimeToStringMethod = parts(1).trim)
          case RollsConfigKey.validateIdentPrefix =>
            config.copy(validateIdentPrefix = parts(1).trim.split('|').map(_.trim).toList)
          case RollsConfigKey.validateShouldStartsWith => config.copy(validateShouldStartsWith = parts(1).trim)
      }
  }
}
