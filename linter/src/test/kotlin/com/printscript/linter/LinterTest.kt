package com.printscript.linter

import com.google.gson.Gson
import com.printscript.lexer.Lexer
import com.printscript.parser.PrintParser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class LinterTest {
  private val gson = Gson()
  private val style1 = gson.fromJson(File(this::class.java.getResource("/style1.json")!!.file).readText(), LinterConfig::class.java)
  private val style2 = gson.fromJson(File(this::class.java.getResource("/style2.json")!!.file).readText(), LinterConfig::class.java)
  private val style3 = gson.fromJson(File(this::class.java.getResource("/style3.json")!!.file).readText(), LinterConfig::class.java)
  private val style4 = gson.fromJson(File(this::class.java.getResource("/style4.json")!!.file).readText(), LinterConfig::class.java)
  private val style5 = gson.fromJson(File(this::class.java.getResource("/style5.json")!!.file).readText(), LinterConfig::class.java)
  private val style6 = gson.fromJson(File(this::class.java.getResource("/style6.json")!!.file).readText(), LinterConfig::class.java)
  private val style7 = gson.fromJson(File(this::class.java.getResource("/style7.json")!!.file).readText(), LinterConfig::class.java)

  @Test
  fun testStyle1() {
    val lexer = Lexer()
    val astList = PrintParser().parse(lexer.lex(File("src/test/resources/style1.txt").reader()))
    val result = mutableListOf<String>()
    while (astList.hasNext()) {
      Linter.lint(astList.next(), style1).forEach { violation ->
        result.add(violation.toString())
      }
    }
    val expectedAt0 = "Casing violation at 2:5, camel case expected"
    val expectedAt1 = "Expression inside print statement at 3:1"
    assert(result.size == 2)
    assertEquals(expectedAt0, result[0])
    assertEquals(expectedAt1, result[1])
  }

  @Test
  fun testStyle2() {
    val lexer = Lexer()
    val astList = PrintParser().parse(lexer.lex(File("src/test/resources/style2.txt").reader()))
    val result = mutableListOf<String>()
    while (astList.hasNext()) {
      result.forEach { println(it) }
      Linter.lint(astList.next(), style2).forEach { violation ->
        result.add(violation.toString())
      }
    }
    val expectedAt0 = "Casing violation at 1:5, snake case expected"
    assert(result.size == 1)
    assertEquals(expectedAt0, result[0])
  }

  @Test
  fun testStyle3() {
    val lexer = Lexer()
    val astList = PrintParser().parse(lexer.lex(File("src/test/resources/style3.txt").reader()))
    val result = mutableListOf<String>()
    while (astList.hasNext()) {
      Linter.lint(astList.next(), style3).forEach { violation ->
        result.add(violation.toString())
      }
    }
    val expectedAt0 = "Casing violation at 1:5, pascal case expected"
    val expectedAt1 = "Expression inside print statement at 3:1"
    assert(result.size == 2)
    assertEquals(expectedAt0, result[0])
    assertEquals(expectedAt1, result[1])
  }

  @Test
  fun testStyle4() {
    val lexer = Lexer()
    val astList = PrintParser().parse(lexer.lex(File("src/test/resources/style4.txt").reader()))
    val result = mutableListOf<String>()
    while (astList.hasNext()) {
      Linter.lint(astList.next(), style4).forEach { violation ->
        result.add(violation.toString())
      }
    }
    val expectedAt0 = "Casing violation at 1:5, kebab case expected"
    val expectedAt1 = "Expression inside print statement at 3:1"
    assert(result.size == 2)
    assertEquals(expectedAt0, result[0])
    assertEquals(expectedAt1, result[1])
  }

  @Test
  fun testStyle5() {
    val lexer = Lexer()
    val astList = PrintParser().parse(lexer.lex(File("src/test/resources/style5.txt").reader()))
    val result = mutableListOf<String>()
    while (astList.hasNext()) {
      Linter.lint(astList.next(), style5).forEach { violation ->
        result.add(violation.toString())
      }
    }
    val expectedAt0 = "Casing violation at 2:5, screaming snake case expected"
    val expectedAt1 = "Expression inside print statement at 4:1"
    assert(result.size == 2)
    assertEquals(expectedAt0, result[0])
    assertEquals(expectedAt1, result[1])
  }

  @Test
  fun testStyle6() {
    val lexer = Lexer()
    val astList = PrintParser().parse(lexer.lex(File("src/test/resources/style6.txt").reader()))
    val result = mutableListOf<String>()
    while (astList.hasNext()) {
      Linter.lint(astList.next(), style6).forEach { violation ->
        result.add(violation.toString())
      }
    }
    val expectedAt0 = "Casing violation at 2:5, screaming kebab case expected"
    val expectedAt1 = "Expression inside print statement at 5:1"
    assert(result.size == 2)
    assertEquals(expectedAt0, result[0])
    assertEquals(expectedAt1, result[1])
  }

  @Test
  fun testStyle72() {
    val lexer = Lexer()
    val astList = PrintParser().parse(lexer.lex(File("src/test/resources/style7.txt").reader()))
    val result = mutableListOf<String>()
    while (astList.hasNext()) {
      Linter.lint(astList.next(), style7).forEach { violation ->
        result.add(violation.toString())
      }
    }
    println(result)
    val expectedAt0 = "Expression inside read input statement at 2:5"
    assert(result.size == 1)
    assertEquals(expectedAt0, result[0])
  }
}
