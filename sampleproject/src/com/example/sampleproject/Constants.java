package com.example.sampleproject;

public class Constants {
	
	public static final int NO_ERROR = 0;
	public static final int ILLEGAL_ARGUMENT_EXCEPTION = -1;
	public static final String ERROR_DIALOG = "Error";
	
	public static final int THREAD_COUNT=10;

	public static final String SUCCESS="SUCCESS";
	public static final String FAILED="FAILED";

	public static final String SUCCESS_MESSAGE = "\nSuccessful connection from IP : ";
	public static final String SOCKET_CONNECTION_FAILED = "\nSocket connection failed from IP: \n";

	public static final String CONNECTED_TO_MESSAGE = "\nConnected to " ;
	public static final String SENT_MESSAGE = "\n <Requested stuff> \n";
	public static final String ACCESS_DENIED = "Access Denied for ";
	public static final String GENERAL_EXCEPTION = "\n General Exception occured for \n";
	public static final String SECURITY_EXCEPTION = "\n You are not authorized to access this site";
	public static final int SECURITY_ERROR = -3;

	public static final int NORMAL_PORT = 4445;
	public static final int SECURE_PORT = 4444;
	
	public static final String IP_REGEX="(^((\\d|\\d{2}|([0-1]\\d{2})|(2[0-4][0-9])|(25[0-5]))\\.){3}(\\d|\\d{2}|([0-1]\\d{2})|(2[0-4][0-9])|(25[0-5]))$)|(\\*)";
	public static final String WEBSITE_REGEX="(^(((http|https)://)?)([A-Za-z0-9.]*)$)|(\\*)";

	public static final String DELIMITER="^";
	public static final String FINISHED_SENDING="END_OF_TRANSMISSION_FROM_WEBSITE";
	
	public static final String ADD_RULE = "Add Rule";
	public static final String DELETE_RULE = "Delete Rule";
	public static final String UPDATE_RULE = "Update Rule";
	public static final String VIEW_RULES = "View Rules";
}
