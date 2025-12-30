package tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GenerateAst {
  private static final int INDENT_SIZE = 2;

  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.err.println("Usage: generate_ast <output_directory>");
      System.exit(64);
    }
    String outputDir = args[0];

    // @formatter:off
    defineAst(outputDir, "Expr", Arrays.asList("Binary : Expr left, Token operator, Expr right",
        "Grouping : Expr expression", "Literal : Object value", "Unary : Token operator, Expr right",
        "Conditional : Expr condition, Expr thenBranch, Expr elseBranch"));
    // @formatter:om
  }

  private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
    String path = outputDir + "/" + baseName + ".java";
    PrintWriter writer = new PrintWriter(path, "UTF-8");

    writer.print("""
        package jlox;

        import java.util.List;

        abstract class %s {
        """.formatted(baseName));

    defineVisitor(writer, baseName, types);

    types.stream()
         .map(type -> type.split(":"))
         .forEach(parts -> {
           String className = parts[0].trim();
           String fields = parts[1].trim();
           defineType(writer, baseName, className, fields);
         });

    writer.print("""
          abstract <R> R accept(Visitor<R> visitor);
        }
        """);
    writer.close();
  }

  private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
    String visitorMethods = types.stream()
                                 .map(type -> type.split(":")[0])
                                 .map(String::trim)
                                 .map(t -> "R visit%s%s(%s %s);".formatted(t, baseName, t, baseName.toLowerCase()))
                                 .collect(Collectors.joining("\n"))
                                 .indent(INDENT_SIZE);

    String visitorTemplate = """
        interface Visitor<R> {
        %s
        }
        """.formatted(visitorMethods)
           .indent(INDENT_SIZE);

    writer.print(visitorTemplate);
    writer.flush();
  }

  private static void defineType(PrintWriter writer, String baseName, String className, String fieldList) {
    var fields = Arrays.stream(fieldList.split(","))
                       .map(String::trim)
                       .filter(s -> !s.isEmpty())
                       .map(f -> {
                         int lastSpace = f.lastIndexOf(" ");
                         return new String[] { f.substring(0, lastSpace).trim(), f.substring(lastSpace).trim() };
                       })
                       .toList();

    String declarations = fields.stream()
                                .map(f -> "final %s %s;".formatted(f[0], f[1]))
                                .collect(Collectors.joining("\n"))
                                .indent(INDENT_SIZE);

    String assignments = fields.stream()
                               .map(f -> "this.%s = %s;".formatted(f[1], f[1]))
                               .collect(Collectors.joining("\n"))
                               .indent(INDENT_SIZE * 2);

    String subClassTemplate = """
        static class %s extends %s {
          public %s(%s) {
        %s
          }

        %s

          @Override
          <R> R accept(Visitor<R> visitor) {
            return visitor.visit%s%s(this);
          }
        }
        """.formatted(className, baseName, className, fieldList, assignments, declarations, className, baseName)
           .indent(INDENT_SIZE);

    writer.print(subClassTemplate);
    writer.flush();
  }
}
