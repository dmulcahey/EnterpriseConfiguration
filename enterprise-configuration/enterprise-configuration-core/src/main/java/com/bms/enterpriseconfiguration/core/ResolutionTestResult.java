package com.bms.enterpriseconfiguration.core;

import com.google.common.base.Optional;

public class ResolutionTestResult {

	private Optional<Throwable> possibleException = Optional.absent();
	private Optional<String> informationMessage = Optional.absent();
	private Optional<String> warningMessage = Optional.absent();
	private Optional<String> errorMessage = Optional.absent();
	private String testClassName;
	
	public Optional<Throwable> getPossibleException() {
		return possibleException;
	}
	
	public void setPossibleException(Throwable possibleException) {
		this.possibleException = Optional.fromNullable(possibleException);
	}

	public Optional<String> getInformationMessage() {
		return informationMessage;
	}

	public void setInformationMessage(String informationMessage) {
		this.informationMessage = Optional.fromNullable(informationMessage);
	}

	public Optional<String> getWarningMessage() {
		return warningMessage;
	}

	public void setWarningMessage(String warningMessage) {
		this.warningMessage = Optional.fromNullable(warningMessage);
	}

	public Optional<String> getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = Optional.fromNullable(errorMessage);
	}

	public boolean isSuccessful() {
		return !errorMessage.isPresent() && !possibleException.isPresent();
	}
	
	String getTestClassName(){
		return this.testClassName;
	}
	
	void setTestClassName(String className){
		this.testClassName = className;
	}

	@Override
	public String toString() {
		return "ResolutionTestResult [possibleException=" + possibleException
				+ ", informationMessage=" + informationMessage
				+ ", warningMessage=" + warningMessage + ", errorMessage="
				+ errorMessage + ", testClassName=" + testClassName + "]";
	}
	
}
