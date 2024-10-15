package jlox;

import static jlox.TokenType.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {
	private final String source;
	private final List<Token> tokens = new ArrayList<>();
	private int start = 0;
	private int current = 0;
	private int line = 1;
	
	private static final Map<String, TokenType> keywords;
	
	static {
		keywords = new HashMap<>();
		keywords.put("and", AND);
		keywords.put("class", CLASS);
		keywords.put("else", ELSE);
		keywords.put("false", FALSE);
		keywords.put("for", FOR);
		keywords.put("fun", FUN);
		keywords.put("if", IF);
		keywords.put("nil", NIL);
		keywords.put("or", OR);
		keywords.put("print", PRINT);
		keywords.put("return", RETURN);
		keywords.put("super", SUPER);
		keywords.put("this", THIS);
		keywords.put("true", TRUE);
		keywords.put("var", VAR);
		keywords.put("while", WHILE);
	}
	
	
	Scanner(String source) {
		this.source = source;
	}
	
	List<Token> scanTokens() {
		while (!isAtEnd()) {
			// We are at the beginning of the next lexeme
			start = current;
			scanToken();
		}
		
		tokens.add(new Token(EOF, "", null, line));
		return tokens;
	}
	
	private void scanToken() {
		char c = advance();
		switch(c) {
		// one character tokens
		case '(': addToken(LEFT_PAREN); break;
		case ')': addToken(RIGHT_PAREN); break;
		case '{': addToken(LEFT_BRACE); break;
		case '}': addToken(RIGHT_BRACE); break;
		case ',': addToken(COMMA); break;
		case '.': addToken(DOT); break;
		case '-': addToken(MINUS); break;
		case '+': addToken(PLUS); break;
		case ';': addToken(SEMICOLON); break;
		case '*': addToken(STAR); break;
			
		// one-or-two character tokens
		case '!':
			addToken(match('=') ? BANG_EQUAL : BANG);
			break;
		case '=':
			addToken(match('=') ? EQUAL_EQUAL : EQUAL);
			break;
		case '<':
			addToken(match('=') ? LESS_EQUAL : EQUAL);
			break;
		case '>':
			addToken(match('=') ? GREATER_EQUAL : EQUAL);
			break;
			
		// comments
		case '/':
			if (match('/')) {
				scanComment();
			} else if (match('*')) {
				scanBlockComment();
			} else {
				addToken(SLASH);
			}
			break;
			
		// whitespace
		case ' ':
		case '\r':
		case '\t':
			// Future TODO: these could be captured as a token
			break;
			
		// newline
		case '\n':
			line++;
			break;
			
		// string literals
		case '"':
			scanString();
			break;
			
			
		default:
			if (isDigit(c)) {
				scanNumber();
			} else if (isAlpha(c)) {
				scanIdentifier();
			} else {
				Lox.error(line,  "Unexpected character.");
				break;
			}
		}
	}
	
	private void scanIdentifier() {
		while (isAlphaNumeric(peek())) advance();
		
		String text = source.substring(start, current);
		TokenType type = keywords.get(text);
		if (type == null) type = IDENTIFIER;
		addToken(type);
	}
	
	private void scanNumber() {
		while (isDigit(peek())) advance();
		
		// handle fractional part
		if (peek() == '.' && isDigit(peekNext())) {
			// consume the ".'
			advance();
			
			while(isDigit(peek())) advance();
		}
		
		String value = source.substring(start, current);
		addToken(NUMBER, Double.parseDouble(value));
	}
	
	private void scanString() {
		while (peek() != '"' && !isAtEnd()) {
			if (peek() == '\n') line++;
			advance();
		}
		
		if (isAtEnd()) {
			Lox.error(line, "Unterminated string.");
			return;
		}
		
		// Have arrived at the closing "
		advance();
	
		// Future TODO: consider handling escaped sequences
		String value = source.substring(start + 1, current - 1);
		addToken(STRING, value);
	}
	
	private void scanComment() {
		// Future TODO: this could capture the comment as a token
		while (peek() != '\n' && !isAtEnd()) advance();
	}
	
	private void scanBlockComment() {
		while (!isAtEnd()) {
			if (peek() == '*' && peekNext() == '/') {
				advance();
				advance();
				break;
			}
			
			if (peek() == '\n') {
				line++;
			}
			
			advance();
		}
		
		Lox.error(line, "Unterminated block comment.");
	}
	
	private char advance() {
		return source.charAt(current++);
	}
	
	private boolean match(char expected) {
		if (isAtEnd()) return false;
		if (source.charAt(current) != expected) return false;
		
		current++;
		return true;
	}
	
	private char peek() {
		if (isAtEnd()) return '\0';
		return source.charAt(current);
	}
	
	private char peekNext() {
		if (current + 1 >= source.length()) return '\0';
		return source.charAt(current + 1);
	}
	
	private boolean isAlpha(char c) {
		return (c >= 'a' && c <= 'z') ||
			   (c >= 'A' && c <= 'Z') ||
			   (c == '_');
	}
	
	private boolean isAlphaNumeric(char c) {
		return isAlpha(c) || isDigit(c);
	}
	
	private boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}
	
	private boolean isAtEnd() {
		return current >= source.length();
	}
	
	private void addToken(TokenType type) {
		addToken(type, null);
	}
	
	private void addToken(TokenType type, Object literal) {
		String text = source.substring(start, current);
		tokens.add(new Token(type, text, literal, line));
	}
}
