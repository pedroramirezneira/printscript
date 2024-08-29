package visitor

import logger.ILogger
import node.ASTVisitor
import node.AssignationNode
import node.DoubleExpressionNode
import node.LiteralNode
import node.PrintStatementNode
import node.VariableDeclarationNode
import util.Context
import util.Solver

/**
 * Visitor that logs print statements
 */
internal class TracingVisitor(
  private val context: Context,
  private val visitor: ASTVisitor,
  private val logger: ILogger,
  private val print: Boolean,
) : ASTVisitor {
  override fun visit(node: DoubleExpressionNode) {
    visitor.visit(node)
  }

  override fun visit(node: LiteralNode<*>) {
    visitor.visit(node)
  }

  override fun visit(node: PrintStatementNode) {
    val result = Solver.getValue(context, node.expression)
    logger.log(result.toString())
    if (print) visitor.visit(node)
  }

  override fun visit(node: VariableDeclarationNode) {
    visitor.visit(node)
  }

  override fun visit(node: AssignationNode) {
    visitor.visit(node)
  }
}
