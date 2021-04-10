package com.neurotec.samples.util;

public class AsyncTaskResult<T> {

	private T mResult;
	private Exception mException;

	public T getResult() {
		return mResult;
	}

	public Exception getException() {
		return mException;
	}

	public AsyncTaskResult() {
	}

	public AsyncTaskResult(T result) {
		this.mResult = result;
	}

	public AsyncTaskResult(Exception exception) {
		this.mException = exception;
	}

}
