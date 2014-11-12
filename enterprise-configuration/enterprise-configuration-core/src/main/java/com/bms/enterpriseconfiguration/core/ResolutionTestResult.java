package com.bms.enterpriseconfiguration.core;

import com.google.common.base.Optional;

public class ResolutionTestResult extends AbstractPrintable {

	private Optional<Throwable> possibleException;
	private Optional<String> informationMessage;
	private Optional<String> warningMessage;
	private Optional<String> errorMessage;
	private boolean successful;
	
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
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}
	
}
