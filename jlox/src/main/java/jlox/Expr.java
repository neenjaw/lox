package jlox;

import java.util.List;

abstract class Expr {
  interface Visitor<R> {
    R visitAssignExpr(Assign expr);
    R visitBinaryExpr(Binary expr);
    R visitGroupingExpr(Grouping expr);
    R visitLiteralExpr(Literal expr);
    R visitUnaryExpr(Unary expr);
    R visitVariableExpr(Variable expr);
    R visitConditionalExpr(Conditional expr);
  
  }
  static class Assign extends Expr {
    public Assign(Token name, Expr value) {
      this.name = name;
      this.value = value;
  
    }
  
    final Token name;
    final Expr value;
  
  
    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitAssignExpr(this);
    }
  }
  static class Binary extends Expr {
    public Binary(Expr left, Token operator, Expr right) {
      this.left = left;
      this.operator = operator;
      this.right = right;
  
    }
  
    final Expr left;
    final Token operator;
    final Expr right;
  
  
    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitBinaryExpr(this);
    }
  }
  static class Grouping extends Expr {
    public Grouping(Expr expression) {
      this.expression = expression;
  
    }
  
    final Expr expression;
  
  
    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitGroupingExpr(this);
    }
  }
  static class Literal extends Expr {
    public Literal(Object value) {
      this.value = value;
  
    }
  
    final Object value;
  
  
    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitLiteralExpr(this);
    }
  }
  static class Unary extends Expr {
    public Unary(Token operator, Expr right) {
      this.operator = operator;
      this.right = right;
  
    }
  
    final Token operator;
    final Expr right;
  
  
    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitUnaryExpr(this);
    }
  }
  static class Variable extends Expr {
    public Variable(Token name) {
      this.name = name;
  
    }
  
    final Token name;
  
  
    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitVariableExpr(this);
    }
  }
  static class Conditional extends Expr {
    public Conditional(Expr condition, Expr thenBranch, Expr elseBranch) {
      this.condition = condition;
      this.thenBranch = thenBranch;
      this.elseBranch = elseBranch;
  
    }
  
    final Expr condition;
    final Expr thenBranch;
    final Expr elseBranch;
  
  
    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitConditionalExpr(this);
    }
  }
  abstract <R> R accept(Visitor<R> visitor);
}
