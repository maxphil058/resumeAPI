package com.resumeAI.resumeAPI.model;

public class Choice {

	/*
	 * {
	 * 
	 * "model": "gpt-3.5-turbo-0125", "choices": [ { "index": 0, "message": {
	 * "role": "assistant", "content":
	 * "Why couldn't the bicycle stand up by itself?\nBecause it was two tired!",
	 * "refusal": null, "annotations": [] }, "logprobs": null, "finish_reason":
	 * "stop" } ], }
	 * 
	 */

	private Message message;

	/**
	 * @return the message
	 */
	public Message getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(Message message) {
		this.message = message;
	}

}
