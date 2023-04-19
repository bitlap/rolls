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
  classSchemaPostUri: String = "http://localhost:18000/rolls-doc",
  classSchemaQueryUri: String = "http://localhost:18000/rolls-schema",
  postClassSchemaToServer: Boolean = false,
  validateShouldStartsWith: String = ""
)
object RollsConfig:
  lazy val default: RollsConfig = RollsConfig()
end RollsConfig

final class RollsSetting(configString: List[String]) {

  private enum ConfigKey:
    case classSchema, prettyToString, classSchemaFolder, classSchemaFileName,
      classSchemaPostUri, classSchemaQueryUri, postClassSchemaToServer, stringMask, rollsRuntimeClass,
      rollsRuntimeToStringMethod, validateIdentPrefix, validateShouldStartsWith

  def config: RollsConfig = readConfig()

  private def readConfig(): RollsConfig = {
    val default = RollsConfig.default
    configString
      .foldLeft(default) { (config, line) =>
        val parts = line.split('=')
        assert(parts.length == 2, "incorrect config line = " + line)
        val configValue = if (parts(1).trim.isEmpty) None else Some(parts(1).trim)
        ConfigKey.valueOf(parts(0).trim) match
          case ConfigKey.classSchema                => config.copy(classSchema = configValue)
          case ConfigKey.prettyToString             => config.copy(prettyToString = configValue)
          case ConfigKey.stringMask                 => config.copy(stringMask = parts(1).trim)
          case ConfigKey.classSchemaFolder          => config.copy(classSchemaFolder = parts(1).trim)
          case ConfigKey.classSchemaFileName        => config.copy(classSchemaFileName = parts(1).trim)
          case ConfigKey.classSchemaPostUri         => config.copy(classSchemaPostUri = parts(1).trim)
          case ConfigKey.classSchemaQueryUri        => config.copy(classSchemaQueryUri = parts(1).trim)
          case ConfigKey.postClassSchemaToServer    => config.copy(postClassSchemaToServer = parts(1).trim.toBoolean)
          case ConfigKey.rollsRuntimeClass          => config.copy(rollsRuntimeClass = parts(1).trim)
          case ConfigKey.rollsRuntimeToStringMethod => config.copy(rollsRuntimeToStringMethod = parts(1).trim)
          case ConfigKey.validateIdentPrefix =>
            config.copy(validateIdentPrefix = parts(1).trim.split('|').map(_.trim).toList)
          case ConfigKey.validateShouldStartsWith => config.copy(validateShouldStartsWith = parts(1).trim)
      }
  }
}
