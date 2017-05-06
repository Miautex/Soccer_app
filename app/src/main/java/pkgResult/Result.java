package pkgResult;

public class Result {

	private boolean success;
	
	private pkgError.Error error;
	
	public Result() {}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public pkgError.Error getError() {
		return error;
	}

	public void setError(pkgError.Error error) {
		this.error = error;
	}
	
	
}
