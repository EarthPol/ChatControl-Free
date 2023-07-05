package org.mineacademy.chatcontrol.rules;

import java.util.List;
import java.util.Objects;

import org.mineacademy.chatcontrol.util.Common;

import lombok.Getter;

/**
 * Custom handler that handles message caught by {@link Rule}.
 */
@Getter
public final class Handler {

	/**
	 * The name of the handler. It's automatically set from the handler name in
	 * handlers file.
	 */
	private final String name;

	/**
	 * The id/name of the rule associated with this handler.
	 */
	private String ruleId = "UNSET";

	/**
	 * A permission that makes player bypass the checks.
	 */
	private String bypassPermission;

	/**
	 * List of commands that will be ignored from the check.
	 */
	private List<String> ignoredInCommands;

	/**
	 * A message displayed to the player that triggered the handler. Set to 'none'
	 * to disable.
	 */
	private String playerWarningMessage;

	/**
	 * A message broadcasted to everyone. Set to 'none' to disable.
	 */
	private String broadcastMessage;

	/**
	 * People with specified permission will recieve {@link #staffAlertMessage}.
	 * Functional only when {@link #staffAlertMessage} is 'none'.
	 */
	private String staffAlertPermission;

	/**
	 * A message broadcasted to staff with {@link #staffAlertPermission}. Set to
	 * 'none' to disable.
	 */
	private String staffAlertMessage;

	/**
	 * A message logged in the server's console. Set to 'none' to disable.
	 */
	private String consoleMessage;

	/**
	 * A list of commands to be executed. Variables: {player} {message} Can be empty.
	 */
	private List<String> commandsToExecute;

	/**
	 * A name of a file that message will be writed in. Variables: {time} {player}
	 * {message} Set to 'none' to disable writing.
	 */
	private String writeToFileName;

	/**
	 * Should the message be blocked from appearing? This cancels for instance
	 * player chat event or command event.
	 */
	private boolean messageBlocked = false;

	/**
	 * Economy. How much money to take from player? Requires Vault.
	 */
	private Double fine;

	/**
	 * A replacement that replaces only part of the message caught by a rule.
	 */
	private String messageReplacement;

	/**
	 * A message that replaces the entire message caught by a rule.
	 */
	private String rewriteTo;

	/**
	 * Creates a new handler instance.
	 *
	 * @param name
	 *            the name of this handler
	 * @param ruleID
	 */
	public Handler(String name, String ruleID) {
		this.name = name;

		if (ruleID != null)
			this.ruleId = ruleID;
	}

	public void setBypassPermission(String bypassPermission) {
		Common.checkBoolean(this.bypassPermission == null, "Bypass permission already set for: " + this);

		this.bypassPermission = bypassPermission;
	}

	public void setIgnoredInCommands(List<String> ignoredInCommands) {
		Common.checkBoolean(this.ignoredInCommands == null, "Ignored commands already set for: " + this);

		this.ignoredInCommands = ignoredInCommands;
	}

	public void setPlayerWarningMessage(String playerWarnMsg) {
		Common.checkBoolean(this.playerWarningMessage == null, "Player warn message already set for: " + this);

		this.playerWarningMessage = playerWarnMsg;
	}

	public void setBroadcastMessage(String broadcastMsg) {
		Common.checkBoolean(this.broadcastMessage == null, "Broadcast message already set for: " + this);

		this.broadcastMessage = broadcastMsg;
	}

	public void setStaffAlertMessage(String staffAlertMsg) {
		Common.checkBoolean(this.staffAlertMessage == null, "Staff alert message already set for: " + this);

		this.staffAlertMessage = staffAlertMsg;
	}

	public void setStaffAlertPermission(String staffAlertPermission) {
		Objects.requireNonNull(staffAlertMessage, "Staff alert message is null, cannot get staff permission! Handler: " + this);

		this.staffAlertPermission = staffAlertPermission;
	}

	public void setConsoleMessage(String consoleMsg) {
		Common.checkBoolean(this.consoleMessage == null, "Console message already set for: " + this);

		this.consoleMessage = consoleMsg;
	}

	public void setCommandsToExecute(List<String> commandsToExecute) {
		Common.checkBoolean(this.commandsToExecute == null, "Commands to execute already set for: " + this);

		this.commandsToExecute = commandsToExecute;
	}

	public void setWriteToFileName(String writeToFileName) {
		Common.checkBoolean(this.writeToFileName == null, "Write to file path already set for: " + this);

		this.writeToFileName = writeToFileName;
	}

	public void setMessageBlocked() {
		Common.checkBoolean(!messageBlocked, "Message is already blocked for: " + this);

		messageBlocked = true;
	}

	public void parseFine(String line) {
		Common.checkBoolean(fine == null, "Fine already set on: " + this);

		try {
			final double fine = Double.parseDouble(line);
			this.fine = fine;
		} catch (final NumberFormatException ex) {
			throw new RuntimeException("Corrupted fine value, expected double, got: " + line);
		}
	}

	public void setMessageReplacement(String msgReplacement) {
		Common.checkBoolean(!messageBlocked, "Replacement cannot be defined when the message is blocked: " + this);
		Common.checkBoolean(rewriteTo == null, "Whole message replacement already defined for: " + this);

		this.messageReplacement = msgReplacement;
	}

	public void setRewriteTo(String wholeMsgReplacement) {
		Common.checkBoolean(!messageBlocked, "Whole replacement cannot be defined when the message is blocked: " + this);
		Common.checkBoolean(messageReplacement == null, "Part message replacement already defined for: " + this);

		rewriteTo = wholeMsgReplacement;
	}

	private String printCommands() {
		String commands = "(" + commandsToExecute.size() + ")";
		for (final String command : commandsToExecute)
			commands += command;

		return commands;
	}

	@Override
	public String toString() {
		return Common.stripColors("    Handler{\n" + "        Name: \'" + name + "\'\n" + (ruleId != null ? "        Rule ID: " + ruleId + "\n" : "") + (ignoredInCommands != null ? "        Ignored In Commands: " + ignoredInCommands + "\n" : "") + (bypassPermission != null ? "        Bypass Permission: \'" + bypassPermission + "\'\n" : "") + (playerWarningMessage != null ? "        Player Warn Msg: \'" + playerWarningMessage + "\'\n" : "")
				+ (broadcastMessage != null ? "        Broadcast Msg: \'" + broadcastMessage + "\'" : "") + (staffAlertPermission != null ? "        Staff Alert Permission: \'" + staffAlertPermission + "\'\n" : "") + (staffAlertMessage != null ? "        Staff Alert Msg: \'" + staffAlertMessage + "\'\n" : "") + (consoleMessage != null ? "        Console Msg: \'" + consoleMessage + "\'\n" : "")
				+ (commandsToExecute != null ? "        Commands To Execute: \'" + printCommands() + "\'\n" : "")
				+ (writeToFileName != null ? "        Write To File Name: \'" + writeToFileName + "\'\n" : "") + (messageBlocked ? "        Block Message: \'" + messageBlocked + "\'\n" : "") + (messageReplacement != null ? "        Replace Part With: \'" + messageReplacement + "\'\n" : "") + (rewriteTo != null ? "        Replace Whole With: \'" + rewriteTo + "\'\n" : "") + "    }");
	}
}