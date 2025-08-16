package com.resumeAI.resumeAPI.model;

public class Message {

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

	private String content;

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
}
