package jlox;

import jlox.Expr.Conditional;

/**
 * This is a challenge question 3
 */
public class RpnAstPrinter implements Expr.Visitor<String> {
  String print(Expr expr) {
    return expr.accept(this);
  }

  @Override
  public String visitBinaryExpr(Expr.Binary expr) {
    return visit(expr.operator.lexeme, expr.left, expr.right);
  }

  @Override
  public String visitGroupingExpr(Expr.Grouping expr) {
    return visit("group", expr.expression);
  }

  @Override
  public String visitLiteralExpr(Expr.Literal expr) {
    if (expr.value == null)
      return "nil";
    return expr.value.toString();
  }

  @Override
  public String visitUnaryExpr(Expr.Unary expr) {
    return visit(expr.operator.lexeme, expr.right);
  }

  @Override
  public String visitConditionalExpr(Conditional expr) {
    throw new UnsupportedOperationException("Unimplemented method 'visitConditionalExpr'");
  }

  private String visit(String name, Expr... exprs) {
    StringBuilder builder = new StringBuilder();

    boolean first = true;
    for (Expr expr : exprs) {
      if (!first)
        builder.append(" ");
      first = false;
      builder.append(expr.accept(this));
    }
    builder.append(" ").append(name);
    ;

    return builder.toString();
  }

  /*
   * For debug testing
   */
  // public static void main(String[] args) {
  // Expr expression = new Expr.Binary(
  // new Expr.Unary(
  // new Token(TokenType.MINUS, "-", null, 1),
  // new Expr.Literal(123)),
  // new Token(TokenType.STAR, "*", null, 1),
  // new Expr.Grouping(
  // new Expr.Literal(45.67)));

  // System.out.println(new RpnAstPrinter().print(expression));
  // }
}
