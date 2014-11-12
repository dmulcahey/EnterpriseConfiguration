package com.bms.enterpriseconfiguration.core;

import com.google.common.base.Optional;

public class ResolutionTestResult {

	private Optional<Throwable> possibleException;
	private Optional<String> informationMessage;
	private Optional<String> warningMessage;
	private Optional<String> errorMessage;
	private boolean successful;
	
	public Optional<Throwable> getPossibleException() {
		return possibleException;
	}
	
	public void setPossibleException(Throwable possibleException) {
		this.possibleException = Optional.of(possibleException);
	}

	public Optional<String> getInformationMessage() {
		return informationMessage;
	}

	public void setInformationMessage(String informationMessage) {
		this.informationMessage = Optional.of(informationMessage);
	}

	public Optional<String> getWarningMessage() {
		return warningMessage;
	}

	public void setWarningMessage(String warningMessage) {
		this.warningMessage = Optional.of(warningMessage);
	}

	public Optional<String> getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = Optional.of(errorMessage);
	}

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	@Override
	public String toString() {
		return "ResolutionTestResult [possibleException=" + possibleException
				+ ", informationMessage=" + informationMessage
				+ ", warningMessage=" + warningMessage + ", errorMessage="
				+ errorMessage + ", successful=" + successful + "]";
	}
	
}
