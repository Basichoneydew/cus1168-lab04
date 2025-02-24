package academy.javapro;

class ExpressionParser {
    private final String input;
    private int position;

    public ExpressionParser(String input) {
        this.input = input;
        this.position = 0;
    }

    // expr → expr + term
    public double parseExpression() {
        double result = parseTerm();
        while (position < input.length() && input.charAt(position) == '+') {
            position++;
            double rightTerm = parseTerm();
            result += rightTerm;
        }
        return result;
    }

    // term → term * factor
    private double parseTerm() {
        double result = parseFactor();
        while (position < input.length() && input.charAt(position) == '*') {
            position++;
            double rightFactor = parseFactor();
            result *= rightFactor;
        }
        return result;
    }

    // factor → ( expr )
    private double parseFactor() {
        if (position < input.length() && input.charAt(position) == '(') {
            position++;
            double result = parseExpression();
            if (position >= input.length() || input.charAt(position) != ')') {
                throw new RuntimeException(String.format("Missing closing parenthesis at this position: %d", position));
            }
            position++;
            return result;
        } else if (position < input.length() && (Character.isDigit(input.charAt(position))
                || input.charAt(position) == '.')) {
            return parseNumber();
        } else {
            throw new RuntimeException(
                    String.format("Unexpected character at position %d", position));
        }
    }

    // Parse a numeric value
    private double parseNumber() {
        if (position >= input.length() ||
                (!Character.isDigit(input.charAt(position)) && input.charAt(position) != '.')) {
            throw new RuntimeException(String.format("Expected number at this position: %d", position));
        }

        StringBuilder number = new StringBuilder();
        boolean hasDecimalPoint = false;

        while (position < input.length() &&
                (Character.isDigit(input.charAt(position)) || input.charAt(position) == '.')) {
            if (input.charAt(position) == '.') {
                if (hasDecimalPoint) {
                    throw new RuntimeException(
                            String.format("Multiple decimal points at the position %d", position));
                }
                hasDecimalPoint = true;
            }
            number.append(input.charAt(position));
            position++;
        }

        try {
            return Double.parseDouble(number.toString());
        } catch (NumberFormatException e) {
            throw new RuntimeException(
                    String.format("Invalid format at position %d: %s", position, number));
        }
    }

    public static void main(String[] args) {
        // Test cases
        String[] testCases = {
                "2 + 3 * (4 + 5)", // Complex expression with parentheses
                "2 + 3 * 4", // Basic arithmetic with precedence
                "(2 + 3) * 4", // Parentheses changing precedence
                "2 * (3 + 4) * (5 + 6)", // Multiple parentheses
                "1.5 + 2.5 * 3", // Decimal numbers
                "47 * ",
                "1 + (2 + 3"
        };

        // Process each test case
        for (String expression : testCases) {
            System.out.println("\nTest Case: " + expression);
            try {
                ExpressionParser parser = new ExpressionParser(expression.replaceAll("\\s+", "")); // Remove spaces
                double result = parser.parseExpression();
                System.out.println("Result: " + result);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}