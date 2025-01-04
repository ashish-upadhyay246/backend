package com.hexaware.Exceptions;

public class EmployeeCustomExceptions {

	public static class EmployeeNotFoundException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public EmployeeNotFoundException(String message) {
	        super(message);
	    }
	}

	public static class LeaveRequestFailedException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public LeaveRequestFailedException(String message) {
	        super(message);
	    }
	}

	public static class InvalidLeaveException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public InvalidLeaveException(String message) {
	        super(message);
	    }
	}

	public static class ProfileUpdateFailedException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public ProfileUpdateFailedException(String message) {
	        super(message);
	    }
	}
}
