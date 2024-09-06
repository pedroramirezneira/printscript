package com.printscript.engine

import com.printscript.interpreter.PrintInterpreter
import com.printscript.lexer.Lexer
import com.printscript.parser.Parser
import java.io.File

fun main() {
  val lexer = Lexer()
  val parser = Parser()
  val test = parser.parse(lexer.lex(File("engine/src/test/resources/test.txt").reader()))
  PrintInterpreter().interpret(test)
}
