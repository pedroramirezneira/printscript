package rule

class SpaceAroundEquals(private val active: Boolean) : ConfigurableRule {
  override fun apply(): String {
    val result = StringBuilder()
    if (active) {
      result.append(" ")
    }
    result.append("=")
    if (active) {
      result.append(" ")
    }
    return result.toString()
  }
}
