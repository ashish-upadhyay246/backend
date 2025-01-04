package com.hexaware.Exceptions;

public class UserCustomExceptions {
	
    public static class UserNotFoundException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public UserNotFoundException(String message) {
            super(message);
        }
    }
    
    public static class DuplicateUsernameException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public DuplicateUsernameException(String message) {
            super(message);
        }
    }
    
    public static class RoleNotFoundException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public RoleNotFoundException(String message) {
            super(message);
        }
    }

	public class EmployeeNotFoundException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public EmployeeNotFoundException(String message) {
	        super(message);
	    }
	}

	public class DepartmentNotFoundException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public DepartmentNotFoundException(String message) {
	        super(message);
	    }
	}

	public class LeaveNotFoundException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public LeaveNotFoundException(String message) {
	        super(message);
	    }
	}

	public class InvalidLeaveApprovalException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public InvalidLeaveApprovalException(String message) {
	        super(message);
	    }
	}

}
