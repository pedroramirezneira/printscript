package com.printscript.interpreter.util

import com.printscript.interpreter.OperationException
import com.printscript.interpreter.ReferenceException
import com.printscript.interpreter.input.Input
import com.printscript.models.node.ASTNode
import com.printscript.models.node.DoubleExpressionNode
import com.printscript.models.node.LiteralNode
import com.printscript.models.node.ReadEnvNode
import com.printscript.models.node.ReadInputNode

internal object Solver {
  @Throws(Exception::class)
  fun getValue(context: Context, node: ASTNode, input: Input): Any {
    return when (node) {
      is ReadInputNode -> {
        val value = getValue(context, node.expression, input)
        val response = input.read(value.toString())
        try {
          response.toBooleanStrict()
        } catch (e: IllegalArgumentException) {
          try {
            response.toInt()
          } catch (e: NumberFormatException) {
            try {
              response.toDouble()
            } catch (e: NumberFormatException) {
              response
            }
          }
        }
      }

      is ReadEnvNode -> {
        val env = getValue(context, node.expression, input)
        when (Environment.hasGlobalVariable(env.toString())) {
          true -> Environment.getGlobalVariable(env.toString())!!
          false -> throw ReferenceException("Environment variable $env not found.")
        }
      }

      is LiteralNode<*> -> {
        when {
          node.value is String -> getLiteral(context, node.value as String)
          else -> node.value!!
        }
      }

      is DoubleExpressionNode -> {
        val a = getValue(context, node.left, input)
        val b = getValue(context, node.right, input)
        when (node.operator) {
          "+" -> add(a, b)
          "-" -> subtract(a, b)
          "*" -> multiply(a, b)
          "/" -> divide(a, b)
          else -> throw OperationException("Operator ${node.operator} not supported.")
        }
      }

      else -> throw OperationException("Operation not supported.")
    }
  }

  private fun round(number: Number): Number {
    return when {
      number.toDouble() % 1 == 0.0 -> number.toInt()
      else -> number
    }
  }

  private fun add(a: Any, b: Any): Any {
    return when {
      a is Number && b is Number -> round(a.toDouble() + b.toDouble())
      a is Number && b is String -> a.toString() + b
      a is String && b is Number -> a + b.toString()
      a is String && b is String -> a + b
      a is String && b is Boolean -> a + b.toString()
      else -> throw OperationException("Operation $a + $b not supported.")
    }
  }

  private fun subtract(a: Any, b: Any): Any {
    return when {
      a is Number && b is Number -> round(a.toDouble() - b.toDouble())
      else -> throw OperationException("Operation $a - $b not supported.")
    }
  }

  private fun multiply(a: Any, b: Any): Any {
    return when {
      a is Number && b is Number -> round(a.toDouble() * b.toDouble())
      else -> throw OperationException("Operation $a * $b not supported.")
    }
  }

  private fun divide(a: Any, b: Any): Any {
    return when {
      a is Number && b is Number -> round(a.toDouble() / b.toDouble())
      else -> throw OperationException("Operation $a / $b not supported.")
    }
  }

  private fun getLiteral(context: Context, string: String): Any {
    return when {
      string.startsWith("\"") -> string.replace("\"", "")
      string.startsWith("'") -> string.replace("'", "")
      else -> context.get(string)?.value ?: throw ReferenceException("Variable $string not declared.")
    }
  }
}
