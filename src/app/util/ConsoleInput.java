package app.util;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleInput {

	/*
	 * 
	 * -- LEER DOUBLE (ENTRE VALORES) STRING -> 98 CHAR -> 129 BOOLEAN -> 156
	 */

	// ENTEROS -> 32
	// |
	// -- LEER ENTEROS ->
	// -- LEER ENTEROS (ENTRE VALORES) ->
	//
	// DOUBLE -> 65
	// |
	// -- LEER DOUBLE ->
	// -- LEER DOUBLE (>)

	private final Scanner sc;

	public ConsoleInput(Scanner sc) {
		this.sc = sc;
	}

	// UTILIDAD INTERNA

	private void clean() {
		sc.nextLine();
	}

	private String defaultMsg(String msg, String fallback) {
		return (msg == null || msg.isBlank()) ? fallback : msg;
	}

	// ENTEROS

	public int readInt(String msg) {
		int value;
		String prompt = defaultMsg(msg, "Introduce un número entero: ");

		while (true) {
			System.out.print(prompt);
			try {
				value = sc.nextInt();
				return value;
			} catch (InputMismatchException e) {
				System.out.println("❌ Debes introducir un número entero válido.");
			} finally {
				clean();
			}
		}
	}

	public int readIntInRange(String msg, int min, int max) {
		int value;
		String prompt = defaultMsg(msg, "Introduce un número entre " + min + " y " + max + ": ");

		do {
			value = readInt(prompt);
			if (value < min || value > max) {
				System.out.println("❌ Debe estar entre " + min + " y " + max);
			}
		} while (value < min || value > max);

		return value;
	}

	// DOUBLE

	public double readDouble(String msg) {
		double value;
		String prompt = defaultMsg(msg, "Introduce un número decimal: ");

		while (true) {
			System.out.print(prompt);
			try {
				value = sc.nextDouble();
				return value;
			} catch (InputMismatchException e) {
				System.out.println("❌ Debes introducir un número válido.");
			} finally {
				clean();
			}
		}
	}

	public double readDoubleGreaterThan(String msg, double min) {
		double value;
		String prompt = defaultMsg(msg, "Introduce un número mayor que " + min + ": ");

		do {
			value = readDouble(prompt);
			if (value <= min) {
				System.out.println("❌ Debe ser mayor que " + min);
			}
		} while (value <= min);

		return value;
	}

	public double readDoubleLowerThan(String msg, double max) {
		double value;
		String prompt = defaultMsg(msg, "Introduce un número menor que " + max + ": ");

		do {
			value = readDouble(prompt);
			if (value <= max) {
				System.out.println("❌ Debe ser mayor que " + max);
			}
		} while (value <= max);

		return value;
	}

	// STRING

	public String readString(String msg) {
		String value;
		String prompt = defaultMsg(msg, "Introduce un texto: ");

		do {
			System.out.print(prompt);
			value = sc.nextLine().trim();
			if (value.isEmpty()) {
				System.out.println("❌ El texto no puede estar vacío.");
			}
		} while (value.isEmpty());

		return value;
	}

	public String readString(String msg, int maxLength) {
		String value;
		String prompt = defaultMsg(msg, "Introduce un texto (máx. " + maxLength + " caracteres): ");

		do {
			value = readString(prompt);
			if (value.length() > maxLength) {
				System.out.println("❌ Máximo " + maxLength + " caracteres.");
			}
		} while (value.length() > maxLength);

		return value;
	}

	// CHAR

	public char readChar(String msg, char... validOptions) {
		char value;
		String prompt = defaultMsg(msg, "Introduce un carácter: ");

		while (true) {
			System.out.print(prompt);
			String input = sc.nextLine().trim().toLowerCase();

			if (input.length() == 1) {
				value = input.charAt(0);

				if (validOptions.length == 0) {
					return value;
				}

				for (char c : validOptions) {
					if (value == Character.toLowerCase(c)) {
						return value;
					}
				}
			}
			System.out.println("❌ Carácter no válido.");
		}
	}

	// BOOLEAN (sí / no)

	public boolean readYesNo(String msg) {
		String prompt = defaultMsg(msg, "¿Deseas continuar?");
		char value = readChar(prompt + " (s/n): ", 's', 'n');
		return value == 's';
	}
}
