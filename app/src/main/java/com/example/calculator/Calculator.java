package com.example.calculator;

public class Calculator {

    private String expression;
    private int pos;

    public double calculer(String expr) {
        this.expression = expr.replaceAll("\\s+", "");
        this.pos = 0;
        return parseExpression();
    }

    // garde l'ancienne méthode pour ne pas casser le code existant
    public double calculer(double a, double b, String operator) {
        if (operator.equals("+")) return a + b;
        if (operator.equals("-")) return a - b;
        if (operator.equals("*")) return a * b;
        if (operator.equals("/")) {
            if (b == 0) return Double.NaN;
            return a / b;
        }
        return 0;
    }

    private double parseExpression() {
        double result = parseTerm();
        while (pos < expression.length()) {
            char op = expression.charAt(pos);
            if (op == '+') { pos++; result += parseTerm(); }
            else if (op == '-') { pos++; result -= parseTerm(); }
            else break;
        }
        return result;
    }

    private double parseTerm() {
        double result = parseFactor();
        while (pos < expression.length()) {
            char op = expression.charAt(pos);
            if (op == '*') { pos++; result *= parseFactor(); }
            else if (op == '/') {
                pos++;
                double diviseur = parseFactor();
                if (diviseur == 0) return Double.NaN;
                result /= diviseur;
            }
            else break;
        }
        return result;
    }

    private double parseFactor() {
        if (pos < expression.length() && expression.charAt(pos) == '(') {
            pos++; // saute (
            double result = parseExpression();
            if (pos < expression.length() && expression.charAt(pos) == ')') {
                pos++; // saute )
            }
            return result;
        }
        // nombre négatif
        if (pos < expression.length() && expression.charAt(pos) == '-') {
            pos++;
            return -parseFactor();
        }
        int start = pos;
        while (pos < expression.length() &&
                (Character.isDigit(expression.charAt(pos)) || expression.charAt(pos) == '.')) {
            pos++;
        }
        return Double.parseDouble(expression.substring(start, pos));
    }
}