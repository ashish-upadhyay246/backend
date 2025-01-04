package com.hexaware.Exceptions;

public class PayrollCustomExceptions {

	public static class PayrollNotFoundException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public PayrollNotFoundException(String message) {
	        super(message);
	    }
	}

	public static class PayrollCreationFailedException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public PayrollCreationFailedException(String message) {
	        super(message);
	    }
	}

	public static class PayrollUpdateFailedException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public PayrollUpdateFailedException(String message) {
	        super(message);
	    }
	}
}
